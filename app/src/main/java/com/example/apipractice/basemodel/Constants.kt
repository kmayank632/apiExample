package com.example.apipractice.basemodel

object Constants {

    const val KEY="KEY"

    interface USER_TYPE {
        companion object {
            val PATIENT = "PATIENT"
            val VLE = "VLE"
            val DOCTOR = "DOCTOR"
            val DIAGNOSTIC = "DIAGNOSTIC"
        }
    }
}