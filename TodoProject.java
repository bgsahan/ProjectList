package com.example.lenovo.projectlist;

import java.io.Serializable;

/**
 * Created by Lenovo on 11.2.2017.
 */

public class TodoProjekt implements Serializable {

    private String mTodo;
    private int mChecked;

    public TodoProjekt(String todo, int checked){
        this.mTodo = todo;
        this.mChecked = checked;
    }

    public String getTodo(){
        return this.mTodo;
    }

    public void setTodo(String todo){
        this.mTodo = todo;
    }

    public int getChecked(){
        return this.mChecked;
    }

    public void setChecked(int checked){
        this.mChecked = checked;
    }

}
