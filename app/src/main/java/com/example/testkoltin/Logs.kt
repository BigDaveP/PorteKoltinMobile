package com.example.testkoltin

import com.google.gson.annotations.SerializedName

data class Logs(
    @SerializedName("_id") val id: String,
    @SerializedName("username") val name: String,
    @SerializedName("31UID") val UID: String,
    @SerializedName("dateModified") val date: String,
    @SerializedName("serrure") val serrure: String,
)