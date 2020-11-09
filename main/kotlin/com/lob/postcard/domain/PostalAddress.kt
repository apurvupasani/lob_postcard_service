package com.lob.postcard.domain

import com.lob.postcard.domain.response.PostalAddress
import com.lob.postcard.extensions.cleanUp

data class PostalAddress(
    val line1: String?,
    val line2: String?,
    val city: String?,
    val state: String?,
    val zip: String?,
) {
    fun toGetPostalCardAddress(): PostalAddress =
        PostalAddress(
            line1 = line1,
            line2 = line2,
            city = city,
            state = state,
            zip = zip
        )

    val cleanAddress: String =
        "${line1.cleanUp()} ${line2.cleanUp()} ${city.cleanUp()} ${state.cleanUp()} ${zip.cleanUp()} "
}