package com.example.almohanna.coloryourphotograph;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;

import com.example.almohanna.coloryourphotograph.Database.ColorYourPhotoDbHelper;

import java.util.ArrayList;

/**
 * Created by Reem on 23-Nov-17.
 */

public class ImageAdapter extends ArrayAdapter<byte[]> {

    Context context;
    ArrayList<byte[]> images;
    int count;
    Gallery display_adapter;
    //ArrayList<byte[]> imageArry = new ArrayList<byte[]>();
    ColorYourPhotoDbHelper DbHelper = new ColorYourPhotoDbHelper(this.getContext());

    public ImageAdapter(Context context, ArrayList<byte[]> images) {
        super(context, 0, images);
        this.context = context;
        this.images = images;
    }

    //retriva all images
    public View getView(final int position, View convertView, ViewGroup parent) {
        View listItemView = convertView;
        if (listItemView == null) {
            listItemView = LayoutInflater.from(getContext()).inflate(
                    R.layout.gallery, parent, false);
        }

        ImageView imgView = (ImageView) listItemView.findViewById(R.id.img);
        byte[] retrivedImage = images.get(position);
        //    imgBitmap = BitmapFactory.decodeByteArray(retrivedImage, 0, retrivedImage.length);
        //  imgView.setImageBitmap(imgBitmap);
        Bitmap tempBitmap = BitmapFactory.decodeByteArray(retrivedImage, 0, retrivedImage.length);
        imgView.setImageBitmap(tempBitmap);
        ImageView coloringPage = (ImageView) listItemView.findViewById(R.id.brush);
        coloringPage.setTag(tempBitmap);
        coloringPage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setClass(context, ColoringPage.class);
                intent.putExtra("Bitmap2", (Bitmap) v.getTag());
                context.startActivity(intent);
                //Log.i("adapter", " numbers of images befor delete " + getCount());

                count = DbHelper.getImagesCount();
                Log.i("adapter", " numbers of images befor delete " + count);
            }
        });
        ImageView deleteImage = (ImageView) listItemView.findViewById(R.id.drop);
        deleteImage.setTag(tempBitmap);
        deleteImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //(Bitmap) v.getTag()
                //DbHelper.DeleteImage((long) v.getId());
                showDeleteConfirmationDialog((long) v.getId());
                images.remove(getItem(position));
                notifyDataSetChanged();
                Log.i("adapter", " image deleted from database successfully");
                //Log.i("adapter", " numbers of images after delete " + getCount());
                count = DbHelper.getImagesCount();

                Log.i("adapter", " numbers of images after delete " + count);
                //Log.i("adapter", " row " + isDeleted);

            }
        });
        return listItemView;
    }


    public int getCount() {
        return images.size();

    }

    public byte[] getItem(int position) {
        return images.get(position);
    }


    private void showDeleteConfirmationDialog(final long itemId){ //final byte[] position) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setMessage("هل انت متاكد من عملية الحذف؟");
        builder.setTitle("حذف صورة!");
        builder.setIcon(R.drawable.delete);
        builder.setPositiveButton("حذف", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {

                DbHelper.DeleteImage(itemId);
                //images.remove(position);
                display_adapter.displayNew();

                /*for (int i = 0; i < list.size(); i++) {

                    byte[] img = list.get(i);
                    imageArry.add(img);

                }
                ListView listView = (ListView) findViewById(R.id.list);
                View emptyView = findViewById(R.id.empty_view);
                listView.setEmptyView(emptyView);

                ImageAdapter adapter = new ImageAdapter(context, imageArry);
                listView.setAdapter(adapter);
                notifyDataSetChanged();
                //finish();
                */
            }
        });
        builder.setNegativeButton("الغاء", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                if (dialog != null) {
                    dialog.dismiss();
                }
            }
        });
        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }
}

