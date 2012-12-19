package jp.ac.iwasaki.android.safetycard;

import java.util.ArrayList;
import java.util.HashMap;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.os.Bundle;
import android.util.Log;
import android.view.ContextMenu;
import android.view.MenuItem;
import android.view.View;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.SimpleCursorAdapter;
import android.widget.AdapterView.AdapterContextMenuInfo;

public class SCKirokuIchiranActivity extends Activity
		implements OnClickListener, OnItemClickListener {

	protected static final String TAG = "SCKirokuIchiranActivity";

    private SQLiteDatabase db;
    private SimpleCursorAdapter adapter;
    private SCDaoKiroku mDao;
    private Cursor mCursor;
    
    // GUI部品への参照
    Button mButtonShinki;
    ListView listview;

	// リストビューを長押しした時の処理を指し示す定数
	private static final int CONTEXT_DELETE_REC = ContextMenu.FIRST;

	AdapterContextMenuInfo info;

	@Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_kirokuichiran);
        
        // GUI部品の生成とイベント処理対応を追加
        (mButtonShinki = (Button)findViewById(R.id.buttonShinkiSakusei)).setOnClickListener(this);
        listview = (ListView)findViewById(R.id.listKirokuichiran);
        
        // リストビューで長押しで切るようにする
        registerForContextMenu(listview);
        // リストビュー長押し時のリスナーをセット
        listview.setOnItemClickListener(this);
	}

    // 前面へ復帰した時に行う処理
    @Override
    protected void onResume() {
        super.onResume();

       // データベースのオープン
        SCDatabaseHelper helper =
            new SCDatabaseHelper(getApplicationContext());
        try {
            db = helper.getWritableDatabase(); // 書き込み可能状態で開く
        } catch (SQLiteException e) {
        	Log.d(TAG, e.toString());
        	finish();
        }
        
        // DAOの取得
        mDao = new SCDaoKiroku(db);
        // リストビュー（ListView）で一覧表示するメソッドを呼ぶ
        showList();
    }

	@Override
	protected void onPause() {
		super.onPause();
		
		mCursor.close();
		db.close();
	}
	
    // リスト一覧表示
    private void showList() {
        // 全レコードを含むカーソルを取得
        mCursor = mDao.findCursorAll();
        // カーソル管理を任せる
        //startManagingCursor(mCursor);//deplicatedされたから使っちゃダメー
        
        // テーブルの列（カラム）とレイアウトの部品とを関連付ける
        String[] from = new String[] {
                SCDaoKiroku.COL_DATE, SCDaoKiroku.COL_TIME, SCDaoKiroku.COL_PLACE };
        int[] to = new int[] { R.id.col_yyyymmdd, R.id.col_hhmm, R.id.col_place };
        adapter = new SimpleCursorAdapter(this, R.layout.row_kirokuichiran, mCursor, from, to);
        // リストビューに表示させる
        listview.setAdapter(adapter);
        
    }
	
    // クリックされた時の処理
	@Override
	public void onClick(View v) {
		// 新規作成ボタンが押されたよ
		if (v.getId() == R.id.buttonShinkiSakusei) {
			Intent intent = new Intent(
					getApplicationContext(),
					SCKirokuActivity.class);
			intent.putExtra("newflag", 1);
			startActivity(intent);
		}
	}

	// ListViewの項目がクリックされたので、編集モードでレコード内容をみるよ
	@Override
	public void onItemClick(AdapterView<?> arg0, View view, int position, long id) {
		Intent intent = new Intent(
				getApplicationContext(),
				SCKirokuActivity.class);
		intent.putExtra("newflag", 0);
		intent.putExtra("id", id);
		startActivity(intent);
	}

	// ListViewの項目を長押しされた時のメニュー表示を初期化
    public void onCreateContextMenu(ContextMenu menu, View v,
    		ContextMenuInfo menuInfo) {
    	super.onCreateContextMenu(menu, v, menuInfo);
    	menu.add(0, CONTEXT_DELETE_REC, 0, "削除");
    }

    // ListViewの項目を長押しされた時の処理
    @Override
    public boolean onContextItemSelected(MenuItem item) {
     	// 何番目の項目を押されたかを知らないとね
    	info = (AdapterContextMenuInfo) item.getMenuInfo();

    	switch (item.getItemId()) {
    	case CONTEXT_DELETE_REC:
    		kakuninAndDelRecord();
    		break;
    	}
    	return true;
    }

    // 削除していいかの確認
	private void kakuninAndDelRecord() {
    	AlertDialog.Builder builder = new AlertDialog.Builder(this);
    	builder.setTitle("SafetyCard");

    	final String message = "削除してよろしいですか？";
    	builder.setMessage(message);
    	builder.setPositiveButton("OK",new android.content.DialogInterface.OnClickListener() {
	        public void onClick(android.content.DialogInterface dialog,int whichButton) {

				mDao.delete(info.id);
				showList();
	        }
	    });
    	builder.setNegativeButton("Cancel",new android.content.DialogInterface.OnClickListener() {
	        public void onClick(android.content.DialogInterface dialog,int whichButton) {
	        	;
	        }
	    });
    	builder.create();
    	builder.show();
	}
}
