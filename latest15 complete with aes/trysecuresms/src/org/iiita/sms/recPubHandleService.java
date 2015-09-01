package org.iiita.sms;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;

import android.app.Service;
import android.content.Intent;
import android.os.Bundle;
import android.os.IBinder;
import android.widget.Toast;

public class recPubHandleService extends Service {

	private String status;
	private String number;
	private String type;
	private String body;

	public void onCreate() {
		super.onCreate();
		// how to get data from 		
		Toast.makeText(this,"Inside recPubHandleService", Toast.LENGTH_SHORT).show();
		
	}
	
	public int onStartCommand(Intent intent, int flags, int startId) {        
        // We want this service to continue running until it is explicitly
        // stopped, so return sticky.
		Bundle extras = intent.getExtras();			
	    number = extras.get("number").toString();
	    body = extras.get("body").toString();
	    
	    System.out.println("public after pass:"+body);
	    
	    String FILENAME = "fr_"+number+"_pb";		

		FileOutputStream fos;
		try {
			fos = openFileOutput(FILENAME, getApplicationContext().MODE_PRIVATE);
			fos.write(body.getBytes());
			fos.close();
			Toast.makeText(getApplicationContext(),"written to "+FILENAME, Toast.LENGTH_SHORT).show();
			System.out.println("written to "+FILENAME);
			
			FileInputStream fin = openFileInput( FILENAME ); 		
 			DataInputStream in = new DataInputStream(fin);
 			BufferedReader br = new BufferedReader(new InputStreamReader(in));	 				 			
 			
 			String strLine;
 			String read_key = "";
			while ((strLine = br.readLine()) != null)   {
 				  // Print the content on the console
 				 read_key += strLine;
 		    }
			
			Toast.makeText(getApplicationContext(),"i read: " + read_key, Toast.LENGTH_SHORT).show();
			fin.close();
			
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	    
		call_generate_keypair();
		
		return START_STICKY;
	}
	
	private void call_generate_keypair( ) {
		// TODO Auto-generated method stub
		Intent i = new Intent(getApplicationContext(),generateKeyService.class);
		i.putExtra("status","fr" );
		i.putExtra("number", number);
		i.putExtra("type", "pr");
		startService( i );
	}
	
	@Override
	public IBinder onBind(Intent arg0) {
		// TODO Auto-generated method stub
		return null;
	}

}
