package com.example.almohanna.coloryourphotograph;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.Toast;

import com.example.almohanna.coloryourphotograph.Database.ColorYourPhotoDbHelper;

public class Levels extends Activity {

    ColorYourPhotoDbHelper colorYourPhotoDbHelper = new ColorYourPhotoDbHelper(this);
    String difficultyLevel = "Easy";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.levels);

        Button easy = (Button) findViewById(R.id.easy);
        Button meduim = (Button) findViewById(R.id.meduim);
        Button advance = (Button) findViewById(R.id.advance);

        easy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                colorYourPhotoDbHelper.updateLevel(difficultyLevel);
                Log.i("level", "level updated successfully to " + difficultyLevel);
                Toast.makeText(getApplicationContext(), "تم اختيار مستوى الصعوبة بنجاح", Toast.LENGTH_SHORT).show();
                Intent homeIntent = new Intent(Levels.this, Home.class);
                startActivity(homeIntent);
            }
        });

        meduim.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                difficultyLevel = "Medium";
                colorYourPhotoDbHelper.updateLevel(difficultyLevel);
                Log.i("level", "level updated successfully to " + difficultyLevel);
                Toast.makeText(getApplicationContext(), "تم اختيار مستوى الصعوبة بنجاح", Toast.LENGTH_SHORT).show();
                Intent homeIntent = new Intent(Levels.this, Home.class);
                startActivity(homeIntent);
            }
        });

        advance.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                difficultyLevel = "Advanced";
                colorYourPhotoDbHelper.updateLevel(difficultyLevel);
                Log.i("level", "level updated successfully to " + difficultyLevel);
                Toast.makeText(getApplicationContext(), "تم اختيار مستوى الصعوبة بنجاح", Toast.LENGTH_SHORT).show();
                Intent homeIntent = new Intent(Levels.this, Home.class);
                startActivity(homeIntent);
            }
        });

        // home button
        ImageButton home = (ImageButton) findViewById(R.id.home1);
        home.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent homeIntent = new Intent(Levels.this, Home.class);
                startActivity(homeIntent);
            }
        });

    }
}
