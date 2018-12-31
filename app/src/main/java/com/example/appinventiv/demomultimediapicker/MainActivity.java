package com.example.appinventiv.demomultimediapicker;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.multiple_media_picker.ImagesGallery;
import com.multiple_media_picker.VideosGallery;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private static final int MULTIPLE_IMAGE_INTENT = 101;
    private static final int REQUEST_CODE = 501;
    ArrayList<String> selectedImages, selectedVideos;
    private static int MULTIPLE_VIDEO_INTENT = 201;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        selectedImages = new ArrayList<>();
        selectedVideos = new ArrayList<>();
    }

    public void openMultipleImage(List<String> selectedImages) {
        Intent intent = new Intent(this, ImagesGallery.class);
        intent.putExtra("selectedList", (Serializable) selectedImages);
        // Set the title
        intent.putExtra("title", "Select media");
        // Mode 1 for both images and videos selection, 2 for images only and 3 for videos!
        intent.putExtra("mode", 2);
        intent.putExtra("maxSelection", 4); // Optional
        startActivityForResult(intent, MULTIPLE_IMAGE_INTENT);
    }

    public void openMultipleVideo(List<String> selectedVideos) {
        Intent intent = new Intent(this, VideosGallery.class);
        intent.putExtra("selectedList", (Serializable) selectedVideos);
        // Set the title
        intent.putExtra("title", "Select media");
        // Mode 1 for both images and videos selection, 2 for images only and 3 for videos!
        intent.putExtra("mode", 3);
        intent.putExtra("maxSelection", 1); // Optional
        startActivityForResult(intent, MULTIPLE_VIDEO_INTENT);
    }


    public void onImagesClicked(View view) {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, REQUEST_CODE);
        } else
            openMultipleImage(selectedImages);
    }

    public void onVideosClicked(View view) {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, REQUEST_CODE);
        } else
            openMultipleImage(selectedImages);
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == REQUEST_CODE) {
            if (permissions.length > 0 && grantResults.length > 0 && grantResults[0] != PackageManager.PERMISSION_DENIED) {
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, REQUEST_CODE);

            }
        }
    }
}
