package com.application.club.guestlist.bookingTable;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.SystemClock;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatImageView;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Toast;

import com.application.club.guestlist.R;
import com.application.club.guestlist.clubdetails.ClubDetailsListActivity;
import com.application.club.guestlist.clubdetails.TableDetailsItem;
import com.application.club.guestlist.clubdetails.TicketDetailsItem;
import com.application.club.guestlist.utils.Constants;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.InputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import at.lukle.clickableareasimage.ClickableArea;
import at.lukle.clickableareasimage.ClickableAreasImage;
import at.lukle.clickableareasimage.OnClickableAreaClickedListener;
import uk.co.senab.photoview.PhotoView;
import uk.co.senab.photoview.PhotoViewAttacher;

import static com.application.club.guestlist.utils.Constants.CLUB_NAME;

/**
 * Created by Oclemmy on 5/10/2016 for ProgrammingWizards Channel and http://www.Camposha.com.
 */
public class TableSelectionActivity extends AppCompatActivity  implements OnClickableAreaClickedListener {



    TableBookingListAdapter adapter;
    private ArrayList<TableDetailsItem> ticketDetailsrowItems;
    ListView list;
    String layoutURL;
    Bitmap bitmap;
    Canvas canvas;
    float xRatio;
    ImageView layoutImage;
    Context c;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.table_selection_activity);
        getSupportActionBar().setTitle("Table Booking");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        c = this;




        // Add image
        layoutImage = (ImageView) findViewById(R.id.tablelayout);



        //layoutImage.setImageResource(R.drawable.table_layout);

        final LinearLayout layout = (LinearLayout) findViewById(R.id.mainlayout);
        ViewTreeObserver vto = layoutImage.getViewTreeObserver();
        vto.addOnGlobalLayoutListener (new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                layout.getViewTreeObserver().removeOnGlobalLayoutListener(this);
                int width  = layoutImage.getMeasuredWidth();//1080
                int height = layoutImage.getMeasuredHeight();//1397

            }
        });



        Bitmap loadedBitmap = BitmapFactory.decodeResource(this.getResources(),
                R.drawable.table_layout);
        bitmap = loadedBitmap.copy(Bitmap.Config.ARGB_8888, true);
        bitmap  = Bitmap.createScaledBitmap(bitmap, bitmap.getWidth(), bitmap.getHeight(), true);
        //bitmap = loadedBitmap.copy(Bitmap.Config.ARGB_8888, true);
        canvas = new Canvas(bitmap);

        canvas.drawBitmap(bitmap, 0, 0, null);
        //canvas.drawColor(Color.BLUE);

        //xRatio = (float)bitmap.getWidth() / layoutImage.getWidth();

//        final PhotoView layoutImage = findViewById(R.id.tablelayout);

        Intent intent = getIntent();
        final String clubName  = intent.getStringExtra(CLUB_NAME);
        final String clubidx  = intent.getStringExtra(Constants.CLUB_ID);
        final String date = intent.getStringExtra(Constants.EVENTDATE);
        final String imageURL = intent.getStringExtra(Constants.IMAGE_URL);
        final String tableDiscount = intent.getStringExtra(Constants.TABLE_DISCOUNT);
        final String ticketDetailsJsonArryStr = intent.getStringExtra(Constants.TICKET_DETAILS);
        JSONArray ticketDetailsListJsonArray = null;

        List<ClickableArea> clickableAreas = new ArrayList<>();

        try {

            ticketDetailsListJsonArray = new JSONArray(ticketDetailsJsonArryStr);

            if(ticketDetailsListJsonArray != null){

                for(int i=0; i < ticketDetailsListJsonArray.length(); i++){
                    TableDetailsItem ticketDetailsItemObj = new TableDetailsItem();
                    JSONObject tableDetailJObj = ticketDetailsListJsonArray.getJSONObject(i);
                    int locX = Integer.parseInt(tableDetailJObj.getString(Constants.LOC_X));
                    int locY = Integer.parseInt(tableDetailJObj.getString(Constants.LOC_Y));


                    int OHight = Integer.parseInt(tableDetailJObj.getString(Constants.O_HIGHT));
                    int OWidth = Integer.parseInt(tableDetailJObj.getString(Constants.O_WIDTH));

                    layoutURL = tableDetailJObj.getString(Constants.LAYOUT_URL);

                    TableDetailsItem tr = new TableDetailsItem();
                    tr.setClubid(clubidx);
//                    tr.setClubname(clubName);
//                    tr.setLocX(locX);
//                    tr.setLocX(locY);
//                    tr.setOHight(OHight);
//                    tr.setOWidth(OWidth);

                    Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);
                    paint.setColor(Color.BLUE);

                    int width  = 1080;//layout.getMeasuredWidth();//
                    int height = 1397;//layout.getMeasuredHeight();//

                    int xRatio, yRatio;
                    int xPos, yPos;
                    if (bitmap.getWidth() >= width) {
                        xRatio = bitmap.getWidth() / width;//layoutImage.getWidth();
                        xPos = (int) Math.floor(xRatio * locX);
                    } else {
                        xRatio = width / bitmap.getWidth();
                        xPos = (int) Math.floor(xRatio*locX-((width - bitmap.getWidth())/2));
                    }

                    if (bitmap.getHeight() >= height) {
                        yRatio = bitmap.getHeight() / height;
                        yPos = (int)Math.floor(locY*yRatio);
                    } else {
                        yRatio = height / bitmap.getHeight();
                        yPos = (int) Math.floor(yRatio*locY-((height - bitmap.getHeight())/2));
                    }

                    canvas.drawCircle(xPos, yPos, 50, paint);
                    //canvas.getHeight()
                   // canvas.drawCircle(locX, locY, 50, paint);
                    //canvas.drawRoundRect(new RectF(locX,locY,locX+10,locY+10), 2, 2, paint);


                    clickableAreas.add(new ClickableArea(locX, locY, 50, 50, tr));



                }

            }

        }catch (Exception ex){
            ex.printStackTrace();
        }
