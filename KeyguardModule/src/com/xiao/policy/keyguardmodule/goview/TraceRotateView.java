package com.xiao.policy.keyguardmodule.goview;

import com.xiao.policy.keyguardmodule.R;
import com.xiao.policy.keyguardmodule.goview.OnUnlockListener;

import android.content.Context;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.RotateAnimation;
import android.view.animation.Transformation;
import android.view.animation.Animation.AnimationListener;
import android.widget.FrameLayout;
import android.widget.ImageView;

public class TraceRotateView extends FrameLayout implements AnimationListener{



	private static final int STATE_NORMAL = 0;
	private static final int STATE_PRESSED = 1;
	private static final int DURATION = 1000;
	
	private Drawable mUnlockwave = null;
	private AlphaAnimation mWaveAlphaAnimation = null;
	private Transformation mTrasformation = new Transformation();
	
	private int mWidth = 0;
	private int mHeight = 0;
	
	private RotateAnimation mLRotateAnimation = null;
	private RotateAnimation mCRotateAnimation = null;
	private RotateAnimation mRRotateAnimation = null;
	
	private int mState = STATE_NORMAL;
	
	private View mPressedView = null;
	private View mNormalView = null;
	
	private ImageView mWaveView = null;
	private ImageView mStartBtn = null;
	private ImageView mRoleView = null;
	private ImageView mLBallView = null;
	private ImageView mCBallView = null;
	private ImageView mRBallView = null;
	
	private OnUnlockListener mOnUnLockListener = null;

	
	public TraceRotateView(Context context, AttributeSet attrs) {
		super(context, attrs);
		init();
	}
	
	public TraceRotateView(Context context) {
		super(context);
		init();
	}
	
	private void init() {
		LayoutInflater mInflater = (LayoutInflater)getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		View mView = mInflater.inflate(R.layout.tracerotate, null);
		addView(mView);
		
		mNormalView = mView.findViewById(R.id.unlock_normal_state);
		mPressedView = mView.findViewById(R.id.unlock_pressed_state);
		
		mWaveView = (ImageView)mView.findViewById(R.id.unlock_wave);
		mRoleView = (ImageView)mView.findViewById(R.id.unlock_role);
		mLBallView = (ImageView)mView.findViewById(R.id.unlock_lball);
		mCBallView = (ImageView)mView.findViewById(R.id.unlock_cball);
		mRBallView = (ImageView)mView.findViewById(R.id.unlock_rball);
		
		mStartBtn = (ImageView)mView.findViewById(R.id.unlock_start);//
		
		mWaveAlphaAnimation = new AlphaAnimation(1.0f, 0.2f);
		mWaveAlphaAnimation.setDuration(500);
		mWaveAlphaAnimation.setRepeatCount(-1);
		mWaveAlphaAnimation.setRepeatMode(Animation.REVERSE);
		
		mLRotateAnimation = new RotateAnimation(0.0f, 325.0f, 
				Animation.RELATIVE_TO_SELF, 0.5f,
				Animation.RELATIVE_TO_SELF, 0.5f);
		mCRotateAnimation = new RotateAnimation(0.0f, 335.0f, 
				Animation.RELATIVE_TO_SELF, 0.5f,
				Animation.RELATIVE_TO_SELF, 0.5f);
		mRRotateAnimation = new RotateAnimation(0.0f, 345.0f, 
				Animation.RELATIVE_TO_SELF, 0.5f,
				Animation.RELATIVE_TO_SELF, 0.5f);
		
		mLRotateAnimation.setDuration(DURATION);
		mCRotateAnimation.setDuration(DURATION + 100);
		mRRotateAnimation.setDuration(DURATION + 200);
		
		mLRotateAnimation.setAnimationListener(this);
		mCRotateAnimation.setAnimationListener(this);
		mRRotateAnimation.setAnimationListener(this);
		
		mWaveView.setAnimation(mWaveAlphaAnimation);
		mWaveAlphaAnimation.startNow();
	}

	public void setOnUnLockListener(OnUnlockListener listener){
		mOnUnLockListener = listener;
	}
	
	public boolean onTouchEvent(MotionEvent event){
		final int action = event.getAction();
		boolean handled = false;
		switch (action){
		case MotionEvent.ACTION_DOWN:
			handleDown(event);
			handled = true;
			break;
		case MotionEvent.ACTION_UP:
		case MotionEvent.ACTION_CANCEL:
			handleUp(event);
			handled = true;
			break;
		}
		invalidate();
		return handled? true:super.onTouchEvent(event);
	}
	
	private void handleUp(MotionEvent event) {
		switchState(STATE_NORMAL);
		
	}

	private void handleDown(MotionEvent event) {
		int x = (int)event.getX();
		int y = (int)event.getY();
		Rect outRect = new Rect();
		
		mStartBtn.getHitRect(outRect);
		
		if(outRect.contains(x,y)){
			switchState(STATE_PRESSED);
		}
	}

	private void switchState(int state) {
		if(mOnUnLockListener != null){
			mOnUnLockListener.onStateChange();
		}
		mState = state;
		
		if (state == STATE_NORMAL){
			mPressedView.setVisibility(View.GONE);
			mLRotateAnimation.cancel();
			mCRotateAnimation.cancel();
			mRRotateAnimation.cancel();
			
			mNormalView.setVisibility(View.VISIBLE);
			mWaveView.setAnimation(mWaveAlphaAnimation);
			mWaveAlphaAnimation.startNow();
			
		}else if (state == STATE_PRESSED){
			mNormalView.setVisibility(View.GONE);
			mWaveAlphaAnimation.cancel();
			
			mPressedView.setVisibility(View.VISIBLE);
			mLBallView.setAnimation(mLRotateAnimation);
			mCBallView.setAnimation(mCRotateAnimation);
			mRBallView.setAnimation(mRRotateAnimation);
			mLRotateAnimation.startNow();
			mCRotateAnimation.startNow();
			mRRotateAnimation.startNow();
		}
	}

	@Override
	public void onAnimationEnd(Animation animation) {
		if(mLRotateAnimation == animation){
			mLBallView.setVisibility(View.GONE);
		}else if(mCRotateAnimation == animation){
			mCBallView.setVisibility(View.GONE);
		}else if(mRRotateAnimation == animation){
			mRBallView.setVisibility(View.GONE);
			if(mOnUnLockListener != null && mState == STATE_PRESSED){
				mOnUnLockListener.onUnlock();
			}
		}
		
	}

	@Override
	public void onAnimationRepeat(Animation animation) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onAnimationStart(Animation animation) {
		// TODO Auto-generated method stub
		
	}

}
