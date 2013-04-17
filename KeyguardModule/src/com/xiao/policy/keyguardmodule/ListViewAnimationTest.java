package com.xiao.policy.keyguardmodule;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.AnimationDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.animation.LayoutAnimationController;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;

public class ListViewAnimationTest extends Activity {
	private ListView mListView;
	private ListAdapter1 mListAdapter;
	private int[] drawableId = {
			R.drawable.a_01,
			R.drawable.a_02,
			R.drawable.a_03,
			R.drawable.a_04,
			R.drawable.a_05,
			R.drawable.a_06
	};
    private Handler mHandler = new Handler(){  	
    	public void handleMessage(android.os.Message msg) {
    		
    	};
    };
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.listview_animation_test);

		mListView = (ListView) findViewById(R.id.listView1);
		mListAdapter = createListAdapter2();
		mListView.setAdapter(mListAdapter);
		// mListView.setAdapter(createListAdapter());
		// LayoutAnimationController controller = new
		// LayoutAnimationController(animation);
		// controller.setOrder(LayoutAnimationController.ORDER_NORMAL);
		// controller.setDelay(0.5f);
		// mListView.setLayoutAnimation(controller);

		mListView.setOnItemClickListener(new ListView.OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				// TODO Auto-generated method stub
				//mListAdapter.setSelectedPosition(arg2);
				//mListAdapter.notifyDataSetInvalidated();
				startRun(arg2);
			}
		});

	}

	private ListAdapter1 createListAdapter2() {
		ArrayList<ButtonView> buttonListView = new ArrayList<ButtonView>();
		ButtonView a = new ButtonView(R.string.item01);
		buttonListView.add(a);
		ButtonView b = new ButtonView(R.string.item02);
		buttonListView.add(b);
		ButtonView c = new ButtonView(R.string.item03);
		buttonListView.add(c);
		ButtonView d = new ButtonView(R.string.item04);
		buttonListView.add(d);

		ListAdapter1 adapter = new ListAdapter1(buttonListView);
		return adapter;
	}

	private ListAdapter createListAdapter() {
		List<HashMap<String, String>> list = new ArrayList<HashMap<String, String>>();
		HashMap<String, String> m1 = new HashMap<String, String>();
		m1.put("name", "bauble");
		m1.put("sex", "male");
		HashMap<String, String> m2 = new HashMap<String, String>();
		m2.put("name", "Allorry");
		m2.put("sex", "male");
		HashMap<String, String> m3 = new HashMap<String, String>();
		m3.put("name", "Allotory");
		m3.put("sex", "male");
		HashMap<String, String> m4 = new HashMap<String, String>();
		m4.put("name", "boolbe");
		m4.put("sex", "male");
		list.add(m1);
		list.add(m2);
		list.add(m3);
		list.add(m4);
		SimpleAdapter simpleAdapter = new SimpleAdapter(this, list,
				R.layout.list_item, new String[] { "name", "sex" }, new int[] {
						R.id.name, R.id.sex });
		return simpleAdapter;
	}

	public class ListAdapter1 extends BaseAdapter {

		ArrayList<ButtonView> arrayList = null;
		LayoutInflater inflater;
		View view;
		ButtonLayoutHolder buttonLayoutHolder;
		LinearLayout buttonLayout = null;
		TextView buttonText = null;

		private int selectedPosition = -1;// 选中的位置
        private int drawableId = -1;//图片的ID
		
		public ListAdapter1(ArrayList<ButtonView> buttonListView) {
			// TODO Auto-generated constructor stub
			arrayList = buttonListView;
		}

		@Override
		public int getCount() {
			// TODO Auto-generated method stub
			return arrayList.size();
		}

		@Override
		public Object getItem(int position) {
			// TODO Auto-generated method stub
			return arrayList.get(position);
		}

		@Override
		public long getItemId(int position) {
			// TODO Auto-generated method stub
			return position;
		}

		public void setSelectedPosition(int position) {
			selectedPosition = position;
		}

		public void setSelectedPosition(int position, int drawId) {
			selectedPosition = position;
			drawableId = drawId;
		}
		
		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			// TODO Auto-generated method stub
			inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			view = inflater.inflate(R.layout.button_layout, null, false);
			buttonLayoutHolder = (ButtonLayoutHolder) view.getTag();

			if (buttonLayoutHolder == null) {
				buttonLayoutHolder = new ButtonLayoutHolder();
				buttonLayoutHolder.buttonLayout = (LinearLayout) view
						.findViewById(R.id.LinearLayoutButton);
				buttonLayoutHolder.textView = (TextView) view
						.findViewById(R.id.TextViewButton);
				view.setTag(buttonLayoutHolder);
			}
			buttonLayout = buttonLayoutHolder.buttonLayout;
			buttonText = buttonLayoutHolder.textView;
			if (selectedPosition == position) {
				buttonText.setSelected(true);
				buttonText.setPressed(true);
				//buttonLayout.setBackgroundColor(Color.RED);
				if (drawableId != -1){
					buttonLayout.setBackgroundResource(drawableId); 
				}
	            //AnimationDrawable animationDrawable = (AnimationDrawable)  
	            //buttonLayout.getBackground();  
	            //animationDrawable.start();  
			} else {
				buttonText.setSelected(false);
				buttonText.setPressed(false);
				buttonLayout.setBackgroundColor(Color.TRANSPARENT);

			}

			buttonText.setTextColor(Color.WHITE);
			buttonText.setText(arrayList.get(position).textViewId);

			return view;

		}

	};
	
	class FrameRunnable implements Runnable{
        private int position;
		private int drawId;
		public FrameRunnable(int position, int index) {
			this.position = position;
			this.drawId = index;
		}
		@Override
		public void run() {
			mListAdapter.setSelectedPosition(position, drawId);
			mListAdapter.notifyDataSetInvalidated();
		}
		
	}
	
	private void startRun(int position){
		for(int i = 0; i < 6;i++){
			mHandler.postDelayed(new FrameRunnable(position, drawableId[i]) , 30*i);
		}
	}

}

class ButtonView {
	int textViewId;

	ButtonView(int tId) {
		textViewId = tId;
	}
}

class ButtonLayoutHolder {
	LinearLayout buttonLayout;
	TextView textView;
}


