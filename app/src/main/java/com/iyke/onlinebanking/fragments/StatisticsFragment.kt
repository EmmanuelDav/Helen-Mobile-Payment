package com.iyke.onlinebanking.fragments

import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.github.mikephil.charting.animation.Easing
import com.github.mikephil.charting.charts.BarChart
import com.github.mikephil.charting.charts.PieChart
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.components.Legend
import com.github.mikephil.charting.data.*
import com.github.mikephil.charting.formatter.PercentFormatter
import com.github.mikephil.charting.utils.ColorTemplate
import com.iyke.onlinebanking.R
import java.text.DecimalFormat


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

        // pie
        pieChart = v.findViewById(R.id.activity_main_piechart);
        setupPieChart();
        loadPieChartData();

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

    private fun loadPieChartData() {
        val entries: ArrayList<PieEntry> = ArrayList()
        entries.add(PieEntry(0.2f, "Food & Dining"))
        entries.add(PieEntry(0.15f, "Medical"))
        entries.add(PieEntry(0.10f, "Entertainment"))
        entries.add(PieEntry(0.25f, "Electricity and Gas"))
        entries.add(PieEntry(0.3f, "Housing"))
        val colors: ArrayList<Int> = ArrayList()
        for (color in ColorTemplate.MATERIAL_COLORS) {
            colors.add(color)
        }
        for (color in ColorTemplate.VORDIPLOM_COLORS) {
            colors.add(color)
        }
        val dataSet = PieDataSet(entries, "Expense Category")
        dataSet.colors = colors
        val data = PieData(dataSet)
        data.setDrawValues(true)
        data.setValueFormatter(PercentFormatter(DecimalFormat()))
        data.setValueTextSize(12f)
        data.setValueTextColor(Color.BLACK)
        pieChart.data = data
        pieChart.invalidate()
        pieChart.animateY(1400, Easing.getEasingFunctionFromOption(Easing.EasingOption.EaseInOutQuad))
    }

    private fun setupPieChart() {
        pieChart.isDrawHoleEnabled = true
        pieChart.setUsePercentValues(true)
        pieChart.setEntryLabelTextSize(12f)
        pieChart.setEntryLabelColor(Color.BLACK)
        pieChart.centerText = "Spending by Category"
        pieChart.setCenterTextSize(24f)
        pieChart.description.isEnabled = false
        val l: Legend = pieChart.legend
        l.setVerticalAlignment(Legend.LegendVerticalAlignment.TOP)
        l.setHorizontalAlignment(Legend.LegendHorizontalAlignment.RIGHT)
        l.setOrientation(Legend.LegendOrientation.VERTICAL)
        l.setDrawInside(false)
        l.setEnabled(true)
    }
}