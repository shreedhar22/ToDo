package com.sudhishkr.codepath.todo;

import android.content.Intent;
import android.widget.BaseAdapter;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

/**
 * Created by skasabar on 9/20/16.
 */
public class CustomListViewAdapter extends BaseAdapter {

    String [] result;
    Context context;
    DBHandle db;
    private static LayoutInflater inflater=null;

    public CustomListViewAdapter(MainActivity mainActivity, DBHandle dbHandle) {
        db=dbHandle;
        result=db.getAllTasks();
        context=mainActivity;
        inflater = ( LayoutInflater )context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public View getView(final int position, View view, ViewGroup viewGroup) {
        // TODO Auto-generated method stub
        ListHolder listHolder=new ListHolder();
        final View rowView;
        rowView = inflater.inflate(R.layout.task_list_view, null);
        listHolder.listTextViewTaskName = (TextView) rowView.findViewById(R.id.listTextViewTaskName);
        listHolder.listTextViewTaskName.setText(result[position]);
        listHolder.listTextViewTaskStatus = (TextView) rowView.findViewById(R.id.listTextViewTaskStatus);
        listHolder.listTextViewTaskStatus.setText(db.getTaskStatus(result[position]));
        listHolder.listTextViewTaskDueDate = (TextView) rowView.findViewById(R.id.listTextViewTaskDueDate);
        listHolder.listTextViewTaskDueDate.setText(db.getTaskDueDate(result[position]));
        listHolder.listTextViewTaskPriority = (TextView) rowView.findViewById(R.id.listTextViewTaskPriority);
        listHolder.listTextViewTaskPriority.setText(db.getTaskPriority(result[position]));

        rowView.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(context, "view mode for task " + result[position] + "!", Toast.LENGTH_LONG).show();
                Intent viewTaskIntent = new Intent(context, ViewTaskActivity.class);
                viewTaskIntent.putExtra(MainActivity.BUNDLE_TASK_NAME, result[position]);
                ((MainActivity) context).startActivityForResult(viewTaskIntent, MainActivity.REQUEST_CODE_ViewTaskActivity);
            }
        });

        rowView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                Toast.makeText(context, "deleted task " + result[position] + "!", Toast.LENGTH_LONG).show();
                db.deleteTask(result[position]);
                result = db.getAllTasks();
                notifyDataSetChanged();
                return false;
            }
        });
        return rowView;
    }

    @Override
    public int getCount() {
        return result.length;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public Object getItem(int position) {
        return position;
    }

    public class ListHolder
    {
        TextView listTextViewTaskName;
        TextView listTextViewTaskDueDate;
        TextView listTextViewTaskPriority;
        TextView listTextViewTaskStatus;
    }
}
