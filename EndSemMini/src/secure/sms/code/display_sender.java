package secure.sms.code;

import java.util.List;

import android.app.ListActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

public class display_sender extends ListActivity {
	
	public static String sender;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
	  
		System.out.println("Entered display senders ");
		
		super.onCreate(savedInstanceState);
	  	
		List<String> list_senders = send_sms.DB.selectAll_sender();
					
		if( list_senders.size() != 0 ){
		String[] senders = new String[list_senders.size()];
		
		for ( int i = 0 ; i < list_senders.size() ; i++){
			senders[i] = list_senders.get(i);
		}
		this.setListAdapter(new ArrayAdapter<String>(this,
				android.R.layout.simple_list_item_1, senders));

		}
		System.out.println("Got the list of senders");
				
				
		System.out.println("Displayed List");
	}

	/**
	 * 	Click Event of a item in list
	 */
	@Override
	protected void onListItemClick(ListView l, View v, int position, long id) {
		super.onListItemClick(l, v, position, id);
		
		// Get the item that was clicked
		Object o = this.getListAdapter().getItem(position);
		String keyword = o.toString();
		
		System.out.println("Sender Selected " + keyword);
			
		this.sender = keyword;
		
		Toast.makeText(this, "You selected: " + keyword, Toast.LENGTH_LONG)
				.show();		
		
    		Intent i = new Intent();
    		i.setClassName("secure.sms.code", "secure.sms.code.sender_message");
    		startActivity(i);    	    	
		
	}

}
