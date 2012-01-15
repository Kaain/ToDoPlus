package de.fhb.mobile.ToDoListAndroidApp;

import java.util.ArrayList;
import java.util.List;

import de.fhb.mobile.ToDoListAndroidApp.R;
import de.fhb.mobile.ToDoListAndroidApp.models.Todo;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

public class ToDoListActivity extends Activity {
	
	private List<Todo> todos;
	
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.todolist);

        // Zugriff auf die Listenansicht
        ListView listview = (ListView)findViewById(R.id.listView);
        
        // lies die Optionen aus
        initTodos();
        //final String[] options = getResources().getStringArray(R.array.todo);
        
        /**
         * erzeuge einen Adapter, der dem listview Zugriff auf die Inhalte ermöglicht
         * Diesem wird außerdem die Ansicht übermittelt, die für ein einzelnes Listenelement verwendet werden soll
         */
        ArrayAdapter<Todo> adapter = new TodoAdapter(this,R.layout.listelement,todos);
        
        // setze den Adapter auf die Listenansicht
        listview.setAdapter(adapter);
    }
    
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu, menu);
        return true;
    }
    
    @Override
	public boolean onPrepareOptionsMenu(Menu menu) {
		MenuItem allTodosItem = menu.findItem(R.id.all_todos);
		allTodosItem.setEnabled(false);
		return true;
	}
    
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle item selection
        switch (item.getItemId()) {
        case R.id.all_todos:
        	//TODO
            return true;
        case R.id.contacts:
        	//TODO
            return true;
        default:
            return super.onOptionsItemSelected(item);
        }
    }
    
    private class TodoAdapter extends ArrayAdapter<Todo> {
    	public TodoAdapter(Context context, int textViewResourceId, List<Todo> todos) {
    		super(context, textViewResourceId, todos);
    	}
    	
    	@Override
		public View getView(final int position, View listItemView, ViewGroup parent) {
    		LayoutInflater vi = (LayoutInflater) ToDoListActivity.this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    		View v = vi.inflate(R.layout.listelement, null);
    		TextView txt = (TextView)v.findViewById(R.id.todoNameText);
    		txt.setText("Todo " + position);
    		txt.setOnClickListener(new OnClickListener() {
    			@Override
    			public void onClick(View v) {
    				//Toast.makeText(ToDoListActivity.this, "Todo " + position, Toast.LENGTH_LONG).show();
    				startActivity(new Intent(ToDoListActivity.this, ToDoDetailsActivity.class));
    			}
    		});
    		return v;
    	}
    }
    
    private void initTodos(){
    	todos = new ArrayList<Todo>();
    	for(int i=0;i<11;i++){
    		Todo newTodo = new Todo(i);
    		newTodo.setName("Name "+ i);
    		todos.add(newTodo);
    	}
    }
}