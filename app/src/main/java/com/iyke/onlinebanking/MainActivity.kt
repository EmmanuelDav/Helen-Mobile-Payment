package com.iyke.onlinebanking

import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.findNavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupWithNavController
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuth.AuthStateListener
import com.iyke.onlinebanking.databinding.ActivityMainBinding
import com.iyke.onlinebanking.viewmodel.UserDataViewModel


class MainActivity : AppCompatActivity() {

    private var authStateListener: AuthStateListener? = null
    var userDataViewModel: UserDataViewModel? = null
    var firebaseAuth:FirebaseAuth?= null
    private  lateinit var  binding:ActivityMainBinding


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        userDataViewModel = ViewModelProvider(this)[UserDataViewModel::class.java]
        val bottomNavigationView = binding.bottomNavigatinView

        val navHostFragment = supportFragmentManager.findFragmentById(R.id.nav_fragment) as? NavHostFragment
        val navController = navHostFragment!!.navController

        bottomNavigationView.setupWithNavController(navController)

        val visibleDestinations = setOf(
            R.id.homeFragment, R.id.cardFragment, R.id.statisticsFragment, R.id.profileFragment
        )

        navController.addOnDestinationChangedListener { _, destination, _ ->
            bottomNavigationView.visibility = if (destination.id in visibleDestinations) View.VISIBLE else View.GONE
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