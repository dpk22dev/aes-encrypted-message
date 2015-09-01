package secure.sms.code;

import java.io.FileOutputStream;
import java.io.OutputStreamWriter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

public class passwd_change extends Activity {
	
	Button change_passwd;
	TextView New_passwd;
	TextView Conf_passwd;
	EditText e_New_passwd;
	EditText e_Conf_passwd;
	
	@Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);               
        
        LinearLayout layout = new LinearLayout(this);
        
        layout.setOrientation(LinearLayout.VERTICAL); 
        
        New_passwd = new TextView(this);
        New_passwd.setText("New Password: ");
        layout.addView(New_passwd , new LinearLayout.LayoutParams( 
        		ViewGroup.LayoutParams.WRAP_CONTENT,
                ViewGroup.LayoutParams.WRAP_CONTENT,
                0));
        
        
        e_New_passwd = new EditText(this);
        e_New_passwd.setTransformationMethod(new android.text.method.PasswordTransformationMethod()); 
        e_New_passwd.setText("");
        layout.addView(e_New_passwd, new LinearLayout.LayoutParams( 
        		ViewGroup.LayoutParams.FILL_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT,
                0));
        
        Conf_passwd = new TextView(this);
        Conf_passwd.setText("Retype New Password: ");
        
        layout.addView(Conf_passwd , new LinearLayout.LayoutParams( 
        		ViewGroup.LayoutParams.WRAP_CONTENT,
                ViewGroup.LayoutParams.WRAP_CONTENT,
                0));
        
        e_Conf_passwd = new EditText(this);
        e_Conf_passwd.setText("");
        e_Conf_passwd.setTransformationMethod(new android.text.method.PasswordTransformationMethod());
        layout.addView(e_Conf_passwd , new LinearLayout.LayoutParams( 
        		ViewGroup.LayoutParams.FILL_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT,
                0));
        
        change_passwd = new Button(this);
        change_passwd.setText("Change Password");
                
        change_passwd.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
            	System.out.println(e_New_passwd.getText().toString() + " " + e_Conf_passwd.getText().toString());
            	if(e_New_passwd.getText().toString().equals(e_Conf_passwd.getText().toString()))
            	{
            		System.out.println("Passwd Matched");
                	//Toast.makeText(getBaseContext(), "Password Matched" , Toast.LENGTH_SHORT).show();
                	FileOutputStream fOut = null;

         		   OutputStreamWriter osw = null;

         		   try{
         			   System.out.println("File making");
         		   fOut = openFileOutput("public.dat", Context.MODE_PRIVATE);

         		   osw = new OutputStreamWriter(fOut);
         		   System.out.println("File making done");
         		   osw.write(e_New_passwd.getText().toString());
         		   main.passwd_length = e_New_passwd.getText().toString().length();
         		   System.out.println("File writing done");
         		   osw.close();

         		   fOut.close();
         		  //e_Old_passwd.setText("");
         		  e_New_passwd.setText("");
         		  e_Conf_passwd.setText("");
         		  
         		 Toast.makeText(getBaseContext(), "Master Password Changed" , Toast.LENGTH_SHORT).show();
         		 
         		   passwd_change.this.finish();
         		   }catch(Exception e){

         		   e.printStackTrace(System.err);

         		   }
            	}
            	else
            	{
            		System.out.println("Passwords don't Matched");
                	Toast.makeText(getBaseContext(), "Password Don't  Match" , Toast.LENGTH_SHORT).show();
                	//e_Old_passwd.setText("");
                	e_New_passwd.setText("");
                	e_Conf_passwd.setText("");	
            	}
            }
        });

        
        layout.addView(change_passwd, new LinearLayout.LayoutParams( 
        		ViewGroup.LayoutParams.FILL_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT,
                0));
        

        setContentView(layout);

        
	}

}
