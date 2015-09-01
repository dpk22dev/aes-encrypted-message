package org.iiita.sms;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.telephony.SmsManager;
import android.telephony.SmsMessage;
import android.widget.Toast;

public class incomingSMS extends BroadcastReceiver {

	private static final String recqueryString = "@pub";
	private static final String sendqueryString = "@#pub";
	private static final String SMS_RECEIVED = "android.provider.Telephony.SMS_RECEIVED";
	private static final String messageString = "@message";
	private static Context context;
	private String msg;	
	private byte[] data;
	private String number;
	private String mbody;
	private String rbody;
	private String sbody;
	
	
	@Override
	public void onReceive(Context _context, Intent _intent) {
		// TODO Auto-generated method stub
		
		if (_intent.getAction().equals(SMS_RECEIVED)) {
			SmsManager sms = SmsManager.getDefault();
			context = _context;
			Bundle bundle = _intent.getExtras();
			if (bundle != null) {
			Object[] pdus = (Object[]) bundle.get("pdus");
			SmsMessage[] messages = new SmsMessage[pdus.length];
			for (int i = 0; i < pdus.length; i++)
			messages[i] = SmsMessage.createFromPdu((byte[]) pdus[i]);
			
			for (SmsMessage message : messages) {
			msg = message.getMessageBody();
			number = message.getOriginatingAddress();			
			
			Toast.makeText(_context, "message received", 
                    Toast.LENGTH_SHORT).show();			
			
			if ( msg.startsWith(recqueryString) ) {
				
//				Toast.makeText(_context, "recquery detected",Toast.LENGTH_SHORT).show();			
				rbody = msg.substring(recqueryString.length());
				System.out.println("incomingSMS broadcast receiver public:" + rbody);
				//call recpubhandleservice
				recPubHandle();
			}
			
			else if ( msg.startsWith(messageString) ) {
				mbody = msg.substring(messageString.length());
				System.out.println("incomingSMS broadcast receiver message body:" + mbody);				
				//call messagehandleservice
				messageHandle();
			}
			
			else if( msg.startsWith(sendqueryString) ){
				sbody = msg.substring(sendqueryString.length());
				//call sendpubhandleservice
				System.out.println("incomingSMS broadcast sender public:" + sbody);
				sendPubHandle();
			}
			
			}
			
			}
		}
		
	}

	private void messageHandle() {
		// TODO Auto-generated method stub
		
		Intent i = new Intent(context,messageHandleService.class);		
		i.putExtra("number", number);
		i.putExtra("body", mbody);
		System.out.println("encrypted message:" + mbody);
//		Toast.makeText(context, "calling sendPubHandleService",Toast.LENGTH_SHORT).show();			
		context.startService( i );
		
	}

	private void sendPubHandle() {
		// TODO Auto-generated method stub
		
		Intent i = new Intent(context,sendPubHandleService.class);		
		i.putExtra("number", number);
		i.putExtra("body", sbody);
		System.out.println("incomingSMS public sender:"+sbody);
//		Toast.makeText(context, "calling sendPubHandleService",Toast.LENGTH_SHORT).show();			
		context.startService( i );
		
	}

	private void recPubHandle() {
		// TODO Auto-generated method stub
		Intent i = new Intent(context,recPubHandleService.class);		
		i.putExtra("number", number);
		i.putExtra("body", rbody);
		System.out.println("incomingSMS public receiver:"+rbody);
//		Toast.makeText(context, "calling recPubHandleService",Toast.LENGTH_SHORT).show();			
		context.startService( i );
	}
	
}
