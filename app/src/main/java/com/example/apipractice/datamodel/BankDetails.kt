package com.example.apipractice.datamodel

import android.os.Parcelable
import androidx.annotation.Keep
import kotlinx.parcelize.Parcelize

@Parcelize
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
) : Parcelable