package com.example.fragment;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.nb_iotutility.MainActivity;
import com.example.nb_iotutility.R;

public class Fragment5 extends Fragment implements OnClickListener{
	protected static final int SHOW_RESPONSE = 0;
	private View mView1;
	private TextView tvMsgRes;
	private Button mBtnSend;
	private EditText mEtATCmd;
	public String m_AT_CMD;
	public MainActivity ma;
	private Button mBtnGetATCmd;
	public int nSel=0;
	public Button mBtnStartPPP,mBtnStopPPP,mBtnManualNet,mBtnEnScram,mBtnDisabScram,mBtnPWR;
	
	private String items[] = {"AT", "AT+CSQ", "AT+CIMI","AT+CGATT=0",
			"AT+PSRAT","ATE0","ATI","AT+LCTSW","AT+CGSN=?",
			"AT+ICCID=?","AT+CNUM","AT+CFUN=0","AT+CEREG=1"
			,"AT+COPS?","AT+COPS=1,2,\"46000\"","AT+MODODR?",
			"AT+LTEOPMOD?","AT+LCECELLINFO","AT+LSCELLINFO",
			"AT+CPSMS?","AT+CEDRXS?","AT+CEDRXRDP","AT+CPIN",
			"AT+CGDCONT?","AT+LSIPCALL?"};
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState) {	
		mView1 = inflater.inflate(R.layout.fragment5, null);
		
		 tvMsgRes = (TextView) mView1.findViewById(R.id.deviceResBox); 
		 mBtnSend = (Button) mView1.findViewById(R.id.btnSentAT2BT);
		 mEtATCmd = (EditText) mView1.findViewById(R.id.etCmdToSend);
		 mBtnGetATCmd = (Button) mView1.findViewById(R.id.btnSelBT);
		 ///////////////////////////////////////////////////////////////
		 //mBtnStartPPP,mBtnStopPPP,mBtnManualNet,mBtnEnScram,mBtnDisabScram,mBtnPWR;
		 mBtnStartPPP = (Button) mView1.findViewById(R.id.btnStartPPP);
		 mBtnStopPPP = (Button) mView1.findViewById(R.id.btnStopPPP);
		 mBtnManualNet = (Button) mView1.findViewById(R.id.btnManualSelNet);
		 mBtnEnScram = (Button) mView1.findViewById(R.id.btnEnableScrambing);
		 mBtnDisabScram = (Button) mView1.findViewById(R.id.btnDisableScrambing);
		 mBtnPWR = (Button) mView1.findViewById(R.id.btnSendPWR);
		 
		 mBtnStartPPP.setOnClickListener(this);
		 mBtnStopPPP.setOnClickListener(this);
		 mBtnManualNet.setOnClickListener(this);
		 mBtnEnScram.setOnClickListener(this);
		 mBtnDisabScram.setOnClickListener(this);
		 mBtnPWR.setOnClickListener(this);
		 
		 ma = (MainActivity) this.getActivity();
		 
		 mBtnGetATCmd.setOnClickListener(new OnClickListener(){

			public void onClick(View arg0) {
				        AlertDialog dialog = new AlertDialog.Builder(ma)
				                .setIcon(R.drawable.bt_logo)//设置标题的图片
				                .setTitle("请选择AT命令")//设置对话框的标题
				                .setSingleChoiceItems(items, 0, new DialogInterface.OnClickListener() {
				                    public void onClick(DialogInterface dialog, int which) {
				                        //Toast.makeText(ma, items[which], Toast.LENGTH_SHORT).show();
				                    	nSel= which;
				                    }
				                })
				                .setNegativeButton("取消", new DialogInterface.OnClickListener() {
				                    public void onClick(DialogInterface dialog, int which) {
				                        dialog.dismiss();
				                    }
				                })
				                .setPositiveButton("确定", new DialogInterface.OnClickListener() {
				                    public void onClick(DialogInterface dialog, int which) 
				                    {
				                    	//String[] name_addr =  items[nSel].split(">>");
				                    	
				                    	Toast.makeText(ma, "Choice:" + items[nSel], Toast.LENGTH_SHORT).show();
				                    	mEtATCmd.setText(items[nSel]);
				                        dialog.dismiss();
				                    }
				                }).create();
				        dialog.show();
				
			}});
		 
		 ma.fragmentExpertHandler = new Handler()
			{

				@Override
				public void handleMessage(Message msg) {
	    			switch(msg.what){
	    			case SHOW_RESPONSE:
	    				String response = (String)msg.obj;
	    				tvMsgRes.setText(response);
	    				break;
	    			default:
	    				break;
	    				
	    			}
					super.handleMessage(msg);
				}
				
			};
			
		 
		 mBtnSend.setOnClickListener(new OnClickListener(){

			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				m_AT_CMD = "@#" + mEtATCmd.getText().toString().trim()+"#@\r\n";
				ma.bis.sendCmd(m_AT_CMD);
			}});
		
		return mView1;	
	}

	public void onClick(View arg0) {
		// TODO Auto-generated method stub
		switch(arg0.getId()){
		case R.id.btnStartPPP:
			ma.bis.sendCmd("@#AT+LSIPCALL=1#@\r\n");
			break;
		case R.id.btnStopPPP:
			ma.bis.sendCmd("@#AT+LSIPCALL=0#@\r\n");
			break;
		case R.id.btnManualSelNet:
			ma.bis.sendCmd("@#AT+COPS=1,2,\"46000\"#@\r\n");
			break;
		case R.id.btnEnableScrambing:
			ma.bis.sendCmd("ENABLE SCRAM\r\n");
			break;
		case R.id.btnDisableScrambing:
			ma.bis.sendCmd("DISABLE SCRAM\r\n");
			break;
		case R.id.btnSendPWR:
			ma.bis.sendCmd("PWR\r\n");
			break;
		default:
			break;
		}
	}	
}