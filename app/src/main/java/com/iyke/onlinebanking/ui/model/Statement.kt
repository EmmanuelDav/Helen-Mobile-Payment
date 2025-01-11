package com.iyke.onlinebanking.ui.model

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
class Statement(
    val amount: String,
    val type: String,
    val client: String,
    val time: com.google.firebase.Timestamp,
    var message: String
):Parcelable