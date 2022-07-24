package com.iyke.onlinebanking.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Spinner
import androidx.appcompat.widget.LinearLayoutCompat
import androidx.fragment.app.Fragment
import androidx.navigation.Navigation
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
import com.iyke.onlinebanking.model.MonthlySalesData


class StatisticsFragment : Fragment() {

    var barChart: BarChart? = null
    var barEntriesArrayList: ArrayList<BarEntry> = ArrayList()
    var lableName: ArrayList<String> = ArrayList()
    var monthlySalesDataArrayList: ArrayList<MonthlySalesData> = ArrayList()


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val v =  inflater.inflate(R.layout.fragment_statistics, container, false)
        barChart = v.findViewById(R.id.chart) as BarChart
        fillMonthlySalesArrayList();
        for (i in monthlySalesDataArrayList.indices) {
            val month = monthlySalesDataArrayList[i].month
            val sales = monthlySalesDataArrayList[i].sales
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
        xAxis.labelCount = lableName!!.size
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
    private fun fillMonthlySalesArrayList() {
        monthlySalesDataArrayList.clear()
        monthlySalesDataArrayList.add(MonthlySalesData("Jan", 242000))
        monthlySalesDataArrayList.add(MonthlySalesData("Feb", 300000))
        monthlySalesDataArrayList.add(MonthlySalesData("Mar", 150000))
        monthlySalesDataArrayList.add(MonthlySalesData("Apr", 250000))
        monthlySalesDataArrayList.add(MonthlySalesData("May", 242000))
        monthlySalesDataArrayList.add(MonthlySalesData("June", 300000))
        monthlySalesDataArrayList.add(MonthlySalesData("July", 150000))
        monthlySalesDataArrayList.add(MonthlySalesData("Aug", 210000))
        monthlySalesDataArrayList.add(MonthlySalesData("Sep", 242000))
        monthlySalesDataArrayList.add(MonthlySalesData("Oct", 320000))
        monthlySalesDataArrayList.add(MonthlySalesData("Nov", 150000))
        monthlySalesDataArrayList.add(MonthlySalesData("Dec EGypt", 200000))
    }
}