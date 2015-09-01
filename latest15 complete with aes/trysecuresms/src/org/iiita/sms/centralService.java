package org.iiita.sms;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

import android.app.Service;
import android.content.Intent;
import android.os.Bundle;
import android.os.IBinder;
import android.widget.Toast;

public class centralService extends Service {

	private String number = "9999";
	private String body = "hi hello";


	public void onCreate() {
		super.onCreate();
		// how to get data from 		
		Toast.makeText(this,"Inside CentralService", Toast.LENGTH_SHORT).show();
		
	}
	
	public int onStartCommand(Intent intent, int flags, int startId) {        
        // We want this service to continue running until it is explicitly
        // stopped, so return sticky.
		Bundle extras = intent.getExtras();		
	    number = extras.get("numbers").toString();
	    body = extras.get("text").toString();
	    
		Toast.makeText(getApplicationContext(), number + body, Toast.LENGTH_SHORT).show();
		
		String FILENAME = "to_"+number+"_m";		

		FileOutputStream fos;
		try {
			fos = openFileOutput(FILENAME, getApplicationContext().MODE_PRIVATE);
			fos.write(body.getBytes());
			fos.close();
			Toast.makeText(getApplicationContext(),"written to "+FILENAME, Toast.LENGTH_SHORT).show();
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		call_generate_keypair( );
		
        return START_STICKY;
    }
	
	
	private void call_generate_keypair( ) {
		// TODO Auto-generated method stub
		Intent i = new Intent(getApplicationContext(),generateKeyService.class);
		i.putExtra("status","to" );
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
