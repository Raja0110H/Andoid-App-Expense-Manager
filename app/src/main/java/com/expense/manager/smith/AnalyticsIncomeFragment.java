package com.expense.manager.smith;

import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.Toast;
import com.expense.manager.R;
import com.expense.manager.Model.bean.CategoryBean;
import com.github.mikephil.charting.animation.Easing;
import com.github.mikephil.charting.charts.HorizontalBarChart;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter;
import com.github.mikephil.charting.formatter.PercentFormatter;
import com.github.mikephil.charting.highlight.Highlight;
import com.github.mikephil.charting.listener.OnChartValueSelectedListener;
import com.github.mikephil.charting.utils.MPPointF;
import com.github.mikephil.charting.utils.Utils;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class AnalyticsIncomeFragment extends BaseFragment {
    private String TotalExpense = "";
    List<CategoryBean> W;
    private HorizontalBarChart barChart;
    /* access modifiers changed from: private */
    public LinearLayout barChartLayout;
    private ArrayList<BarEntry> barEntries;
    private ArrayList<Integer> colors;
    /* access modifiers changed from: private */
    public Context context;
    private View emptyBarChart;
    private Double[] incomeAmount;
    private String[] incomeColor;
    private String[] incomeName;
    private PieChart pieChart;
    /* access modifiers changed from: private */
    public LinearLayout pieChartLayout;
    private ArrayList<String> xVals;
    private ArrayList<PieEntry> yvalues;

    public View onCreateView(LayoutInflater layoutInflater, ViewGroup viewGroup, Bundle bundle) {
        View inflate = layoutInflater.inflate(R.layout.fragment_chart, viewGroup, false);
        this.context = getActivity();
        inflate.findViewById(R.id.btnPieChart).setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                AnalyticsIncomeFragment.this.barChartLayout.setVisibility(View.GONE);
                AnalyticsIncomeFragment.this.pieChartLayout.setVisibility(View.VISIBLE);
            }
        });
        inflate.findViewById(R.id.btnBarChart).setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                AnalyticsIncomeFragment.this.pieChartLayout.setVisibility(View.GONE);
                AnalyticsIncomeFragment.this.barChartLayout.setVisibility(View.VISIBLE);
            }
        });
        this.pieChartLayout = (LinearLayout) inflate.findViewById(R.id.chart_in);
        this.barChartLayout = (LinearLayout) inflate.findViewById(R.id.linearlay);
        this.pieChart = (PieChart) inflate.findViewById(R.id.piechart_income);
        this.barChart = (HorizontalBarChart) inflate.findViewById(R.id.barchart_income);
        this.emptyBarChart = inflate.findViewById(R.id.emptyPieChar);
        PrepareDataForChart();
        DrowMyBarChart();
        return inflate;
    }

    private void DrowMyBarChart() {
        this.barChart.getDescription().setEnabled(false);
        this.barChart.setFitBars(true);
        BarDataSet barDataSet = new BarDataSet(this.barEntries, "");
        barDataSet.setColors((List<Integer>) this.colors);
        barDataSet.setDrawValues(true);
        BarData barData = new BarData(barDataSet);
        barData.setBarWidth(0.5f);
        barData.setValueTextSize(10.0f);
        barData.setValueTextColor(-1);
        this.barChart.setData(barData);
        this.barChart.setDrawGridBackground(false);
        this.barChart.invalidate();
        this.barChart.animateXY(1400, 1400);
        this.barChart.getAxisRight().setDrawGridLines(false);
        this.barChart.getAxisRight().setDrawLabels(false);
        this.barChart.getXAxis().setDrawGridLines(false);
        this.barChart.setPinchZoom(true);
        this.barChart.getAxisRight().setDrawGridLines(false);
        this.barChart.setFitBars(true);
        this.barChart.setDrawValueAboveBar(false);
        Legend legend = this.barChart.getLegend();
        legend.setPosition(Legend.LegendPosition.BELOW_CHART_LEFT);
        legend.setDrawInside(false);
        legend.setTextColor(getResources().getColor(R.color.black));
        legend.setForm(Legend.LegendForm.CIRCLE);
        legend.setWordWrapEnabled(true);
        legend.setFormSize(5.0f);
        legend.setTextSize(11.0f);
        legend.setXEntrySpace(5.0f);
        legend.setYEntrySpace(5.0f);
        XAxis xAxis = this.barChart.getXAxis();
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        xAxis.setDrawAxisLine(false);
        xAxis.setGranularityEnabled(true);
        xAxis.setLabelRotationAngle(45.0f);
        this.barChart.getXAxis().setValueFormatter(new IndexAxisValueFormatter((Collection<String>) this.xVals));
        YAxis axisLeft = this.barChart.getAxisLeft();
        axisLeft.setLabelCount(5, false);
        axisLeft.setSpaceTop(15.0f);
        axisLeft.setDrawAxisLine(false);
        YAxis axisRight = this.barChart.getAxisRight();
        axisRight.setLabelCount(5, false);
        axisRight.setSpaceTop(15.0f);
        axisRight.setDrawAxisLine(false);
        axisRight.setEnabled(false);
    }

    public void onStart() {
        super.onStart();
    }

    private void PrepareDataForChart() {
        double d;
        MyDatabaseHandler myDatabaseHandler = new MyDatabaseHandler(this.context);
        this.W = myDatabaseHandler.getCategoryList(2);
        this.yvalues = new ArrayList<>();
        this.xVals = new ArrayList<>();
        this.colors = new ArrayList<>();
        this.barEntries = new ArrayList<>();
        for (int i = 0; i < this.W.size(); i++) {
            CategoryBean categoryBean = this.W.get(i);
            categoryBean.setCategoryTotal(myDatabaseHandler.getCategoryAmount(categoryBean.getCategoryGroup(), categoryBean.getId()));
        }
        this.incomeAmount = new Double[this.W.size()];
        this.incomeName = new String[this.W.size()];
        this.incomeColor = new String[this.W.size()];
        for (int i2 = 0; i2 < this.W.size(); i2++) {
            this.incomeAmount[i2] = Double.valueOf(Double.parseDouble(this.W.get(i2).getCategoryTotal()));
            this.incomeName[i2] = this.W.get(i2).getName();
            this.incomeColor[i2] = this.W.get(i2).getColor();
            this.yvalues.add(new PieEntry(Float.parseFloat(this.W.get(i2).getCategoryTotal()), this.W.get(i2).getName()));
            this.xVals.add(this.W.get(i2).getName());
            this.colors.add(Integer.valueOf(Color.parseColor(this.W.get(i2).getColor())));
            this.barEntries.add(new BarEntry((float) i2, Float.parseFloat(this.W.get(i2).getCategoryTotal())));
        }
        try {
            d = MyUtils.RoundOff(Double.parseDouble(myDatabaseHandler.getTotalAmountByTime(2, "1", "9").replaceAll(",", "")));
        } catch (NumberFormatException e) {
            d = Utils.DOUBLE_EPSILON;
        }
        if (i0().equals("¢") || i0().equals("₣") || i0().equals("₧") || i0().equals("﷼") || i0().equals("₨")) {
            this.TotalExpense = String.format("%.2f", new Object[]{Double.valueOf(d)}) + i0();
        } else {
            this.TotalExpense = i0() + String.format("%.2f", new Object[]{Double.valueOf(d)});
        }
        setPieChart();
    }

    public void onResume() {
        super.onResume();
    }

    private void setPieChart() {
        this.pieChart.setUsePercentValues(true);
        PieDataSet pieDataSet = new PieDataSet(this.yvalues, "");
        pieDataSet.setColors((List<Integer>) this.colors);
        pieDataSet.setDrawIcons(false);
        pieDataSet.setValueTextColor(getResources().getColor(R.color.white));
        pieDataSet.setSliceSpace(3.0f);
        pieDataSet.setIconsOffset(new MPPointF(0.0f, 40.0f));
        pieDataSet.setSelectionShift(5.0f);
        PieData pieData = new PieData(pieDataSet);
        pieData.setValueFormatter(new PercentFormatter());
        pieDataSet.setValueTextSize(13f);
        this.pieChart.setData(pieData);
        this.pieChart.getDescription().setEnabled(false);
        this.pieChart.setDrawHoleEnabled(true);
        this.pieChart.setTransparentCircleRadius(30.0f);
        this.pieChart.setHoleRadius(30.0f);
        this.pieChart.animateY(1400, Easing.EasingOption.EaseInOutQuad);
        this.pieChart.setExtraOffsets(0.0f, 5.0f, 0.0f, 5.0f);
        this.pieChart.setDrawSliceText(false);
        PieChart pieChart2 = this.pieChart;
        pieChart2.setCenterText(getString(R.string.total) + "\n" + this.TotalExpense);
        Legend legend = this.pieChart.getLegend();
        legend.setDrawInside(false);
        legend.setForm(Legend.LegendForm.CIRCLE);
        legend.setXEntrySpace(7.0f);
        legend.setYEntrySpace(0.0f);
        legend.setYOffset(10.0f);
        legend.setTextSize(12.0f);
        legend.setWordWrapEnabled(true);
        legend.setDrawInside(false);
        legend.getCalculatedLineSizes();
        legend.setEnabled(true);
        this.pieChart.setRotationAngle(0.0f);
        this.pieChart.setRotationEnabled(true);
        this.pieChart.highlightValues((Highlight[]) null);
        this.pieChart.setHighlightPerTapEnabled(true);
        this.pieChart.invalidate();
        this.pieChart.setOnChartValueSelectedListener(new OnChartValueSelectedListener() {
            public void onNothingSelected() {
            }

            public void onValueSelected(Entry entry, Highlight highlight) {
                String str;
                PieEntry pieEntry = (PieEntry) entry;
                double value = (double) pieEntry.getValue();
                if (AnalyticsIncomeFragment.this.i0().equals("¢") || AnalyticsIncomeFragment.this.i0().equals("₣") || AnalyticsIncomeFragment.this.i0().equals("₧") || AnalyticsIncomeFragment.this.i0().equals("﷼") || AnalyticsIncomeFragment.this.i0().equals("₨")) {
                    str = String.format("%.2f", new Object[]{Double.valueOf(value)}) + AnalyticsIncomeFragment.this.i0();
                } else {
                    str = AnalyticsIncomeFragment.this.i0() + String.format("%.2f", new Object[]{Double.valueOf(value)});
                }
                Toast.makeText(AnalyticsIncomeFragment.this.context, "Name : " + pieEntry.getLabel() + "\nTotal Amount : " + str, 0).show();
            }
        });
    }
}
