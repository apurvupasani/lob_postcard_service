package com.lob.postcard.repository

import com.fasterxml.jackson.annotation.JsonInclude
import com.fasterxml.jackson.core.type.TypeReference
import com.fasterxml.jackson.databind.DeserializationFeature
import com.fasterxml.jackson.databind.MapperFeature
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import com.lob.postcard.domain.PostalAddress
import com.lob.postcard.domain.request.PostCardAddressAddRequestBody
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Repository
import reactor.core.publisher.Mono
import reactor.kotlin.core.publisher.toMono
import javax.annotation.PostConstruct
import kotlin.reflect.jvm.internal.impl.load.java.structure.JavaClass

@Repository
open class PostalAddressRepository {

    private val localAddresses: MutableSet<PostalAddress> = mutableSetOf()

    val logger: Logger get() = LoggerFactory.getLogger(javaClass)

    @PostConstruct
    fun init() {
        val objectMapper = jacksonObjectMapper()
            .configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false)
            .configure(DeserializationFeature.FAIL_ON_MISSING_CREATOR_PROPERTIES, true)
            .setSerializationInclusion(JsonInclude.Include.NON_NULL)
        objectMapper
            .readValue(
                javaClass.getResource("/addresses.json").readText(),
                object : TypeReference<List<PostCardAddressAddRequestBody>>() {}
            ).map { localAddresses.add(it.toPostalAddress()) }
    }

    fun getAddresses(query: String): Mono<List<PostalAddress>> {
        synchronized(localAddresses) {
            return localAddresses.filter { it.cleanAddress.contains(query, true) }.toMono()
        }
    }

    fun addAddress(addressAddRequestBody: PostCardAddressAddRequestBody): Mono<Boolean> {
        val postCardAddress = addressAddRequestBody.toPostalAddress()
        synchronized(localAddresses) {
            return localAddresses.add(postCardAddress).toMono()
        }
    }
}