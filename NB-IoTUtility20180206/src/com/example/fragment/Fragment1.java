package com.example.fragment;

import java.io.IOException;

import android.app.Activity;
import android.content.Context;
import android.content.res.Resources.NotFoundException;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;


import com.example.nb_iotutility.MainActivity;
import com.example.nb_iotutility.R;

public class Fragment1 extends Fragment implements OnClickListener{
    protected static final int SHOW_RESPONSE = 0;
	private View view1;
    public MainActivity ma;
    public TextView tvStatus;
    private Button btnConnectBT;
    
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		//ma=null;
		ma = (MainActivity) this.getActivity();
		view1 = inflater.inflate(R.layout.fragment1, null);
		btnConnectBT = (Button) view1.findViewById(R.id.btnConnect);
		btnConnectBT.setOnClickListener(this); 
		tvStatus = (TextView) view1.findViewById(R.id.tvBtStatus);
		
		Button btnCloseApp = (Button) view1.findViewById(R.id.btnAppExit);
		btnCloseApp.setOnClickListener(this);
		
		ma.fragmentHandler = new Handler()
		{

			@Override
			public void handleMessage(Message msg) {
    			switch(msg.what){
    			case SHOW_RESPONSE:
    				String response = (String)msg.obj;
    				tvStatus.setText(response);
    				if(response.indexOf("Connected")>0)
    				{	
    					tvStatus.setText("蓝牙已连接");
    					btnConnectBT.setText("断开设备");
    			    }
    				
    				break;
    			default:
    				break;
    				
    			}
				super.handleMessage(msg);
			}
			
		};
		return view1;
	}
	@Override
	public void onAttach(Activity activity) {
		ma = (MainActivity) activity;
		super.onAttach(activity);
	}
	public void onClick(View arg0) {
		// TODO Auto-generated method stub
		ma = (MainActivity) this.getActivity();
		if(arg0.getId() == R.id.btnAppExit)
		{
			ma.bis.closeBt();
			ma.finish();
		}
		
		if(ma!=null && arg0.getId() == R.id.btnConnect)
		{
			if(ma.m_bBTstatus == false)
			{
				ma.bis.runBT();

			}else{
				ma.bis.closeBt();
				ma.m_bBTstatus = false;
				tvStatus.setText("蓝牙未连接");
				btnConnectBT.setText("连接设备");
			}
		}
	}
	@Override
	public void onResume() {
		if(ma !=null){
			if(ma.m_bBTstatus){
				tvStatus.setText("蓝牙已连接");
				btnConnectBT.setText("断开设备");
			}
			else{
				tvStatus.setText("蓝牙未连接");
				btnConnectBT.setText("连接设备");
			}
		}
		super.onResume();
	}
	
}
