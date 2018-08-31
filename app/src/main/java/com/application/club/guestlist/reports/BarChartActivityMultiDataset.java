
package com.application.club.guestlist.reports;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.WindowManager;
import android.widget.SeekBar;
import android.widget.SeekBar.OnSeekBarChangeListener;
import android.widget.TextView;

import com.application.club.guestlist.R;
import com.application.club.guestlist.utils.Constants;
import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.components.AxisBase;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.formatter.IAxisValueFormatter;
import com.github.mikephil.charting.formatter.LargeValueFormatter;
import com.github.mikephil.charting.highlight.Highlight;
import com.github.mikephil.charting.interfaces.datasets.IBarDataSet;
import com.github.mikephil.charting.listener.OnChartValueSelectedListener;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

public class BarChartActivityMultiDataset extends AppCompatActivity implements
        OnChartValueSelectedListener {

    private BarChart mChart;
    String tableBookingData;
    String passBookingData;
    String guestListBookingData;

    TextView bookingtv;
    int bookingTicketCount = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_barchart);


        Intent intent = getIntent();
        tableBookingData  = intent.getStringExtra(Constants.TABLE_BOOKING_DATA);
        passBookingData  = intent.getStringExtra(Constants.PASS_BOOKING_DATA);
        guestListBookingData  = intent.getStringExtra(Constants.GUESTLIST_BOOKING_DATA);




        mChart = findViewById(R.id.chart1);
        mChart.setOnChartValueSelectedListener(this);
        mChart.getDescription().setEnabled(false);

//        mChart.setDrawBorders(true);

        // scaling can now only be done on x- and y-axis separately
        mChart.setPinchZoom(false);

        mChart.setDrawBarShadow(false);

        mChart.setDrawGridBackground(false);

        // create a custom MarkerView (extend MarkerView) and specify the layout
        // to use for it
//        MyMarkerView mv = new MyMarkerView(this, R.layout.custom_marker_view);
//        mv.setChartView(mChart); // For bounds control
//        mChart.setMarker(mv); // Set the marker to the chart



//        Legend l = mChart.getLegend();
//        l.setVerticalAlignment(Legend.LegendVerticalAlignment.TOP);
//        l.setHorizontalAlignment(Legend.LegendHorizontalAlignment.RIGHT);
//        l.setOrientation(Legend.LegendOrientation.VERTICAL);
//        l.setDrawInside(true);
//        l.setYOffset(0f);
//        l.setXOffset(10f);
//        l.setYEntrySpace(0f);
//        l.setTextSize(8f);

        IAxisValueFormatter xAxisFormatter = new DayAxisValueFormatter(mChart);

        XAxis xAxis = mChart.getXAxis();
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        //xAxis.setTypeface(mTfLight);
        xAxis.setDrawGridLines(false);
        xAxis.setGranularity(1f); // only intervals of 1 day
        xAxis.setLabelCount(7);
        xAxis.setValueFormatter(xAxisFormatter);

        YAxis leftAxis = mChart.getAxisLeft();
        leftAxis.setValueFormatter(new LargeValueFormatter());
        leftAxis.setDrawGridLines(false);
        leftAxis.setSpaceTop(35f);
        leftAxis.setAxisMinimum(0f); // this replaces setStartAtZero(true)

        mChart.getAxisRight().setEnabled(false);

        setData(6, 50);
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
                set1 = new BarDataSet(yVals1, "Pass");
                set1.setColor(Color.rgb(104, 241, 175));
                set2 = new BarDataSet(yVals2, "Guestlist");
                set2.setColor(Color.rgb(164, 228, 251));
                set3 = new BarDataSet(yVals3, "Table");
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





    @Override
    public void onValueSelected(Entry e, Highlight h) {
        Log.i("Activity", "Selected: " + e.toString() + ", dataSet: " + h.getDataSetIndex());
    }

    @Override
    public void onNothingSelected() {
        Log.i("Activity", "Nothing selected.");
    }
}
