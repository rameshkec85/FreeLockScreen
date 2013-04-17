package com.xiao.policy.keyguardmodule;

import com.xiao.policy.keyguardmodule.goview.OnUnlockListener;
import com.xiao.policy.keyguardmodule.goview.TraceRotateView;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.widget.LinearLayout;
import android.widget.Toast;

public class GoLockScreen extends Activity {
	
	private final static String TAG = "GoLockScreen";
	private UnlockWidgetCommonMethods mUnlockWidgetMethods;
	private View mUnlockWidget;
	
	private interface UnlockWidgetCommonMethods{
		
		public void uodateResources();
		
		public View getView();
		
		public void reset(boolean animate);
		
		public void ping();
		
	}
	
	class LockViewMethods implements OnUnlockListener,UnlockWidgetCommonMethods{

		private final View mView;
		
		LockViewMethods(View lockView){
			mView = lockView;
		}
		
		@Override
		public View getView() {
			return mView;
		}

		@Override
		public void ping() {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void reset(boolean animate) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void uodateResources() {
			// TODO Auto-generated method stub
			
		}


		public void onStateChange() {
			// TODO Auto-generated method stub
			
		}


		public void onUnlock() {
			Intent intent = new Intent();
			Toast.makeText(getApplicationContext(), "unLock", Toast.LENGTH_SHORT).show();
			
		}
		
	}
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.keyguard_lockscreen_go);
		
		mUnlockWidget = findViewById(R.id.unlock_widget_go);
		
		if(mUnlockWidget instanceof TraceRotateView){
			TraceRotateView traceRotateView = (TraceRotateView) mUnlockWidget;
			LockViewMethods traceRotateViewMethods = new LockViewMethods(traceRotateView);
			traceRotateView.setOnUnLockListener(traceRotateViewMethods);
		}
		
	}
	

}
