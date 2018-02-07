package com.example.fragment;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
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
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.AudioManager;
import android.media.SoundPool;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import android.app.Activity;

import com.example.fragment.ReportSignalInfo;
import com.example.nb_iotutility.BaiduMapActivity;
import com.example.nb_iotutility.MainActivity;
import com.example.nb_iotutility.MyImageView;
import com.example.nb_iotutility.NBSignalPgsBar;
import com.example.nb_iotutility.R;

public class Fragment3 extends Fragment implements OnClickListener{
	protected static final int SHOW_RESPONSE = 0;
	private static final int RESULT_OK = -1;
	private static final int REQUEST_ORIGINAL = 0;
	private static final int REQUEST_GPS = 8;
	private View curView;
	SoundPool sp;
	HashMap<Integer, Integer> spMap;
	private MyImageView miv;
	public MainActivity ma;
	private String sdPath;//SD卡的路径 
	private String picPath;//图片存储路径 
	private Button mBtnTakePhoto, mBtnPositioning, mBtnUpload;
	private ImageView mImageView; 
//	private static final int REQUEST_ENABLE_BT = 0;
//	private static final int NET_READ_TIMEOUT_MILLIS = 0;
//	private static final int NET_CONNECT_TIMEOUT_FILLIS = 0;
//	private  BluetoothSocket mmSocket;
//	private BluetoothAdapter mBluetoothAdapter;
	public String sLng,sLat;
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
		sLng=sLat="";
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
        //Get SD path.
		sdPath = Environment.getExternalStorageDirectory().getPath(); 
        picPath = sdPath + "/" + "NB-IoT_SignalInfo.png"; 
        mImageView = (ImageView) curView.findViewById(R.id.ivPhoto);
        
		btnQueryNBInfo = (Button) curView.findViewById(R.id.btnF3InfoSearch);
		btnQueryNBInfo.setOnClickListener(this);
		//mBtnTakePhoto, mBtnPositioning, mBtnUpload;
		mBtnTakePhoto = (Button) curView.findViewById(R.id.btnF3TakePhoto);
		mBtnTakePhoto.setOnClickListener(this);
		mBtnPositioning = (Button) curView.findViewById(R.id.btnF3Positioning);
		mBtnPositioning.setOnClickListener(this);
		mBtnUpload = (Button) curView.findViewById(R.id.btnF3Upload);
		mBtnUpload.setOnClickListener(this);
		
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
		
		//miv = (MyImageView) curView.findViewById(R.id.myImgView01);
		rsi = new ReportSignalInfo(ma);
		
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
		case R.id.btnF3TakePhoto:
		      Intent intent2 = new Intent(MediaStore.ACTION_IMAGE_CAPTURE); 
		      Uri uri = Uri.fromFile(new File(picPath)); 
		      //为拍摄的图片指定一个存储的路径 
		      intent2.putExtra(MediaStore.EXTRA_OUTPUT, uri); 
		      startActivityForResult(intent2, REQUEST_ORIGINAL); 			
			break;
		case R.id.btnF3Positioning:
			//TODO: Tomorrow.
	        Intent intent = new Intent(ma, BaiduMapActivity.class);
	        
	        Bundle bundle = new Bundle();  
	        bundle.putString("GPS", "Get GPS Info");  
	  
	        intent.putExtras(bundle);  
	  
	        // 参数 REQUEST_ONE ：请求依据，作为在onActivityResult做处理时识别码  
	        startActivityForResult(intent, REQUEST_GPS);			
			break;
		case R.id.btnF3Upload:
			Toast.makeText(this.getActivity().getApplicationContext(), "Send scan signal!", Toast.LENGTH_LONG).show();
	        rsi.setDevName("NB-PoC001","ERICSSON SH WILD-C","31.2364733460","121.3627737870");
	        rsi.setNetworkInfo(mStrMCC,mStrMNC, mStrTAC, mStrGCELLID, mStrSINR, mStrRSRP, mStrRSRQ, mStrRSSI);
	        rsi.setPHY(mStrBAND, mStrEARFCN, mStrCAT, mStrPCI);
	        rsi.setEPS("0.0.0.0", "Pending");		
	        rsi.sendRequestWithHttpClient();
			
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
	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		// TODO Auto-generated method stub
		super.onActivityResult(requestCode, resultCode, data);
		
		Log.d("ARIC","Save photo."+resultCode);
		
		   if (resultCode == RESULT_OK) { 
			        //FileInputStream fis = null; 
			        try { 
			          Log.e("ARIC",picPath); 
			          //把图片转化为字节流 
			          //fis = new FileInputStream(picPath); 
			          //把流转化图片 
			          //Bitmap bitmap = BitmapFactory.decodeStream(fis); 
			          Bitmap bitmap = getBitmapByWidth(picPath,100,0);
			          mImageView.setImageBitmap(bitmap); 
			          //bitmap.recycle();
			        }finally{ 
			        } 
			    		        
			    } 
           if (requestCode == REQUEST_GPS) {
               sLng = data.getStringExtra("LNG");
               sLat = data.getStringExtra("LAT");

           }
		   
	}
	public Bitmap getBitmapByWidth(String localImagePath, int width, int addedScaling) {
        if (TextUtils.isEmpty(localImagePath)) {
            return null;
        }

        Bitmap temBitmap = null;

        try {
            BitmapFactory.Options outOptions = new BitmapFactory.Options();

            // 设置该属性为true，不加载图片到内存，只返回图片的宽高到options中。
            outOptions.inJustDecodeBounds = true;

            // 加载获取图片的宽高
            BitmapFactory.decodeFile(localImagePath, outOptions);

            int height = outOptions.outHeight;

            if (outOptions.outWidth > width) {
                // 根据宽设置缩放比例
                outOptions.inSampleSize = outOptions.outWidth / width + 1 + addedScaling;
                outOptions.outWidth = width;

                // 计算缩放后的高度
                height = outOptions.outHeight / outOptions.inSampleSize;
                outOptions.outHeight = height;
            }

            // 重新设置该属性为false，加载图片返回
            outOptions.inJustDecodeBounds = false;
            temBitmap = BitmapFactory.decodeFile(localImagePath, outOptions);
        } catch (Throwable t) {
            t.printStackTrace();
        }

        return temBitmap;
    }
}