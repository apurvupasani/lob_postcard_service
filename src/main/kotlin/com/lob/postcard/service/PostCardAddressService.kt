package com.lob.postcard.service

import arrow.core.Either
import arrow.core.Left
import arrow.core.Right
import arrow.core.Some
import arrow.core.extensions.list.foldable.nonEmpty
import com.lob.postcard.domain.PostalAddress
import com.lob.postcard.domain.enums.Error
import com.lob.postcard.domain.enums.Error.*
import com.lob.postcard.domain.request.PostCardAddressRequestBody
import com.lob.postcard.extensions.cleanUp
import com.lob.postcard.repository.PostalAddressRepository
import org.springframework.stereotype.Service
import reactor.core.publisher.Mono
import reactor.kotlin.core.publisher.toMono

@Service
class PostCardAddressService(
    private val postalAddressRepository: PostalAddressRepository
) {
    fun getAddresses(query: String?): Mono<Either<Error, List<PostalAddress>>> =
        when (val validatedResult = validateQuery(query)) {
            null -> postalAddressRepository.getAddresses(query.cleanUp()).map { Right(it) }
            else -> Left(validatedResult).toMono()
        }

    fun addAddress(requestBody: PostCardAddressRequestBody): Mono<Either<List<Error>, Int>> {
        val validatedResult = validateAddAddress(requestBody)
        return when {
            validatedResult.nonEmpty() -> Left(validatedResult).toMono()
            else ->
                postalAddressRepository.addAddress(requestBody)
                    .map {
                        if (it != -1) {
                            Right(it)
                        } else {
                            Left(listOf(DuplicateRecord))
                        }
                    }
        }
    }

    fun updateAddress(
        addressId: String?,
        requestBody: PostCardAddressRequestBody
    ): Mono<Either<List<Error>, PostalAddress>> {
        val validatedResult = validateUpdateAddress(addressId, requestBody)
        return when {
            validatedResult.nonEmpty() -> Left(validatedResult).toMono()
            else ->
                postalAddressRepository
                    .updateAddress(addressId?.toInt() ?: 0, requestBody)
                    .map {
                        if (it is Some) {
                            Right(it.t)
                        } else {
                            Left(listOf(RecordNotFound))
                        }
                    }
        }
    }

    private fun validateUpdateAddress(
        addressId: String?,
        requestBody: PostCardAddressRequestBody): List<Error> {
        val errorList: MutableList<Error> = mutableListOf()
        if (addressId.isNullOrBlank()) {
            errorList.add(AddressIdBlank)
        } else if(addressId.toIntOrNull() == null) {
            errorList.add(AddressIdNotANumber)
        }

        if (requestBody.line1.isNullOrBlank() &&
            requestBody.line2.isNullOrBlank() &&
            requestBody.state.isNullOrBlank() &&
            requestBody.city.isNullOrBlank() &&
            requestBody.zip.isNullOrBlank()) {
            errorList.add(EmptyUpdateRequest)
        }
        return errorList.toList()
    }

    private fun validateAddAddress(requestBody: PostCardAddressRequestBody): List<Error> {
        val errorList: MutableList<Error> = mutableListOf()
        if (requestBody.line1.isNullOrBlank() && requestBody.line2.isNullOrBlank()) {
            errorList.add(Line1AndLine2BothEmpty)
        }
        if (requestBody.state.isNullOrBlank()) {
            errorList.add(StateEmpty)
        }
        if (requestBody.state?.length != 2 &&
            requestBody.state?.toCharArray()?.all { Character.isLetter(it) } == true) {
            errorList.add(StateInvalid)
        }
        if (requestBody.city.isNullOrBlank()) {
            errorList.add(CityEmpty)
        }
        if (requestBody.zip.isNullOrBlank()) {
            errorList.add(ZipCodeEmpty)
        }
        if (requestBody.zip?.length != 5 && requestBody.zip?.toIntOrNull() != null) {
            errorList.add(ZipCodeNotAValidNumber)
        }
        return errorList.toList()
    }


    private fun validateQuery(query: String?): Error? =
        when {
            query.isNullOrBlank() -> Error.EmptyQuery
            query.length > 100 -> Error.InvalidQuery
            else -> null
        }
}