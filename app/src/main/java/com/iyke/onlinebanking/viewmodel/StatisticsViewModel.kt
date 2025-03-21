package com.iyke.onlinebanking.viewmodel

import android.app.Application
import android.content.SharedPreferences
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.navigation.Navigation
import com.google.firebase.Timestamp
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import com.iyke.onlinebanking.R
import com.iyke.onlinebanking.models.BankStatements
import com.iyke.onlinebanking.models.CardInfo
import com.iyke.onlinebanking.ui.bind.CardClicked
import com.iyke.onlinebanking.ui.card.CardDetails
import com.iyke.onlinebanking.utils.Constants

class StatisticsViewModel(application: Application) : AndroidViewModel(application),
    CardClicked<CardInfo> {

    private val context = getApplication<Application>().applicationContext
    private val db: FirebaseFirestore by lazy { FirebaseFirestore.getInstance() }
    var cardListener: CardClicked<CardInfo> = this

    var statementArrayList:ArrayList<BankStatements> = ArrayList()
    val cards = MutableLiveData<ArrayList<CardInfo>>(ArrayList<CardInfo>())
    lateinit var cardDetails: View

    private val firebaseUser = FirebaseAuth.getInstance().currentUser
    val firebaseEmail = firebaseUser?.email  // User's email
    val displayName = firebaseUser?.displayName

    fun fetchStatement() {
        db.collection(Constants.USERS).document(firebaseEmail!!)
            .collection(Constants.STATEMENT).orderBy("time", Query.Direction.DESCENDING)
            .get()
            .addOnSuccessListener { documents ->
                for (doc in documents) {
                    val statement = BankStatements(
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
                val cardDetails = ArrayList<CardInfo>()
                for (doc in documents) {
                    val card = CardInfo(
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

    override fun onCardClick(cards: CardInfo) {
        val bundle = Bundle()
        bundle.putParcelable("Cards", cards)
        Navigation.findNavController(cardDetails).navigate(R.id.action_cardFragment_to_cardDetails, bundle)
    }

}