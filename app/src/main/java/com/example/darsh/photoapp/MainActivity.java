package com.example.darsh.photoapp;

import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.provider.MediaStore;
import android.content.pm.PackageInfo;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.GridLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.amazonaws.mobile.client.AWSMobileClient;
import com.amazonaws.mobile.client.AWSStartupHandler;
import com.amazonaws.mobile.client.AWSStartupResult;

import java.io.File;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    static final int REQUEST_IMAGE_CAPTURE = 1;
    static final String TAG = "MainActivity";
    static final int NUM_IMAGES_ROW = 3;
    static final int NUM_IMAGES_COLUMN = 3;
    ImageView displayimageView;
    private static final int PICK_IMAGE = 100;
    Uri imageUri;

    GridLayout gridLayout;
    int imageCount=0;
    ArrayList<Bitmap> imageList;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        AWSMobileClient.getInstance().initialize(this, new AWSStartupHandler() {
            @Override
            public void onComplete(AWSStartupResult awsStartupResult) {
                //Log.i(TAG,"AWSMobileClient is instantiated and you are connected to AWS!");
                Log.i("YourMainActivity", "AWSMobileClient is instantiated and you are connected to AWS!");
            }
        }).execute();

        setContentView(R.layout.activity_main);

        Button CaptureImage = (Button) findViewById(R.id.CaptureImage);
//        Button viewImage = (Button) findViewById(R.id.View);
//        displayimageView = (ImageView) findViewById(R.id.displayimageView);

        gridLayout = (GridLayout) findViewById(R.id.gridLayout);
        gridLayout.removeAllViews();
//        gridLayout.setColumnCount(NUM_IMAGES_COLUMN);
//        gridLayout.setRowCount(NUM_IMAGES_ROW);
        imageCount = 0;
        imageList = new ArrayList<Bitmap>();
        imageList.clear();

        //Disable the CaptureImage button if user has no camera
        if(!hasCamera())
            CaptureImage.setEnabled(false);
    }

    //Check if user has a camera
    private boolean hasCamera()
    {
        return getPackageManager().hasSystemFeature(PackageManager.FEATURE_CAMERA_ANY);
    }

    //Launching the Camera
    public void launchCamera(View view)
    {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        //Take a picture and pass results along to onActiviyResult
        startActivityForResult(intent, REQUEST_IMAGE_CAPTURE);

    }


    //To return the image taken
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        if(requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK)
        {
            //Get the photo
            Log.i(TAG,"Inside onActivityResult()");
            //        imageCount++;
            super.onActivityResult(requestCode, resultCode, data);
            if(data != null){
                Bundle extras = data.getExtras();
                Bitmap photo = (Bitmap) extras.get("data");
                imageList.add(photo);
            }
            Log.i(TAG,"photo added to ImageList");
            // displayimageView.setImageBitmap(photo);
        }
    }

    public void DisplayImage(View view){
        Log.i(TAG,"Inside DisplayImage()");
//        int layoutWidth = gridLayout.getMeasuredWidth();
//        int layoutHeight = gridLayout.getMeasuredHeight();

        // GridLayout.Spec rowSpan = GridLayout.spec(GridLayout.UNDEFINED, 1);
        // GridLayout.Spec colspan = GridLayout.spec(GridLayout.UNDEFINED, 1);

//        GridLayout.LayoutParams layoutParams = new GridLayout.LayoutParams();
//        layoutParams.width = layoutWidth/NUM_IMAGES_ROW;
//        layoutParams.height = layoutHeight/NUM_IMAGES_COLUMN;


//        File imageFile = new File(imagePath);
//        int i=0;
        gridLayout.removeAllViews();
        for(Bitmap bmp : imageList){
//            i++;
            ImageView image = new ImageView(this);
//          Bitmap bitmap = BitmapFactory.decodeFile(imageFile.getAbsolutePath());
//            image.setLayoutParams(layoutParams);

            image.setImageBitmap(bmp);

            ////////////////////////////////////////////////////////////////
//            GridLayout.LayoutParams params = (GridLayout.LayoutParams) image.getLayoutParams();
//            params.width = (gridLayout.getWidth()/gridLayout.getColumnCount());// -params.rightMargin - params.leftMargin;
//            image.setLayoutParams(params);
            ////////////////////////////////////////////////////////////



//            linearLayout.addView(image);
//            gridLayout.addView(image,layoutParams);
            gridLayout.addView(image);
            Log.i(TAG,"ImageView Added!");
        }
    }

    public void clearScreen(View view) {
        if(imageList.size() > 0){
            imageList.clear();
        }
        gridLayout.removeAllViews();
    }
}