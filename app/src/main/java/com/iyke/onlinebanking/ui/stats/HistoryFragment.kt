package com.iyke.onlinebanking.ui.stats

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.viewpager2.widget.ViewPager2
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator
import com.iyke.onlinebanking.R
import com.iyke.onlinebanking.ui.adapters.ViewPagerAdapter

class HistoryFragment : Fragment() {

    private lateinit var adapter: ViewPagerAdapter
    private lateinit var viewPager: ViewPager2
    private lateinit var tabLayout: TabLayout

    private val tabTitle = arrayOf(
        "Expense",
        "Income"
    )
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view  = inflater.inflate(R.layout.fragment_history, container, false)
        viewPager = view.findViewById(R.id.viewPager)
        tabLayout = view.findViewById(R.id.tabLayout)
        intiView()
        return view
    }
    private fun intiView() {
        adapter = ViewPagerAdapter(requireActivity())
        adapter.addFragment(SendHistory())
        adapter.addFragment(ReceiveFragment())
        viewPager.adapter = adapter
        TabLayoutMediator(tabLayout, viewPager) { tab, position ->
            tab.text = tabTitle[position]
        }.attach()
    }
}