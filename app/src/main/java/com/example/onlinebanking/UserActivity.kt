package com.example.onlinebanking

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MotionEvent
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.activity_user.*
import kotlin.system.exitProcess

class UserActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_user)

        phone_number_textView.text = FirebaseAuth.getInstance().currentUser?.phoneNumber.toString()

        imageView_change_pin.setOnTouchListener OnTouchListener@{ v, event ->
            when (event.action){
                MotionEvent.ACTION_DOWN -> {
                    imageView_change_pin.setBackgroundResource(R.drawable.icon_menu_bg_custom_2)
                }
                MotionEvent.ACTION_UP -> {
                    imageView_change_pin.setBackgroundResource(R.drawable.icon_menu_bg_custom_1)
                    val callBox = ConfirmPinDialog(this)
                    callBox.show()
                    callBox.setOnDismissListener {
                        if(callBox.confirmed)
                        {
                            intent = Intent(this,SetNewPinActivity::class.java)
                            startActivity(intent)
                        }
                    }
                }
            }
            return@OnTouchListener true
        }

        imageView_logout.setOnTouchListener OnTouchListener@{ v, event ->
            when (event.action){
                MotionEvent.ACTION_DOWN -> {
                    imageView_logout.setBackgroundResource(R.drawable.icon_menu_bg_custom_2)
                }
                MotionEvent.ACTION_UP -> {
                    imageView_logout.setBackgroundResource(R.drawable.icon_menu_bg_custom_1)
                    FirebaseAuth.getInstance().signOut()
                    intent = Intent(this,RegistrationActivity::class.java)
                    intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK.or(Intent.FLAG_ACTIVITY_NEW_TASK) //kills previous activities
                    startActivity(intent)
                }
            }
            return@OnTouchListener true
        }
        imageView_exit.setOnTouchListener OnTouchListener@{ v, event ->
            when (event.action){
                MotionEvent.ACTION_DOWN -> {
                    imageView_exit.setBackgroundResource(R.drawable.icon_menu_bg_custom_2)
                }
                MotionEvent.ACTION_UP -> {
                    imageView_exit.setBackgroundResource(R.drawable.icon_menu_bg_custom_1)
                    finish()
                    exitProcess(0)
                }
            }
            return@OnTouchListener true
        }
        imageView_send_money.setOnTouchListener OnTouchListener@{ v, event ->
            when (event.action){
                MotionEvent.ACTION_DOWN -> {
                    imageView_send_money.setBackgroundResource(R.drawable.icon_menu_bg_custom_2)
                }
                MotionEvent.ACTION_UP -> {
                    imageView_send_money.setBackgroundResource(R.drawable.icon_menu_bg_custom_1)
                    intent = Intent(this,SendMoneyActivity::class.java)
                    startActivity(intent)
                }
            }
            return@OnTouchListener true
        }

        imageView_lend_money.setOnTouchListener OnTouchListener@{ v, event ->
            when (event.action){
                MotionEvent.ACTION_DOWN -> {
                    imageView_lend_money.setBackgroundResource(R.drawable.icon_menu_bg_custom_2)
                }
                MotionEvent.ACTION_UP -> {
                    imageView_lend_money.setBackgroundResource(R.drawable.icon_menu_bg_custom_1)
                }
            }
            return@OnTouchListener true
        }

        imageView_donation.setOnTouchListener OnTouchListener@{ v, event ->
            when (event.action){
                MotionEvent.ACTION_DOWN -> {
                    imageView_donation.setBackgroundResource(R.drawable.icon_menu_bg_custom_2)
                }
                MotionEvent.ACTION_UP -> {
                    imageView_donation.setBackgroundResource(R.drawable.icon_menu_bg_custom_1)
                }
            }
            return@OnTouchListener true
        }

        imageView_cashout.setOnTouchListener OnTouchListener@{ v, event ->
            when (event.action){
                MotionEvent.ACTION_DOWN -> {
                    imageView_cashout.setBackgroundResource(R.drawable.icon_menu_bg_custom_2)
                }
                MotionEvent.ACTION_UP -> {
                    imageView_cashout.setBackgroundResource(R.drawable.icon_menu_bg_custom_1)
                    intent = Intent(this,CashOutActivity::class.java)
                    startActivity(intent)
                }
            }
            return@OnTouchListener true
        }

        imageView_mobile_recharge.setOnTouchListener OnTouchListener@{ v, event ->
            when (event.action){
                MotionEvent.ACTION_DOWN -> {
                    imageView_mobile_recharge.setBackgroundResource(R.drawable.icon_menu_bg_custom_2)
                }
                MotionEvent.ACTION_UP -> {
                    imageView_mobile_recharge.setBackgroundResource(R.drawable.icon_menu_bg_custom_1)
                }
            }
            return@OnTouchListener true
        }

        imageView_make_payment.setOnTouchListener OnTouchListener@{ v, event ->
            when (event.action){
                MotionEvent.ACTION_DOWN -> {
                    imageView_make_payment.setBackgroundResource(R.drawable.icon_menu_bg_custom_2)
                }
                MotionEvent.ACTION_UP -> {
                    imageView_make_payment.setBackgroundResource(R.drawable.icon_menu_bg_custom_1)
                }
            }
            return@OnTouchListener true
        }

        imageView_pay_bill.setOnTouchListener OnTouchListener@{ v, event ->
            when (event.action){
                MotionEvent.ACTION_DOWN -> {
                    imageView_pay_bill.setBackgroundResource(R.drawable.icon_menu_bg_custom_2)
                }
                MotionEvent.ACTION_UP -> {
                    imageView_pay_bill.setBackgroundResource(R.drawable.icon_menu_bg_custom_1)
                }
            }
            return@OnTouchListener true
        }

        imageView_toll_fee.setOnTouchListener OnTouchListener@{ v, event ->
            when (event.action){
                MotionEvent.ACTION_DOWN -> {
                    imageView_toll_fee.setBackgroundResource(R.drawable.icon_menu_bg_custom_2)
                }
                MotionEvent.ACTION_UP -> {
                    imageView_toll_fee.setBackgroundResource(R.drawable.icon_menu_bg_custom_1)
                    intent = Intent(this,TollFeeActivity::class.java)
                    startActivity(intent)
                }
            }
            return@OnTouchListener true
        }

        imageView_statement.setOnTouchListener OnTouchListener@{ v, event ->
            when (event.action){
                MotionEvent.ACTION_DOWN -> {
                    imageView_statement.setBackgroundResource(R.drawable.icon_menu_bg_custom_2)
                }
                MotionEvent.ACTION_UP -> {
                    imageView_statement.setBackgroundResource(R.drawable.icon_menu_bg_custom_1)
                    intent = Intent(this,StatementActitvity::class.java)
                    startActivity(intent)
                }
            }
            return@OnTouchListener true
        }


    }

}
