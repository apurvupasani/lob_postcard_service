package com.lob.postcard.domain.request

import com.lob.postcard.domain.PostalAddress

data class PostCardAddressAddRequestBody(
    val line1: String?,
    val line2: String?,
    val city: String?,
    val state: String?,
    val zip: String?,
) {
    fun toPostalAddress(): PostalAddress =
        PostalAddress(
            line1 = line1,
            line2 = line2,
            city = city,
            state = state,
            zip = zip
        )
}