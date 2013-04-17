package com.xiao.policy.keyguardmodule;

import android.app.Activity;
import android.os.Bundle;
import android.view.Window;
import android.view.WindowManager;

public class JellyBeanLockScreen extends Activity{
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);
//		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
//				   WindowManager.LayoutParams.FLAG_FULLSCREEN);
		
		setContentView(R.layout.keyguard_jellybean);
		initViews();
	}

	private void initViews() {
		
		
	}

}