//
//        Picasso.with(this.getApplicationContext()).load(Constants.HTTP_URL+layoutURL).into(layoutImage);
//
//       ?

//        try{
//            URL myUrl = new URL("Constants.HTTP_URL+layoutURL");
//            InputStream inputStream = (InputStream)myUrl.getContent();
//            Drawable drawable = Drawable.createFromStream(inputStream, null);
//            layoutImage.setImageResource(drawable);
//
//        }catch (Exception ex){
//
//        }



        //layoutImage.setImageBitmap(bitmap);
        layoutImage.setImageDrawable(new BitmapDrawable(getResources(), bitmap));

        ClickableAreasImage clickableAreasImage = new ClickableAreasImage(new PhotoViewAttacher(layoutImage), this);

        // Set your clickable areas to the image
        clickableAreasImage.setClickableAreas(clickableAreas);

        //Toast.makeText(this, canvas.getHeight(), Toast.LENGTH_SHORT).show();


        layoutImage.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_DOWN){
//                    textView.setText("Touch coordinates : " +
//                            String.valueOf(event.getX()) + "x" + String.valueOf(event.getY()));


                    Toast.makeText(c, "Touch coordinates : " + String.valueOf(event.getX()) + "x" + String.valueOf(event.getY()), Toast.LENGTH_SHORT).show();
                }
                return true;
            }
        });



    }


    // Listen for touches on your images:
    @Override
    public void onClickableAreaTouched(Object item) {

            //String text = ((Character) item).getFirstName() + " " + ((Character) item).getLastName();
            Toast.makeText(this, "vihci", Toast.LENGTH_SHORT).show();
        int w = layoutImage.getWidth();
        int h = layoutImage.getHeight();

    }

    @Override
    public boolean onSupportNavigateUp(){

        finish();
        return true;
    }


//    @Override
//    public boolean onOptionsItemSelected(MenuItem item) {
//        // Handle action bar item clicks here. The action bar will
//        // automatically handle clicks on the Home/Up button, so long
//        // as you specify a parent activity in AndroidManifest.xml.
//        int id = item.getItemId();
//
//        //noinspection SimplifiableIfStatement
//        if (id == R.id.action_settings) {
//            return true;
//        } else if (id == android.R.id.home) {
//            finish();
//            return true;
//        }
//
//        return super.onOptionsItemSelected(item);
//    }

//    @Override
//    public void onItemClick(AdapterView<?> parent, View view, int position,
//                            long id) {
//
////        Toast.makeText(getActivity(), menutitles[position], Toast.LENGTH_SHORT)
////                .show();
//        // we will get jasson array list here
//
//        TicketDetailsItem ticketDetailItemsObj = ticketDetailsrowItems.get(position);
//        String clubId = ticketDetailItemsObj.getClubid();
//        Intent intent = new Intent(this, ClubDetailsListActivity.class);
//        intent.putExtra(Constants.CLUB_ID, clubId);
//        startActivity(intent);
//
//    }

}
