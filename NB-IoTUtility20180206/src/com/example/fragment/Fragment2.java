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

public class Fragment2 extends Fragment implements OnClickListener{
	protected static final int SHOW_RESPONSE = 0;
	private View mP2View;
	public MainActivity ma;
	private Button mBtnAttach;
	private Button mBtnGroup[] = new Button[25];
	private EditText mEtPLMN, mEtAPN, mEtTCPServer, mEtTCPPort, mEtUdpServer, mEtUdpPort,mEtTcpTx,mEtUdpTx;
	private TextView mTvTcpRecv, mTvUdpRecv;
	
	private String eDRX_items[] = {"AT+EDRXRDP", "AT+EDRXS=0", "AT+EDRXS=1,5,\"0000\"",
			"AT+EDRXS=1,5,\"0001\"","AT+EDRXS=1,5,\"0010\"","AT+EDRXS=1,5,\"0011\"","AT+EDRXS=1,5,\"0100\"",
			"AT+EDRXS=1,5,\"0101\"","AT+EDRXS=1,5,\"0111\"","AT+EDRXS=1,5,\"1000\"","AT+EDRXS=1,5,\"1001\""};
	private String PSM_items[] = {"AT+CPSMS?", "AT+CPSMS=0", "AT+CPSMS=1,,,\"10000101\",\"\00000011\"",
			"AT+CPSMS=1,,,\"10100101\",\"\00000011\"","AT+CPSMS=1,,,\"01100101\",\"\00000011\"",
			"AT+CPSMS=1,,,\"10100101\",\"\00100011\"","AT+CPSMS=1,,,\"01100101\",\"\01100011\""};
	
