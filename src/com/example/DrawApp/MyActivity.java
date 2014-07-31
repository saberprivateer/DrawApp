package com.example.DrawApp;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.gesture.Gesture;
import android.gesture.GestureStroke;
import android.graphics.*;
import android.graphics.drawable.BitmapDrawable;
import android.media.Image;
import android.net.Uri;
import android.os.Bundle;
import android.app.ActionBar;
import android.os.Environment;
import android.util.Log;
import android.view.*;
import android.widget.*;
import android.gesture.GestureOverlayView;

import java.io.*;
import java.util.ArrayList;

import static android.R.color.holo_green_light;

public class MyActivity extends Activity {
    /**
     * Called when the activity is first created.
     */

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);


        SpinnerAdapter mSpinnerAdapter = ArrayAdapter.createFromResource(this,
                R.array.action_list, android.R.layout.simple_spinner_dropdown_item);

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
                saveSig("temp.png");
                ImageView iv = (ImageView) findViewById(R.id.imageView);
                iv.setImageBitmap(BitmapFactory.decodeFile("/sdcard/temp.png"));
            }

            @Override
            public void onGestureCancelled(GestureOverlayView gestureOverlayView, MotionEvent motionEvent) {

            }
        });

    }


    //Begin Menu Stuff
    private ShareActionProvider mShareActionProvider;

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu, menu);
//http://www.androidhive.info/2013/11/android-working-with-action-bar/
        MenuItem editSpinner = menu.findItem(R.id.menu_change);
        setupChangeSpinner(editSpinner);

        MenuItem item = menu.findItem(R.id.menu_share);
        mShareActionProvider = (ShareActionProvider) item.getActionProvider();

        Intent sendIntent = new Intent();
        sendIntent.setAction(Intent.ACTION_SEND);
        sendIntent.setType("image/*");
        saveSig("final.png");
        File sdCard = Environment.getExternalStorageDirectory();

        File sharedFile = new File(sdCard + "/final.png");
        Uri uri = Uri.fromFile(sharedFile);
        sendIntent.putExtra(Intent.EXTRA_STREAM, uri);
        setShareIntent(sendIntent);

        //  mShareActionProvider.setShareHistoryFileName(null);


        return true;
    }

    private void setupChangeSpinner(MenuItem item) {
        View view = item.getActionView();
        if (view instanceof Spinner) {
            Spinner spinner = (Spinner) view;
            spinner.setAdapter(ArrayAdapter.createFromResource(this, R.array.action_list, android.R.layout.simple_spinner_dropdown_item));

        }
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
                ImageView iv = (ImageView) findViewById(R.id.imageView);
                iv.setImageBitmap(BitmapFactory.decodeFile(""));
                return true;
            case R.id.menu_change:
                tv = (TextView) findViewById(R.id.textView);
                tv.setText("You Clicked Change");
                ActionBar.OnNavigationListener mOnNavigationListener = new ActionBar.OnNavigationListener() {
                    // Get the same strings provided for the drop-down's ArrayAdapter
                    String[] strings = getResources().getStringArray(R.array.action_list);
                    GestureOverlayView gestures = (GestureOverlayView) findViewById(R.id.gestureOverlayView);
                    @Override
                    public boolean onNavigationItemSelected(int position, long itemId) {
                        if (position == 1) {
                            gestures.setGestureColor(Color.BLUE);
                            gestures.setUncertainGestureColor(Color.BLUE);
                        } else {
                            gestures.setUncertainGestureColor(Color.GREEN);
                            gestures.setGestureColor(Color.GREEN);
                        }

                        // Apply changes

                        return true;
                    }
                };


                return true;
            case R.id.menu_save:
                tv = (TextView) findViewById(R.id.textView);
                tv.setText("SAVING...");
                tv.setTextColor(Color.MAGENTA);
                saveSig("final.png");
                tv.setText("SAVE COMPLETE");
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

    public void saveSig(String fname) {
        try {
            GestureOverlayView gestureView = (GestureOverlayView) findViewById(R.id.gestureOverlayView);
            gestureView.setDrawingCacheEnabled(true);
            Bitmap bm = Bitmap.createBitmap(gestureView.getDrawingCache());
            gestureView.setDrawingCacheEnabled(false);
            ByteArrayOutputStream bytes = new ByteArrayOutputStream();
            bm.compress(Bitmap.CompressFormat.PNG, 100, bytes);
            File f = new File(Environment.getExternalStorageDirectory()
                    + File.separator + fname);
            f.createNewFile();
            FileOutputStream os = new FileOutputStream(f);
            os.write(bytes.toByteArray());
            os.close();
        } catch (Exception e) {
            Log.v("Gestures", e.getMessage());
            e.printStackTrace();
        }
        return;
    }


}