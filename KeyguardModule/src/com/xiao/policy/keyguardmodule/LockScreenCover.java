package com.xiao.policy.keyguardmodule;
import com.xiao.policy.keyguardmodule.view.ImageCover;

import android.app.Activity;
import android.os.Bundle;
import android.view.Window;
import android.widget.Toast;

public class LockScreenCover extends Activity{
	ImageCover joke;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.lockscreen_cover);
		
        joke=(ImageCover) findViewById(R.id.c_joke);
        joke.setOnClickIntent(new ImageCover.OnViewClick() {
			
			@Override
			public void onClick() {
				// TODO Auto-generated method stub
				Toast.makeText(LockScreenCover.this, "事件触发", 1000).show();
				System.out.println("1");
			}
		});
	}

}
