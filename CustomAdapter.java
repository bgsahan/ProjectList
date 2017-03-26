package com.example.lenovo.projectlist;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by Lenovo on 11.2.2017.
 */

public class CustomTodoAdapter extends ArrayAdapter<TodoProjekt> {

    ArrayList<TodoProjekt> dataSet;
    Context mContext;

    public CustomTodoAdapter(Context context, ArrayList<TodoProjekt> data){
        super(context, 0, data);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent){

        final TodoProjekt todoProjekt = getItem(position);

        if(convertView == null) {
            LayoutInflater inflater = LayoutInflater.from(getContext());
            convertView = inflater.inflate(R.layout.row_project_todo_layout, parent, false);
        }

        TextView name = (TextView) convertView.findViewById(R.id.textView1);
        CheckBox cb = (CheckBox) convertView.findViewById(R.id.checkBox1);

        name.setText(todoProjekt.getTodo());

        if(todoProjekt.getChecked() == 1){
            cb.setChecked(true);
        }else{
            cb.setChecked(false);
        }

        cb.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(todoProjekt.getChecked() == 1){
                    todoProjekt.setChecked(0);
                    return;
                }

                if(todoProjekt.getChecked() == 0){
                    todoProjekt.setChecked(1);
                    return;
                }
            }
        });

        return convertView;
    }
}
