package com.oursky.todo_android.widget;

import android.content.Context;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.TextView;

import com.oursky.todo_android.R;
import com.oursky.todo_android.content.model.Task;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by yuyauchun on 13/3/15.
 */
public class ToDoItemAdapter extends ArrayAdapter<Task> {
    private LayoutInflater inflater;
    private ToDoListListener listener;
    private boolean isEditing = false;
    private List<Task> tasks = new ArrayList<>();

    public ToDoItemAdapter(Context context) {
        super(context, 0);
        this.inflater = LayoutInflater.from(context);
        try {
            listener = (ToDoListListener) context;
        } catch (ClassCastException e) {
            throw new ClassCastException("Activity must implement ToDoListFinishedListener.");
        }
    }

    public ArrayList<Task> getTasks() {
        return (ArrayList<Task>) tasks;
    }

    public void setTasks(ArrayList<Task> tasks) {
        this.tasks = tasks;
        refresh();
    }

    public void addTask() {
        if (!isEditing) {
            isEditing = true;
            tasks.add(new Task());
            refresh();
            listener.onAddedTask();
        }
    }

    public void onResume() {
        isEditing = false;
    }

    public interface ToDoListListener {
        void setTaskFinished(Task position);

        void setEditTaskFinished(int position, Task task);

        void onAddedTask();
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = inflater.inflate(R.layout.partial_to_do_item, parent, false);
            convertView.setTag(new ToDoItemViewHolder(convertView));
        }
        final ToDoItemViewHolder holder = (ToDoItemViewHolder) convertView.getTag();

        final Task task = getItem(position);
        holder.bindData(position, task);

        return convertView;
    }


    public class ToDoItemViewHolder implements TextView.OnEditorActionListener, View.OnFocusChangeListener, CompoundButton.OnCheckedChangeListener {
        public TextView txtTask;
        public EditText editTask;
        public CheckBox checkBox;
        private Task task;
        private int position;

        public ToDoItemViewHolder(View view) {
            txtTask = (TextView) view.findViewById(R.id.partial_to_do_task);
            checkBox = (CheckBox) view.findViewById(R.id.partial_to_do_checkbox);
            editTask = (EditText) view.findViewById(R.id.partial_to_do_edit_task);
            editTask.setOnFocusChangeListener(this);
            editTask.setOnEditorActionListener(this);
            checkBox.setOnCheckedChangeListener(this);
        }

        public void bindData(final int position, final Task task) {
            this.task = task;
            this.position = position;
            initView();
        }

        private void initView() {
            checkBox.setChecked(task.isFinished());
            if (task.getName() != null && task.getName().length() != 0) {
                txtTask.setText(task.getName());
                editTask.setVisibility(View.GONE);
                txtTask.setVisibility(View.VISIBLE);
            } else {
                editTask.setText("");
                editTask.setVisibility(View.VISIBLE);
                txtTask.setVisibility(View.GONE);
            }
        }

        @Override
        public boolean onEditorAction(TextView v, int actionId, KeyEvent keyEvent) {
            if (actionId == EditorInfo.IME_ACTION_DONE) {
                String name = ((EditText) v).getText().toString();
                task.setName(name);
                txtTask.setText(name);
                v.setVisibility(View.GONE);
                txtTask.setVisibility(View.VISIBLE);
                tasks.set(position, task);
                isEditing = false;
                listener.setEditTaskFinished(position, task);
            }
            return false;
        }

        @Override
        public void onFocusChange(View v, boolean hasFocus) {
            if (!hasFocus) {
                if (v != null && v.getParent() != null && !TextUtils.isEmpty(((EditText) v).getText())) {
                    String name = ((EditText) v).getText().toString();
                    task.setName(name);
                    txtTask.setText(name);
                    v.setVisibility(View.GONE);
                    txtTask.setVisibility(View.VISIBLE);
                    listener.setEditTaskFinished(position, task);
                }
            }
        }

        @Override
        public void onCheckedChanged(CompoundButton compoundButton, boolean isChecked) {
            task.setIsFinished(isChecked);
            if (isChecked) {
                listener.setTaskFinished(tasks.remove(position));
                refresh();
            }
        }
    }

    private void refresh() {
        clear();
        addAll(tasks);
    }
}
