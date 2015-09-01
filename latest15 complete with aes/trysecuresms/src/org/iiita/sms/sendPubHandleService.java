package org.iiita.sms;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

import android.app.Service;
import android.content.Intent;
import android.os.Bundle;
import android.os.IBinder;
import android.widget.Toast;

public class sendPubHandleService extends Service {

	private String number;
	private String body;

	public void onCreate() {
		super.onCreate();
		// how to get data from 		
		Toast.makeText(this,"Inside sendPublicHandleService", Toast.LENGTH_SHORT).show();
		
	}
	
	public int onStartCommand(Intent intent, int flags, int startId) {        
        // We want this service to continue running until it is explicitly
        // stopped, so return sticky.
		Bundle extras = intent.getExtras();		
	    number = extras.get("number").toString();
	    body = extras.get("body").toString();
	    
	    String FILENAME = "to_"+number+"_pb";		

		FileOutputStream fos;
		try {
			fos = openFileOutput(FILENAME, getApplicationContext().MODE_PRIVATE);
			fos.write(body.getBytes());
			fos.close();
			Toast.makeText(getApplicationContext(),"written to "+FILENAME, Toast.LENGTH_SHORT).show();
			System.out.println("sendPublicHandleService written to "+FILENAME);
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	    
		callGenSecret();
		
	    return START_STICKY;
	    
	}
	
	private void callGenSecret() {
		// TODO Auto-generated method stub
		Intent i = new Intent(getApplicationContext(),calcSecretKeyService.class);				
		i.putExtra("sender", true);
		startService( i );
	}

	@Override
	public IBinder onBind(Intent arg0) {
		// TODO Auto-generated method stub
		return null;
	}

}
