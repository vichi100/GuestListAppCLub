
package com.application.club.guestlist.reports;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.RectF;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.SeekBar;
import android.widget.TextView;

import com.application.club.guestlist.R;
import com.application.club.guestlist.utils.ColorTemplate;
import com.application.club.guestlist.utils.Constants;
import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.Legend.LegendForm;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.XAxis.XAxisPosition;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.components.YAxis.AxisDependency;
import com.github.mikephil.charting.components.YAxis.YAxisLabelPosition;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.formatter.IAxisValueFormatter;
import com.github.mikephil.charting.formatter.LargeValueFormatter;
import com.github.mikephil.charting.highlight.Highlight;
import com.github.mikephil.charting.interfaces.datasets.IBarDataSet;
import com.github.mikephil.charting.listener.OnChartValueSelectedListener;
import com.github.mikephil.charting.utils.MPPointF;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

public class MultiBarChartActivity extends AppCompatActivity implements
        OnChartValueSelectedListener {

    protected BarChart mChart;

    String tableBookingData;
    String passBookingData;
    String guestListBookingData;

    TextView bookingtv;
    int bookingTicketCount = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
//                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_barchart);
        getSupportActionBar().setTitle("Daily Sales Chart");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        //bookingtv = findViewById(R.id.bookingCount);

        Intent intent = getIntent();
        tableBookingData  = intent.getStringExtra(Constants.TABLE_BOOKING_DATA);
        passBookingData  = intent.getStringExtra(Constants.PASS_BOOKING_DATA);
        guestListBookingData  = intent.getStringExtra(Constants.GUESTLIST_BOOKING_DATA);


//        mSeekBarX = findViewById(R.id.seekBar1);
//        mSeekBarY = findViewById(R.id.seekBar2);

        mChart = findViewById(R.id.chart1);
        mChart.setOnChartValueSelectedListener(this);

        mChart.setDrawBarShadow(false);
        mChart.setDrawValueAboveBar(true);

        mChart.getDescription().setEnabled(false);

        // if more than 60 entries are displayed in the chart, no values will be
        // drawn
        mChart.setMaxVisibleValueCount(60);

        // scaling can now only be done on x- and y-axis separately
        mChart.setPinchZoom(false);

        mChart.setDrawGridBackground(false);
        // mChart.setDrawYLabels(false);

        IAxisValueFormatter xAxisFormatter = new DayAxisValueFormatter(mChart);

        XAxis xAxis = mChart.getXAxis();
        xAxis.setPosition(XAxisPosition.BOTTOM);
        //xAxis.setTypeface(mTfLight);
        xAxis.setDrawGridLines(false);
        xAxis.setGranularity(1f); // only intervals of 1 day
        xAxis.setLabelCount(7);
        xAxis.setValueFormatter(xAxisFormatter);

        //IAxisValueFormatter custom = new MyAxisValueFormatter();

        YAxis leftAxis = mChart.getAxisLeft();
        //leftAxis.setTypeface(mTfLight);
        leftAxis.setLabelCount(8, false);
        //eftAxis.setValueFormatter(custom);
        leftAxis.setPosition(YAxisLabelPosition.OUTSIDE_CHART);
        leftAxis.setSpaceTop(15f);
        leftAxis.setAxisMinimum(0f); // this replaces setStartAtZero(true)

        YAxis rightAxis = mChart.getAxisRight();
        rightAxis.setDrawGridLines(false);
        //rightAxis.setTypeface(mTfLight);
        rightAxis.setLabelCount(8, false);
        //rightAxis.setValueFormatter(custom);
        rightAxis.setSpaceTop(15f);
        rightAxis.setAxisMinimum(0f); // this replaces setStartAtZero(true)

        Legend l = mChart.getLegend();
        l.setVerticalAlignment(Legend.LegendVerticalAlignment.BOTTOM);
        l.setHorizontalAlignment(Legend.LegendHorizontalAlignment.LEFT);
        l.setOrientation(Legend.LegendOrientation.HORIZONTAL);
        l.setDrawInside(false);
        l.setForm(LegendForm.SQUARE);
        l.setFormSize(9f);
        l.setTextSize(11f);
        l.setXEntrySpace(4f);
        // l.setExtra(ColorTemplate.VORDIPLOM_COLORS, new String[] { "abc",
        // "def", "ghj", "ikl", "mno" });
        // l.setCustom(ColorTemplate.VORDIPLOM_COLORS, new String[] { "abc",
        // "def", "ghj", "ikl", "mno" });

//        XYMarkerView mv = new XYMarkerView(this, xAxisFormatter);
//        mv.setChartView(mChart); // For bounds control
//        mChart.setMarker(mv); // Set the marker to the chart

        setData(6, 50);

//        // setting data
//        mSeekBarY.setProgress(50);
//        mSeekBarX.setProgress(12);



        // mChart.setDrawLegend(false);
    }






    private void setData(int count, float range) {

        try {

            float start = 1f;

            ArrayList<BarEntry> yVals1 = new ArrayList<BarEntry>();
            ArrayList<BarEntry> yVals2 = new ArrayList<BarEntry>();
            ArrayList<BarEntry> yVals3 = new ArrayList<BarEntry>();

            JSONArray passbookingDataJArray = new JSONArray(passBookingData.toString());
            bookingTicketCount = 0;

            for(int i = 0; i < passbookingDataJArray.length(); i++){
                JSONObject jobj = passbookingDataJArray.getJSONObject(i);
                String costStr = jobj.getString(Constants.COSTAFTERDISCOUNT);
                float val = Float.valueOf(costStr);
                yVals1.add(new BarEntry(i, val));
                bookingTicketCount ++;
            }

            JSONArray guestListBookingDataJArray = new JSONArray(guestListBookingData.toString());
            bookingTicketCount = 0;

            for(int i = 0; i < guestListBookingDataJArray.length(); i++){
                JSONObject jobj = guestListBookingDataJArray.getJSONObject(i);
                String costStr = jobj.getString(Constants.COSTAFTERDISCOUNT);
                float val = Float.valueOf(costStr);
                yVals2.add(new BarEntry(i, val));
                bookingTicketCount ++;
            }

            JSONArray tableBookingDataJArray = new JSONArray(tableBookingData.toString());
            bookingTicketCount = 0;

            for(int i = 0; i < tableBookingDataJArray.length(); i++){
                JSONObject jobj = tableBookingDataJArray.getJSONObject(i);
                String costStr = jobj.getString(Constants.COSTAFTERDISCOUNT);
                float val = Float.valueOf(costStr);
                yVals3.add(new BarEntry(i, val));
                bookingTicketCount ++;
            }
            //bookingtv.setText(Integer.toString(bookingTicketCount));

//            for (int i = (int) start; i < start + count + 1; i++) {
//                float mult = (range + 1);
//                float val = (float) (Math.random() * mult);
//
//                if (Math.random() * 100 < 25) {
//                    yVals1.add(new BarEntry(i, val));
//                } else {
//                    yVals1.add(new BarEntry(i, val));
//                }
//            }

            BarDataSet set1, set2, set3;

            if (mChart.getData() != null && mChart.getData().getDataSetCount() > 0) {

                set1 = (BarDataSet) mChart.getData().getDataSetByIndex(0);
                set2 = (BarDataSet) mChart.getData().getDataSetByIndex(1);
                set3 = (BarDataSet) mChart.getData().getDataSetByIndex(2);
                set1.setValues(yVals1);
                set2.setValues(yVals2);
                set3.setValues(yVals3);
                mChart.getData().notifyDataChanged();
                mChart.notifyDataSetChanged();

            } else {
                // create 4 DataSets
                set1 = new BarDataSet(yVals1, "Company A");
                set1.setColor(Color.rgb(104, 241, 175));
                set2 = new BarDataSet(yVals2, "Company B");
                set2.setColor(Color.rgb(164, 228, 251));
                set3 = new BarDataSet(yVals3, "Company C");
                set3.setColor(Color.rgb(242, 247, 158));

                BarData data = new BarData(set1, set2, set3);
                data.setValueFormatter(new LargeValueFormatter());

                mChart.setData(data);
            }

            float groupSpace = 0.08f;
            float barSpace = 0.03f; // x4 DataSet
            float barWidth = 0.2f; // x4 DataSet
            int startYear = 0;

            int endYear = startYear + 3;

            // specify the width each bar should have
            mChart.getBarData().setBarWidth(barWidth);

            // restrict the x-axis range
            mChart.getXAxis().setAxisMinimum(startYear);

            // barData.getGroupWith(...) is a helper that calculates the width each group needs based on the provided parameters
            mChart.getXAxis().setAxisMaximum(startYear + mChart.getBarData().getGroupWidth(groupSpace, barSpace) * 3);
            mChart.groupBars(startYear, groupSpace, barSpace);
            mChart.invalidate();

        }catch (Exception ex){
            ex.printStackTrace();
        }
    }

    protected RectF mOnValueSelectedRectF = new RectF();

    @SuppressLint("NewApi")
    @Override
    public void onValueSelected(Entry e, Highlight h) {

        if (e == null)
            return;

        RectF bounds = mOnValueSelectedRectF;
        mChart.getBarBounds((BarEntry) e, bounds);
        MPPointF position = mChart.getPosition(e, AxisDependency.LEFT);

        Log.i("bounds", bounds.toString());
        Log.i("position", position.toString());

        Log.i("x-index",
                "low: " + mChart.getLowestVisibleX() + ", high: "
                        + mChart.getHighestVisibleX());

        MPPointF.recycleInstance(position);
    }

    @Override
    public void onNothingSelected() { }

    @Override
    public boolean onSupportNavigateUp(){

        finish();
        return true;
    }
}
