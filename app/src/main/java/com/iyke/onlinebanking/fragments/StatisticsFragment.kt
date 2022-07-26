package com.iyke.onlinebanking.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Spinner
import androidx.annotation.LayoutRes
import androidx.appcompat.widget.LinearLayoutCompat
import androidx.databinding.BindingAdapter
import androidx.fragment.app.Fragment
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.Navigation
import androidx.recyclerview.widget.RecyclerView
import com.github.mikephil.charting.charts.BarChart
import com.github.mikephil.charting.charts.PieChart
import com.github.mikephil.charting.components.Description
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.data.BarData
import com.github.mikephil.charting.data.BarDataSet
import com.github.mikephil.charting.data.BarEntry
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter
import com.github.mikephil.charting.utils.ColorTemplate
import com.iyke.onlinebanking.R
import com.iyke.onlinebanking.adapters.UniversalRecyclerAdapter
import com.iyke.onlinebanking.model.MonthlySalesData
import com.iyke.onlinebanking.viewmodel.StatisticsViewModel
import com.iyke.onlinebanking.viewmodel.UserDataViewModel


class StatisticsFragment : Fragment() {

    var barChart: BarChart? = null
    var statementViewModel:StatisticsViewModel?= null
    var barEntriesArrayList: ArrayList<BarEntry> = ArrayList()
    var lableName: ArrayList<String> = ArrayList()



    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val v =  inflater.inflate(R.layout.fragment_statistics, container, false)
        statementViewModel = activity?.let { ViewModelProvider(it).get(StatisticsViewModel::class.java) }
        barEntriesArrayList.clear()
        lableName.clear()
        statementViewModel!!.statementArrayList.clear()
        statementViewModel!!.fetchStatement()
        barChart = v.findViewById(R.id.chart) as BarChart


        for (i in statementViewModel!!.statementArrayList.indices) {
            val month = statementViewModel!!.statementArrayList[i].client
            val sales = statementViewModel!!.statementArrayList[i].amount.toInt()
            barEntriesArrayList.add(BarEntry(i.toFloat(), sales.toFloat()))
            lableName.add(month)
        }
        val barDataSet = BarDataSet(barEntriesArrayList, "Monthly Sales")
        barDataSet.setColors(*ColorTemplate.COLORFUL_COLORS)
        val description = Description()
        description.text = "Months"
        barChart!!.description = description
        val barData = BarData(barDataSet)
        barChart!!.data = barData
        val xAxis = barChart!!.xAxis
        xAxis.valueFormatter = IndexAxisValueFormatter(lableName);
        xAxis.setDrawGridLines(false);
        xAxis.setDrawAxisLine(false);
        xAxis.granularity = 1f;
        xAxis.labelCount = lableName.size
        xAxis.labelRotationAngle = 270F;
        barChart!!.animateY(2000);
        barChart!!.invalidate();

        val spinnerPeriods: Spinner = v.findViewById(R.id.spinnerPeriods)
        val adapter = ArrayAdapter.createFromResource(
            requireContext(),
            R.array.periods,
            android.R.layout.simple_spinner_item
        )
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_item)
        spinnerPeriods.adapter = adapter

        v.findViewById<LinearLayoutCompat>(R.id.linearLayoutCompat4).setOnClickListener {
            Navigation.findNavController(it).navigate(R.id.action_statisticsFragment_to_historyFragment)
        }
        return v
    }
}