package com.application.club.guestlist.reports;

import com.application.club.guestlist.DisplayBookingTabLayout.MainActivity;
import com.application.club.guestlist.service.EventListener;
import com.application.club.guestlist.service.SocketOperator;
import com.application.club.guestlist.utils.ColorTemplate;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;

import android.os.Bundle;
import android.os.SystemClock;
import android.support.v4.app.Fragment;
import android.text.SpannableString;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;
import com.application.club.guestlist.R;
import com.application.club.guestlist.utils.Constants;
import com.application.club.guestlist.utils.UtillMethods;
import com.github.mikephil.charting.animation.Easing;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;

import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.formatter.PercentFormatter;
import com.github.mikephil.charting.highlight.Highlight;
import com.github.mikephil.charting.listener.OnChartValueSelectedListener;
import com.github.mikephil.charting.utils.MPPointF;
import com.github.mikephil.charting.animation.Easing;


import org.json.JSONArray;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;


public class DailySummaryReport extends Fragment implements OnChartValueSelectedListener , EventListener {

    boolean getData = false;

    JSONArray bookedTableDetailsListObj ;
    JSONArray bookedPassDetailsListObj;
    JSONArray bookedGuestListDetailsListObj;

    JSONArray bookedTableDetailsJArray;
    JSONArray bookedGuestlistDetailsJArray;
    JSONArray bookedPassDetailsJArray;

    float totalPassSale;
    float totalTableSale;
    float totalGuestListSale;

    TextView tableCounttv;
    TextView passCounttv;
    TextView guestlistCounttv;
    BookingTableData bookingTableData;


