package com.example.almohanna.coloryourphotograph;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ListView;

import com.example.almohanna.coloryourphotograph.Database.ColorYourPhotoDbHelper;

import java.util.ArrayList;

/**
 * Created by user on 27/09/17.
 */
public class Gallery extends Activity {

    // Bitmap photo;
    ColorYourPhotoDbHelper DbHelper = new ColorYourPhotoDbHelper(this);
    ArrayList<byte[]> imageArry = new ArrayList<byte[]>();
    ImageAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.maingallery);

//image views implementation
        /*
        photo = this.getIntent().getParcelableExtra("Bitmap2");
        ImageView coloringPage = (ImageView) findViewById(R.id.brush);
        coloringPage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setClass(Gallery.this,ColoringPage.class);
                intent.putExtra("Bitmap", photo );
                startActivity(intent);
            }
        });
        ImageView deleteImage = (ImageView) findViewById(R.id.drop);
        deleteImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DbHelper.DeleteImage(photo);
                Log.i("adapter", " image deleted from database successfully");
            }
        });
*/

        ArrayList<byte[]> list = DbHelper.retrieveAllImages();
        for (int i = 0; i < list.size(); i++) {

            byte[] img = list.get(i);
            imageArry.add(img);

        }
        ListView listView = (ListView) findViewById(R.id.list);

        adapter = new ImageAdapter(this, imageArry);
        listView.setAdapter(adapter);

        // home button
        ImageButton home = (ImageButton) findViewById(R.id.home1);
        home.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent homeIntent = new Intent(Gallery.this, Home.class);
                startActivity(homeIntent);
            }
        });
    }
}
