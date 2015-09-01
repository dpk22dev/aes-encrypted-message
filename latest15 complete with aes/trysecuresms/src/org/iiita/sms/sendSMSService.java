package org.iiita.sms;

import android.app.Activity;
import android.app.PendingIntent;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.IBinder;
import android.telephony.SmsManager;
import android.widget.Toast;

public class sendSMSService extends Service {

	private String number;	
	private String body;

	public void onCreate() {
		super.onCreate();
		// how to get data from 		
		Toast.makeText(this,"Inside sendSMSService", Toast.LENGTH_SHORT).show();
		
	}
	
	public int onStartCommand(Intent intent, int flags, int startId) {        
        // We want this service to continue running until it is explicitly
        // stopped, so return sticky.
		Bundle extras = intent.getExtras();			    	    
	    number = extras.get("number").toString();	    
	    body = extras.get("body").toString();
	    
	    //body = extras.get("body").toString();
	    Toast.makeText(this,"public: " + body, Toast.LENGTH_LONG).show();
	    
	    System.out.println("sendSMSService public: "+ body);	    	   
	    		
	    send();
	    return START_STICKY;
	}
	
	private void send() {
		// TODO Auto-generated method stub
		 String SENT = "SMS_SENT";
	     String DELIVERED = "SMS_DELIVERED";
	 
	        PendingIntent sentPI = PendingIntent.getBroadcast(this, 0,
	            new Intent(SENT), 0);
	 
	        PendingIntent deliveredPI = PendingIntent.getBroadcast(this, 0,
	            new Intent(DELIVERED), 0);
	 
	        //---when the SMS has been sent---
	        registerReceiver(new BroadcastReceiver(){
	            @Override
	            public void onReceive(Context arg0, Intent arg1) {
	                switch (getResultCode())
	                {
	                    case Activity.RESULT_OK:	                    		                    	
	                        Toast.makeText(getBaseContext(), "SMS sent", 
	                                Toast.LENGTH_SHORT).show();	                        
	                        break;
	                    case SmsManager.RESULT_ERROR_GENERIC_FAILURE:	                    	
	                        Toast.makeText(getBaseContext(), "Generic failure", 
	                                Toast.LENGTH_SHORT).show();
	                        handle_not_sent( false ); 
	                        break;
	                    case SmsManager.RESULT_ERROR_NO_SERVICE:	                    	
	                        Toast.makeText(getBaseContext(), "No service", 
	                                Toast.LENGTH_SHORT).show();
	                        handle_not_sent( false ); 
	                        break;
	                    case SmsManager.RESULT_ERROR_NULL_PDU:	                    	
	                        Toast.makeText(getBaseContext(), "Null PDU", 
	                                Toast.LENGTH_SHORT).show();
	                        handle_not_sent( false ); 
	                        break;
	                    case SmsManager.RESULT_ERROR_RADIO_OFF:	                    	
	                        Toast.makeText(getBaseContext(), "Radio off", 
	                                Toast.LENGTH_SHORT).show();
	                        handle_not_sent( false ); 
	                        break;
	                }
	            }
	        }, new IntentFilter(SENT));
	 
	        //---when the SMS has been delivered---
	        registerReceiver(new BroadcastReceiver(){
	            @Override
	            public void onReceive(Context arg0, Intent arg1) {
	                switch (getResultCode())
	                {
	                    case Activity.RESULT_OK:	                    	
	                        Toast.makeText(getBaseContext(), "SMS delivered", 
	                                Toast.LENGTH_SHORT).show();	                        
	                        break;
	                    case Activity.RESULT_CANCELED:
	                        Toast.makeText(getBaseContext(), "SMS not delivered", 
	                                Toast.LENGTH_SHORT).show();	                      
	                        break;                        
	                }
	            }
	        }, new IntentFilter(DELIVERED));        
	 
	        SmsManager sms = SmsManager.getDefault();
	        	        
	        sms.sendTextMessage(number, null, body, sentPI, deliveredPI);
//	        else
//	        	sms.sendDataMessage(number , null, (short) 3492, body_bytes, sentPI, deliveredPI);

			
			//take chance
	}
	
	
	
	protected void handle_not_sent(boolean c) {
		// TODO Auto-generated method stub
		Toast.makeText(getBaseContext(), "SMS not delivered DO SOMETHING", 
                Toast.LENGTH_SHORT).show();
	}

	@Override
	public IBinder onBind(Intent arg0) {
		// TODO Auto-generated method stub
		return null;
	}

}