    private PieChart mChart;
    SocketOperator socketOperator;
    protected String[] mParties = new String[] {
            "Guestlist", "Pass", "Table"
    };

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.report_daily_summary_fragment, container, false);
        return rootView;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {

        super.onActivityCreated(savedInstanceState);

        final List<Date> dateList = UtillMethods.getCurrentWeekDates();
        bookingTableData = BookingTableData.getInstance();

        final SimpleDateFormat simpleDateformat = new SimpleDateFormat("dd/MMM/yyyy");

        final TextView dateTv = getActivity().findViewById(R.id.todaydate);
        dateTv.setText(simpleDateformat.format(dateList.get(0)));

        TextView tablev = getActivity().findViewById(R.id.tablev);
        tablev.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onClickTicketsType(v);
            }
        });

        TextView passv = getActivity().findViewById(R.id.passv);
        passv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onClickTicketsType(v);
            }
        });

        TextView guestlistv = getActivity().findViewById(R.id.guestlistv);
        guestlistv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onClickTicketsType(v);
            }
        });

        tableCounttv = getActivity().findViewById(R.id.tablecount);
        tableCounttv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onClickTicketsType(v);
            }
        });

        passCounttv = getActivity().findViewById(R.id.passcount);
        passCounttv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onClickTicketsType(v);
            }
        });
        guestlistCounttv = getActivity().findViewById(R.id.guestlistcount);
        guestlistCounttv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onClickTicketsType(v);
            }
        });

        sendDataRequest(dateList, 0);


        final Button monb = getActivity().findViewById(R.id.mon);
        monb.setBackgroundColor(Color.rgb(217, 80, 138));
        monb.findFocus();
        monb.setTextColor(Color.BLACK);
        monb.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                dateTv.setText(simpleDateformat.format(dateList.get(0)));
                monb.findFocus();
                monb.setTextColor(Color.BLACK);
                sendDataRequest(dateList, 0);
               setData(4,100);
            }
        });

        monb.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (!hasFocus) {
                    monb.setTextColor(Color.WHITE);
                }
            }
        });

        final Button tueb = getActivity().findViewById(R.id.Tue);
        tueb.setBackgroundColor(Color.rgb(254, 169, 138));
        tueb.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                dateTv.setText(simpleDateformat.format(dateList.get(1)));
                tueb.findFocus();
                tueb.setTextColor(Color.BLACK);
                sendDataRequest(dateList, 1);
                setData(4,100);
            }
        });

        tueb.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (!hasFocus) {
                    tueb.setTextColor(Color.WHITE);
                }
            }
        });

        final Button wedb = getActivity().findViewById(R.id.Wed);
        wedb.setBackgroundColor(Color.rgb(185, 206, 172));
        wedb.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                wedb.findFocus();
                dateTv.setText(simpleDateformat.format(dateList.get(2)));
                wedb.setTextColor(Color.BLACK);
                sendDataRequest(dateList, 2);
                setData(4,100);
            }
        });

        wedb.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (!hasFocus) {
                    wedb.setTextColor(Color.WHITE);
                }
            }
        });

        final Button thub = getActivity().findViewById(R.id.Thu);
        thub.setBackgroundColor(Color.rgb(106, 167, 134));
        thub.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                thub.findFocus();
                dateTv.setText(simpleDateformat.format(dateList.get(3)));
                thub.setTextColor(Color.BLACK);
                sendDataRequest(dateList, 3);
                setData(4,100);
            }
        });

        thub.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (!hasFocus) {
                    thub.setTextColor(Color.WHITE);
                }
            }
        });

        final Button frib = getActivity().findViewById(R.id.Fri);
        frib.setBackgroundColor(Color.rgb(53, 194, 209));
        frib.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                frib.findFocus();
                dateTv.setText(simpleDateformat.format(dateList.get(4)));
                frib.setTextColor(Color.BLACK);
                sendDataRequest(dateList, 4);
                setData(4,100);
            }
        });

        frib.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (!hasFocus) {
                    frib.setTextColor(Color.WHITE);
                }
            }
        });



        final Button satb = getActivity().findViewById(R.id.Sat);
        satb.setBackgroundColor(Color.rgb(200, 138, 255));
        satb.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                satb.findFocus();
                dateTv.setText(simpleDateformat.format(dateList.get(5)));
                satb.setTextColor(Color.BLACK);
                sendDataRequest(dateList, 5);
                setData(4,100);
            }
        });
        satb.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (!hasFocus) {
                    satb.setTextColor(Color.WHITE);
                }
            }
        });

        final Button sunb = getActivity().findViewById(R.id.Sun);
        sunb.setBackgroundColor(Color.rgb(134, 172, 254));
        sunb.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                sunb.findFocus();
                dateTv.setText(simpleDateformat.format(dateList.get(6)));
                sunb.setTextColor(Color.BLACK);
                sendDataRequest(dateList, 6);
                setData(4,100);
            }
        });
        sunb.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (!hasFocus) {
                    sunb.setTextColor(Color.WHITE);
                }
            }
        });



        mChart = getActivity().findViewById(R.id.chart1);
        mChart.setUsePercentValues(false);
        mChart.getDescription().setEnabled(false);
        mChart.setExtraOffsets(5, 10, 5, 5);

        mChart.setDragDecelerationFrictionCoef(0.95f);

        //mChart.setCenterTextTypeface(mTfLight);
        mChart.setCenterText(generateCenterSpannableText());

        mChart.setDrawHoleEnabled(true);
        mChart.setHoleColor(Color.WHITE);

        mChart.setTransparentCircleColor(Color.WHITE);
        mChart.setTransparentCircleAlpha(110);

        mChart.setHoleRadius(48f);
        mChart.setTransparentCircleRadius(51f);

        mChart.setDrawCenterText(true);

        mChart.setRotationAngle(0);
        // enable rotation of the chart by touch
        mChart.setRotationEnabled(true);
        mChart.setHighlightPerTapEnabled(true);

        // add a selection listener
        mChart.setOnChartValueSelectedListener(this);
        mChart.setRotationEnabled(false);
        mChart.setHighlightPerTapEnabled(true);

