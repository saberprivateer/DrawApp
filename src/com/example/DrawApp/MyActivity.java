package com.example.DrawApp;

import android.app.Activity;
import android.content.Intent;
import android.gesture.Gesture;
import android.gesture.GestureStroke;
import android.graphics.Bitmap;
import android.graphics.Paint;
import android.os.Bundle;
import android.app.ActionBar;
import android.view.*;
import android.widget.ImageView;
import android.widget.ShareActionProvider;
import android.widget.TextView;
import android.gesture.GestureOverlayView;
import android.graphics.Color;
import android.graphics.Canvas;

import java.util.ArrayList;

import static android.R.color.holo_green_light;

public class MyActivity extends Activity {
    /**
     * Called when the activity is first created.
     */

    private Bitmap bitmap;
    private ImageView imageView;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);


        GestureOverlayView gestures = (GestureOverlayView) findViewById(R.id.gestureOverlayView);
        gestures.setDrawingCacheEnabled(true);
        gestures.addOnGestureListener(new GestureOverlayView.OnGestureListener() {
            @Override
            public void onGestureStarted(GestureOverlayView gestureOverlayView, MotionEvent motionEvent) {
                TextView tv = (TextView) findViewById(R.id.textView);
                tv.setText("Click the Edit menu to add a color!");
                tv.setTextColor(Color.GREEN);
            }

            @Override
            public void onGesture(GestureOverlayView gestureOverlayView, MotionEvent motionEvent) {

            }

            @Override
            public void onGestureEnded(GestureOverlayView gestureOverlayView, MotionEvent motionEvent) {
                TextView tv = (TextView) findViewById(R.id.textView);
                if (tv.getText() == "") {
                    tv.setText("Click the Edit menu to add a color!");
                    tv.setTextColor(Color.BLUE);
                } else {
                   tv.setText("Are you done? Then save or share your creation!");
                   tv.setTextColor(Color.RED);
                }
            }

            @Override
            public void onGestureCancelled(GestureOverlayView gestureOverlayView, MotionEvent motionEvent) {

            }
        });


        //     imageView = (ImageView) findViewById(R.id.imageView);

        //    Display display= getWindowManager().getDefaultDisplay();
        //    bitmap=Bitmap.createBitmap(50,50, Bitmap.Config.ARGB_8888);

    }


    //Begin Menu Stuff
    private ShareActionProvider mShareActionProvider;

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu, menu);
        MenuItem item = menu.findItem(R.id.menu_share);
        mShareActionProvider = (ShareActionProvider) item.getActionProvider();


        //placeholder intent until I can figure out how to grab the image
        Intent sendIntent = new Intent();
        sendIntent.setAction(Intent.ACTION_SEND);
        sendIntent.putExtra(Intent.EXTRA_TEXT, "This is my text to send.");
        sendIntent.setType("text/plain");
        setShareIntent(sendIntent);


        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle presses on the action bar items
        switch (item.getItemId()) {
            case R.id.menu_new_picture:
                TextView tv = (TextView) findViewById(R.id.textView);
                tv.setText("");
                GestureOverlayView gestures = (GestureOverlayView) findViewById(R.id.gestureOverlayView);
                gestures.clear(true);
                return true;
            //add case to save bitmap
            //add case to change color
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    // Call to update the share intent
    private void setShareIntent(Intent shareIntent) {
        if (mShareActionProvider != null) {
            mShareActionProvider.setShareIntent(shareIntent);
        }
    }
}