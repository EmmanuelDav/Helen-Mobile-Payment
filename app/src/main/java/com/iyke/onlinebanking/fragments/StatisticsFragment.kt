package com.iyke.onlinebanking.fragments

import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Spinner
import androidx.fragment.app.Fragment
import com.github.mikephil.charting.charts.BarChart
import com.github.mikephil.charting.charts.PieChart
import com.github.mikephil.charting.data.*
import com.github.mikephil.charting.utils.ColorTemplate
import com.iyke.onlinebanking.R


class StatisticsFragment : Fragment() {

    lateinit var barEntriesList: ArrayList<BarEntry>
   lateinit var  pieChart: PieChart

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val v =  inflater.inflate(R.layout.fragment_statistics, container, false)
        val chart: BarChart = v.findViewById(R.id.chart) as BarChart
        val barDataSet1 = BarDataSet(getBarChartDataForSet1(), "Brand 1")
        barDataSet1.color = Color.rgb(0, 155, 0)
        val barDataSet2 = BarDataSet(getBarChartDataForSet2(), "Brand 2")
        barDataSet2.setColors(*ColorTemplate.COLORFUL_COLORS)
        val data  = BarData(barDataSet1, barDataSet2)
        chart.data = data
        chart.animateXY(2000, 2000)
        chart.invalidate()

        val spinnerPeriods: Spinner = v.findViewById(R.id.spinnerPeriods)
        val adapter = ArrayAdapter.createFromResource(
            requireContext(),
            R.array.periods,
            android.R.layout.simple_spinner_item
        )

        adapter.setDropDownViewResource(android.R.layout.simple_spinner_item)
        spinnerPeriods.adapter = adapter

        return v
    }

    private fun getBarChartDataForSet1(): ArrayList<BarEntry> {
        barEntriesList = ArrayList()
        barEntriesList.add(BarEntry(1f, 1f))
        barEntriesList.add(BarEntry(2f, 2f))
        barEntriesList.add(BarEntry(3f, 3f))
        barEntriesList.add(BarEntry(4f, 4f))
        barEntriesList.add(BarEntry(5f, 5f))
        return barEntriesList
    }

    private fun getBarChartDataForSet2(): ArrayList<BarEntry> {
        barEntriesList = ArrayList()
        barEntriesList.add(BarEntry(1f, 10f))
        barEntriesList.add(BarEntry(2f, 8f))
        barEntriesList.add(BarEntry(3f, 6f))
        barEntriesList.add(BarEntry(4f, 4f))
        barEntriesList.add(BarEntry(5f, 2f))
        return barEntriesList
    }

}