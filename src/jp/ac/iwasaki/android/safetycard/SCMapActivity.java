package jp.ac.iwasaki.android.safetycard;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.webkit.WebView;

public class SCMapActivity extends Activity {

	private static final String TAG = "SCMapActivity";
	WebView webview;
	String mLatitude, mLongitude;
	
	@Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

		Bundle extras = getIntent().getExtras();
		if (extras != null) {
			mLatitude = extras.getString("LATITUDE");
			mLongitude = extras.getString("LONGITUDE");
		}
		
        webview = new WebView(this);
        String urlString = "http://maps.google.co.jp/maps?q=" + mLatitude + "," + mLongitude;
        Log.d(TAG, urlString);
        webview.loadUrl( urlString );
        setContentView(webview);
        
	}
}
