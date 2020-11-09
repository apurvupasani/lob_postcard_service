package com.lob.postcard.service

import arrow.core.Either
import arrow.core.Left
import arrow.core.Right
import arrow.core.extensions.list.foldable.nonEmpty
import com.lob.postcard.domain.PostalAddress
import com.lob.postcard.domain.enums.Error
import com.lob.postcard.domain.enums.Error.CityCannotBeEmpty
import com.lob.postcard.domain.enums.Error.Line1AndLine2CantBeEmpty
import com.lob.postcard.domain.enums.Error.StateCannotBeEmpty
import com.lob.postcard.domain.enums.Error.StateMustBeTwoAlphabets
import com.lob.postcard.domain.enums.Error.ZipCodeCannotBeEmpty
import com.lob.postcard.domain.enums.Error.ZipCodeMustBeANumber
import com.lob.postcard.domain.request.PostCardAddressAddRequestBody
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

    fun addAddress(requestBody: PostCardAddressAddRequestBody): Mono<Either<List<Error>, String>> {
        val validatedResult = validateAddAddress(requestBody)
        return when {
            validatedResult.nonEmpty() -> Left(validatedResult).toMono()
            else ->
                postalAddressRepository.addAddress(requestBody)
                    .map {
                        val result = if (it) "Success" else "Fail"
                        Right(result)
                    }
        }
    }

    private fun validateAddAddress(requestBody: PostCardAddressAddRequestBody): List<Error> {
        val errorList: MutableList<Error> = mutableListOf()
        if (requestBody.line1.isNullOrBlank() && requestBody.line2.isNullOrBlank()) {
            errorList.add(Line1AndLine2CantBeEmpty)
        }
        if (requestBody.state.isNullOrBlank()) {
            errorList.add(StateCannotBeEmpty)
        }
        if (requestBody.state?.length != 2 &&
            requestBody.state?.toCharArray()?.all { Character.isLetter(it) } == true) {
            errorList.add(StateMustBeTwoAlphabets)
        }
        if (requestBody.city.isNullOrBlank()) {
            errorList.add(CityCannotBeEmpty)
        }
        if (requestBody.zip.isNullOrBlank()) {
            errorList.add(ZipCodeCannotBeEmpty)
        }
        if (requestBody.zip?.length != 6 && requestBody.zip?.toIntOrNull() != null) {
            errorList.add(ZipCodeMustBeANumber)
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