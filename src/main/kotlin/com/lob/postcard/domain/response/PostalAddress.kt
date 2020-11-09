package com.lob.postcard.domain.response

import com.fasterxml.jackson.annotation.JsonInclude
import com.fasterxml.jackson.annotation.JsonInclude.Include.NON_EMPTY
import com.fasterxml.jackson.annotation.JsonInclude.Include.NON_NULL

@JsonInclude(NON_NULL)
data class PostalAddress(
    @JsonInclude(NON_EMPTY) val line1: String? = null,
    @JsonInclude(NON_EMPTY) val line2: String? = null,
    @JsonInclude(NON_EMPTY) val city: String? = null,
    @JsonInclude(NON_EMPTY) val state: String? = null,
    @JsonInclude(NON_EMPTY) val zip: String? = null,
)