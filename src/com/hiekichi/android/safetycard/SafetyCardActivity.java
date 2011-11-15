package com.hiekichi.android.safetycard;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

public class SafetyCardActivity extends Activity implements OnClickListener {

	Button mButtonChuuijikou, mButtonKiroku, mButtonPersonaldata; // 処理ボタン
	Button mButtonAbout; // ...についてボタン

	@Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        
        // GUI部品への参照を取得
        (mButtonChuuijikou   = (Button)findViewById(R.id.buttonChuuijikou)).setOnClickListener(this);
        (mButtonKiroku       = (Button)findViewById(R.id.buttonKiroku)).setOnClickListener(this);
        (mButtonPersonaldata = (Button)findViewById(R.id.buttonPersonaldata)).setOnClickListener(this);
        (mButtonAbout        = (Button)findViewById(R.id.buttonAbout)).setOnClickListener(this);
        
        
    }

	// ボタンが押された
	@Override
	public void onClick(View arg0) {
		Log.d("DEBUG", arg0.toString());

		if (arg0.getId() == R.id.buttonAbout) {
			showAboutDialog();
		}
		else if (arg0.getId() == R.id.buttonChuuijikou) {
			showWebPage( getString(R.string.taiouhouhou_url) );
		}
		else if (arg0.getId() == R.id.buttonKiroku) {
			startKirokuIchiran();
		}
	}
	
	// メソッド群
	private void showAboutDialog() {
    	AlertDialog.Builder builder1 = new AlertDialog.Builder(this);
    	builder1.setTitle(getString(R.string.app_name));
    	builder1.setIcon(R.drawable.ic_launcher);
    	
    	final String message1 = getString(R.string.about_message);
    	builder1.setMessage(message1);
    	builder1.setPositiveButton("OK",new android.content.DialogInterface.OnClickListener() {
	        public void onClick(android.content.DialogInterface dialog,int whichButton) {
	        }
	    });
    	builder1.create();
    	builder1.show();
	}
	
	private void showWebPage(String url) {
        Intent intent=new Intent("android.intent.action.VIEW",
                Uri.parse( url ));
            startActivity(intent);
	}
	
	private void startKirokuIchiran() {
		Intent intent = new Intent(
				getApplicationContext(),
				KirokuIchiranActivity.class);
		startActivity(intent);

	}
}