package com.iyke.onlinebanking.fragments

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.LayoutRes
import androidx.databinding.BindingAdapter
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.iyke.onlinebanking.R
import com.iyke.onlinebanking.adapters.UniversalRecyclerAdapter
import com.iyke.onlinebanking.databinding.FragmentHomeBinding
import com.iyke.onlinebanking.viewmodel.UserDataViewModel

class HomeFragment : Fragment() {

    var userDataViewModel: UserDataViewModel? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val v: FragmentHomeBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_home, container, false)
        v.lifecycleOwner = this
        userDataViewModel = ViewModelProvider(this).get(UserDataViewModel::class.java)
        v.viewmodel = userDataViewModel
        v.executePendingBindings()
        v.viewmodel?.fetchUserDetails()
        v.viewmodel?.fetchStatement(v.root)
        return v.root
    }

}


@BindingAdapter(value = ["tools:statement", "tools:layout", "tools:onclick"], requireAll = false)
fun <T> setHomeAdapter(
    recyclerView: RecyclerView,
    statement: MutableLiveData<ArrayList<T>>,
    @LayoutRes layout: Int = R.layout.item_recent,
    listener: Any
) {
    if (recyclerView.adapter == null) {
        recyclerView.adapter =
            UniversalRecyclerAdapter(layout, statement.value ?: ArrayList(), listener)
    } else {
        if (recyclerView.adapter is UniversalRecyclerAdapter<*>) {
            val items = statement.value ?: ArrayList()
            (recyclerView.adapter as UniversalRecyclerAdapter<T>).updateData(items)
        }
    }
}