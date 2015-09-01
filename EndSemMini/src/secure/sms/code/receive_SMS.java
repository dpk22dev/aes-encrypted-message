package secure.sms.code;


import java.math.BigInteger;
import java.util.List;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.telephony.SmsManager;
import android.telephony.SmsMessage;
import android.widget.Toast;
 
public class receive_SMS extends BroadcastReceiver
{	
	/**
	 * Decrypting the message 
	 * @param str : message to be decrypted
	 * @param value : key for decryption
	 * @return : decrypted plaintext message.
	 * @throws Exception
	 */
	public static String getMessage(String str, String value) throws Exception
	{
		int i;
        
        //String value = new String("9");
        byte[] raw = value.getBytes();
        int l = raw.length;
        System.out.println("Length :: " + l);
        byte[] x = new byte[16];
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
        Cipher cip = null;
		cip = Cipher.getInstance("AES");
        
        cip.init(Cipher.DECRYPT_MODE, key);
		byte[] enc = android.util.Base64.decode(str, android.util.Base64.DEFAULT);
		
		byte[] ori = cip.doFinal(enc);
		String mess = new String(ori);
		
		return mess;
		
	}
	
	/**
	 * for recieving the message
	 */
    @Override
    public void onReceive(Context context, Intent intent) 
    {	
        //---get the SMS message passed in---
        Bundle bundle = intent.getExtras();        
        SmsMessage[] msgs = null;
        String str = "";            
        if (bundle != null)
        {
            //---retrieve the SMS message received---
            Object[] pdus = (Object[]) bundle.get("pdus");
            msgs = new SmsMessage[pdus.length];  
            
            for (int i=0; i<msgs.length; i++){
            	msgs[i] = SmsMessage.createFromPdu((byte[])pdus[i]);                
            	str = msgs[i].getMessageBody().toString();
            	
            	System.out.println("something received  " + str.substring(0, 12) + "@#@#@#@#@#");
            	
            	if (str.substring(0, 13).equals("Key_Exchange "))
            	{
            		String sender = msgs[i].getOriginatingAddress();
            		
            		for (int in = 0 ; in < send_sms.list.size() ; in++){
            			if ( sender.endsWith(send_sms.list.elementAt(in).ph_no)){
            				send_sms.list.removeElementAt(in);
            				System.out.println("key deleted");
             			   	break;
             		   }
             	   }
            		
            		System.out.println("something received _ 32423 4 " );
            		
            		String sg = "";
            		
            		int index = Character.digit(str.charAt(13), 10);
            		
            		System.out.println("index" + index);
            	   
            		String s = str.substring(15); 
            		
            		send_sms.make_con.handshake(index);
            		sg = ("Get_Yb " + send_sms.make_con.Ya.toString());
            	   
            		System.out.println("Sending to sender .. " + sg);
            		Toast.makeText(context,"get_yb:" + sg , Toast.LENGTH_SHORT).show();
            	   
            		SmsManager sms = SmsManager.getDefault();
            		Toast.makeText(context,"number: "+msgs[i].getOriginatingAddress() , Toast.LENGTH_SHORT).show();
            		sms.sendTextMessage(msgs[i].getOriginatingAddress() , null, sg , null , null);

            		System.out.println("sent :: to :: sender :: " + sg);
            	   
            		send_sms.make_con.Yb = new BigInteger(s);          	   
            		send_sms.make_con.getKey();
            		
            		key_record kr = new key_record();
            		kr.ph_no = msgs[i].getOriginatingAddress();
            		kr.key = send_sms.make_con.key;
            		send_sms.list.add(kr);
            		
            		System.out.println("Generated key at receiver ::" + send_sms.make_con.key);
            		for (int in = 0 ; in < send_sms.list.size() ; in++){
            			System.out.println(in + " " + send_sms.list.elementAt(in).ph_no + " " + send_sms.list.elementAt(in).key);
             	   }
            		Toast.makeText(context, "Key Exchange Done ", Toast.LENGTH_SHORT).show();
            		
            	   
               }else if (str.substring(0,7).equals("Get_Yb ")){
            	   System.out.println("Received at sender ::" + str);
            	   String s = str.substring(7);
            	   
            	   send_sms.make_con.Yb = new BigInteger(s);
            	   send_sms.make_con.getKey();
            	   
            	   key_record kr = new key_record();
            	   kr.ph_no = msgs[i].getOriginatingAddress();
            	   kr.key = send_sms.make_con.key;
            	   send_sms.list.add(kr);
            	   
            	   System.out.println(send_sms.list.size() + " ---- " + send_sms.list.elementAt(0).key);
            	   
            	   System.out.println("Generated key at sender :: " + send_sms.make_con.key);

            	   for (int in = 0 ; in < send_sms.list.size() ; in++){
           			System.out.println(in + " " + send_sms.list.elementAt(in).ph_no + " " + send_sms.list.elementAt(in).key);
            	   }
            	   Toast.makeText(context, "Key Exchange Done " , Toast.LENGTH_SHORT).show();
               }else if (str.equals("KEY_NOT_FOUND_EXCEPTION_DELETE_KEY")){
            	   
            	   String sender = msgs[i].getOriginatingAddress();
            	   
            	   for (int in = 0 ; in < send_sms.list.size() ; in++){
            		   if ( sender.endsWith(send_sms.list.elementAt(in).ph_no) ){
            			   send_sms.list.removeElementAt(in);
            			   break;
            		   }
            	   }
            	   Toast.makeText(context, "Resend the message to " + sender, Toast.LENGTH_SHORT).show();
            	   
               } else{
            	   System.out.println("Normal Message");
            	   String key = null;
            	   String sender = msgs[i].getOriginatingAddress();
            	   boolean test = false;
            	   for (int in = 0 ; in < send_sms.list.size() ; in++){
            		   if ( sender.endsWith(send_sms.list.elementAt(in).ph_no) ){
            			   test = true;
            			   key = send_sms.list.elementAt(in).key.toString();
            			   break;
            		   }
            	   }
            	   
            	   if ( test == false){
            		   SmsManager sms = SmsManager.getDefault();
               			sms.sendTextMessage(sender , null, "KEY_NOT_FOUND_EXCEPTION_DELETE_KEY" , null , null);
               			System.out.println("Key Not Found ");
            	   }else{
            		   String message = null;
            		   System.out.println("Simple Message Recived");
            		   /**
            		    * 		Decrypt here ...
            		    */
            	   
            		   try {
						message = "SMS from " + msgs[i].getOriginatingAddress() + " :: " + getMessage(str, key) + "\n";
					} catch (Exception e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
            		   //---display the new SMS message---
            		   Toast.makeText(context, message , Toast.LENGTH_SHORT).show();
            		   System.out.println("I am in the receiver & now main message recived " + str);
                   
            		   /**
            		    * 		Store SMS
            		    */
            		   
            		   		test = false;
            		   		List<String> sender_list = send_sms.DB.selectAll_sender();
            		   		List<message_record> rc;
            		   		
            		   		for ( int in = 0 ; i < sender_list.size() ; i++){
            		   			if (sender.endsWith(sender_list.get(in))){
            		   				sender = sender_list.get(in);
            		   				test = true;
            		   				break;
            		   			}
            		   		}
            		   		
            		   		if ( test == false){
            		   			send_sms.DB.insert(sender);
            		   			send_sms.DB.create_sender(sender);
            		   			System.out.println("Table Created");
            		   		}
            		   		
            		   		send_sms.DB.TABLE_NAME = "Table_" + sender;
            		   		
            		   		send_sms.DB.insert(msgs[i].getMessageBody(), key);
            		   
            		   /**
            		    * 		SMS Stored 
            		    */
            		   		
            		   		/**
            		   		 *  	Printing Database
            		   		 */
            		   				sender_list = send_sms.DB.selectAll_sender();
            		   				System.out.println("something from database " + sender_list.size());
            		   				for ( int in = 0 ; in < sender_list.size() ;  in++){
            		   					send_sms.DB.TABLE_NAME = "Table_" + sender_list.get(in);
            		   					
            		   					System.out.println( sender_list.get(in) );
            		   					
            		   					rc = send_sms.DB.selectAll();
            		   					
            		   					for ( int j = 0 ; j < rc.size() ; j++){
            		   						System.out.println(rc.get(j).message + " -- " + rc.get(j).key);
            		   					}
            		   					
            		   				}
            		   		/**
            		   		 * 		Database Printed
            		   		 */
            	   }
               }
               
            }
            System.out.println("I am out \" for loop \" of the receiver");
        }
        System.out.println("out of the bundle");
    }
}