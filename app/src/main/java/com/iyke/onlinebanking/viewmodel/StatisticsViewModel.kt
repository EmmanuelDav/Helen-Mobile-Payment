package com.iyke.onlinebanking.viewmodel

import android.app.Application
import android.app.ProgressDialog
import android.content.SharedPreferences
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.navigation.Navigation
import com.github.mikephil.charting.charts.BarChart
import com.github.mikephil.charting.components.Description
import com.github.mikephil.charting.data.BarData
import com.github.mikephil.charting.data.BarDataSet
import com.github.mikephil.charting.data.BarEntry
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter
import com.github.mikephil.charting.utils.ColorTemplate
import com.google.firebase.Timestamp
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import com.iyke.onlinebanking.model.CardDetails
import com.iyke.onlinebanking.model.Statement
import com.iyke.onlinebanking.model.Users
import com.iyke.onlinebanking.utils.Constants

class StatisticsViewModel(application: Application) : AndroidViewModel(application) {

    private val context = getApplication<Application>().applicationContext
    private val db: FirebaseFirestore by lazy { FirebaseFirestore.getInstance() }

    var statementArrayList:ArrayList<Statement> = ArrayList()
    val cards = MutableLiveData<ArrayList<CardDetails>>(ArrayList<CardDetails>())



    val sh: SharedPreferences = context.getSharedPreferences(Constants.PREFERENCE, AppCompatActivity.MODE_PRIVATE)
    private val firebaseEmail = sh.getString(Constants.EMAIL, "")

    fun fetchStatement() {
        db.collection(Constants.USERS).document(firebaseEmail!!)
            .collection(Constants.STATEMENT).orderBy("time", Query.Direction.DESCENDING)
            .get()
            .addOnSuccessListener { documents ->
                for (doc in documents) {
                    val statement = Statement(
                        doc[Constants.AMOUNT].toString(),
                        doc[Constants.TYPE].toString(),
                        doc[Constants.CLIENT_NAME].toString(),
                        doc[Constants.TIME] as Timestamp
                    )
                    statementArrayList.add(statement)
                }
            }.addOnFailureListener {}
    }

    fun fetchCards() {
        db.collection(Constants.USERS).document(firebaseEmail!!)
            .collection(Constants.CARDS)
            .get()
            .addOnSuccessListener { documents ->
                val cardDetails = ArrayList<CardDetails>()
                for (doc in documents) {
                    val card = CardDetails(
                        doc["CardLabel"].toString(),
                        doc["CardName"].toString(),
                        doc["CardNumber"].toString(),
                        doc["ExpireDate"].toString(),
                        doc["CVC"].toString(),
                        doc["ZipCode"].toString(),
                        doc["City"].toString(),
                    )
                    cardDetails.add(card)
                }
                cards.value = cardDetails
            }.addOnFailureListener {}
    }


}