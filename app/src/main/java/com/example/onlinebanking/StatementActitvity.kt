package com.example.onlinebanking

import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import com.google.firebase.firestore.auth.User
import com.xwray.groupie.GroupAdapter
import com.xwray.groupie.Item
import com.xwray.groupie.ViewHolder
import kotlinx.android.synthetic.main.activity_statement_actitvity.*
import kotlinx.android.synthetic.main.statement_row.view.*
import java.sql.Timestamp

class StatementActitvity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_statement_actitvity)

        if(!CheckInternet(this).checkNow())
        {
            finish()
        }

        val db = FirebaseFirestore.getInstance()
        db.collection("users").document(FirebaseAuth.getInstance().currentUser?.phoneNumber.toString())
        .get()
            .addOnSuccessListener { document ->
                textView_statements_balance.text = "Balance: "+document["balance"].toString()
            }
            .addOnFailureListener {

            }

        recyclerView_statements.layoutManager = LinearLayoutManager(this)
        fetchData()


    }

    private fun fetchData()
    {
        val db = FirebaseFirestore.getInstance()
        val adapter = GroupAdapter<ViewHolder>()
        db.collection("users").document(FirebaseAuth.getInstance().currentUser?.phoneNumber.toString()).collection("statements").orderBy("time", Query.Direction.DESCENDING)
            .get()
            .addOnSuccessListener { documents ->
                for (doc in documents)
                {
                    val statement = Statement(doc["amount"].toString(), doc["from"].toString(), doc["client_number"].toString(), doc["time"] as com.google.firebase.Timestamp)
                    adapter.add(StatementItem(statement))
                }
                recyclerView_statements.adapter = adapter
            }
            .addOnFailureListener {

            }
    }

}

class StatementItem(val statement: Statement): Item<ViewHolder>() {
    override fun getLayout(): Int {
        return R.layout.statement_row
    }

    override fun bind(viewHolder: ViewHolder, position: Int) {
        viewHolder.itemView.textView_srow_amount.text = "Amount: "+statement.amount
        if (statement.type == "client"){
            viewHolder.itemView.textView_srow_type.setTextColor(Color.YELLOW)
            viewHolder.itemView.textView_srow_type.text = "Credit"
            viewHolder.itemView.textView_srow_client.text = "From: "+statement.client
        }
        else
        {
            viewHolder.itemView.textView_srow_type.setTextColor(Color.RED)
            viewHolder.itemView.textView_srow_type.text = "Debit"
            viewHolder.itemView.textView_srow_client.text = "To: "+statement.client
        }
        viewHolder.itemView.textView_srow_time.text = statement.time.toDate().toString().subSequence(0, 19)
    }
}


