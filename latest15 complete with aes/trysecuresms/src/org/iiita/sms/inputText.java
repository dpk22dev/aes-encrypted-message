package org.iiita.sms;

import java.util.StringTokenizer;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class inputText extends Activity {

    private String numbers;
    private String body;
   
    @Override
    protected void onCreate(Bundle savedInstanceState) { 
    	super.onCreate(savedInstanceState);
        setContentView(R.layout.smsedit);
        
    	Button saveButton = (Button) findViewById(R.id.save_message);        
        saveButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
            	//check for numbers and body
            	body = ((EditText)findViewById(R.id.body)).getText().toString();
            	numbers = ((EditText)findViewById(R.id.phone_numbers)).getText().toString();
            	if( body.length() == 0 || numbers.length() == 0){
            		Toast.makeText(getApplicationContext(),"Please enter numbers and text", 
                            Toast.LENGTH_SHORT).show();
            	}else{            	
            	verifyAndSave();
            	}
            	//save in database
            	
            }
        });
        
        Button sendButton = (Button) findViewById(R.id.send_message);        
        sendButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
            	//check for numbers and body
            	body = ((EditText)findViewById(R.id.body)).getText().toString();
            	numbers = ((EditText)findViewById(R.id.phone_numbers)).getText().toString();
            	if( body.length() == 0 || numbers.length() == 0){
            		Toast.makeText(getApplicationContext(),"Please enter something in SMS body", 
                            Toast.LENGTH_SHORT).show();
            	}else{           
            		verifyAndsend();
            	}
            	//send
            }
        });
        
        Button cancelButton = (Button) findViewById(R.id.cancel_message);        
        cancelButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
            	finish();
            }
        });
    			
    }

	protected void verifyAndsend() {
		// TODO Auto-generated method stub
		   StringTokenizer st = new StringTokenizer(numbers, " ");
		   while(st.hasMoreTokens()) {
		   String key = st.nextToken();
		   if( isInteger(key) ){
			   sendSMS( key, body );
			   Toast.makeText(getApplicationContext(), "!! Here !!"
				          , Toast.LENGTH_SHORT).show();
		   }else{
			   Toast.makeText(getApplicationContext(), "Unable to send sms to " + key
				          , Toast.LENGTH_SHORT).show();
		   }
		   
		   }
	}

	private void sendSMS(String number, String body) {
		// TODO Auto-generated method stub
//*********send SMS by diffie hellman**********
		Intent intent = new Intent(this, centralService.class);         
		intent.putExtra("numbers",number);
		intent.putExtra("text", body);
		startService(intent);
	}

	protected void verifyAndSave() {
		// TODO Auto-generated method stub
		StringTokenizer st = new StringTokenizer(numbers, " ");
		   while(st.hasMoreTokens()) {
		   String key = st.nextToken();
		   if( isInteger(key) ){
			   saveSMS( key, body );
		   }else{
			   Toast.makeText(getApplicationContext(), "Unable to send sms to " + key
				          , Toast.LENGTH_SHORT).show();
		   }
		   
		   }
	}
	
	private void saveSMS(String key, String body2) {
		// TODO Auto-generated method stub
//************* save sms in database *******		
	}

	private boolean isInteger(String key) {
		// TODO Auto-generated method stub
		try  
	    {  
	       Long.parseLong( key );  
	       return true;  
	    }  
	    catch( Exception e)  
	    {  
	       return false;  
	    }  
	}
    
}