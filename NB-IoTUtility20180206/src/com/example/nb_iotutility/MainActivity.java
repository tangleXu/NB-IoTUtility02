package com.example.nb_iotutility;

import java.util.List;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTabHost;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.webkit.WebView;
import android.widget.ImageView;
import android.widget.Toast;
import android.widget.TabHost.TabSpec;
import android.widget.TextView;

import com.example.fragment.Fragment1;
import com.example.fragment.Fragment2;
import com.example.fragment.Fragment3;
import com.example.fragment.Fragment4;
import com.example.fragment.Fragment5;
import android.webkit.WebView;


public class MainActivity<Content> extends FragmentActivity implements OnBtInteractionCallback {
	public BtInteractionService bis;
	
	public static final int SHOW_RESPONSE = 0;

	private static final int BTMSGRECV = 1;
	private WebView myWebView;
	public String btRecv;
	public Boolean m_bBTstatus=false;
	private Content content;
	/**
	 * FragmentTabhost
	 */
	public FragmentTabHost mTabHost; 
 
	/**
	 * 布局填充器
	 * 
	 */
	private LayoutInflater mLayoutInflater;

	/**
	 * Fragment数组界面
	 * 
	 */
	private Class mFragmentArray[] = { Fragment1.class, Fragment2.class,
			Fragment3.class, Fragment4.class, Fragment5.class };
	/**
	 * 存放图片数组
	 * 
	 */
	private int mImageArray[] = { R.drawable.tab_home_btn,
			R.drawable.tab_message_btn, R.drawable.tab_selfinfo_btn,
			R.drawable.tab_square_btn, R.drawable.tab_more_btn };

	/**
	 * 选修卡文字
	 * 
	 */
	private String mTextArray[] = { "首页", "配置", "终端", "地图", "更多" };
	/**
	 * 
	 * 
	 */
	public Handler handler;
	public Handler fragmentHandler;
	public Handler fragmentExpertHandler;
	public Handler fragmentTerminalHandler;
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
	    this.requestWindowFeature(Window.FEATURE_NO_TITLE);
		 
		setContentView(R.layout.activity_main);
		
		//Init bluetooth instance.
		bis = new BtInteractionService(this);
        bis.initBT();
        
        content = (Content) this;
		initView();
		
		fragmentHandler = null;
		fragmentExpertHandler = null;
		fragmentTerminalHandler = null;
		
        handler = new Handler() {

    		@Override
    		public void handleMessage(Message msg) {
    			// TODO Auto-generated method stub
    			super.handleMessage(msg);
    			switch(msg.what){
    			case SHOW_RESPONSE:
    				String response = (String)msg.obj;
    				
    				Toast.makeText((Context) content,response, 0).show();
    				break;
    			default:
    				break;
    				
    			}
    		}
    	};
		
	}

	/**
	 * 初始化组件
	 */
	private void initView() {
		mLayoutInflater = LayoutInflater.from(this);

		// 找到TabHost
		mTabHost = (FragmentTabHost) findViewById(android.R.id.tabhost);
		mTabHost.setup(this, getSupportFragmentManager(), R.id.realtabcontent);
		// 得到fragment的个数
		int count = mFragmentArray.length;
		for (int i = 0; i < count; i++) {
			// 给每个Tab按钮设置图标、文字和内容
			TabSpec tabSpec = mTabHost.newTabSpec(mTextArray[i])
					.setIndicator(getTabItemView(i));
			// 将Tab按钮添加进Tab选项卡中
			mTabHost.addTab(tabSpec, mFragmentArray[i], null);
			// 设置Tab按钮的背景
			mTabHost.getTabWidget().getChildAt(i)
					.setBackgroundResource(R.drawable.selector_tab_background);
			
		}
	}

	/**
	 *
	 * 给每个Tab按钮设置图标和文字
	 */
	private View getTabItemView(int index) {
		View view = mLayoutInflater.inflate(R.layout.tab_item_view, null);
		ImageView imageView = (ImageView) view.findViewById(R.id.imageview);
		imageView.setImageResource(mImageArray[index]);
		TextView textView = (TextView) view.findViewById(R.id.textview);
		textView.setText(mTextArray[index]);

		return view;
	}

	public void onTimeout() {
		// TODO Auto-generated method stub
		
	}

	public void onBTEnabledAck() {
		// TODO Auto-generated method stub
		showMsgBox("BT Not enabled!");
	}

	public void onBTConnected() {
		// TODO Auto-generated method stub
		showMsgBox("BT Connected!");
		m_bBTstatus = true;
	}

	public void onBTConnectFailure() {
		// TODO Auto-generated method stub
		showMsgBox("BT Connect failure!");
		m_bBTstatus = false;
	}

	public void onBTSendSuccess() {
		// TODO Auto-generated method stub
		showMsgBox("BT Send success!");
	}

	public void onBTSendFailure() {
		// TODO Auto-generated method stub
		showMsgBox("BT send failure!");
	}

	public void onBTRecvSuccess(String recv) {
		// TODO Auto-generated method stub
		//showMsgBox("BT Recv success:"+recv);
		showMsgBox(recv);
	}

	public void onBTRecvFailure() {
		// TODO Auto-generated method stub
		showMsgBox("BT Recv failure!");
	}
	void showMsgBox(String ss){
		//Looper.prepare();
		//Toast.makeText(getApplicationContext(), ss, Toast.LENGTH_SHORT).show();
		Log.d("ARIC", "MainActivity: "+ss);
		Message msg = new Message();
		//msg.what = BTMSGRECV;
		msg.what = SHOW_RESPONSE;
		msg.obj = ss;
		//btRecv = ss;
		//handler.sendMessage(msg);
		//Looper.loop();
		//if(fragmentHandler != null)
		//	fragmentHandler.sendMessage(msg);
		if(mTabHost != null)
		{
			int tabIdx = mTabHost.getCurrentTab();
			Log.d("ARIC","CURRENT TAB:" + tabIdx);
			if(tabIdx == 0)
			{
				if(fragmentHandler != null)
					fragmentHandler.sendMessage(msg);
			}else if(tabIdx == 4){
				if(fragmentExpertHandler != null)
					fragmentExpertHandler.sendMessage(msg);				
			}else if(tabIdx == 2)
			{
				if(fragmentTerminalHandler != null)
					fragmentTerminalHandler.sendMessage(msg);
				
			}
			else
			{
				
			}
		}

	}
}

