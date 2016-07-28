package com.oursky.todo_android.app;

import android.app.ListActivity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;

import com.oursky.todo_android.R;
import com.oursky.todo_android.content.DatabaseHelper;
import com.oursky.todo_android.content.model.Task;
import com.oursky.todo_android.widget.ToDoItemAdapter;

import java.util.ArrayList;
import java.util.List;


public class ToDoListActivity extends ListActivity
implements ToDoItemAdapter.ToDoListListener {
    private ToDoItemAdapter adapter;
    private Button addButton, finishButton;
    private Context context;
    private boolean isEditing = false;

    private List<Task> tasks = new ArrayList<Task>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_to_do_list);
        context = this;

        addButton = (Button) findViewById(R.id.partial_to_do_footer_add);
        finishButton = (Button) findViewById(R.id.partial_to_do_footer_finish);

        adapter = new ToDoItemAdapter(this);

        finishButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            Intent intent = new Intent(context, FinishedListActivity.class);
            startActivity(intent);
            }
        });
        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            if (!isEditing) {
                isEditing = true;
                tasks.add(new Task());
                adapter.clear();
                adapter.addAll(tasks);
                getListView().setSelection(getListView().getCount() - 1);
            }
            }
        });
        setListAdapter(adapter);
    }

    private void hideKeyboard() {
        // Check if no view has focus:
        View view = this.getCurrentFocus();
        if (view != null) {
            view.clearFocus();
            InputMethodManager inputManager =
                    (InputMethodManager) this.getSystemService(Context.INPUT_METHOD_SERVICE);
            inputManager.hideSoftInputFromWindow(
                    view.getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onResume() {
        super.onResume();
        tasks = getDatabaseHelper().getAllUnfinishedtasks();
        adapter.clear();
        adapter.addAll(tasks);
    }

    @Override
    public void setTaskFinished(int position) {
        getDatabaseHelper().updateTask(tasks.remove(position));
        adapter.clear();
        adapter.addAll(tasks);
    }

    @Override
    public void setEditTaskFinished(int position, Task task) {
        task.setId((int) getDatabaseHelper().createTask(task));
        tasks.set(position, task);
        isEditing = false;
        hideKeyboard();
    }

    private DatabaseHelper getDatabaseHelper() {
        return ((BaseApplication) getApplication()).getDatabaseHelper();
    }
}
