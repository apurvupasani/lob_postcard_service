package com.lob.postcard.extensions

fun String?.cleanUp() = this?.replace("[^a-zA-Z0-9]", " ")?.trim() ?: ""