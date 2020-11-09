package com.lob.postcard.domain

import com.lob.postcard.domain.response.PostalAddress as ResponsePostalAddress
import com.lob.postcard.extensions.cleanUp

data class PostalAddress(
    val line1: String?,
    val line2: String?,
    val city: String?,
    val state: String?,
    val zip: String?,
) {
    fun toResponsePostalCardAddress(): ResponsePostalAddress =
        ResponsePostalAddress(
            line1 = line1,
            line2 = line2,
            city = city,
            state = state,
            zip = zip
        )

    fun updateAddress(other: PostalAddress) =
        this.copy(
            line1 = other.line1 ?: line1,
            line2 = other.line2 ?: line2,
            city = other.city ?: city,
            state = other.state ?: state,
            zip = other.zip ?: zip
        )

    val cleanAddress: String =
        "${line1.cleanUp()} ${line2.cleanUp()} ${city.cleanUp()} ${state.cleanUp()} ${zip.cleanUp()} "
}