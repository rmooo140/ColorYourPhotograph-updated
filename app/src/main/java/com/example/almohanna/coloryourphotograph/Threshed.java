package com.example.almohanna.coloryourphotograph;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.SeekBar;


public class Threshed extends Activity {

    public CameraOpen c;
    public ImageView viewPhoto;
    public int lower;
    public int upper;
    public Bitmap photo;
    public SeekBar upperSeekBar;
    public SeekBar lowerSeekBar;
    public Bitmap result;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.threshed);

        c = new CameraOpen();
        photo = this.getIntent().getParcelableExtra("Bitmap");
        viewPhoto = (ImageView) findViewById(R.id.img);
        viewPhoto.setImageBitmap(photo);

        upperSeekBar = (SeekBar) findViewById(R.id.seekBar1);
        lowerSeekBar = (SeekBar) findViewById(R.id.seekBar2);

        // perform lower seek bar change listener event used for getting the progress value
        lowerSeekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            int lowerSeekbarProgressChangedValue = 0;

            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                lowerSeekbarProgressChangedValue = progress;
            }

            public void onStartTrackingTouch(SeekBar seekBar) {
            }

            public void onStopTrackingTouch(SeekBar seekBar) {
               // Toast.makeText(Threshed.this, "Seek bar progress is :" + lowerSeekbarProgressChangedValue, Toast.LENGTH_SHORT).show();
                //lower = lowerSeekBar.getProgress();
                lower = lowerSeekbarProgressChangedValue;
            }
        });

        // perform upper seek bar change listener event used for getting the progress value
        upperSeekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            int upperSeekbarProgressChangedValue = 0;

            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                upperSeekbarProgressChangedValue = progress;
                upper = upperSeekbarProgressChangedValue;
            }

            public void onStartTrackingTouch(SeekBar seekBar) {
            }

            public void onStopTrackingTouch(SeekBar seekBar) {
               // Toast.makeText(Threshed.this, "Seek bar progress is :" + upperSeekbarProgressChangedValue,Toast.LENGTH_SHORT).show();
                //upper = upperSeekBar.getProgress();

            }
        });

        // home button
        ImageButton home = (ImageButton) findViewById(R.id.home1);
        home.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent homeIntent = new Intent(Threshed.this, Home.class);
                startActivity(homeIntent);
            }
        });

        ImageButton nextButton = (ImageButton) findViewById(R.id.next);
        nextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                result = c.CannyEdgeDetectionForSpecificThresholdsValues(photo, lower, upper);
                Intent intent = new Intent();
                intent.setClass(Threshed.this, Confirmation.class);
                intent.putExtra("Bitmap", result);
                startActivity(intent);
            }
        });
    }//end create

    public void showAlert(View view) {
        AlertDialog.Builder myAlert = new AlertDialog.Builder(this);
        myAlert.setMessage("اهلا بك ياصغيري... " + System.getProperty("line.separator")
                + "تستطيع من خلال الحد الاعلى والحد الادنى للخطوط " + System.getProperty("line.separator")
                + "تحديد مستوى الصعوبة التي تريدها " + System.getProperty("line.separator")
                + "اتمنى لك وقتاً ممتعاً ").create();
        myAlert.show();
    }
}
