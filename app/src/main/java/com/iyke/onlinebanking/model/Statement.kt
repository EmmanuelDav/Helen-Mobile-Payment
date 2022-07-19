package com.iyke.onlinebanking.model

import kotlinx.android.parcel.Parcelize

class Statement(val amount: String, val type: String, val client: String, val time : com.google.firebase.Timestamp)