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

    /** Gender Type Keys
     *
     * These Keys are dependent of Configuration defined by Backend in APIs
     * Don't Change it without permission
     * */
    interface GENDER {
        companion object {
            const val MALE = "MALE"
            const val FEMALE = "FEMALE"
            const val TRANSGENDER = "TRANSGENDER"
        }
    }

    /** Banner Type Keys
     *
     * These Keys are dependent of Configuration defined by Backend in APIs
     * Don't Change it without permission
     * */
    interface BANNER_TYPE {
        companion object {
            const val TERMS = "TERMS_CONDITION"
            const val PRIVACY = "PRIVACY_POLICY"
            const val CONTACT_US = "CONTACT_US"
            const val ABOUT = "ABOUT_US"
            const val HOME = "HOME"
            const val REFUND_POLICY = "REFUND_POLICY"
            const val CANCELLATION = "CANCELLATION"
        }
    }
}