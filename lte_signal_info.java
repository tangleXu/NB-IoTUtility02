package com.example.lte_signal01;

import java.util.List;

import android.os.Build;
import android.os.Bundle;
import android.app.Activity;
import android.content.Context;
import android.telephony.CellIdentityLte;
import android.telephony.CellInfo;
import android.telephony.CellInfoLte;
import android.telephony.CellLocation;
import android.telephony.CellSignalStrengthLte;
import android.telephony.PhoneStateListener;
import android.telephony.ServiceState;
import android.telephony.SignalStrength;
import android.telephony.TelephonyManager;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

public class MainActivity extends Activity {
	TextView tvInfo;
	TextView tvInfo2;
	
	TelephonyManager manager;
	List<CellInfo> cellInfoList;
	Button btn1;
	Context curCnt;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		tvInfo = (TextView) findViewById(R.id.textView1);
		tvInfo2 = (TextView) findViewById(R.id.textView2);
		
		curCnt = this;
		
		btn1 = (Button) findViewById(R.id.button1);
		btn1.setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				getMobileDbm(curCnt);
			}});
		
		
	
		PhoneStateListener phoneStateListener = new PhoneStateListener(){

			@Override
			public void onCellInfoChanged(List<CellInfo> cellInfo) {
				// TODO Auto-generated method stub
				super.onCellInfoChanged(cellInfo);
			}

			@Override
			public void onCellLocationChanged(CellLocation location) {
				// TODO Auto-generated method stub
				super.onCellLocationChanged(location);
			}

			@Override
			public void onServiceStateChanged(ServiceState serviceState) {
				// TODO Auto-generated method stub
				super.onServiceStateChanged(serviceState);
			}

			@Override
			@Deprecated
			public void onSignalStrengthChanged(int asu) {
				// TODO Auto-generated method stub
				super.onSignalStrengthChanged(asu);
			}

			@Override
			public void onSignalStrengthsChanged(SignalStrength signalStrength) {
				// TODO Auto-generated method stub
				super.onSignalStrengthsChanged(signalStrength);
				
				String signalInfo = signalStrength.toString();
				tvInfo.setText(signalInfo);
			}
			
		};
		
		manager = (TelephonyManager) this.getSystemService(TELEPHONY_SERVICE);
		manager.listen(phoneStateListener, PhoneStateListener.LISTEN_SIGNAL_STRENGTHS);
		
		
		
		
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.activity_main, menu);
		return true;
	}

	public int getMobileDbm(Context context)
	{
		int dbm = -1;
		
		TelephonyManager tm =(TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
		
		
		if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1)
		{
			cellInfoList = tm.getAllCellInfo();
			if(null!=cellInfoList)
			{
				for(CellInfo cellInfo:cellInfoList)
				{
					if(cellInfo instanceof CellInfoLte){
						CellSignalStrengthLte cellSignalStrengthLte = ((CellInfoLte)cellInfo).getCellSignalStrength();
						CellIdentityLte cellIdLte =((CellInfoLte)cellInfo).getCellIdentity();
						
						tvInfo2.setText(cellInfo.toString() +"\r\n\r\nPLMN:" + tm.getNetworkOperator() 
								+ "\r\nMSISDN:" + tm.getLine1Number()
								+ "\r\nCountryISO:" + tm.getNetworkCountryIso()
								+ "\r\nOP:" + tm.getNetworkOperatorName() 
								+ "\r\nNetworkType:" + tm.getNetworkType()
								+ "\r\nSubscriberId:" + tm.getSubscriberId()
								+ "\r\nDeviceID:" + tm.getDeviceId()
								+ "\r\nRegister Status:" + cellInfo.isRegistered() 
								+ "\r\nAsuLevel:" + cellSignalStrengthLte.getAsuLevel()
								+ "\r\nSignalDbm:" + cellSignalStrengthLte.getDbm()
								+ "\r\nLevel:" + cellSignalStrengthLte.getLevel()
								+ "\r\nTimingAdvance:" + cellSignalStrengthLte.getTimingAdvance()
								+ "\r\nCI:" + cellIdLte.getCi()
								+ "\r\nPCI:" + cellIdLte.getPci()
								+ "\r\nTAC:" + cellIdLte.getTac()
								);
					}
				}
			}
		}
		return dbm;
		
	}
}
