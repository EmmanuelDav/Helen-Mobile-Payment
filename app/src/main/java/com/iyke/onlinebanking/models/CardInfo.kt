package com.iyke.onlinebanking.models

import android.os.Parcelable
import kotlinx.parcelize.Parcelize


@Parcelize
class CardInfo (
    var cardLabel:String?= "",
    var cardName:String?="",
    var cardNumber:String?="",
    var cardExpiryDate:String?="",
    var usersZipCode:String?="",
    var usersCity:String?="",
    var cardCVC:String?="",
):Parcelable