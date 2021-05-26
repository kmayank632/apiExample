package com.example.apipractice.datamodel

data class CustomerProfileModel(
    val `data`: Data? = null ,
    val message: String? = null,
    val status: Boolean? = null
) {
    data class Data(
        val __v: Int? = null,
        val _draft: String? = null,
        val _family: String? = null,
        val _id: String? = null,
        val _vle: Vle? = null,
        val address: Address? = null,
        val alternateNumber: String? = null,
        val alternateNumber2: String? = null,
        val bloodGroup: String? = null,
        val dob: String? = null,
        val documents: List<Document?>? = null,
        val email: String? = null,
        val existingAilments: ExistingAilments? = null,
        val firstName: FirstName? = null,
        val gender: String? = null,
        val healthInsurance: HealthInsurance? = null,
        val lastName: LastName? = null,
        val medoplusId: String? = null,
        val number: String? = null,
        val pictures: List<Picture?>? = null,
        val role: String? = null
    ) {
        data class Vle(
            val _id: String? = null,
            val firstName: FirstName? = null,
            val medoplusId: String? = null
        ) {
            data class FirstName(
                val en: String? = null,
                val hi: String? = null
            )
        }

        data class Address(
            val _id: String? = null,
            val block: Block? = null,
            val district: District? = null,
            val districtCode: Int? = null,
            val geo: List<Int?>? = null,
            val line1: Line1? = null,
            val line2: Line2? = null,
            val state: State? = null,
            val stateCode: Int? = null,
            val stateCodeAlpha: String? = null,
            val zipcode: String? = null
        ) {
            data class Block(
                val en: String? = null,
                val hi: String? = null
            )

            data class District(
                val en: String? = null,
                val hi: String? = null
            )

            data class Line1(
                val en: String? = null,
                val hi: String? = null
            )

            data class Line2(
                val en: String? = null,
                val hi: String? = null
            )

            data class State(
                val en: String? = null,
                val hi: String? = null
            )
        }

        data class Document(
            val _id: String? = null,
            val preview: String? = null,
            val type: String? = null,
            val url: String? = null
        )

        data class ExistingAilments(
            val ailment: List<Ailment?>? = null,
            val isCovidVaccined: Boolean? = null
        ) {
            data class Ailment(
                val _id: String? = null,
                val name: Name? = null,
                val pictures: List<Picture?>? = null
            ) {
                data class Name(
                    val en: String? = null
                )

                data class Picture(
                    val type: String? = null,
                    val url: String? = null
                )
            }
        }

        data class FirstName(
            val en: String? = null,
            val hi: String? = null
        )

        data class HealthInsurance(
            val attachments: List<Attachment?>? = null,
            val companyName: String? = null,
            val isActive: Boolean? = null,
            val policyNumber: String? = null
        ) {
            data class Attachment(
                val _id: String? = null,
                val preview: String? = null,
                val type: String? = null,
                val url: String? = null
            )
        }

        data class LastName(
            val en: String? = null,
            val hi: String? = null
        )

        data class Picture(
            val _id: String? = null,
            val preview: String? = null,
            val type: String? = null,
            val url: String? = null
        )
    }
}