	protected int mEDRXSel=0;
	
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState) {	
		mP2View = inflater.inflate(R.layout.fragment2, null);		
		
		ma = (MainActivity) this.getActivity();
		
		mEtPLMN = (EditText) mP2View.findViewById(R.id.etF2PLMN);
		mEtAPN = (EditText) mP2View.findViewById(R.id.etF2APN);
		
		//mBtnAttach = (Button) mP2View.findViewById(R.id.btnF2Attach);
		
		
		//mBtnAttach.setOnClickListener(this);
		mBtnGroup[0] = (Button) mP2View.findViewById(R.id.btnF2Attach);
		mBtnGroup[1] = (Button) mP2View.findViewById(R.id.btnF2Detach);
		mBtnGroup[2] = (Button) mP2View.findViewById(R.id.btnF2EnablePPP);
		mBtnGroup[3] = (Button) mP2View.findViewById(R.id.btnF2DisablePPP);
		mBtnGroup[4] = (Button) mP2View.findViewById(R.id.btnF2eDRX);
		mBtnGroup[5] = (Button) mP2View.findViewById(R.id.btnF2PSM);
		mBtnGroup[6] = (Button) mP2View.findViewById(R.id.btnF2QueryAPN);
		mBtnGroup[7] = (Button) mP2View.findViewById(R.id.btnF2QueryPLMN);
		mBtnGroup[8] = (Button) mP2View.findViewById(R.id.btnF2QueryAttach);
		mBtnGroup[9] = (Button) mP2View.findViewById(R.id.btnF2QueryIMSI);
		mBtnGroup[10] = (Button) mP2View.findViewById(R.id.btnF2QueryIMEI);
		mBtnGroup[11] = (Button) mP2View.findViewById(R.id.btnF2QueryCSQ);
		mBtnGroup[12] = (Button) mP2View.findViewById(R.id.btnF2ManualCfgNet);
		mBtnGroup[13] = (Button) mP2View.findViewById(R.id.btnF2CfgAPN);
		mBtnGroup[14] = (Button) mP2View.findViewById(R.id.btnF2AutoMode);
		mBtnGroup[15] = (Button) mP2View.findViewById(R.id.btnF2GsmOnly);
		mBtnGroup[16] = (Button) mP2View.findViewById(R.id.btnF2LteOnly);
		mBtnGroup[17] = (Button) mP2View.findViewById(R.id.btnF2GsmLte);
		mBtnGroup[18] = (Button) mP2View.findViewById(R.id.btnF2LteCatm1Only);
		mBtnGroup[19] = (Button) mP2View.findViewById(R.id.btnF2LteNBOnly);
		mBtnGroup[20] = (Button) mP2View.findViewById(R.id.btnF2LteBoth);
		mBtnGroup[21] = (Button) mP2View.findViewById(R.id.btnF2TCPConnect);
		mBtnGroup[22] = (Button) mP2View.findViewById(R.id.btnF2TCPSend);
		mBtnGroup[23] = (Button) mP2View.findViewById(R.id.btnF2UDPConnect);
		mBtnGroup[24] = (Button) mP2View.findViewById(R.id.btnF2UDPSend);
		
		/*
		R.id.btnF2Attach
		R.id.btnF2Detach
		R.id.btnF2EnablePPP
		R.id.btnF2DisablePPP
		R.id.btnF2eDRX
		R.id.btnF2PSM
		R.id.btnF2QueryAPN
		R.id.btnF2QueryPLMN
		R.id.btnF2QueryAttach
		R.id.btnF2QueryIMSI
		R.id.btnF2QueryIMEI
		R.id.btnF2QueryCSQ
		R.id.btnF2ManualCfgNet
		R.id.btnF2CfgAPN
		R.id.btnF2AutoMode
		R.id.btnF2GsmOnly
		R.id.btnF2LteOnly
		R.id.btnF2GsmLte
		R.id.btnF2LteCatm1Only
		R.id.btnF2LteNBOnly
		R.id.btnF2LteBoth
		R.id.btnF2TCPConnect
		R.id.btnF2TCPSend
		R.id.btnF2UDPConnect
		R.id.btnF2UDPSend
		 * */
		for(int i=0;i<25;i++)
		{
			mBtnGroup[i].setOnClickListener(this);
			
		}
		
		ma.fragmentCfgHandler  = new Handler()
		{

			@Override
			public void handleMessage(Message msg) {
    			switch(msg.what){
    			case SHOW_RESPONSE:
    				String response = (String)msg.obj;
    				Toast.makeText(ma, response, Toast.LENGTH_SHORT).show();
    				
    				break;
    			default:
    				break;
    			}
				super.handleMessage(msg);
			}
			
		};
		
		return mP2View; 
	}

	private void cfg_eDRX()
	{
        AlertDialog dialog = new AlertDialog.Builder(ma)
        .setIcon(R.drawable.bt_logo)//设置标题的图片
        .setTitle("请选择eDRX配置")//设置对话框的标题
        .setSingleChoiceItems(eDRX_items, 0, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                //Toast.makeText(ma, items[which], Toast.LENGTH_SHORT).show();
            	mEDRXSel= which;
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
            	
            	Toast.makeText(ma, "Choice:" + eDRX_items[mEDRXSel], Toast.LENGTH_SHORT).show();
            	//mEtATCmd.setText(items[nSel]);
            	ma.bis.sendCmd("#@"+eDRX_items[mEDRXSel]+"@#\r\n");
            	
                dialog.dismiss();
            }
        }).create();
        dialog.show();
		
	}
	private void cfg_PSM()
	{
        AlertDialog dialog = new AlertDialog.Builder(ma)
        .setIcon(R.drawable.bt_logo)//设置标题的图片
        .setTitle("请选择PSM配置")//设置对话框的标题
        .setSingleChoiceItems(PSM_items, 0, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                //Toast.makeText(ma, items[which], Toast.LENGTH_SHORT).show();
            	mEDRXSel= which;
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
            	
            	Toast.makeText(ma, "Choice:" + PSM_items[mEDRXSel], Toast.LENGTH_SHORT).show();
            	//mEtATCmd.setText(items[nSel]);
            	ma.bis.sendCmd("#@"+PSM_items[mEDRXSel]+"@#\r\n");
            	
                dialog.dismiss();
            }
        }).create();
        dialog.show();
		
	}	
	public void onClick(View arg0) {
		String atCmd;
		
		switch(arg0.getId())
		{
		case R.id.btnF2Attach:
				ma.bis.sendCmd("@#AT+CGATT=1#@\r\n");
			break;
		case	R.id.btnF2Detach:
			ma.bis.sendCmd("@#AT+CGATT=0#@\r\n");
			break;
		case	R.id.btnF2EnablePPP:
			ma.bis.sendCmd("@#AT+LSIPCALL=1#@\r\n");
			break;
		case	R.id.btnF2DisablePPP:
			ma.bis.sendCmd("@#AT+LSIPCALL=0#@\r\n");
			break;
		case	R.id.btnF2eDRX:
			//TODO
			cfg_eDRX();
			break;
		case	R.id.btnF2PSM:
			//TODO
			cfg_PSM();
			break;
		case	R.id.btnF2QueryAPN:
			ma.bis.sendCmd("@#AT+CGDCONT?#@\r\n");
			break;
		case	R.id.btnF2QueryPLMN:
			ma.bis.sendCmd("@#AT+COPS?#@\r\n");
			break;
		case	R.id.btnF2QueryAttach:
			ma.bis.sendCmd("@#AT+CGATT?#@\r\n");
			break;
		case	R.id.btnF2QueryIMSI:
			ma.bis.sendCmd("@#AT+CIMI#@\r\n");
			break;
		case	R.id.btnF2QueryIMEI:
			ma.bis.sendCmd("@#AT+CGSN#@\r\n");
			break;
		case	R.id.btnF2QueryCSQ:
			ma.bis.sendCmd("@#AT+CSQ#@\r\n");
			break;
		case	R.id.btnF2ManualCfgNet:
			//TODO: To get the EditText
			atCmd = "@#AT+COPS=1,2,\"" + mEtPLMN.getText().toString().trim() + "\"#@\r\n";
			ma.bis.sendCmd(atCmd);
			break;
		case	R.id.btnF2CfgAPN:
			//TODO: To get the EditText
		    atCmd = "@#AT+CGDCONT=1,\"IP\",\"" + mEtAPN.getText().toString().trim() + "\"#@\r\n";
			ma.bis.sendCmd(atCmd);
			//ma.bis.sendCmd("@#AT+CGDCONT=1#@\r\n");
			break;
		case	R.id.btnF2AutoMode:
			ma.bis.sendCmd("@#AT+MODODR=2#@\r\n");
			break;
		case	R.id.btnF2GsmOnly:
			ma.bis.sendCmd("@#AT+MODODR=3#@\r\n");
			break;
		case	R.id.btnF2LteOnly:
			ma.bis.sendCmd("@#AT+MODODR=5#@\r\n");
			break;
		case	R.id.btnF2GsmLte:
			ma.bis.sendCmd("@#AT+MODODR=8#@\r\n");
			break;
		case	R.id.btnF2LteCatm1Only:
			ma.bis.sendCmd("@#AT+LTEOPMOD=1#@\r\n");
			break;
		case	R.id.btnF2LteNBOnly:
			ma.bis.sendCmd("@#AT+LTEOPMOD=2#@\r\n");
			break;
		case	R.id.btnF2LteBoth:
			ma.bis.sendCmd("@#AT+LTEOPMOD=3#@\r\n");
			break;
		case	R.id.btnF2TCPConnect:
			//TODO:
			break;
		case	R.id.btnF2TCPSend:
			//TODO:
			break;
		case	R.id.btnF2UDPConnect:
			//TODO:
			break;
		case	R.id.btnF2UDPSend:
			//TODO:
				break;
		default:
			break;
		
		}
		
	}	
}