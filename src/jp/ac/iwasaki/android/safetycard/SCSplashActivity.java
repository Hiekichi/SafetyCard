package jp.ac.iwasaki.android.safetycard;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.Window;

public class SCSplashActivity extends Activity {	
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
			SCSplashActivity.this.overridePendingTransition(
					R.xml.activity_fade_in, R.xml.activity_fade_out);
			SCSplashActivity.this.finish();
		}		
	}	
}

/*
	�N������ʁiSplash�j���쐬����ɓ�����A�ȉ��̃T�C�g�ŕ׋������Ă��������܂����B
	�uAndroid�A�v���ŃX�v���b�V����ʂ�\���������@ - MIRAI THE FUTURE�v
	�@	http://d.hatena.ne.jp/yamamotodaisaku/20100126/1264504434
	�u�X�v���b�V����ʂ̍쐬 - untitled.�v
		http://d.hatena.ne.jp/shuno113/20110818/1313696212
*/