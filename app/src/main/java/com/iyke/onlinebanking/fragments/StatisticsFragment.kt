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
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.Navigation
import androidx.recyclerview.widget.RecyclerView
import com.github.mikephil.charting.charts.BarChart
import com.github.mikephil.charting.components.Description
import com.github.mikephil.charting.data.BarData
import com.github.mikephil.charting.data.BarDataSet
import com.github.mikephil.charting.data.BarEntry
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter
import com.github.mikephil.charting.utils.ColorTemplate
import com.iyke.onlinebanking.R
import com.iyke.onlinebanking.adapters.UniversalRecyclerAdapter
import com.iyke.onlinebanking.databinding.FragmentStatisticsBinding
import com.iyke.onlinebanking.viewmodel.StatisticsViewModel
import kotlinx.android.synthetic.main.fragment_statistics.*


class StatisticsFragment : Fragment() {

    var statementViewModel: StatisticsViewModel? = null
    var barEntriesArrayList: ArrayList<BarEntry> = ArrayList()
    var lableName: ArrayList<String> = ArrayList()


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val v: FragmentStatisticsBinding =
            DataBindingUtil.inflate(inflater, R.layout.fragment_statistics, container, false)
        v.lifecycleOwner = this
        statementViewModel = activity?.let { ViewModelProvider(it).get(StatisticsViewModel::class.java) }
        v.executePendingBindings()
        statementViewModel!!.fetchStatement()

        stats(v)
        return v.root
    }

    private fun stats(v: FragmentStatisticsBinding) {
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
        v.chart.description = description
        val barData = BarData(barDataSet)
        v.chart.data = barData
        val xAxis = v.chart.xAxis
        xAxis.valueFormatter = IndexAxisValueFormatter(lableName)
        xAxis.setDrawGridLines(false)
        xAxis.setDrawAxisLine(false)
        xAxis.granularity = 1f
        xAxis.labelCount = lableName.size
        xAxis.labelRotationAngle = 270F
        v.chart.animateY(2000)
        v.chart.invalidate()

        val adapter = ArrayAdapter.createFromResource(
            requireContext(),
            R.array.periods,
            android.R.layout.simple_spinner_item
        )
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_item)
        v.spinnerPeriods.adapter = adapter
    }
}