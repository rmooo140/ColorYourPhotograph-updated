package com.example.almohanna.coloryourphotograph;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;

import com.example.almohanna.coloryourphotograph.Database.ColorYourPhotoDbHelper;

public class Home extends AppCompatActivity {

    ColorYourPhotoDbHelper DbHelper;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        DbHelper= new ColorYourPhotoDbHelper(this);


        // difficulty Levels page
        Button difficultyLevels = (Button) findViewById(R.id.difficultyLevels);
        difficultyLevels.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent difficultyLevelsIntent = new Intent(Home.this, Levels.class);
                startActivity(difficultyLevelsIntent);
            }
        });

        // create Coloring Page
        Button createColoringPage = (Button) findViewById(R.id.createColoringPage);
        createColoringPage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent creatColoringPageIntent = new Intent(Home.this, CameraOpen.class);
                startActivity(creatColoringPageIntent);
            }
        });

        // gallary page
        Button gallary = (Button) findViewById(R.id.gallery);
        gallary.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent gallaryIntent = new Intent(Home.this, Gallery.class);
                startActivity(gallaryIntent);
            }
        });

        // exit button
        ImageButton exit = (ImageButton) findViewById(R.id.exit);
        exit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
                System.exit(0);
            }
        });
    }
}
