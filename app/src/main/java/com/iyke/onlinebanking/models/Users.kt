package com.iyke.onlinebanking.models

import android.os.Parcelable
import android.widget.ImageView
import androidx.databinding.BindingAdapter
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.google.firebase.firestore.PropertyName
import com.iyke.onlinebanking.R
import com.iyke.onlinebanking.utils.Constants.BALANCE
import com.iyke.onlinebanking.utils.Constants.EMAIL
import com.iyke.onlinebanking.utils.Constants.NAME
import com.iyke.onlinebanking.utils.Constants.PHONE_NUMBER
import com.iyke.onlinebanking.utils.Constants.PROFILE
import kotlinx.android.parcel.Parcelize

@Parcelize
class Users(
    @get: PropertyName(NAME) @set: PropertyName(NAME) var name: String = "",
    @get: PropertyName(EMAIL) @set: PropertyName(EMAIL) var email: String = "",
    @get: PropertyName(PHONE_NUMBER) @set: PropertyName(PHONE_NUMBER) var phoneNumber: String = "",
    @get: PropertyName(PROFILE) @set: PropertyName(PROFILE) var profileUri: String = "",
    @get: PropertyName(BALANCE) @set: PropertyName(BALANCE) var balance: Long = 0,
) : Parcelable{
    companion object{
        @BindingAdapter("profileImage")
        @JvmStatic
        fun loadImage(view: ImageView, imageUrl: String?) {
            Glide.with(view.context)
                .load(imageUrl).error(R.drawable.ic_round_person_24).apply(RequestOptions().centerCrop())
                .into(view)
        }
    }
}