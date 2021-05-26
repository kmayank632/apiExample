package com.example.apipractice.datamodel

data class UpdateCustomerProfile(
    val `data`: Data?,
    val message: String?,
    val status: Boolean?
) {
    data class Data(
        val __v: Int?,
        val _draft: String?,
        val _family: String?,
        val _id: String?,
        val _master: String?,
        val _vle: Vle?,
        val address: Address?,
        val aggregateRating: Int?,
        val alternateNumber: String?,
        val alternateNumber2: String?,
        val bloodGroup: String?,
        val createdAt: String?,
        val dob: String?,
        val documents: List<Document?>?,
        val email: String?,
        val existingAilments: ExistingAilments?,
        val firstName: FirstName?,
        val gender: String?,
        val healthInsurance: HealthInsurance?,
        val lastName: LastName?,
        val medoplusId: String?,
        val number: String?,
        val pictures: List<Picture?>?,
        val role: String?,
        val status: String?,
        val updatedAt: String?
    ) {
        data class Vle(
            val _id: String?,
            val firstName: FirstName?,
            val medoplusId: String?
        ) {
            data class FirstName(
                val en: String?,
                val hi: String?
            )
        }

        data class Address(
            val _id: String?,
            val block: Block?,
            val district: District?,
            val districtCode: Int?,
            val geo: List<Int?>?,
            val line1: Line1?,
            val line2: Line2?,
            val state: State?,
            val stateCode: Int?,
            val stateCodeAlpha: String?,
            val zipcode: String?
        ) {
            data class Block(
                val en: String?,
                val hi: String?
            )

            data class District(
                val en: String?,
                val hi: String?
            )

            data class Line1(
                val en: String?,
                val hi: String?
            )

            data class Line2(
                val en: String?,
                val hi: String?
            )

            data class State(
                val en: String?,
                val hi: String?
            )
        }

        data class Document(
            val _id: String?,
            val preview: String?,
            val type: String?,
            val url: String?
        )

        data class ExistingAilments(
            val ailment: List<Ailment?>?,
            val isCovidVaccined: Boolean?
        ) {
            data class Ailment(
                val _id: String?,
                val name: Name?,
                val pictures: List<Picture?>?
            ) {
                data class Name(
                    val en: String?,
                    val hi: String?
                )

                data class Picture(
                    val type: String?,
                    val url: String?
                )
            }
        }

        data class FirstName(
            val en: String?,
            val hi: String?
        )

        data class HealthInsurance(
            val attachments: List<Attachment?>?,
            val companyName: String?,
            val isActive: Boolean?,
            val policyNumber: String?
        ) {
            data class Attachment(
                val _id: String?,
                val preview: String?,
                val type: String?,
                val url: String?
            )
        }

        data class LastName(
            val en: String?,
            val hi: String?
        )

        data class Picture(
            val _id: String?,
            val preview: String?,
            val type: String?,
            val url: String?
        )
    }
}