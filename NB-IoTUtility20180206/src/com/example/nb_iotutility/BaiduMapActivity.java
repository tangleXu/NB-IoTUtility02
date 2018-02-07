package com.example.nb_iotutility;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.View.OnClickListener;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.TextView;


public class BaiduMapActivity extends Activity {
	protected static final int BTMSGRECV = 0;
	private WebView wv;
	private JavaScriptInterface JSInterface;
	public  Handler mHandler;
	public String sLng,sLat;
    private Button btnBack;
	private TextView tv1;
	
    @Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.baidu_map);
		
        wv = (WebView) findViewById(R.id.webView1);
        wv.getSettings().setJavaScriptEnabled(true);
        
        JSInterface = new JavaScriptInterface(this);
        wv.addJavascriptInterface(JSInterface, "JSInterface");
        tv1 = (TextView) findViewById(R.id.textView2);
        wv.loadUrl("file:///android_asset/index.html");
        
        mHandler = new Handler() {  
            public void handleMessage(Message msg) {   
                 switch (msg.what) {   
				case BTMSGRECV:   
                    	  //Toast.makeText(getApplicationContext(), sLng+","+sLat, Toast.LENGTH_SHORT).show();
                    	  tv1.setText(sLng+","+sLat);
                           break;   
                 }   
                 super.handleMessage(msg);   
            }   
       };
       btnBack = (Button) findViewById(R.id.btnBack);
       btnBack.setOnClickListener(new OnClickListener(){

		public void onClick(View arg0) {
			// TODO Auto-generated method stub
			 Intent intent = new Intent();
			 intent.putExtra("LNG", sLng);
			 intent.putExtra("LAT", sLat);
			 setResult(1, intent);
			 finish();
		}});
	}



	public class JavaScriptInterface {
    	Context mContext;
    	JavaScriptInterface(Context c){
    		mContext = c;
    	}
    	
    	@android.webkit.JavascriptInterface
    	public void changeActivity(String str)
    	{
    		//Intent i = new Intent(JavascriptInterfaceActivity.this, next)
    		//Toast.makeText(mContext.getApplicationContext(), "Onclieck!", Toast.LENGTH_SHORT);
    		Log.d("ARIC","Onclick:" + str);
    	}
    	
    	@android.webkit.JavascriptInterface
    	public void gpsReport(String Lng, String Lat)
    	{
    		//Intent i = new Intent(JavascriptInterfaceActivity.this, next)
    		//Toast.makeText(mContext.getApplicationContext(), "Onclick!", Toast.LENGTH_SHORT);
    		Log.d("ARIC","GPS:" + Lng +" " +Lat);
    		sLng = Lng; sLat = Lat;
    		Message msg = new Message();
    		msg.what = BTMSGRECV;
    		mHandler.sendMessage(msg);
    	}
    	
    }
    
}
