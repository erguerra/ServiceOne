package com.github.erguerra.serviceone

import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class ReceivedData(val name: String, val age: Int)