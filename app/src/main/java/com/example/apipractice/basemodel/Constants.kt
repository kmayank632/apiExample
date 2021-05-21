package com.example.apipractice.basemodel

object Constants {

    const val KEY="KEY"


    /** User Type Keys
     *
     * These Keys are dependent of Configuration defined by Backend in APIs
     * Don't Change it without permission
     * */
    interface USER_TYPE {
        companion object {
            val PATIENT = "PATIENT"
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
            const val HOME = "HOME"
        }
    }
}