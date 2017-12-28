
package com.example.almohanna.coloryourphotograph;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.Toast;

import com.example.almohanna.coloryourphotograph.Database.ColorYourPhotoDbHelper;

import org.opencv.android.BaseLoaderCallback;
import org.opencv.android.LoaderCallbackInterface;
import org.opencv.android.OpenCVLoader;
import org.opencv.android.Utils;
import org.opencv.core.Core;
import org.opencv.core.CvType;
import org.opencv.core.Mat;
import org.opencv.core.MatOfDouble;
import org.opencv.imgproc.Imgproc;

import static com.example.almohanna.coloryourphotograph.Database.ColorYourPhotoContract.DifficultyEntry;
import static java.lang.Math.max;
import static java.lang.Math.min;


public class CameraOpen extends AppCompatActivity {

    private static final String TAG = "CameraOpen";

    private static final int CAMERA_REQUEST = 1888;
    Bitmap photo;
    Bitmap imgBitmap;
    private Mat inputMat = null;
    Bitmap cannyImg;
    ColorYourPhotoDbHelper DbHelper;
    String difficultyLevel = "Easy";

    private BaseLoaderCallback mLoaderCallback = new BaseLoaderCallback(this) {
        @Override
        public void onManagerConnected(int status) {
            switch (status) {
                case LoaderCallbackInterface.SUCCESS: {
                    Log.i(TAG, "OpenCV loaded successfully");
                    try {
                        cannyImg = smoothingAndCannyEdgeDetection(photo);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
                break;
                default: {
                    super.onManagerConnected(status);
                }
                break;
            }
        }
    };

    @Override
    public void onResume() {
        super.onResume();
        if (!OpenCVLoader.initDebug()) {
            Log.d(TAG, "Internal OpenCV library not found. Using OpenCV Manager for initialization");
            OpenCVLoader.initAsync(OpenCVLoader.OPENCV_VERSION_3_1_0, this, mLoaderCallback);
        } else {
            Log.d(TAG, "OpenCV library found inside package. Using it!");
            mLoaderCallback.onManagerConnected(LoaderCallbackInterface.SUCCESS);
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.cameraopen);

        DbHelper = new ColorYourPhotoDbHelper(this);

        if (checkSelfPermission(Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED) {
            invoked();
        } else {
            String[] permissionRequ = {Manifest.permission.CAMERA};
            requestPermissions(permissionRequ, CAMERA_REQUEST);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == CAMERA_REQUEST) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                invoked();
            } else {
                Toast.makeText(this, "قم بالسماح بفتح الكاميرا لتستطيع التقاط صورة", Toast.LENGTH_LONG).show();
                Intent homeIntent = new Intent(CameraOpen.this, Home.class);
                startActivity(homeIntent);
            }
        }

    }

    private void invoked() {
        Intent cameraIntent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
        startActivityForResult(cameraIntent, CAMERA_REQUEST);
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == CAMERA_REQUEST && resultCode == Activity.RESULT_OK) {
            photo = (Bitmap) data.getExtras().get("data");

            cannyImg = smoothingAndCannyEdgeDetection(photo);
            Intent intent = new Intent();

            if (difficultyLevel.equals("Easy") || difficultyLevel.equals("Medium"))
                intent.setClass(CameraOpen.this, Confirmation.class);
            else
                intent.setClass(CameraOpen.this, Threshed.class);

            intent.putExtra("Bitmap", cannyImg);
            startActivity(intent);
        }
    }

