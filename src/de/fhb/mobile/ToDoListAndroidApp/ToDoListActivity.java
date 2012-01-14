package de.fhb.mobile.ToDoListAndroidApp;

import de.fhb.mobile.ToDoListAndroidApp.R;
import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

public class ToDoListActivity extends Activity {
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.todolist);
               
        // Zugriff auf die Listenansicht
        ListView listview = (ListView)findViewById(R.id.listView);
        
        // lies die Optionen aus
        final String[] options = getResources().getStringArray(R.array.todo);
        
        /**
         * erzeuge einen Adapter, der dem listview Zugriff auf die Inhalte ermöglicht
         * Diesem wird außerdem die Ansicht übermittelt, die für ein einzelnes Listenelement verwendet werden soll
         */
//        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,R.layout.listelement,R.id.textView1,options);
        ArrayAdapter<String> adapter = new TodoAdapter(this,R.layout.listelement,options);
//        ArrayAdapter<String> adapter = new TodoAdapter(this,R.layout.todos,options);
        
        // setze den Adapter auf die Listenansicht
        listview.setAdapter(adapter);
        
        // ab hier neu
    }
    
    private class TodoAdapter extends ArrayAdapter<String> {
    	public TodoAdapter(Context context, int textViewResourceId, String[] todos) {
    		super(context, textViewResourceId, todos);
    	}
    	
    	@Override
		public View getView(final int position, View listItemView, ViewGroup parent) {
    		LayoutInflater vi = (LayoutInflater) ToDoListActivity.this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
//    		View v = vi.inflate(R.layout.todos, null);
    		View v = vi.inflate(R.layout.listelement, null);
//    		TextView txt = (TextView)v.findViewById(R.id.todoTitle);
    		TextView txt = (TextView)v.findViewById(R.id.textView1);
    		txt.setText("Todo " + position);
    		txt.setOnClickListener(new OnClickListener() {
    			@Override
    			public void onClick(View v) {
    				Toast.makeText(ToDoListActivity.this, "Todo " + position, Toast.LENGTH_LONG).show();
    			}
    		});
    		return v;
    	}
    }
        
/*
        // setze einen Listener, der auf die Auswahl eines Elements reagiert
        listview.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				Log.i(ToDoListActivity.class.getName(), "item at position " + arg2 + " has been selected: " + options[arg2]);
			}
			
		});
    
        
    }
*/
}