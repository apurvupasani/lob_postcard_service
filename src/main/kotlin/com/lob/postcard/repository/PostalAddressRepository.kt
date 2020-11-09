package com.lob.postcard.repository

import arrow.core.None
import arrow.core.Option
import arrow.core.Some
import arrow.core.toOption
import com.fasterxml.jackson.annotation.JsonInclude
import com.fasterxml.jackson.core.type.TypeReference
import com.fasterxml.jackson.databind.DeserializationFeature
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import com.lob.postcard.domain.PostalAddress
import com.lob.postcard.domain.request.PostCardAddressRequestBody
import org.springframework.stereotype.Repository
import reactor.core.publisher.Mono
import reactor.kotlin.core.publisher.toMono
import java.util.concurrent.atomic.AtomicInteger
import javax.annotation.PostConstruct

@Repository
open class PostalAddressRepository {

    private val addressCount = AtomicInteger(0)
    private val localAddressMap: MutableMap<Int, PostalAddress> = mutableMapOf()

    @PostConstruct
    fun init() {
        val objectMapper = jacksonObjectMapper()
            .configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false)
            .configure(DeserializationFeature.FAIL_ON_MISSING_CREATOR_PROPERTIES, true)
            .setSerializationInclusion(JsonInclude.Include.NON_NULL)
        objectMapper
            .readValue(
                javaClass.getResource("/addresses.json").readText(),
                object : TypeReference<List<PostCardAddressRequestBody>>() {}
            ).map {
                localAddressMap.put(addressCount.incrementAndGet(), it.toPostalAddress())
            }
    }

    fun getAddresses(query: String): Mono<List<PostalAddress>> {
        synchronized(localAddressMap) {
            return localAddressMap.values
                .filter { it.cleanAddress.contains(query, true) }
                .toMono()
        }
    }

    fun addAddress(addressRequestBody: PostCardAddressRequestBody): Mono<Int> {
        val postCardAddress = addressRequestBody.toPostalAddress()
        synchronized(localAddressMap) {
            return (if (localAddressMap.containsValue(postCardAddress)) {
                -1
            } else {
                val result = addressCount.incrementAndGet()
                localAddressMap[result] = postCardAddress
                result
            }).toMono()
        }
    }

    fun updateAddress(addressId: Int, addressRequestBody: PostCardAddressRequestBody): Mono<Option<PostalAddress>> {
        val postCardAddress = addressRequestBody.toPostalAddress()
        synchronized(localAddressMap) {
            return when (val address = localAddressMap[addressId]) {
                null -> None.toMono()
                else -> {
                    localAddressMap[addressId] = address.updateAddress(postCardAddress)
                    localAddressMap[addressId].toOption().toMono()
                }
            }
        }
    }
}