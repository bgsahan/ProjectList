package com.example.lenovo.projectlist;

import android.app.Fragment;
import android.app.FragmentTransaction;
import android.app.ListFragment;
import android.content.Context;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Toast;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements
        MainProjectList.button1FragmentChanger,
        MainProjectList.button2FragmentChanger,
        ProjectDetails.ProjectTodoFragmentChanger,
        MainProjectLister.ProjectListFragmentChanger,
        ProjectDetails.ProjectMainListFragmentChanger,
        ProjectTodo.ProjectMainListFragmentChanger,
        ProjectTodo.ProjectDetailsFragmentChanger
{


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

//Adding Action Bar Logo//////////////////////////////////////////////////////////
        //getSupportActionBar().setDisplayShowHomeEnabled(true);
       // getSupportActionBar().setIcon(R.drawable.action_bar_logo);

        if(findViewById(R.id.fragmentContainer) != null){
            if(savedInstanceState != null){
                return;
            }

            MainProjectLister firstFragment = new MainProjectLister();

            getFragmentManager().beginTransaction().add(R.id.fragmentContainer, firstFragment).commit();

        }

    }

    @Override
    public void onBackPressed() {
        int count = getFragmentManager().getBackStackEntryCount();
        if (count == 0) {
            super.onBackPressed();
            //additional code
        } else {
            getFragmentManager().popBackStack();
        }
    }


    @Override
    public void button1ChangeFragment(Fragment fragment) {

        ProjectDetails secondFragment = new ProjectDetails();

        FragmentTransaction transaction = getFragmentManager().beginTransaction();
        transaction.replace(R.id.fragmentContainer, secondFragment);
        //transaction.addToBackStack(null);
        transaction.commit();

    }

    @Override
    public void button2ChangeFragment(Fragment fragment) {

        ProjectTodo thirdFragment = new ProjectTodo();

        FragmentTransaction transaction = getFragmentManager().beginTransaction();
        transaction.replace(R.id.fragmentContainer, thirdFragment);
        //transaction.addToBackStack(null);
        transaction.commit();

    }


    @Override
    public void projectListChangeFragment (Fragment fragment){

        ProjectDetails eighthFragment = new ProjectDetails();

        FragmentTransaction transaction = getFragmentManager().beginTransaction();
        transaction.replace(R.id.fragmentContainer, eighthFragment);
        //transaction.addToBackStack(null);
        transaction.commit();
    }

    @Override
    public void projectTodoChangeFragment (Fragment fragment){

        ProjectTodo ninethFragment = new ProjectTodo();

        FragmentTransaction transaction = getFragmentManager().beginTransaction();
        transaction.replace(R.id.fragmentContainer, ninethFragment);
        //transaction.addToBackStack(null);
        transaction.commit();
    }


    @Override
    public void projectMainListChangeFragment (Fragment fragment){

        MainProjectLister tenthFragment = new MainProjectLister();

        FragmentTransaction transaction = getFragmentManager().beginTransaction();
        transaction.replace(R.id.fragmentContainer, tenthFragment);
       //transaction.addToBackStack(null);
        transaction.commit();
    }


}
