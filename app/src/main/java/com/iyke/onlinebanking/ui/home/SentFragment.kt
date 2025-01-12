package com.iyke.onlinebanking.ui.home

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.LayoutRes
import androidx.databinding.BindingAdapter
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.iyke.onlinebanking.R
import com.iyke.onlinebanking.adapters.UniversalRecyclerAdapter
import com.iyke.onlinebanking.databinding.FragmentSentBinding
import com.iyke.onlinebanking.viewmodel.UserDataViewModel

class SentFragment : Fragment() {

    var userDataViewModel: UserDataViewModel? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val v: FragmentSentBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_sent, container, false)
        v.lifecycleOwner = this
        userDataViewModel = ViewModelProvider(this).get(UserDataViewModel::class.java)
        v.exitSend.setOnClickListener { findNavController().popBackStack() }
        v.model = userDataViewModel
        v.executePendingBindings()
        v.model?.fetchUsers(v.root)
        return v.root
    }
}

@BindingAdapter(value = ["tools:data","tools:itemList", "tools:itemListener"], requireAll = false)
fun <T> setAdapter(recyclerView: RecyclerView, data : MutableLiveData<ArrayList<T>>, @LayoutRes listItem : Int = R.layout.item_recent_contacts, itemListener: Any){
    if (recyclerView.adapter == null){
        recyclerView.adapter =  UniversalRecyclerAdapter(listItem, data.value ?: ArrayList(), itemListener)
    }else{
        if (recyclerView.adapter is UniversalRecyclerAdapter<*>) {
            val items = data.value ?: ArrayList()
            (recyclerView.adapter as UniversalRecyclerAdapter<T>).updateData(items)
        }
    }
}