    public Bitmap smoothingAndCannyEdgeDetection(Bitmap img) {
        // Convert to an array
        inputMat = new Mat();
        Utils.bitmapToMat(img, inputMat);

        // Smooth the image by applying Bilateral Filtering
        Mat BilateralFilterImg = applyBilateralFilter(inputMat);

        // Convert it to a greyscale image
        final Mat greyImg = new Mat(BilateralFilterImg.size(), CvType.CV_8UC1);
        Imgproc.cvtColor(BilateralFilterImg, greyImg, Imgproc.COLOR_RGB2GRAY);

        // Detect edges using Canny Edge Detection
        final Mat edges = new Mat(greyImg.size(), CvType.CV_8UC1);

        int[] thresholds = getCannyThresholds(greyImg);

        Mat resultImg = Imgproc.Canny(greyImg, edges, thresholds[0], thresholds[1]);

        // The result image should be black with white edges --> Flip colors
        //Imgproc.threshold(resultImg, edges, 1, 255, Imgproc.THRESH_BINARY_INV);

        Imgproc.adaptiveThreshold(resultImg, edges, 255, Imgproc.ADAPTIVE_THRESH_MEAN_C, Imgproc.THRESH_BINARY, 5, 10);

        // Convert back to a Bitmap
        imgBitmap = Bitmap.createBitmap(edges.cols(), edges.rows(), Bitmap.Config.ARGB_8888);
        Utils.matToBitmap(edges, imgBitmap);

        return imgBitmap;
    }

    //applay canny for specific thresholds values from seekbars
    public Bitmap CannyEdgeDetectionForSpecificThresholdsValues(Bitmap img, int lower, int upper) {
        inputMat = new Mat();
        Utils.bitmapToMat(img, inputMat);
        Mat BilateralFilterImg = applyBilateralFilter(inputMat);
        final Mat greyImg = new Mat(BilateralFilterImg.size(), CvType.CV_8UC1);
        Imgproc.cvtColor(BilateralFilterImg, greyImg, Imgproc.COLOR_RGB2GRAY);
        final Mat edges = new Mat(greyImg.size(), CvType.CV_8UC1);

        Mat resultImg = Imgproc.Canny(greyImg, edges, upper, lower);
        //Imgproc.threshold(resultImg, edges, 1, 255, Imgproc.THRESH_BINARY_INV);
        Imgproc.adaptiveThreshold(resultImg, edges, 255, Imgproc.ADAPTIVE_THRESH_MEAN_C, Imgproc.THRESH_BINARY, 5, 10);
        imgBitmap = Bitmap.createBitmap(edges.cols(), edges.rows(), Bitmap.Config.ARGB_8888);
        Utils.matToBitmap(edges, imgBitmap);
        return imgBitmap;
    }

    public Mat applyBilateralFilter(Mat src) {
        // convert 4 channel Mat to 3 channel Mat
        Imgproc.cvtColor(src, src, Imgproc.COLOR_RGBA2RGB);
        // create an empty matrix for the result
        Mat dst = new Mat();
        // apply bilateral filter
        Imgproc.bilateralFilter(src, dst, 5, 80, 80);
        return dst;
    }

    public int[] getCannyThresholds(Mat greyImg) {

        int[] thresholds = new int[2];
        // Read difficulty level
        Cursor cursor = DbHelper.readLevel();

        int levelColumnIndex = cursor.getColumnIndex(DifficultyEntry.COLUMN_LEVEL);
        // Iterate through all the returned rows in the cursor
        while (cursor.moveToNext()) {
            difficultyLevel = cursor.getString(levelColumnIndex);
            Log.v(TAG, "level: " + difficultyLevel);
        }
        // Compute the mean and standard deviation for the image
        MatOfDouble meanMat = new MatOfDouble();
        MatOfDouble stdMat = new MatOfDouble();
        Core.meanStdDev(greyImg, meanMat, stdMat);
        double mean = meanMat.get(0, 0)[0];
        double std = stdMat.get(0, 0)[0];

        // Compute the thresholds
        switch (difficultyLevel) {
            case "Easy":
                // the lower thresold
                thresholds[0] = max(0, (int) (mean - std));
                // the upper thresold
                thresholds[1] = min(255, (int) (mean + std));
                break;
            case "Medium":
                // the lower thresold
                thresholds[0] = max(0, (int) (mean - 1.5 * std));
                // the upper thresold
                thresholds[1] = min(255, (int) (mean + 0.5 * std));
                break;
            case "Advanced":
                // the lower thresold
                thresholds[0] = max(0, (int) (mean - 2.0 * std));
                // the upper thresold
                thresholds[1] = (int) (mean);
                break;
        }
        return thresholds;
    }
}