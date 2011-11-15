package com.hiekichi.android.safetycard;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.Window;

public class SplashActivity extends Activity {	
	@Override
	protected void onCreate(Bundle savedInstanceState) {		
		super.onCreate(savedInstanceState);				
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.splash);
		Handler hdl = new Handler();
		hdl.postDelayed(new splashHandler(), 2000);
		
	}
	class splashHandler implements Runnable {
		public void run() {
			Intent i = new Intent(getApplication(), SafetyCardActivity.class);
			startActivity(i);
			SplashActivity.this.overridePendingTransition(
					R.xml.activity_fade_in, R.xml.activity_fade_out);
			SplashActivity.this.finish();
		}		
	}	
}

/*
	起動時画面（Splash）を作成するに当たり、以下のサイトで勉強させていただきました。
	「Androidアプリでスプラッシュ画面を表示させる方法 - MIRAI THE FUTURE」
	　	http://d.hatena.ne.jp/yamamotodaisaku/20100126/1264504434
	「スプラッシュ画面の作成 - untitled.」
		http://d.hatena.ne.jp/shuno113/20110818/1313696212
*/