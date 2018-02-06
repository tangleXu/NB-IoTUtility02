package com.example.fragment;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.HashMap;
import java.util.Set;
import java.util.UUID;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.Context;
import android.content.Intent;
import android.media.AudioManager;
import android.media.SoundPool;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.example.fragment.ReportSignalInfo;
import com.example.nb_iotutility.MainActivity;
import com.example.nb_iotutility.MyImageView;
import com.example.nb_iotutility.NBSignalPgsBar;
import com.example.nb_iotutility.R;

public class Fragment3 extends Fragment implements OnClickListener{
	protected static final int SHOW_RESPONSE = 0;
	private View curView;
	SoundPool sp;
	HashMap<Integer, Integer> spMap;
	private MyImageView miv;
	public MainActivity ma;
	
//	private static final int REQUEST_ENABLE_BT = 0;
//	private static final int NET_READ_TIMEOUT_MILLIS = 0;
//	private static final int NET_CONNECT_TIMEOUT_FILLIS = 0;
//	private  BluetoothSocket mmSocket;
//	private BluetoothAdapter mBluetoothAdapter;
	
	public String mmStrCmd;
	public ReportSignalInfo rsi;
	public String mStrMCC,mStrMNC,mStrTAC,mStrEARFCN,mStrGCELLID,mStrCAT,mStrSINR,mStrPCI,mStrRSRP,mStrRSRQ,mStrRSSI,mStrBAND;
	private NBSignalPgsBar mNBSP_SINR,mNBSP_RSRP, mNBSP_RSRP2, mNBSP_RSSI, mNBSP_RSRQ;
	private TextView m_tvEARFCN, m_tvPCI, m_tvPLMN, m_tvBAND, m_tvTAC, m_tvGCELLID, m_tvRAT, m_tvCAT;
	private TextView m_tvLTEIntra, m_tvLTEInter;
	
