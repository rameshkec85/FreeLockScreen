package com.xiao.policy.keyguardmodule;

import android.os.Bundle;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

public class MainActivity extends Activity implements OnClickListener{

	private Button mLockScreenStyleBtn;
	private Button mListAnimationTestBtn;
	private Button mGoLockScreenBtn;
	private Button mLockScreenCoverBtn;
	private Context mContext;
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        
        initLayout();
    }

    private void initLayout(){
    	mLockScreenStyleBtn = (Button)findViewById(R.id.lockscreen_style_1);
    	mListAnimationTestBtn = (Button)findViewById(R.id.lockscreen_listview);
    	mGoLockScreenBtn = (Button)findViewById(R.id.lockscreen_style_go);
    	mLockScreenCoverBtn = (Button)findViewById(R.id.lockscreen_style_cover);
    	mLockScreenStyleBtn.setOnClickListener(this) ;
    	mListAnimationTestBtn.setOnClickListener(this) ;
    	mGoLockScreenBtn.setOnClickListener(this);
    	mLockScreenCoverBtn.setOnClickListener(this);
    }
    
    
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
//        getMenuInflater().inflate(R.menu.activity_main, menu);
        return true;
    }

	@Override
	public void onClick(View v) {
		switch(v.getId()){
		case R.id.lockscreen_style_1:
			Intent i = new Intent();
			i.setClass(MainActivity.this, JellyBeanLockScreen.class);
			startActivity(i);
			break;
		case R.id.lockscreen_listview:
			Log.i("onClick","lockscreen_listview");
			Intent i2 = new Intent();
			i2.setClass(MainActivity.this, ListViewAnimationTest.class);
			startActivity(i2);
			break;
		case R.id.lockscreen_style_go:
			Intent i3 = new Intent();
			i3.setClass(MainActivity.this, GoLockScreen.class);
			startActivity(i3);
			break;
		case R.id.lockscreen_style_cover:
			Intent i4 = new Intent();
			i4.setClass(MainActivity.this, LockScreenCover.class);
			startActivity(i4);
			break;
		}
		
	}

}
