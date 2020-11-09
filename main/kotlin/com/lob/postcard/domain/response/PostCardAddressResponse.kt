package com.lob.postcard.domain.response
import com.fasterxml.jackson.annotation.JsonInclude
import com.fasterxml.jackson.annotation.JsonInclude.Include.NON_EMPTY
import com.fasterxml.jackson.annotation.JsonInclude.Include.NON_NULL

@JsonInclude(NON_NULL)
data class PostCardAddressResponse(
    @JsonInclude(NON_EMPTY) val addresses: List<PostalAddress>? = null,
    @JsonInclude(NON_EMPTY) val errors: List<String>? = null
)