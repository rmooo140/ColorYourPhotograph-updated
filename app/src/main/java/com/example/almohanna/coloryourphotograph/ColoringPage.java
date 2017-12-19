package com.example.almohanna.coloryourphotograph;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.media.MediaScannerConnection;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.example.almohanna.coloryourphotograph.Database.ColorYourPhotoDbHelper;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.Random;
import java.util.UUID;

import static com.example.almohanna.coloryourphotograph.Database.ColorYourPhotoContract.ToolsEntry;


public class ColoringPage extends Activity implements OnClickListener {
    Bitmap photo;
    ImageView viewPhoto;
    ColorYourPhotoDbHelper colorYourPhotoDbHelper = new ColorYourPhotoDbHelper(this);
    private DrawingView drawView;

    private ImageButton currPaint, drawBtn, eraseBtn, saveBtn;
    private float smallBrush, mediumBrush, largeBrush;
    private SharedPreferences myPref;

    public ColorYourPhotoDbHelper DbHelper;
    public String name;
    public String color;
    public int size;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.coloringpage);
        byte[] bytes = getIntent().getByteArrayExtra("Bitmap2");
        photo = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);;
        viewPhoto = (ImageView) findViewById(R.id.img);
        viewPhoto.setImageBitmap(photo);


        // home button
        ImageButton home = (ImageButton) findViewById(R.id.home1);
        home.setOnClickListener(new View.OnClickListener() {
            @Override

            public void onClick(View v) {

                RelativeLayout view = (RelativeLayout) findViewById(R.id.ColoredPic);
                view.setDrawingCacheEnabled(true);
                view.buildDrawingCache();
                Bitmap photocoloring = view.getDrawingCache();

                ByteArrayOutputStream stream = new ByteArrayOutputStream();
                photocoloring.compress(Bitmap.CompressFormat.PNG, 100, stream);
                byte imageInByte[] = stream.toByteArray();
                colorYourPhotoDbHelper.insertImage(imageInByte);
                Log.i("images", "inserted to database successfully");
                Toast.makeText(getApplicationContext(), "تم حفظ الصورة في معرض الصور", Toast.LENGTH_LONG).show();
                Intent homeIntent = new Intent(ColoringPage.this, Home.class);
                startActivity(homeIntent);
            }
        });


        drawView = (DrawingView) findViewById(R.id.drawing);

        LinearLayout paintLayout = (LinearLayout) findViewById(R.id.paint_colors);
        currPaint = (ImageButton) paintLayout.getChildAt(0);
        currPaint.setImageDrawable(getResources().getDrawable(R.drawable.paint_pressed));


        smallBrush = getResources().getInteger(R.integer.small_size);
        mediumBrush = getResources().getInteger(R.integer.medium_size);
        largeBrush = getResources().getInteger(R.integer.large_size);

        drawBtn = (ImageButton) findViewById(R.id.draw_btn);
        drawBtn.setOnClickListener(this);

        eraseBtn = (ImageButton) findViewById(R.id.erase_btn);
        eraseBtn.setOnClickListener(this);

        saveBtn = (ImageButton) findViewById(R.id.save);
        saveBtn.setOnClickListener(this);

    }


    public void paintClicked(View view) {
        //use chosen color
        drawView.setErase(false);
        drawView.setBrushSize(drawView.getLastBrushSize());
        if (view != currPaint) {
//update color
            ImageButton imgView = (ImageButton) view;
            String color = view.getTag().toString();
            drawView.setColor(color);
        }
    }

    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.draw_btn) {
            //draw button clicked
            final Dialog brushDialog = new Dialog(this);
            brushDialog.setTitle("Brush size:");
            brushDialog.setContentView(R.layout.brush_chooser);

            ImageButton smallBtn = (ImageButton) brushDialog.findViewById(R.id.small_brush);
            smallBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    drawView.setBrushSize(smallBrush);
                    drawView.setLastBrushSize(smallBrush);
                    brushDialog.dismiss();
                }
            });

            ImageButton mediumBtn = (ImageButton) brushDialog.findViewById(R.id.medium_brush);
            mediumBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    drawView.setBrushSize(mediumBrush);
                    drawView.setLastBrushSize(mediumBrush);
                    brushDialog.dismiss();
                }
            });


            ImageButton largeBtn = (ImageButton) brushDialog.findViewById(R.id.large_brush);
            largeBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    drawView.setBrushSize(largeBrush);
                    drawView.setLastBrushSize(largeBrush);
                    brushDialog.dismiss();
                }
            });
            brushDialog.show();
            //drawView.setBrushSize(mediumBrush);
        } else if (view.getId() == R.id.erase_btn) {
            //switch to erase - choose size
            final Dialog brushDialog = new Dialog(this);
            brushDialog.setTitle("Eraser size:");
            brushDialog.setContentView(R.layout.brush_chooser);

            ImageButton smallBtn = (ImageButton) brushDialog.findViewById(R.id.small_brush);
            smallBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    drawView.setErase(true);
                    drawView.setBrushSize(smallBrush);
                    brushDialog.dismiss();
                }
            });
            ImageButton mediumBtn = (ImageButton) brushDialog.findViewById(R.id.medium_brush);
            mediumBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    drawView.setErase(true);
                    drawView.setBrushSize(mediumBrush);
                    brushDialog.dismiss();
                }
            });
            ImageButton largeBtn = (ImageButton) brushDialog.findViewById(R.id.large_brush);
            largeBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    drawView.setErase(true);
                    drawView.setBrushSize(largeBrush);
                    brushDialog.dismiss();
                }

            });
            brushDialog.show();

        } else if (view.getId() == R.id.save) {
            //save drawing

            AlertDialog.Builder saveDialog = new AlertDialog.Builder(this);
            saveDialog.setTitle("حفظ الصورة");
            saveDialog.setMessage("هل تود حفظ الصورة في جهازك؟");//"Save drawing to device Gallery?"
            saveDialog.setPositiveButton("موافق", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int which) {
                    //save drawing


                    RelativeLayout view = (RelativeLayout) findViewById(R.id.ColoredPic);
                    view.setDrawingCacheEnabled(true);
                    view.buildDrawingCache();
                    Bitmap bmp = view.getDrawingCache();


                    String root = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES).toString();
                    File myDir = new File(root + "/saved_images");
                    myDir.mkdirs();
                    Random generator = new Random();
                    int n = 10000;
                    n = generator.nextInt(n);
                    String fname = "Image-" + n + ".jpg";
                    File file = new File(myDir, fname);
                    if (file.exists())
                        file.delete();
                    try {
                        FileOutputStream out = new FileOutputStream(file);
                        bmp.compress(Bitmap.CompressFormat.JPEG, 90, out);
                        out.flush();
                        out.close();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }


                    // Tell the media scanner about the new file so that it is
                    // immediately available to the user.
                    MediaScannerConnection.scanFile(ColoringPage.this, new String[]{file.toString()}, null,
                            new MediaScannerConnection.OnScanCompletedListener() {
                                public void onScanCompleted(String path, Uri uri) {
                                    Log.i("ExternalStorage", "Scanned " + path + ":");
                                    Log.i("ExternalStorage", "-> uri=" + uri);
                                }
                            });


                    drawView.setDrawingCacheEnabled(true);
                    String imgSaved = MediaStore.Images.Media.insertImage(
                            getContentResolver(), drawView.getDrawingCache(),
                            UUID.randomUUID().toString() + ".png", "drawing");


                    if (imgSaved != null) {
                        Toast savedToast = Toast.makeText(getApplicationContext(),
                                "تم حفظ الصورة في جهازك بنجاج ", Toast.LENGTH_SHORT);
                        savedToast.show();
                    } else {
                        Toast unsavedToast = Toast.makeText(getApplicationContext(),
                                "نعتذر حصل خطاء في حفظ الصورة حاول مرة خرى", Toast.LENGTH_SHORT);
                        unsavedToast.show();
                    }
                    drawView.destroyDrawingCache();
                }
            });
            saveDialog.setNegativeButton("الغاء", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int which) {
                    dialog.cancel();
                }
            });
            saveDialog.show();
        }

    }


    public ArrayList<String> tools() {
        ArrayList<String> a = new ArrayList<String>();
        // Read Tools
        Cursor cursor = DbHelper.readTools();
        int nameColumnIndex = cursor.getColumnIndex(ToolsEntry.COLUMN_NAME);
        int colorColumnIndex = cursor.getColumnIndex(ToolsEntry.COLUMN_COLOR);
        int sizeColumnIndex = cursor.getColumnIndex(ToolsEntry.COLUMN_SIZE);

        name = cursor.getString(nameColumnIndex);
        color = cursor.getString(colorColumnIndex);
        size = cursor.getInt(sizeColumnIndex);
        String s = String.valueOf(size);

        a.add(name);
        a.add(color);
        a.add(s);

        return a;
    }
}