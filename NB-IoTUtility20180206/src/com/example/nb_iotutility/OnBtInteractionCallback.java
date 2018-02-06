package com.example.nb_iotutility;

public interface OnBtInteractionCallback {
	//void onSuccessSend(String info);
	void onTimeout();
	
	void onBTEnabledAck();
	void onBTConnected();
	void onBTConnectFailure();
	void onBTSendSuccess();
	void onBTSendFailure();
	
	void onBTRecvSuccess(String recv);
	void onBTRecvFailure();
}
