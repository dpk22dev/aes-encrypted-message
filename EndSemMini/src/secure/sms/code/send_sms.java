package secure.sms.code;


import java.math.BigInteger;
import java.util.Vector;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;

import android.app.Activity;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.telephony.SmsManager;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class send_sms extends Activity {
	
	Button Send_SMS;
    EditText Phone_No;
    EditText SMS_Message;
    Button Key_Exchange;
    //Button See_Saved;
    BigInteger key;
    public static key_hand_shake make_con;
    
    public static Vector < key_record > list;
    
    public static database DB;
        
    @Override
    public void onCreate(Bundle savedInstanceState) 
    {
    	System.out.println("Start..");    	  
    	
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);        
        
        Send_SMS = (Button) findViewById(R.id.Send_SMS);
        Key_Exchange = (Button) findViewById(R.id.Key_Exchange);
        Phone_No = (EditText) findViewById(R.id.Phone_No);
        SMS_Message = (EditText) findViewById(R.id.SMS_Message);
        //See_Saved = (Button) findViewById(R.id.See_Saved);
        make_con = new key_hand_shake();
        list = new Vector<key_record>();
        DB = new database(this);
 
        System.out.println("must be print");
                
        Send_SMS.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) 
            {
            	System.out.println("Send Clicked");
            	
                String phoneNo = Phone_No.getText().toString();
                String message = SMS_Message.getText().toString();                 
                
                
                if (phoneNo.length()>0 && message.length()>0){
                	boolean test = false;
                	System.out.println(phoneNo + " " + message + " "  + list.size());
                	
                	for ( int i = 0 ; i < list.size() ; i++){
                		System.out.println((list.elementAt(i)).ph_no);
                		if ((list.elementAt(i)).ph_no.endsWith(phoneNo)) {
                			key = list.elementAt(i).key;
                			test = true;
                			break;
                		}
                	}
                	
                	System.out.println("Testing");
                	
                	if ( test == true ){
                		try {
							sendSMS(phoneNo, message , key);
						} catch (Exception e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
                	}else{
                		Toast.makeText(getBaseContext(), 
                                " Please Exchange key first ", 
                                Toast.LENGTH_SHORT).show();
                	}
            	}
                else
                    Toast.makeText(getBaseContext(), 
                        "Please enter both phone number and message.", 
                        Toast.LENGTH_SHORT).show();
            }
        });
        
        /**
         * Exchanging the key 
         */
        Key_Exchange.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) 
            {
            	System.out.println("Exchange Clicked");
            	
                String phoneNo = Phone_No.getText().toString();
                String message = SMS_Message.getText().toString();   
        		
                
                if (phoneNo.length()>0 && message.length()>0){
                	boolean test = false;
                	System.out.println(phoneNo + " " + message + " "  + list.size());
                	
                	for ( int i = 0 ; i < list.size() ; i++){
                		System.out.println((list.elementAt(i)).ph_no);
                		if ((list.elementAt(i)).ph_no.endsWith(phoneNo)) {
                			key = list.elementAt(i).key;
                			test = true;
                			break;
                		}
                	}
                
                	if ( test == false) {
                		System.out.println("key exchange to happen");
        		
                		int r =(int) ( Math.random()  * 10 );
                		
                	//	r = 9;
                		message = ("Key_Exchange " + r + " ");
                		make_con.handshake(r);
        		
                		System.out.println("starting to send ...");
        		
                		SmsManager sms = SmsManager.getDefault();
                		message = message.concat(make_con.Ya.toString());
        		
                		System.out.println("sending the message as .... " + message );
        		
                		sms.sendTextMessage(phoneNo, null, message  , null , null);
        		
                		System.out.println("Sent :: " + message );
                		System.out.println("key exchange done ");
                		
                	}else{
                		Toast.makeText(getBaseContext(), 
                                " Key Present now send message ", 
                                Toast.LENGTH_SHORT).show();
                	}
                }else {
                    Toast.makeText(getBaseContext(), 
                            "Please enter both phone number and message.", 
                            Toast.LENGTH_SHORT).show();
                }
            }
        });
        
        
/*        See_Saved.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) 
            {
            	//
            	 // Call Display senders  Activity
            	 //
            	
            	Intent i = new Intent();
            	i.setClassName("secure.sms.code", "secure.sms.code.display_sender");
            	startActivity(i);            	            	
            	
            }
        });
        
  */      
    }
        
            
    
  //---sends an SMS message to another device---
    private void sendSMS(String phoneNumber, String message , BigInteger value) throws Exception{
    	final String str = message;
    	
    	String SENT = "SMS_SENT";
    	String DELIVERED = "SMS_DELIVERED";
    	
        PendingIntent sentPI = PendingIntent.getBroadcast(this, 0, new Intent(SENT), 0);
        
        PendingIntent deliveredPI = PendingIntent.getBroadcast(this, 0, new Intent(DELIVERED), 0);
    	
        //---when the SMS has been sent---
        registerReceiver(new BroadcastReceiver(){
			@Override
			public void onReceive(Context arg0, Intent arg1) {
				switch (getResultCode())
				{
				    case Activity.RESULT_OK:
					    Toast.makeText(getBaseContext(), "SMS sent" + str ,  Toast.LENGTH_SHORT).show();
					    break;
				    case SmsManager.RESULT_ERROR_GENERIC_FAILURE:
					    Toast.makeText(getBaseContext(), "Generic failure", Toast.LENGTH_SHORT).show();
					    break;
				    case SmsManager.RESULT_ERROR_NO_SERVICE:
					    Toast.makeText(getBaseContext(), "No service", Toast.LENGTH_SHORT).show();
					    break;
				    case SmsManager.RESULT_ERROR_NULL_PDU:
					    Toast.makeText(getBaseContext(), "Null PDU", Toast.LENGTH_SHORT).show();
					    break;
				    case SmsManager.RESULT_ERROR_RADIO_OFF:
					    Toast.makeText(getBaseContext(), "Radio off", Toast.LENGTH_SHORT).show();
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
					    Toast.makeText(getBaseContext(), "SMS delivered",  Toast.LENGTH_SHORT).show();
					    break;
				    case Activity.RESULT_CANCELED:
					    Toast.makeText(getBaseContext(), "SMS not delivered",  Toast.LENGTH_SHORT).show();
					    break;					    
				}
			}
        }, new IntentFilter(DELIVERED));        
    	
        SmsManager sms = SmsManager.getDefault();
    	
        /**
         * 		Encryption of message 
         */
        byte[] raw = value.toString().getBytes();
        int l = raw.length;
        byte[] x = new byte[16];
        int i;
        if( l >= 16 ){
        	for(i = 0; i < 16; i++){
        		x[i] = raw[i];
        	}
        } else {
        	for( i = 0; i < l; i++ ){
        		x[i] = raw[i];
        	}
        	for(;i < 16; i++){
        		x[i] = 0;
        	}
        }
        
        SecretKeySpec key = new SecretKeySpec(x, "AES");
        Cipher cip = Cipher.getInstance("AES");
        cip.init(Cipher.ENCRYPT_MODE, key);

        byte[] encrypted = cip.doFinal(message.getBytes());
        message = android.util.Base64.encodeToString(encrypted, android.util.Base64.DEFAULT);
        
        
        sms.sendTextMessage(phoneNumber, null, message , sentPI, deliveredPI);
               
              
    }
    
}
