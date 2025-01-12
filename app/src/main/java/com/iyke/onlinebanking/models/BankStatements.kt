package com.iyke.onlinebanking.models

import android.os.Parcelable
import com.google.firebase.Timestamp
import kotlinx.parcelize.Parcelize


@Parcelize
class BankStatements(
    var amount: String? = "",
    var type: String? = "",
    var sender: String? = "",
    val time: com.google.firebase.Timestamp,
    var message: String? = ""
) : Parcelable