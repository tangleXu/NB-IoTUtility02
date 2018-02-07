package com.example.nb_iotutility;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.UUID;

import android.app.AlertDialog;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Looper;
import android.util.Log;
import android.widget.Toast;

public class BtInteractionService {
	private OnBtInteractionCallback callback;
	private BluetoothAdapter mBluetoothAdapter;
	private static final int REQUEST_ENABLE_BT = 0;
    private  BluetoothSocket mmSocket;
    private String mmStrCmd="";
    public List<String> btDevList;
	protected int nSel;
    public String[] name_addr;
    public String bt_mac_addr;
    public Boolean m_bRunning = true;
    
	public BtInteractionService(OnBtInteractionCallback cb){
		//Save callback.
		this.callback = cb;
		btDevList = new ArrayList<String>();
		nSel = 0;
		bt_mac_addr=null;
	}
	
	public void sendCmd(String str)
	{
		mmStrCmd = str;
	}
	public void closeBt()
	{
		m_bRunning = false;
	}
	public boolean getConnStatus()
	{
		if(mmSocket != null)
			return mmSocket.isConnected();
		else
			return false;
	}
	
	public void initBT(){
		mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
		
        if(mBluetoothAdapter == null){
        	//Device not support bluetooth.
        	Log.d("ARIC","Device not support bluetooth.");
        }else{
        	Log.d("ARIC","Device support bluetooth.");
        }
        
        //Bluetooth adapter is not enable, need callback.
        if(!mBluetoothAdapter.isEnabled()){
        	//Intent enableBtIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
            //startActivityForResult(enableBtIntent, REQUEST_ENABLE_BT);
        	this.callback.onBTEnabledAck();
        }
        
		//Query paired devices.
		Set<BluetoothDevice> pairedDevices = mBluetoothAdapter.getBondedDevices();
		// If there are paired devices
		if (pairedDevices.size() > 0) {
			// Loop through paired devices
			for (BluetoothDevice device : pairedDevices) {
		     // Add the name and address to an array adapter to show in a ListView
				//mArrayAdapter.add(device.getName() + "\n" + device.getAddress());
				Log.d("ARIC", device.getName() + "==>> " + device.getAddress());
				btDevList.add(device.getName()+">>"+device.getAddress());
				if(device.getName().equalsIgnoreCase("HUAWEI U8818")){
					//ConnectThread connectBtThread = new ConnectThread(device);
					//connectBtThread.start();
					//Log.e("ARIC", "Found HUAWEI U8818");
					
					Log.e("ARIC", "Found NB-IoT Utility.");
		    	 }
		     }
		 }  
	}

	public void runBT(){

        final String[] items = btDevList.toArray(new String[0]);
        
        AlertDialog dialog = new AlertDialog.Builder((Context) this.callback)
                .setIcon(R.drawable.bt_logo)//设置标题的图片
                .setTitle("请选择NB-IoT设备")//设置对话框的标题
                .setSingleChoiceItems(items, 0, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                       // Toast.makeText(MainActivity.this, items[which], Toast.LENGTH_SHORT).show();
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
                    	name_addr =  items[nSel].split(">>");
                    	bt_mac_addr = name_addr[1];
                    	//Toast.makeText((Context) callback, "Choice:" + name_addr[0]+bt_mac_addr, Toast.LENGTH_SHORT).show();
                        dialog.dismiss();
                        if(!bt_mac_addr.isEmpty()){
                        BluetoothDevice device = mBluetoothAdapter.getRemoteDevice(bt_mac_addr);
                		ConnectThread connectBtThread = new ConnectThread(device);
                		connectBtThread.start();
                        }
                    }
                }).create();
        dialog.show();
        
		//10:C6:1F:57:2F:F1
		//GUID:24c71ae4-27e4-4194-b6b1-1fb27f962887
		//BluetoothDevice device = mBluetoothAdapter.getRemoteDevice("10:C6:1F:57:2F:F1");
        
        //BluetoothDevice device = mBluetoothAdapter.getRemoteDevice(bt_mac_addr);
		//ConnectThread connectBtThread = new ConnectThread(device);
		//connectBtThread.start();
		
	}
	
	private class RecvThread extends Thread {
		private int count;
		public RecvThread(){
			count =0;
		}

		@Override
		public void run() {
			
			InputStream in = null;
			try {
				in = mmSocket.getInputStream();
			} catch (IOException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			
			while(mmSocket.isConnected()){
				byte[] buffer = new byte[1024];					
				int temp=0;
				//Looper.prepare();
				if(count == 0){
					try {
						count = in.available();
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
				
				if( count !=0 ){
					
					byte[] bt = new byte[count];
					int readCount = 0;
					while(readCount < count){
						try {
							readCount += in.read(bt, readCount, count-readCount);
						} catch (IOException e) {
							callback.onBTRecvFailure();
							e.printStackTrace();
						}
					}
					String ssRecv = new String(bt);
					Log.d("ARIC","###"+ssRecv);
					callback.onBTRecvSuccess(ssRecv);
					//Looper.loop();
					count =0;
				}
				try {
					Thread.sleep(200,10);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				
			}
			try {
				in.close();
				mmSocket.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
	}
	
	private class ConnectThread extends Thread{
		
		private  BluetoothDevice mmDevice;
		
		public ConnectThread(BluetoothDevice device)
		{
			//Use a temporary object that is later assigned.
			
			BluetoothSocket tmp = null;
			mmDevice = device;
			
			try {
				tmp = device.createRfcommSocketToServiceRecord(UUID.fromString("00001101-0000-1000-8000-00805F9B34FB")); //24c71ae4-27e4-4194-b6b1-1fb27f962887"));
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			mmSocket = tmp;
		}
		
		public void run() {
			
			try {
				mmSocket.connect();
				callback.onBTConnected();
			} catch (IOException e) {
				callback.onBTConnectFailure();
				e.printStackTrace();
				try {
					mmSocket.close();
				} catch (IOException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
			}
			
			
			new RecvThread().start();
			
			OutputStream os = null;
			
			try {
				 os = mmSocket.getOutputStream();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			
			while(m_bRunning)
			{
				
				if(mmStrCmd !="")
				{
					byte[] data_tx=mmStrCmd.getBytes();
					try {
						os.write(data_tx);
					} catch (IOException e) {
						callback.onBTSendFailure();
						e.printStackTrace();
					}
					callback.onBTSendSuccess();
					//mmTVInfo.setText("Finished CMD:"+mmStrCmd);
					mmStrCmd="";
				}
				
				try {
					Thread.sleep(300,30);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				
			}
			try {
				os.close();
				mmSocket.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
		}
		
	}
	
}
