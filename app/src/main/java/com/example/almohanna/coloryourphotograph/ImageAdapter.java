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

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.List;

public class ImageAdapter extends ArrayAdapter<ImageModel> {

    Context context;
    ArrayList<ImageModel> images;
    int count;
    ColorYourPhotoDbHelper DbHelper = new ColorYourPhotoDbHelper(this.getContext());

    public ImageAdapter(Context context, List<ImageModel> images) {
        super(context, 0, images);
        this.context = context;
        this.images = new ArrayList<>(images);
    }

    //retriva all images
    public View getView(final int position, View convertView, ViewGroup parent) {
        View listItemView = convertView;
        if (listItemView == null) {
            listItemView = LayoutInflater.from(getContext()).inflate(
                    R.layout.gallery, parent, false);
        }

        ImageView imgView = (ImageView) listItemView.findViewById(R.id.img);
        byte[] retrivedImage = images.get(position).getImage();

        Bitmap tempBitmap = BitmapFactory.decodeByteArray(retrivedImage, 0, retrivedImage.length);
        imgView.setImageBitmap(tempBitmap);

        ImageView coloringPage = (ImageView) listItemView.findViewById(R.id.brush);
        coloringPage.setTag(tempBitmap);
        coloringPage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setClass(context, ColoringPage.class);
                ByteArrayOutputStream stream = new ByteArrayOutputStream();
                Bitmap bmp = (Bitmap) v.getTag();
                bmp.compress(Bitmap.CompressFormat.PNG, 100, stream);
                byte[] bytes = stream.toByteArray();
                intent.putExtra("Bitmap2", bytes);
                context.startActivity(intent);

                count = DbHelper.getImagesCount();
                Log.i("adapter", " numbers of images befor delete " + count);
            }
        });
        ImageView deleteImage = (ImageView) listItemView.findViewById(R.id.drop);
        deleteImage.setTag(tempBitmap);
        deleteImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDeleteConfirmationDialog(getItem(position));
                Log.i("adapter", " image deleted from database successfully");

                Log.i("adapter", " numbers of images after delete " + count);

            }
        });
        return listItemView;
    }


    public int getCount() {
        return images.size();

    }

    public ImageModel getItem(int position) {
        return images.get(position);
    }


    private void showDeleteConfirmationDialog(final ImageModel imageModel) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setMessage("هل انت متاكد من عملية الحذف؟");
        builder.setTitle("حذف صورة!");
        builder.setIcon(R.drawable.delete);
        builder.setPositiveButton("حذف", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {

                images.remove(imageModel);
                notifyDataSetChanged();
                DbHelper.DeleteImage(imageModel.getImageId());
                count = DbHelper.getImagesCount();
                notifyDataSetChanged();

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

