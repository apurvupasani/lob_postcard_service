package com.lob.postcard.api

import com.lob.postcard.domain.enums.Error
import com.lob.postcard.domain.enums.Error.UnExpectedError
import com.lob.postcard.domain.request.PostCardAddressRequestBody
import com.lob.postcard.domain.response.PostCardAddressAddResponse
import com.lob.postcard.domain.response.PostCardAddressResponse
import com.lob.postcard.domain.response.PostCardAddressUpdateResponse
import com.lob.postcard.service.PostCardAddressService
import org.springframework.http.HttpHeaders
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.PutMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestHeader
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.ResponseStatus
import reactor.core.publisher.Mono
import reactor.kotlin.core.publisher.toMono

@Controller
class PostCardController(
    private val postCardAddressService: PostCardAddressService
) {
    @GetMapping(
        value = ["/postcard/addresses"],
        consumes = ["application/json"],
        produces = ["application/json"]
    )
    fun getAddresses(
        @RequestHeader headers: HttpHeaders,
        @RequestParam(value = "query") query: String?,
    ): Mono<ResponseEntity<PostCardAddressResponse>> =
        postCardAddressService.getAddresses(query)
            .map { response ->
                response.fold(
                    { PostCardAddressResponse(errors = listOf(it.message)).toResponseEntity(it) },
                    { PostCardAddressResponse(addresses = it.map { it.toResponsePostalCardAddress() }).toResponseEntity() }
                )
            }
            .onErrorResume {
                PostCardAddressResponse(errors = listOf(UnExpectedError.message))
                    .toResponseEntity(UnExpectedError).toMono()
            }

    @PostMapping(
        value = ["/postcard/address"],
        consumes = ["application/json"],
        produces = ["application/json"]
    )
    @ResponseStatus(HttpStatus.OK)
    fun addAddress(
        @RequestHeader headers: HttpHeaders,
        @RequestBody requestBody: PostCardAddressRequestBody
    ): Mono<ResponseEntity<PostCardAddressAddResponse>> =
        postCardAddressService.addAddress(requestBody)
            .map { response ->
                response.fold(
                    { PostCardAddressAddResponse(errors = it.map { it.message }).toResponseEntity(it.first()) },
                    { PostCardAddressAddResponse(id = it).toResponseEntity() }
                )
            }
            .onErrorResume {
                PostCardAddressAddResponse(errors = listOf(UnExpectedError.message))
                    .toResponseEntity(UnExpectedError).toMono()
            }

    @PutMapping(
        value = ["/postcard/address/{id}"],
        consumes = ["application/json"],
        produces = ["application/json"]
    )
    @ResponseStatus(HttpStatus.OK)
    fun updateAddress(
        @RequestHeader headers: HttpHeaders,
        @PathVariable(value="id") addressId: String?,
        @RequestBody requestBody: PostCardAddressRequestBody
    ): Mono<ResponseEntity<PostCardAddressUpdateResponse>> =
        postCardAddressService.updateAddress(addressId, requestBody)
            .map { response ->
                response.fold(
                    { PostCardAddressUpdateResponse(errors = it.map { it.message }).toResponseEntity(it.first()) },
                    { PostCardAddressUpdateResponse(updatedRecord = it.toResponsePostalCardAddress()).toResponseEntity() }
                )
            }
            .onErrorResume {
                PostCardAddressUpdateResponse(errors = listOf(UnExpectedError.message))
                    .toResponseEntity(UnExpectedError).toMono()
            }


    private fun <T : Any> T.toResponseEntity(error: Error? = null): ResponseEntity<T> {
        val httpStatusCode = when (error) {
            is Error -> if (error == UnExpectedError) HttpStatus.INTERNAL_SERVER_ERROR else HttpStatus.BAD_REQUEST
            else -> HttpStatus.OK
        }

        return ResponseEntity
            .status(httpStatusCode)
            .body(this)
    }
}