	private Button btnQueryNBInfo;
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState) {	
		curView = inflater.inflate(R.layout.fragment3, null);
		
        mmStrCmd = "";
        mStrBAND=mStrMCC=mStrMNC=mStrTAC=mStrEARFCN=mStrGCELLID=mStrCAT=mStrSINR=mStrPCI=mStrRSRP=mStrRSRQ=mStrRSSI="0";
        ma = (MainActivity) this.getActivity();
		ma.fragmentTerminalHandler = new Handler()
		{

				@Override
				public void handleMessage(Message msg) {
	    			switch(msg.what){
	    			case SHOW_RESPONSE:
	    				String response = (String)msg.obj;
	    				//tvMsgRes.setText(response);
	    				//Toast.makeText(ma.getApplicationContext(), response, Toast.LENGTH_LONG).show();
	    				
	    				handle_msg_from_BT(response);
	    				
	    				break;
	    			default:
	    				break;
	    				
	    			}
					super.handleMessage(msg);
				}
				
		};
		
		btnQueryNBInfo = (Button) curView.findViewById(R.id.btnF3InfoSearch);
		btnQueryNBInfo.setOnClickListener(this);
		
		/////////////////////////////////////////////////////////////////////////////
		m_tvEARFCN = (TextView) curView.findViewById(R.id.tvF3EARFCN);
		m_tvPCI = (TextView) curView.findViewById(R.id.tvF3PCI);
		m_tvPLMN = (TextView) curView.findViewById(R.id.tvF3PLMN);
		m_tvBAND = (TextView) curView.findViewById(R.id.tvF3BAND);
		m_tvTAC = (TextView) curView.findViewById(R.id.tvF3TAC);
		m_tvGCELLID = (TextView) curView.findViewById(R.id.tvF3GCELLID);
		m_tvRAT = (TextView) curView.findViewById(R.id.tvF3RAT);
		m_tvCAT = (TextView) curView.findViewById(R.id.tvF3CAT);
		
		m_tvLTEIntra = (TextView) curView.findViewById(R.id.tvLTEINTRA);
		//m_tvLTEInter = (TextView) curView.findViewById(R.id.tvLTEINTER);
		
		mNBSP_RSRP = (NBSignalPgsBar) curView.findViewById(R.id.nbSPB_rsrp);
		mNBSP_RSRP2 = (NBSignalPgsBar) curView.findViewById(R.id.nbSPB_rsrp2);
		mNBSP_SINR = (NBSignalPgsBar) curView.findViewById(R.id.nbSPB_sinr);
		mNBSP_RSSI = (NBSignalPgsBar) curView.findViewById(R.id.nbSPB_rssi);
		mNBSP_RSRQ = (NBSignalPgsBar) curView.findViewById(R.id.nbSPB_rsrq);
		
		
		//ImageButton imgBtn = (ImageButton) curView.findViewById(R.id.img_btn_scan);
		//imgBtn.setOnClickListener((OnClickListener) this);
		//InitSound();
		//InitBluetooth();
		
		//miv = (MyImageView) curView.findViewById(R.id.myImgView01);
		//rsi = new ReportSignalInfo(this.getActivity());
		
		return curView;
	}
	private void handle_msg_from_BT(String recvMsg)
	{
		
		mmStrCmd=mmStrCmd + recvMsg;
		String ret="";
		ret = ParseLineInfo(recvMsg, ".*EARFCN:(.+)GCELLID");
		if(ret != "NA")
		{ 
			mStrEARFCN = ret;
			Log.e("ARIC","RECV:"+ret);
		}
		ret = ParseLineInfo(recvMsg, ".*GCELLID:(.+)TAC");
		//Log.e("ARIC","RECV:"+ret);
		if(ret != "NA")
		{ 
			mStrGCELLID = ret;
			Log.e("ARIC","RECV:"+ret);
		}
		
		ret = ParseLineInfo(recvMsg, ".*TAC:(.+)MCC");
		if(ret != "NA")
		{ 
			mStrTAC = ret;
			Log.e("ARIC","RECV:"+ret);
		}
		
		ret = ParseLineInfo(recvMsg, ".*MCC:(.+)MNC");
		if(ret != "NA")
		{ 
			mStrMCC = ret;
			Log.e("ARIC","RECV:"+ret);
		}
		
		ret = ParseLineInfo(recvMsg, ".*MNC:(.+)DLBW");
		if(ret != "NA")
		{ 
			mStrMNC = ret;
			Log.e("ARIC","RECV:"+ret);
		}
		
		ret = ParseLineInfo(recvMsg, ".*SINR:(.+)CAT");
		if(ret != "NA")
		{ 
			mStrSINR = ret;
			Log.e("ARIC","RECV:"+ret);
		}
		
		ret = ParseLineInfo(recvMsg, ".*CAT:(.+)BAND");
		if(ret != "NA")
		{ 
			mStrCAT = ret;
			Log.e("ARIC","RECV:"+ret);
		}
		
		ret = ParseLineInfo(recvMsg, ".*BAND:(.+)PCI");
		if(ret != "NA")
		{ 
			mStrBAND = ret;
			Log.e("ARIC","RECV:"+ret);
		}
		
		ret = ParseLineInfo(recvMsg, ".*PCI:(.+)RSRP");
		if(ret != "NA")
		{ 
			mStrPCI = ret;
			Log.e("ARIC","RECV:"+ret);
		}
		
		ret = ParseLineInfo(recvMsg, ".*RSRP:(.+)RSRQ");
		if(ret != "NA")
		{ 
			mStrRSRP = ret;
			Log.e("ARIC","RECV:"+ret);
		}
		ret = ParseLineInfo(recvMsg, ".*RSRQ:(.+)RSSI");
		if(ret != "NA")
		{ 
			mStrRSRQ = ret;
			Log.e("ARIC","RECV:"+ret);
		}
		
		ret = ParseLineInfo(recvMsg, ".*RSSI:(.+)[\r|\n| ]");
		if(ret != "NA")
		{ 
			mStrRSSI = ret;
			Log.e("ARIC","RECV:"+ret);
		}
		
		int pFind = mmStrCmd.indexOf("LTE INTRA INFO:");
		if(pFind>0)
		{
			m_tvLTEIntra.setText(mmStrCmd.substring(pFind));
		}
		
//		ret = ParseLineInfo(mmStrCmd, ".*INTER INFO:(.+)OK");
//		if(ret != "NA")
//		{ 
//			m_tvLTEInter.setText(ret);
//			Log.e("ARIC","RECV:"+ret);
//		}
		
		 //m_tvEARFCN, m_tvPCI, m_tvPLMN, m_tvBAND, m_tvTAC, m_tvGCELLID, m_tvRAT, m_tvCAT;
		m_tvEARFCN.setText(mStrEARFCN);
		m_tvPCI.setText(mStrPCI);
		m_tvPLMN.setText(mStrMCC+"/"+mStrMNC);
		m_tvBAND.setText(mStrBAND);
		m_tvTAC.setText(mStrTAC);
		m_tvGCELLID.setText(mStrGCELLID);
		m_tvRAT.setText("GPRS");
		m_tvCAT.setText(mStrCAT);
		
		float fRSRP = Float.parseFloat(mStrRSRP) + 140; // -140~ -40  (0-100)
		mNBSP_RSRP.setText(mStrRSRP); mNBSP_RSRP2.setText(mStrRSRP);
		mNBSP_RSRP.setPercent(fRSRP); mNBSP_RSRP2.setPercent(fRSRP);
		
		float fRSRQ = Float.parseFloat(mStrRSRQ) + 20; // -20~-3  (0-17)
		mNBSP_RSRQ.setText(mStrRSRQ); 
		mNBSP_RSRQ.setPercent((float) (fRSRQ*100/17.0));

		float fRSSI = Float.parseFloat(mStrRSSI) + 120; // -120~0(-120~-40)  (0-80)
		mNBSP_RSSI.setText(mStrRSSI); 
		mNBSP_RSSI.setPercent((float) (fRSSI*100/80.0));

		float fSINR = Float.parseFloat(mStrSINR) + 10; // -10~40 (0-50)
		mNBSP_SINR.setText(mStrSINR); 
		mNBSP_SINR.setPercent((float) (fSINR*100/50.0));
		
		
		
	}
	
	public void InitSound(){
		sp = new SoundPool(5, AudioManager.STREAM_MUSIC,0);
		spMap = new HashMap<Integer, Integer>();
		spMap.put(1,sp.load(this.getActivity(), R.raw.di_btn, 1));
		spMap.put(2,sp.load(this.getActivity(), R.raw.reminder,1));
	}
	public void playSound(int sound, int number) {
		AudioManager am = (AudioManager) this.getActivity().getSystemService(Context.AUDIO_SERVICE);
	float audioMaxVolumn = am.getStreamMaxVolume(AudioManager.STREAM_MUSIC);
	float volumnCurrent = am.getStreamVolume(AudioManager.STREAM_MUSIC);
	float volumnRatio = volumnCurrent / audioMaxVolumn;
	sp.play(spMap.get(sound), volumnRatio, volumnRatio, 1, number,  1f);
	}

	public void onClick(View arg0) {
		// TODO Auto-generated method stub

		switch(arg0.getId())
		{
		case R.id.btnF3InfoSearch:
			ma.bis.sendCmd("@#at+lscellinfo#@\r\n");
			Log.d("ARIC",mmStrCmd);
			mmStrCmd = "";
			
			break;
		default:
			break;
		}
		
//		mmStrCmd="INDEX\r\n";
		
//		Toast.makeText(this.getActivity().getApplicationContext(), "Send scan signal!", Toast.LENGTH_LONG).show();
//        rsi.setDevName("NB-PoC001","ERICSSON SH WILD-C","31.2364733460","121.3627737870");
//        rsi.setNetworkInfo(mStrMCC,mStrMNC, mStrTAC, mStrGCELLID, mStrSINR, mStrRSRP, mStrRSRQ, mStrRSSI);
//        rsi.setPHY(mStrBAND, mStrEARFCN, mStrCAT, mStrPCI);
//        rsi.setEPS("0.0.0.0", "Pending");		
//        rsi.sendRequestWithHttpClient();
//		miv.play();
	}
	public String ParseLineInfo(String line, String regex_str) {
		// TODO Auto-generated method stub
		
		//Parse EARFDN
		Pattern p = Pattern.compile(regex_str);
		Matcher m = p.matcher(line);
		
		boolean rs = m.find();
		if(rs){
			return m.group(1)+"";
		}
		
		//for(int i=1;i<=m.groupCount();i++){
		//	Log.e("ARIC","RECV:"+m.group(i));
		//}
		return "NA";
		
	}		
}