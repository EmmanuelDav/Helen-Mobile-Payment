package com.iyke.onlinebanking.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import androidx.annotation.LayoutRes
import androidx.appcompat.widget.LinearLayoutCompat
import androidx.databinding.BindingAdapter
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.Navigation
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.button.MaterialButton
import com.iyke.onlinebanking.R
import com.iyke.onlinebanking.adapters.UniversalRecyclerAdapter
import com.iyke.onlinebanking.databinding.FragmentCardBinding
import com.iyke.onlinebanking.viewmodel.ProfileViewModel
import com.iyke.onlinebanking.viewmodel.StatisticsViewModel

class CardFragment : Fragment() {

    private var statisticsViewModel: StatisticsViewModel? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        val v:FragmentCardBinding = DataBindingUtil.inflate(inflater,R.layout.fragment_card, container, false)
        v.lifecycleOwner = this
        statisticsViewModel = ViewModelProvider(this).get(StatisticsViewModel::class.java)
        v.addCards.setOnClickListener {
            Navigation.findNavController(it).navigate(R.id.action_cardFragment_to_addCardFragemnt)
        }
        v.viewmodel = statisticsViewModel
        v.executePendingBindings()
        v.viewmodel?.fetchCards()
        return v.root
    }
}


@BindingAdapter(value = ["tools:card", "tools:cardItem", "tools:cardClick"], requireAll = false)
fun <T> setCardAdapter(
    recyclerView: RecyclerView,
    statement: MutableLiveData<ArrayList<T>>,
    @LayoutRes layout: Int = R.layout.card_items,
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