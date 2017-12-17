package com.example.almohanna.coloryourphotograph;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;

public class Confirmation extends Activity {
    static Bitmap photo;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.confirmation);

         photo = this.getIntent().getParcelableExtra("Bitmap");
        ImageView viewPhoto = (ImageView) findViewById(R.id.img);
        viewPhoto.setImageBitmap(photo);

        //coloring intent
        ImageView startColoring = (ImageView) findViewById(R.id.start_coloring);
        startColoring.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setClass(Confirmation.this,ColoringPage.class);
                intent.putExtra("Bitmap2",photo);
                startActivity(intent);

            }
        });

        //back button intent
        ImageView backButton = (ImageView) findViewById(R.id.back);
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent backIntent = new Intent(Confirmation.this, CameraOpen.class);
                startActivity(backIntent);
            }
        });

        // home button intent
        ImageButton home = (ImageButton) findViewById(R.id.home1);
        home.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent homeIntent = new Intent(Confirmation.this, Home.class);
                startActivity(homeIntent);
            }
        });
    }


}
