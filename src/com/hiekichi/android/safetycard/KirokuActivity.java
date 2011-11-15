package com.hiekichi.android.safetycard;

import java.io.File;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;

public class KirokuActivity extends Activity implements OnClickListener {

	final int REQUEST_CAMERA = 1;

	Button mButton;
	ImageView mImageVCamera;
	
	File mTmpFile;
	String mFileName;
	
	@Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_kiroku);
        
        (mButton = (Button)findViewById(R.id.buttonCamera)).setOnClickListener(this);
        mImageVCamera = (ImageView)findViewById(R.id.imageCamera);
	}

	@Override
	public void onClick(View v) {
		if (v.getId() == R.id.buttonCamera) {
			Intent intent = new Intent();
		    intent.setAction(MediaStore.ACTION_IMAGE_CAPTURE);
		    startActivityForResult(intent, REQUEST_CAMERA);
		}
	}

	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        Log.d("DEBUG", requestCode + "," + resultCode + "," + data.toString());
		if (resultCode == RESULT_OK && requestCode == REQUEST_CAMERA) {
	        //Log.d("DEBUG", "OK");
	        
	        Bitmap bitmap = (Bitmap) data.getExtras().get("data");
	        mImageVCamera.setImageBitmap(bitmap);
	    }
		else {
	        Log.d("DEBUG", "Camera result NG.");
		}
	}
}


