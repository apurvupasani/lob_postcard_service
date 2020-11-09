package com.lob.postcard.repository

import com.lob.postcard.domain.PostalAddress
import com.lob.postcard.domain.request.PostCardAddressAddRequestBody
import org.springframework.stereotype.Repository
import reactor.core.publisher.Mono
import reactor.kotlin.core.publisher.toMono

@Repository
class PostalAddressRepository {
    private val localAddresses: MutableSet<PostalAddress> = mutableSetOf()

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