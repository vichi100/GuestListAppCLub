
package com.application.club.guestlist.reports;

import com.application.club.guestlist.utils.ColorTemplate;
import android.annotation.SuppressLint;
import android.graphics.RectF;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.WindowManager;
import android.widget.SeekBar;
import android.widget.SeekBar.OnSeekBarChangeListener;
import android.widget.TextView;
import android.widget.Toast;

import com.application.club.guestlist.R;
import com.application.club.guestlist.utils.Constants;
import com.application.club.guestlist.utils.UtillMethods;
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
import com.github.mikephil.charting.highlight.Highlight;
import com.github.mikephil.charting.interfaces.datasets.IBarDataSet;
import com.github.mikephil.charting.interfaces.datasets.IDataSet;
import com.github.mikephil.charting.listener.OnChartValueSelectedListener;
import android.content.Intent;


import com.github.mikephil.charting.utils.MPPointF;

import org.json.JSONArray;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import static com.application.club.guestlist.utils.Constants.CLUB_NAME;

public class BarChartActivity extends AppCompatActivity implements
        OnChartValueSelectedListener {

    protected BarChart mChart;
    private SeekBar mSeekBarX, mSeekBarY;
    private TextView tvX, tvY;
    String bookingData;
    TextView bookingtv;
    int bookingTicketCount = 0;
    List<String> dateStrList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
//                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_barchart);
        getSupportActionBar().setTitle("Days Sales Chart");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        //bookingtv = findViewById(R.id.bookingCount);

        final List<Date> dateList = UtillMethods.getCurrentWeekDates();

        final SimpleDateFormat simpleDateformat = new SimpleDateFormat("dd/MMM/yyyy");

        dateStrList = new ArrayList<>();
        for(int i = 0; i < dateList.size(); i++){
            dateStrList.add(simpleDateformat.format(dateList.get(i)));
        }

        Intent intent = getIntent();
        bookingData  = intent.getStringExtra(Constants.BOOKING_DATA);


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

            JSONArray bookingDataJArray = new JSONArray(bookingData.toString());
            bookingTicketCount = 0;
            Map<String, Integer> dateDataMap = new HashMap<>();

            for(int i = 0; i < bookingDataJArray.length(); i++){
                JSONObject jobj = bookingDataJArray.getJSONObject(i);
                Integer cost = Integer.parseInt(jobj.getString(Constants.COSTAFTERDISCOUNT).trim());
                if(cost == 0){
                    cost = 350;
                }
                String bookingDate = jobj.getString(Constants.BOOKINGDATE);
                if(dateDataMap.containsKey(bookingDate)){
                    Integer costx = dateDataMap.get(bookingDate);
                    cost = costx+cost;
                    dateDataMap.put(bookingDate, cost);
                }else {
                    dateDataMap.put(bookingDate, cost);
                }
                int bookingDateIndex = dateStrList.indexOf(bookingDate);
//                float val = Float.valueOf(costStr);
//                yVals1.add(new BarEntry((float)bookingDateIndex, val));
                bookingTicketCount ++;
            }

            Iterator<String> itr = dateDataMap.keySet().iterator();
            while (itr.hasNext()){
                String bookingDatex = itr.next();
                Integer cost = dateDataMap.get(bookingDatex);
                int bookingDateIndex = dateStrList.indexOf(bookingDatex);
                yVals1.add(new BarEntry((float)bookingDateIndex, (float)cost));
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

            BarDataSet set1;

            if (mChart.getData() != null &&
                    mChart.getData().getDataSetCount() > 0) {
                set1 = (BarDataSet) mChart.getData().getDataSetByIndex(0);
                set1.setValues(yVals1);
                mChart.getData().notifyDataChanged();
                mChart.notifyDataSetChanged();
            } else {
                set1 = new BarDataSet(yVals1, "Total Bookings = "+bookingTicketCount);

                set1.setDrawIcons(false);

//            set1.setColors(ColorTemplate.MATERIAL_COLORS);

            /*int startColor = ContextCompat.getColor(this, android.R.color.holo_blue_dark);
            int endColor = ContextCompat.getColor(this, android.R.color.holo_blue_bright);
            set1.setGradientColor(startColor, endColor);*/

                int startColor1 = ContextCompat.getColor(this, android.R.color.holo_orange_light);
                int startColor2 = ContextCompat.getColor(this, android.R.color.holo_blue_light);
                int startColor3 = ContextCompat.getColor(this, android.R.color.holo_orange_light);
                int startColor4 = ContextCompat.getColor(this, android.R.color.holo_green_light);
                int startColor5 = ContextCompat.getColor(this, android.R.color.holo_red_light);
                int endColor1 = ContextCompat.getColor(this, android.R.color.holo_blue_dark);
                int endColor2 = ContextCompat.getColor(this, android.R.color.holo_purple);
                int endColor3 = ContextCompat.getColor(this, android.R.color.holo_green_dark);
                int endColor4 = ContextCompat.getColor(this, android.R.color.holo_red_dark);
                int endColor5 = ContextCompat.getColor(this, android.R.color.holo_orange_dark);

                ArrayList<Integer> colors = new ArrayList<Integer>();
                for (int c : ColorTemplate.JOYFUL_COLORS)
                    colors.add(c);

//            List<GradientColor> gradientColors = new ArrayList<>();
//            gradientColors.add(new GradientColor(startColor1, endColor1));
//            gradientColors.add(new GradientColor(startColor2, endColor2));
//            gradientColors.add(new GradientColor(startColor3, endColor3));
//            gradientColors.add(new GradientColor(startColor4, endColor4));
//            gradientColors.add(new GradientColor(startColor5, endColor5));

                set1.setColors(colors);

                ArrayList<IBarDataSet> dataSets = new ArrayList<IBarDataSet>();
                dataSets.add(set1);

                BarData data = new BarData(dataSets);
                data.setValueTextSize(10f);
                data.setBarWidth(0.9f);

                mChart.setData(data);
            }

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
