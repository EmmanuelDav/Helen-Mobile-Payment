package com.iyke.onlinebanking.ui.model

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
class CardDetails(
    var cardLabel: String,
    var cardName: String,
    var cardNumber: String,
    var cardDate: String,
    var cardCVC: String,
    var zipCode: String,
    var city: String
): Parcelable