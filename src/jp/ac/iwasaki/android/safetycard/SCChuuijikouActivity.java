package jp.ac.iwasaki.android.safetycard;

import android.app.Activity;
import android.os.Bundle;
import android.webkit.WebView;

public class SCChuuijikouActivity extends Activity {

	WebView webview;
	
	@Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        webview = new WebView(this);  
        webview.loadUrl("file:///android_asset/chuuijikou.html");
        setContentView(webview);
        
	}
}
