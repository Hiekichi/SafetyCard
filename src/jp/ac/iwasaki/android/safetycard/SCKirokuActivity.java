package jp.ac.iwasaki.android.safetycard;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.util.Calendar;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.location.LocationProvider;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.WindowManager.LayoutParams;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.TimePicker;

public class SCKirokuActivity extends Activity
			implements OnClickListener, DatePickerDialog.OnDateSetListener, TimePickerDialog.OnTimeSetListener, LocationListener {

	// エラー確認用のタグ
	private static final String TAG = "SCKirokuActivity";
	
	// カメラ操作用の定数
	static final int REQUEST_CAMERA1 = 1;
	static final int REQUEST_CAMERA2 = 2;
	static final int REQUEST_CAMERA3 = 3;

	// GUI部品への参照
	Button mButtonCamera1, mButtonCamera2, mButtonCamera3, mButtonSave, mButtonMap;
	EditText et1;
	TextView mTvDate, mTvTime;
	EditText mEtPlace, mEtName, mEtTelNo, mEtAddress, mEtCarNo, mEtOther;
	ImageView mImageVCamera1, mImageVCamera2, mImageVCamera3;
	TextView mTvLatitudeLabel, mTvLatitudeValue;
	TextView mTvLongitudeLabel, mTvLongitudeValue;

	// ファイル入出力と日付操作用
	File mTmpFile;
	String mFileName;
	int mYear, mMonth, mDay, mHour, mMinute;

	// 新規作成か修正かの判別用
	static final int FLAG_OLD_EDIT   = 0;
	static final int FLAG_NEW_CREATE = 1;
	int mNewFlag = FLAG_OLD_EDIT;

	// データベース操作用
	private SQLiteDatabase db;
	private SCDaoKiroku mDao;
	long mId;
    private SCRecKiroku mRecKiroku;

    // GPS操作用
	private LocationManager mLocationManager; // ロケーションマネージャー（GPS処理用）
	Handler mHandler; // ハンドラ
	private ProgressDialog mProgressDialog; // プログレスダイアログ
	double mLatitudeValue, mLongitudeValue; // 緯度、経度
	boolean mReGetLocationFlag; // 位置を再取得するときに立てるフラグ
	
	@Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.getWindow().setSoftInputMode(LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
        setContentView(R.layout.layout_kiroku);

        (mButtonSave = (Button)findViewById(R.id.buttonSave)).setOnClickListener(this);

        (mButtonCamera1 = (Button)findViewById(R.id.buttonCamera1)).setOnClickListener(this);
        mImageVCamera1 = (ImageView)findViewById(R.id.imageCamera1);
        mImageVCamera1.setDrawingCacheEnabled(true);
        (mButtonCamera2 = (Button)findViewById(R.id.buttonCamera2)).setOnClickListener(this);
        mImageVCamera2 = (ImageView)findViewById(R.id.imageCamera2);
        mImageVCamera2.setDrawingCacheEnabled(true);
        (mButtonCamera3 = (Button)findViewById(R.id.buttonCamera3)).setOnClickListener(this);
        mImageVCamera3 = (ImageView)findViewById(R.id.imageCamera3);
        mImageVCamera3.setDrawingCacheEnabled(true);

        (mTvDate   = (TextView)findViewById(R.id.tvDate)).setOnClickListener(this);
        (mTvTime   = (TextView)findViewById(R.id.tvTime)).setOnClickListener(this);
        mEtPlace   = (EditText)findViewById(R.id.etPlace);
        mEtName    = (EditText)findViewById(R.id.etName);
        mEtTelNo   = (EditText)findViewById(R.id.etTelNo);
        mEtAddress = (EditText)findViewById(R.id.etAddress);
        mEtCarNo   = (EditText)findViewById(R.id.etCarNo);
        mEtOther   = (EditText)findViewById(R.id.etOther);

        // ### GPS用の各種準備　###
        (mButtonMap = (Button)findViewById(R.id.buttonMap)).setOnClickListener(this);
        mButtonMap.setEnabled(false); // 最初は使用不可にしとくよ
        mButtonMap.setTextColor(Color.WHITE);
        mTvLatitudeLabel = (TextView)findViewById(R.id.tvLatitudeLabel);
        mTvLatitudeValue = (TextView)findViewById(R.id.tvLatitudeValue);
        mTvLongitudeLabel = (TextView)findViewById(R.id.tvLongitudeLabel);
        mTvLongitudeValue = (TextView)findViewById(R.id.tvLongitudeValue);
        // ロケーションマネージャ
        mLocationManager = (LocationManager) getSystemService(LOCATION_SERVICE);
        // スレッド処理用ハンドラ
        mHandler = new Handler();
		// ロケーション情報の使用（更新間隔を設定）
        if (mLocationManager != null) {
            mLocationManager.requestLocationUpdates(
                LocationManager.GPS_PROVIDER,
//                LocationManager.NETWORK_PROVIDER,
                3000, // 3秒以上経過しないとUpdateしない
                1,     // 1m以上の変化が無いとUpdateしない
                this);
        }
		mTvLatitudeLabel.setText("緯度");
		mTvLongitudeLabel.setText("経度");

	}

	@Override
	protected void onResume() {
		super.onResume();

		 // データベースを開く
		SCDatabaseHelper helper =
            new SCDatabaseHelper(getApplicationContext());
        try {
            db = helper.getWritableDatabase();
        } catch (SQLiteException e) {
        	Log.d(TAG, e.toString());
        }
        
        // DAOを取得
        mDao = new SCDaoKiroku(db);

        // どのような状態から呼び出されたのかを判断し、初期化処理をわける
		Bundle extras = getIntent().getExtras();
		if (extras != null) {
			mNewFlag = extras.getInt("newflag");
			if (mNewFlag == FLAG_NEW_CREATE) {
				// 新規作成
				if ( mYear == 0) {
					Calendar calendar = Calendar.getInstance();
					mYear  = calendar.get(Calendar.YEAR);
					mMonth = calendar.get(Calendar.MONTH) + 1;
					mDay   = calendar.get(Calendar.DAY_OF_MONTH);
					setHiduke();
					mHour   = calendar.get(Calendar.HOUR_OF_DAY);
					mMinute = calendar.get(Calendar.MINUTE);
					setJikoku();
				}
			}
			else if ( mNewFlag == FLAG_OLD_EDIT ) {
				// 編集
				mId = extras.getLong("id");

		        mRecKiroku = mDao.getRecById(mId);
		        
		        mTvDate.setText(    mRecKiroku.getDate() );
		        mTvTime.setText(    mRecKiroku.getTime() );
		        mEtPlace.setText(   mRecKiroku.getPlace() );
		        mEtName.setText(    mRecKiroku.getName() );
		        mEtTelNo.setText(   mRecKiroku.getTel_no() );
		        mEtAddress.setText( mRecKiroku.getAddress() );
		        mLatitudeValue = Double.valueOf(mRecKiroku.getLatitude());
		        mTvLatitudeValue.setText( String.valueOf(mLatitudeValue));
		        mLongitudeValue = Double.valueOf(mRecKiroku.getLongitude());
		        mTvLongitudeValue.setText( String.valueOf(mLongitudeValue));
		        mEtCarNo.setText(   mRecKiroku.getCar_no() );
		        mEtOther.setText(   mRecKiroku.getOther() );
			
		        Bitmap bitmap;
		        bitmap = bytes2bmp( mRecKiroku.getPhoto1() );
		        if (bitmap != null)     mImageVCamera1.setImageBitmap( bitmap );
		        bitmap = bytes2bmp( mRecKiroku.getPhoto2() );
		        if (bitmap != null)     mImageVCamera2.setImageBitmap( bitmap );
		        bitmap = bytes2bmp( mRecKiroku.getPhoto3() );
		        if (bitmap != null)     mImageVCamera3.setImageBitmap( bitmap );
			
		        if (mLatitudeValue == 0.0D && mLongitudeValue == 0.0D) {
		        	mButtonMap.setEnabled(false);
		        }
		        else {
		        	mButtonMap.setEnabled(true);
		        }
			}
		}
		
	}

	@Override
	protected void onPause() {
		super.onPause();
		
		// ロケーションマネージャを解放
		 if (mLocationManager != null) {
	            mLocationManager.removeUpdates(this);
	     }
		 
		 // データベースを閉じる
		 db.close();
	}

	@Override
	public void onClick(View v) {
		// クリックされたボタンによってそれぞれ操作を行う　（case文の書き方が色々ゴメンナサイ）
		Log.d(TAG, v.toString());
		if (v.getId() ==  R.id.buttonSave) {
				Log.d(TAG, "保存ボタンが押されたよ");
				// 新規作成で呼び出されたときの、新規保存処理
				if ( mNewFlag == FLAG_NEW_CREATE) {
					Log.d(TAG, "新規作成で保存するよ");
					// 保存するレコードを新規作成する
		            SCRecKiroku recNamae =
		                new SCRecKiroku( 0, // insert
		                              ((EditText)findViewById(R.id.etName)).getText().toString(),
		                              ((EditText)findViewById(R.id.etTelNo)).getText().toString(),
		                              ((EditText)findViewById(R.id.etAddress)).getText().toString(),
		                              String.valueOf(mLatitudeValue),
		                              String.valueOf(mLongitudeValue),
		                              ((EditText)findViewById(R.id.etCarNo)).getText().toString(),
		                              ((TextView)findViewById(R.id.tvDate)).getText().toString(),
		                              ((TextView)findViewById(R.id.tvTime)).getText().toString(),
		                              ((EditText)findViewById(R.id.etPlace)).getText().toString(),
		                              ((EditText)findViewById(R.id.etOther)).getText().toString(),
		                              image2bytes(mImageVCamera1),
		                              image2bytes(mImageVCamera2),
		                              image2bytes(mImageVCamera3)
		                              );
		            // レコードを挿入（つまり新規でレコードを追加）する
		            mDao.insert(recNamae);
		            // アクティビティ（今の画面）を閉じる
		            finish();
				}
				// 既存のレコードを編集したのを保存する
				else if (mNewFlag == FLAG_OLD_EDIT) {
					Log.d(TAG, "編集モードで保存するよ");
			        mRecKiroku.setDate( mTvDate.getText().toString() );
			        mRecKiroku.setTime( mTvTime.getText().toString() );
			        mRecKiroku.setPlace( mEtPlace.getText().toString() );
			        mRecKiroku.setName( mEtName.getText().toString() );
			        mRecKiroku.setTel_no( mEtTelNo.getText().toString() );
			        mRecKiroku.setAddress( mEtAddress.getText().toString() );
			        mRecKiroku.setLatitude(String.valueOf(mLatitudeValue));
			        mRecKiroku.setLongitude(String.valueOf(mLongitudeValue));
			        mRecKiroku.setCar_no( mEtCarNo.getText().toString() );
			        mRecKiroku.setOther( mEtOther.getText().toString() );
                    mRecKiroku.setPhoto1( image2bytes(mImageVCamera1) );
                    mRecKiroku.setPhoto2( image2bytes(mImageVCamera2) );
                    mRecKiroku.setPhoto3( image2bytes(mImageVCamera3) );

		            mDao.update(mRecKiroku);
		            finish();
				}
		}
		// カメラボタンの処理　　　（冗長ですね）
		else if (v.getId() == R.id.buttonCamera1) {
				Intent intent = new Intent();
			    intent.setAction(MediaStore.ACTION_IMAGE_CAPTURE);
			    startActivityForResult(intent, REQUEST_CAMERA1);
		}
		else if (v.getId() == R.id.buttonCamera2) {
				Intent intent = new Intent();
			    intent.setAction(MediaStore.ACTION_IMAGE_CAPTURE);
			    startActivityForResult(intent, REQUEST_CAMERA2);
		}
		else if (v.getId() == R.id.buttonCamera3) {
				Intent intent = new Intent();
			    intent.setAction(MediaStore.ACTION_IMAGE_CAPTURE);
			    startActivityForResult(intent, REQUEST_CAMERA3);
		}
		// 日付の処理
		else if (v.getId() == R.id.tvDate) {
				DatePickerDialog datePickerDialog = new DatePickerDialog(
					    this,
					    this,
					    mYear,
					    mMonth - 1,
					    mDay
				);
				datePickerDialog.show();
		}
		else if (v.getId() == R.id.tvTime) {
				TimePickerDialog timePickerDialog = new TimePickerDialog(
						this,
						this,
						mHour,
						mMinute,
						true
				);
				timePickerDialog.show();
		}
		else if (v.getId() == R.id.buttonMap) {
				// 現在の緯度経度で、Google Mapを開く
				Intent intent = new Intent(
						getApplicationContext(),
						SCMapActivity.class);
				intent.putExtra("LATITUDE", String.valueOf(mLatitudeValue));
				intent.putExtra("LONGITUDE", String.valueOf(mLongitudeValue));
				startActivity(intent);
		}
	}

	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        Log.d(TAG, requestCode + "," + resultCode + "," + data.toString());
		if (resultCode == RESULT_OK) {

			if (requestCode == REQUEST_CAMERA1) {
		        mImageVCamera1.setImageBitmap((Bitmap) data.getExtras().get("data"));
		    }
			else if (requestCode == REQUEST_CAMERA2) {
		        mImageVCamera2.setImageBitmap((Bitmap) data.getExtras().get("data"));
		    }
			else if (requestCode == REQUEST_CAMERA3) {
		        mImageVCamera3.setImageBitmap((Bitmap) data.getExtras().get("data"));
		    }
			else {
		        Log.d(TAG, "Camera result NG.");
			}
		}
	}
	
	@Override
	// DatePickerDialogによる日付取得処理
	public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
		mYear = year;
    	mMonth = monthOfYear + 1;
    	mDay = dayOfMonth;
    	setHiduke();
    }
	
	private void setHiduke() {
		mTvDate.setText(String.format("%4d/%2d/%2d", mYear, mMonth, mDay));
	}

	@Override
	public void onTimeSet(TimePicker view, int h, int m) {
		mHour   = h;
		mMinute = m;
		setJikoku();
	}
	
	private void setJikoku() {
		mTvTime.setText(String.format("%2d:%2d", mHour, mMinute));
	}
	
	// ImageViewをバイト列に変換する
	public static byte[] image2bytes(ImageView imageView) {
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		Bitmap bmp = imageView.getDrawingCache();
		if ( bmp != null ) {
			bmp.compress(CompressFormat.JPEG, 100, baos);
			return baos.toByteArray();
		}
		else {
			return null;
		}
	}
	
	// バイト列からビットマップを生成する
    public static Bitmap bytes2bmp(byte[] data) {
    	if (data != null) {
    		return BitmapFactory.decodeByteArray(data,0,data.length);
    	}
    	else {
    		return null;
    	}
    } 
   
    /*
    private void subGpsThread() {
    	// プログレスダイアログの作成と表示
        mProgressDialog = new ProgressDialog(this);
        mProgressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        mProgressDialog.setMessage("GPSで位置情報を取得しています\nしばらくお待ちください");
        mProgressDialog.setCancelable(true);
        mProgressDialog.show();

		Thread thread = new Thread() {
			public void run() {
				// ここに時間のかかる処理を記述する
				gpsLocationProcess();
		        
		        mHandler.post(new Runnable() {
		        	@Override
		        	public void run() {
		        		// ここに時間のかかる処理後に行う処理を記述する

						mProgressDialog.dismiss(); // プログレスダイアログを消す
		        	}
		        });
			}
		};
		thread.start();
    }
	*/
    
    private void gpsLocationProcess() {
    }
    
	@Override
	public void onLocationChanged(Location location) {
		if ( (mLatitudeValue == 0.0D && mLongitudeValue == 0.0D) || mReGetLocationFlag == true ) {
			mReGetLocationFlag = false; // フラグを寝かせる
			//mLatitudeString = String.format("%4.2f",location.getLatitude());
			//mLongitudeString = String.format("%4.2f",location.getLongitude());
			mLatitudeValue = location.getLatitude();
			mLongitudeValue = location.getLongitude();
			mTvLatitudeValue.setText( Double.toString(mLatitudeValue) );
			mTvLongitudeValue.setText( Double.toString(mLongitudeValue) );
			
			mButtonMap.setEnabled(true);
	        mButtonMap.setTextColor(Color.BLACK);
		}
	}

	@Override
	public void onProviderDisabled(String arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onProviderEnabled(String arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onStatusChanged(String provider, int status, Bundle extras) {
		switch (status) {
        case LocationProvider.AVAILABLE:
            Log.v("Status", "AVAILABLE");
            break;
        case LocationProvider.OUT_OF_SERVICE:
            Log.v("Status", "OUT_OF_SERVICE");
            break;
        case LocationProvider.TEMPORARILY_UNAVAILABLE:
            Log.v("Status", "TEMPORARILY_UNAVAILABLE");
            break;
        }
	}
}


