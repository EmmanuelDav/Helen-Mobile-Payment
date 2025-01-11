package com.iyke.onlinebanking.ui.activities

import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.findNavController
import androidx.navigation.ui.setupWithNavController
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuth.AuthStateListener
import com.iyke.onlinebanking.R
import com.iyke.onlinebanking.utils.Constants
import com.iyke.onlinebanking.viewmodel.UserDataViewModel
import kotlinx.android.synthetic.main.activity_main.*


class MainActivity : AppCompatActivity() {

    private var authStateListener: AuthStateListener? = null
    var userDataViewModel: UserDataViewModel? = null
    var firebaseAuth:FirebaseAuth?= null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        userDataViewModel = ViewModelProvider(this).get(UserDataViewModel::class.java)
        val bottomNavigationView = bottom_navigatin_view


        val navController = findNavController(R.id.nav_fragment)
        bottomNavigationView.setupWithNavController(navController)
        navController.addOnDestinationChangedListener { _, destination, _ ->
            when (destination.id) {
                R.id.homeFragment -> bottomNavigationView.visibility = View.VISIBLE
                R.id.cardFragment -> bottomNavigationView.visibility = View.VISIBLE
                R.id.statisticsFragment -> bottomNavigationView.visibility = View.VISIBLE
                R.id.profileFragment -> bottomNavigationView.visibility = View.VISIBLE
                else -> bottomNavigationView.visibility = View.GONE
            }
        }
        firebaseAuth = FirebaseAuth.getInstance()
        authStateListener = AuthStateListener { firebaseAuth ->
            val user = firebaseAuth.currentUser

            if (user != null) {

                Log.d("TAG", "onAuthStateChanged:signed_in:" + user.email+user.phoneNumber+user.displayName);

            } else {

                Log.d("TAG", "onAuthStateChanged:signed_out");
            }
        }
    }

    override fun onStart() {
        super.onStart()
        firebaseAuth!!.addAuthStateListener(authStateListener!!)
    }

    override fun onStop() {
        super.onStop()
        if (authStateListener != null) {
            firebaseAuth!!.removeAuthStateListener(authStateListener!!)
        }
    }
}