package com.oursky.todo_android.app;

import android.app.ListActivity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;

import com.oursky.todo_android.R;
import com.oursky.todo_android.content.DatabaseHelper;
import com.oursky.todo_android.content.model.Task;
import com.oursky.todo_android.widget.ToDoItemAdapter;

import java.util.ArrayList;


public class ToDoListActivity extends ListActivity implements ToDoItemAdapter.ToDoListListener {
    private ToDoItemAdapter adapter;
    private Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_to_do_list);
        context = this;

        Button addButton = (Button) findViewById(R.id.partial_to_do_footer_add);
        Button finishButton = (Button) findViewById(R.id.partial_to_do_footer_finish);

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
                adapter.addTask();
            }
        });

        setListAdapter(adapter);
        ArrayList<Task> tasks = (ArrayList<Task>) getDatabaseHelper().getAllUnfinishedtasks();
        adapter.setTasks(tasks);
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        outState.putParcelableArrayList("tasks", adapter.getTasks());
        super.onSaveInstanceState(outState);
    }

    @Override
    protected void onRestoreInstanceState(Bundle state) {
        ArrayList<Task> tasks = state.getParcelableArrayList("task");
        adapter.setTasks(tasks);
        super.onRestoreInstanceState(state);
    }

    private void hideKeyboard() {
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
    public void onResume() {
        super.onResume();
        adapter.onResume();
    }

    @Override
    public void setTaskFinished(Task task) {
        getDatabaseHelper().updateTask(task);
    }

    @Override
    public void setEditTaskFinished(int position, Task task) {
        task.setId((int) getDatabaseHelper().createTask(task));
        hideKeyboard();
    }

    @Override
    public void onAddedTask() {
        getListView().setSelection(getListView().getCount() - 1);
    }

    private DatabaseHelper getDatabaseHelper() {
        return ((BaseApplication) getApplication()).getDatabaseHelper();
    }
}