//        mChart.setMaxAngle(180f); // HALF CHART
//        mChart.setRotationAngle(180f);
//        mChart.setCenterTextOffset(0, -20);

        setData(4, 100);
        // entry label styling
        mChart.setEntryLabelColor(Color.WHITE);
        //mChart.setEntryLabelTypeface(mTfRegular);
        mChart.setEntryLabelTextSize(12f);
    }

    public void onClickTicketsType(View v){
        Intent intent = new Intent(getActivity(),
                MainActivity.class);
        startActivity(intent);
    }

    private void sendDataRequest(List<Date> dateList, int n){
        try{
            socketOperator = new SocketOperator(this);
            SharedPreferences settings = getActivity().getSharedPreferences(Constants.PREFS_NAME, 0);
            String clubname = settings.getString("clubname","");
            String clubId = settings.getString("clubid","");
            JSONObject getReportDataFromDatabaseJobj = new JSONObject();
            getReportDataFromDatabaseJobj.put("action", "getDataForReportChartFromDatabase");
            getReportDataFromDatabaseJobj.put(Constants.CLUB_ID, clubId);
            final SimpleDateFormat simpleDateformat = new SimpleDateFormat("dd/MMM/yyyy");
            getReportDataFromDatabaseJobj.put(Constants.EVENT_DATE, simpleDateformat.format(dateList.get(n)));
            socketOperator.sendMessage(getReportDataFromDatabaseJobj);


            while(!getData){
                SystemClock.sleep(1000);
            }

            getData = false;

        }catch (Exception ex){
            ex.printStackTrace();

        }
    }

    private void setData(int count, float range) {

        try {

            float mult = range;
            totalPassSale = 0;
            totalTableSale = 0;
            totalGuestListSale = 0;

            int tableCount = 0;
            int passCount = 0;
            int guestListCount = 0;


            ArrayList<PieEntry> entries = new ArrayList<PieEntry>();
            bookedPassDetailsJArray = new JSONArray(bookedPassDetailsListObj.toString());
            for (int i = 0; i < bookedPassDetailsJArray.length(); i++) {
                JSONObject jobj = bookedPassDetailsJArray.getJSONObject(i);
                String costStr = jobj.getString(Constants.COSTAFTERDISCOUNT);
                int cost = Integer.parseInt(costStr.trim());
                totalPassSale = totalPassSale+cost;
                passCount++;
            }
            PieEntry passEntry = new PieEntry(totalPassSale, "PASS", bookedPassDetailsJArray);
            entries.add(passEntry);
            passCounttv.setText(Integer.toString(passCount));
            bookingTableData.setBookedPassDetailsJArray(bookedPassDetailsJArray);

            bookedTableDetailsJArray = new JSONArray(bookedTableDetailsListObj.toString());
            for (int i = 0; i < bookedTableDetailsJArray.length(); i++) {
                JSONObject jobj = bookedTableDetailsJArray.getJSONObject(i);
                String costStr = jobj.getString(Constants.COSTAFTERDISCOUNT);
                int cost = Integer.parseInt(costStr.trim());
                totalTableSale = totalTableSale+cost;
                tableCount++;
            }
            PieEntry tableEntry = new PieEntry(totalTableSale, "TABLE", bookedTableDetailsJArray);
            entries.add(tableEntry);
            tableCounttv.setText(Integer.toString(tableCount));
            bookingTableData.setBookedTableDetailsJArray(bookedTableDetailsJArray);

            bookedGuestlistDetailsJArray = new JSONArray(bookedGuestListDetailsListObj.toString());
            for (int i = 0; i < bookedGuestlistDetailsJArray.length(); i++) {
                JSONObject jobj = bookedGuestlistDetailsJArray.getJSONObject(i);
                String costStr = jobj.getString(Constants.COSTAFTERDISCOUNT);
                // assume one beer avg cost in pubs
                int cost = Integer.parseInt("350");
                totalGuestListSale = totalGuestListSale+cost;
                guestListCount++;
            }
            PieEntry guestListEntry = new PieEntry(totalGuestListSale, "GUESTLIST",bookedGuestlistDetailsJArray);
            entries.add(guestListEntry);
            guestlistCounttv.setText(Integer.toString(guestListCount));
            bookingTableData.setBookedGuestlistDetailsJArray(bookedGuestlistDetailsJArray);

            // NOTE: The order of the entries when being added to the entries array determines their position around the center of
            // the chart.
//            for (int i = 0; i < mParties.length; i++) {
//                entries.add(new PieEntry((float) ((Math.random() * mult) + mult / 5),
//                        mParties[i % mParties.length]));
//            }

            //PieDataSet dataSet = new PieDataSet(entries, "Election Results");
            PieDataSet dataSet = new PieDataSet(entries, "");

            dataSet.setDrawIcons(false);

            dataSet.setSliceSpace(3f);
            dataSet.setIconsOffset(new MPPointF(0, 40));
            dataSet.setSelectionShift(5f);

            // add a lot of colors

            ArrayList<Integer> colors = new ArrayList<Integer>();

            for (int c : ColorTemplate.VORDIPLOM_COLORS)
                colors.add(c);


            dataSet.setColors(colors);
            //dataSet.setSelectionShift(0f);

//            dataSet.setValueLinePart1OffsetPercentage(80.f);
//            dataSet.setValueLinePart1Length(0.2f);
//            dataSet.setValueLinePart2Length(0.4f);
//            //dataSet.setUsingSliceColorAsValueLineColor(true);
//
//            //dataSet.setXValuePosition(PieDataSet.ValuePosition.OUTSIDE_SLICE);
//            dataSet.setYValuePosition(PieDataSet.ValuePosition.OUTSIDE_SLICE);

            PieData data = new PieData(dataSet);
            data.setValueFormatter(new PercentFormatter());
            data.setValueTextSize(11f);
            data.setValueTextColor(Color.WHITE);
            mChart.setData(data);

            // undo all highlights
            mChart.highlightValues(null);
            mChart.setCenterText(generateCenterSpannableText());
            mChart.animateY(1400, Easing.getEasingFunctionFromOption(Easing.EasingOption.EaseInOutQuad));
            mChart.invalidate();

        }catch (Exception ex){
            ex.printStackTrace();
        }
    }

    private SpannableString generateCenterSpannableText() {
        SpannableString s;

        if((int)totalGuestListSale != 0 || (int)totalTableSale != 0|| (int)totalPassSale != 0){
             s = new SpannableString("Sales Projection Graph");
        }else{
             s = new SpannableString("No Booking Available");
        }


//        s.setSpan(new RelativeSizeSpan(1.5f), 0, 14, 0);
//        s.setSpan(new StyleSpan(Typeface.NORMAL), 14, s.length() - 15, 0);
//        s.setSpan(new ForegroundColorSpan(Color.GRAY), 14, s.length() - 15, 0);
//        s.setSpan(new RelativeSizeSpan(.65f), 14, s.length() - 15, 0);
//        s.setSpan(new StyleSpan(Typeface.ITALIC), s.length() - 14, s.length(), 0);
//        s.setSpan(new ForegroundColorSpan(com.github.mikephil.charting.utils.ColorTemplate.getHoloBlue()), s.length() - 14, s.length(), 0);
        return s;
    }

    @Override
    public void onValueSelected(Entry e, Highlight h) {

        if (e == null)
            return;
        Log.i("VAL SELECTED",
                "Value: " + e.getY() + ", index: " + h.getX()
                        + ", DataSet index: " + h.getDataSetIndex());

        Intent intent = new Intent(getActivity(),
                BarChartActivity.class);
        intent.putExtra(Constants.BOOKING_DATA, e.getData().toString());

//        intent.putExtra(Constants.PASS_BOOKING_DATA, bookedPassDetailsJArray.toString());
//        intent.putExtra(Constants.TABLE_BOOKING_DATA, bookedTableDetailsJArray.toString());
//        intent.putExtra(Constants.GUESTLIST_BOOKING_DATA, bookedGuestlistDetailsJArray.toString());
        startActivity(intent);
    }

    @Override
    public void onNothingSelected() {
        Log.i("PieChart", "nothing selected");
    }


    public void eventReceived(String message){
        // conver message to list
        if(message != null){

            try{
                JSONObject allBookingDetailsObj = new JSONObject(message);
                bookedTableDetailsListObj = allBookingDetailsObj.getJSONArray("bookedTableDetailsList");
                bookedPassDetailsListObj = allBookingDetailsObj.getJSONArray("bookedPassDetailsList");
                bookedGuestListDetailsListObj = allBookingDetailsObj.getJSONArray("bookedGuestListDetailsList");


            }catch (Exception ex){
                ex.printStackTrace();

            }

        }


        getData = true;



    }

}
