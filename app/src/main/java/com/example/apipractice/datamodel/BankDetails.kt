package com.example.apipractice.datamodel

import androidx.annotation.Keep

@Keep
data class BankDetails(
    val accountHolderName: AccountHolderName?,
    val accountNumber: String?,
    val accountType: String?,
    val bank: Bank?,
    val bankBranch: BankBranch?,
    val gstApplicable: Boolean?,
    val gstin: String?,
    val ifsc: String?
)