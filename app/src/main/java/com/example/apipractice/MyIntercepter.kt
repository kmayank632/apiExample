package com.example.apipractice

import okhttp3.Interceptor
import okhttp3.Response

class MyIntercepter : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        val request = chain.request()
            .newBuilder()
            .addHeader("Accept:", "application/json")
            .addHeader(
                "Authorization",
                "Bearer eyJhbGciOiJSUzUxMiIsInR5cCI6IkpXVCJ9.eyJfbWFzdGVyIjoiNjAzOGM0MGFkMjQ5MWU0ZTZmMzFhZmNkIiwiX3VzZXIiOiI2MDM4YzQwYmQyNDkxZTRlNmYzMWFmY2YiLCJpYXQiOjE2MjEzMTQ4OTQsImV4cCI6MTYyOTA5MDg5NCwiYXVkIjoidW5kZWZpbmVkIiwiaXNzIjoiTWVkb3BsdXMiLCJzdWIiOiI3NTA4MDc5NTkxIiwianRpIjoiZTJlYzk4ZTItNDczZS00Y2E4LWJkZmUtZGY4ZTZhNzZkY2I5In0.XBWmopybzssfPBSl0PZE9tmiDEbXvCy2J97oqRoluEr1vPaSjQzBeqDJ7zceEbDmSeIafTDWAgQ87KlXFBBI0ya6-fXRoGGalaTXF9tgQmzi8_A4PuB4mOrlAGEVipRYAO60l4y7E5RvIQtpDmwp1nFEwgMldex4rjRNdGFzBOQ")
            .build()
        return chain.proceed(request)
    }
}