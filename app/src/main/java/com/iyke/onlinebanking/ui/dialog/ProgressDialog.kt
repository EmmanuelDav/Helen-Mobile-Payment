package com.iyke.onlinebanking.ui.dialog

import android.app.Dialog
import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import com.iyke.onlinebanking.R

class ProgressDialog( activity:Context ) : Dialog(activity) {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_progress_dialog)

        window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        setCanceledOnTouchOutside(false)
        setOnCancelListener {
            dismiss()
        }
    }

}