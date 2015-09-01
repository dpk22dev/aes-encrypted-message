package secure.sms.code;

import java.util.List;
import android.app.ListActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

public class sender_message extends ListActivity{
	public int[] ids;
	public int[] voice_tags;
	public static String snd_msg;
	public static int msg_id;
	public static int res;
	public static String button_id;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
	  
		System.out.println("Entered sender message ");
		
		super.onCreate(savedInstanceState);
	
		res = 0;
		
	  // Create an array of Strings, that will be put to our ListActivity
		
		send_sms.DB.TABLE_NAME = "Table_" + display_sender.sender;
		
		List<message_record> messages = send_sms.DB.selectAll();
		
		String[] msg = new String[messages.size()];
		
		ids = new int[messages.size()];
		voice_tags = new int[messages.size()];
		
		for ( int i = 0 ; i < messages.size() ; i++){
			String value = messages.get(i).key;
			String message = messages.get(i).message;			
			
			try {
				message = secure.sms.code.receive_SMS.getMessage(message, value);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			msg[i] = message;
			ids[i] = messages.get(i).id;
			voice_tags[i] = messages.get(i).voice_tag;
		}
		
		System.out.println("Got the list of messages");
		
		// Create an ArrayAdapter, that will actually make the Strings above
		// appear in the ListView
		this.setListAdapter(new ArrayAdapter<String>(this,
				android.R.layout.simple_list_item_1, msg ));
		
		System.out.println("Displayed List");
	}

	/**
	 * Showing the selected message and displaying it on full
	 */
	@Override
	protected void onListItemClick(ListView l, View v, int position, long id) {
		super.onListItemClick(l, v, position, id);
		
		// Get the item that was clicked
		Object o = this.getListAdapter().getItem(position);
		String keyword = o.toString();
		
		System.out.println("Message " + keyword + " Position " + position);
		
		this.snd_msg = keyword;
		this.msg_id = ids[position];
		if(voice_tags[position] == 0)
		{
			this.button_id = "Add Voice Tag";
		}
		else
		{
			this.button_id = "Listen Voice Tag";
		}
		
		//Toast.makeText(this, "You selected: " + keyword, Toast.LENGTH_LONG).show();
				
		
    		Intent i = new Intent();
    		i.setClassName("secure.sms.code", "secure.sms.code.show_message");
    		startActivity(i);
    	
    		this.finish();
    	
		
	}
	
	

}
