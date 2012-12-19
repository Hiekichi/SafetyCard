package jp.ac.iwasaki.android.safetycard;

import android.app.Activity;
import android.os.Bundle;
import android.widget.TextView;

public class SCPersonaldataActivity extends Activity {

	TextView mTvName, mTvTel, mTvBlood, mTvOther;
	
	@Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_personaldata);
        
        mTvName  = (TextView)findViewById(R.id.tv_name);
        mTvTel   = (TextView)findViewById(R.id.tv_tel);
        mTvBlood = (TextView)findViewById(R.id.tv_blood);
        mTvOther = (TextView)findViewById(R.id.tv_other);

        loadPrivateData();
	}

	
	private void loadPrivateData() {
        mTvName.setText(  SCPreferenceMain.getName(getApplicationContext()).toString() );
        mTvTel.setText(   SCPreferenceMain.getTel(getApplicationContext()).toString() );
        mTvBlood.setText( SCPreferenceMain.getBlood(getApplicationContext()).toString() );
        mTvOther.setText( SCPreferenceMain.getOther(getApplicationContext()).toString() );

	}
}
