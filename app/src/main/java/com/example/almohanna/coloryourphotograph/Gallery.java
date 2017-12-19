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

        display();

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

    public void display() {
        ArrayList<byte[]> list = DbHelper.retrieveAllImages();
        for (int i = 0; i < list.size(); i++) {

            byte[] img = list.get(i); 
            imageArry.add(img);

        }
        ListView listView = (ListView) findViewById(R.id.list);
        View emptyView = findViewById(R.id.empty_view);
        listView.setEmptyView(emptyView);

        adapter = new ImageAdapter(this, imageArry);
        listView.setAdapter(adapter);

    }


    public void displayNew() {
        ArrayList<byte[]> list1 = DbHelper.retrieveAllImages();
        ArrayList<byte[]> imageArryAfterDeleted = new ArrayList<byte[]>();

        for (int i = 0; i < list1.size(); i++) {

            byte[] img = list1.get(i);
            imageArryAfterDeleted.add(img);

        }
        ListView listView = (ListView) findViewById(R.id.list);
        View emptyView = findViewById(R.id.empty_view);
        listView.setEmptyView(emptyView);

        ImageAdapter a = new ImageAdapter(this, imageArryAfterDeleted);
        listView.setAdapter(a);

    }
}
