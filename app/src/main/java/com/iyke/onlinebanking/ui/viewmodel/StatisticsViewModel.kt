package com.iyke.onlinebanking.ui.viewmodel

import android.app.Application
import android.content.SharedPreferences
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.navigation.Navigation
import com.google.firebase.Timestamp
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import com.iyke.onlinebanking.R
import com.iyke.onlinebanking.intface.CardClicked
import com.iyke.onlinebanking.models.CardDetails
import com.iyke.onlinebanking.models.Statement
import com.iyke.onlinebanking.utils.Constants

class StatisticsViewModel(application: Application) : AndroidViewModel(application),CardClicked<CardDetails> {

    private val context = getApplication<Application>().applicationContext
    private val db: FirebaseFirestore by lazy { FirebaseFirestore.getInstance() }
    var cardListener: CardClicked<CardDetails> = this

    var statementArrayList:ArrayList<Statement> = ArrayList()
    val cards = MutableLiveData<ArrayList<CardDetails>>(ArrayList<CardDetails>())
    lateinit var cardDetails: View



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
                        doc[Constants.TIME] as Timestamp,
                    doc[Constants.MESSAGE].toString()
                    )
                    statementArrayList.add(statement)
                }
            }.addOnFailureListener {}
    }

    fun fetchCards(view: View) {

        cardDetails = view

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

    override fun onCardClick(cards: CardDetails) {
        val bundle = Bundle()
        bundle.putParcelable("Cards", cards)
        Navigation.findNavController(cardDetails).navigate(R.id.action_cardFragment_to_cardDetails, bundle)
    }

}