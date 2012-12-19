package jp.ac.iwasaki.android.safetycard;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.Toast;

public class SafetyCardActivity extends Activity implements OnClickListener {

	Button mButtonChuuijikou, mButtonKiroku, mButtonPersonaldata; // 操作ボタン
	Button mButtonAbout; // 「このアプリについて」

	// MENUボタン
	private static final int MENU_SETTINGS     = Menu.FIRST;

	@Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        
        // GUI部品
        (mButtonChuuijikou   = (Button)findViewById(R.id.buttonChuuijikou)).setOnClickListener(this);
        (mButtonKiroku       = (Button)findViewById(R.id.buttonKiroku)).setOnClickListener(this);
        (mButtonPersonaldata = (Button)findViewById(R.id.buttonPersonaldata)).setOnClickListener(this);
        (mButtonAbout        = (Button)findViewById(R.id.buttonAbout)).setOnClickListener(this);
        
        
    }
	
	@Override
	public void onResume() {
		super.onResume();
		
		if (SCPreferenceMain.getName(getApplicationContext()).toString().equals("")) {
			// 名前が登録されていなければ、登録を促す
			Toast.makeText(getApplicationContext(), "最初にパーソナルデータを登録してください", Toast.LENGTH_LONG).show();
			Intent intentSettings = new Intent(
					getApplicationContext(),
		            SCPreferenceMain.class);  
			startActivity(intentSettings);
		}
	}
	

	// ボタンによる動作
	@Override
	public void onClick(View arg0) {
		//Log.d("DEBUG", arg0.toString());

		if (arg0.getId() == R.id.buttonAbout) {
			// 「このアプリについて」説明ボタン
			showAboutDialog();
		}
		else if (arg0.getId() == R.id.buttonPersonaldata) {
			// パーソナルデータの表示
			startPersonaldata();
		}
		else if (arg0.getId() == R.id.buttonChuuijikou) {
			// 緊急時対処法法の表示
			showWebPage( getString(R.string.taiouhouhou_url) );
			//showWebPage("file:///android_asset/safetycard.html");
		}
		else if (arg0.getId() == R.id.buttonKiroku) {
			// 記録一覧表示
			startKirokuIchiran();
		}
	}

	// MENUの初期化処理、最初に一度だけ呼び出される
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		boolean result = super.onCreateOptionsMenu(menu);
		
		menu.add(0, MENU_SETTINGS, Menu.NONE, "設定").setIcon(R.drawable.ic_menu_preferences);

		return result;
	}

	// MENUによる動作
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		
		case MENU_SETTINGS:
			// 設定（パーソナルデータの登録）
			Intent intentSettings = new Intent(
					getApplicationContext(),
		            SCPreferenceMain.class);  
			startActivity(intentSettings);
			break;
		}
		return super.onOptionsItemSelected(item);
	}
	
	// 「このアプリについて」説明
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
	
	// 緊急時対処方法の表示
	private void showWebPage(String url) {
/*        Intent intent=new Intent("android.intent.action.VIEW",
                Uri.parse( url ));
            startActivity(intent);
*/
		Intent intent = new Intent(
				getApplicationContext(),
				SCChuuijikouActivity.class);
		startActivity(intent);
	}

	// 記録一覧表示
	private void startKirokuIchiran() {
		Intent intent = new Intent(
				getApplicationContext(),
				SCKirokuIchiranActivity.class);
		startActivity(intent);

	}

	// パーソナルデータ表示
	private void startPersonaldata() {
		Intent intent = new Intent(
				getApplicationContext(),
				SCPersonaldataActivity.class);
		startActivity(intent);

	}
}