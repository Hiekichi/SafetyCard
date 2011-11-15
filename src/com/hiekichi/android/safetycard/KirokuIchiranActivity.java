package com.hiekichi.android.safetycard;

import java.util.ArrayList;
import java.util.HashMap;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ListView;
import android.widget.SimpleAdapter;

public class KirokuIchiranActivity extends Activity implements OnClickListener {

	Button mButtonShinki;
	
	@Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_kirokuichiran);
        
        // GUIオブジェクトの参照
        (mButtonShinki = (Button)findViewById(R.id.buttonShinkiSakusei)).setOnClickListener(this);

        // ListViewに表示
        ArrayList<HashMap<String,Object>> outputArray = new ArrayList<HashMap<String,Object>>();
        for( int i = 0; i < 10; i++ ) {
            HashMap<String, Object> item = new HashMap<String, Object>();
            item.put("yyyymmdd", "2011/11/0" + i );
            item.put("hhmm"    , "15:0" + i );
            item.put("place"    , "東京都新宿" + i + "丁目" );
            outputArray.add(item);
        }
        
        SimpleAdapter myAdapter = new SimpleAdapter(
                this,
                outputArray,
                R.layout.row_kirokuichiran,
                new String[] {"yyyymmdd", "hhmm", "place"},
                new int[] { R.id.col_yyyymmdd, R.id.col_hhmm, R.id.col_place }
         );
        
        ListView listView = (ListView)findViewById(R.id.listKirokuichiran);
        listView.setAdapter(myAdapter);
	}

	@Override
	public void onClick(View v) {
		if (v.getId() == R.id.buttonShinkiSakusei) {
			Intent intent = new Intent(
					getApplicationContext(),
					KirokuActivity.class);
			startActivity(intent);

		}
		
	}

}
