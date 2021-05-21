package com.example.apipractice.basemodel


import androidx.annotation.Keep
import com.google.gson.annotations.SerializedName

@Keep
open class BaseModel(
    @SerializedName("status") val status: Boolean = false,
    @SerializedName("message") val message: String = "",

    /**
     * Because this class is also used as Error Model
     * So retryRequire param is used to identity that
     * error can be fix with retry or not.
     *
     * the para will not set from any api
     * */
    val retryRequire: Boolean = true
) {
    override fun toString(): String = "${javaClass.simpleName} : STATUS: $status MESSAGE: $message"

}

