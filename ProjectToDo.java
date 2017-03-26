package com.example.lenovo.projectlist;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Fragment;
import android.app.ListFragment;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.NavUtils;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;

/**
 * Created by Lenovo on 28.1.2017.
 */

public class ProjectTodo extends ListFragment implements AdapterView.OnItemLongClickListener {

    ProjektSingleton projektSingleton;
    int projectNumber;
    ArrayList<Projekt> projektArrayList;
    Projekt selectedProject;
    ArrayList<TodoProjekt> listOfTodos;
    String todoTitle;
    ArrayList<TodoProjekt> selectedTodoList;
    TodoProjekt newTodo;
    CustomTodoAdapter adapter;

    Button addTodoButton;
    EditText addTodoText;

    int actionBar_icon_select;

    private final String FILENAME="testfile15.txt";

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        projektSingleton = ProjektSingleton.getInstance();
        projectNumber = projektSingleton.getProjectNumber();
        projektArrayList = projektSingleton.getProjectList();
        selectedProject = projektArrayList.get(projectNumber);
        listOfTodos = selectedProject.getTodoList();

        ((AppCompatActivity) getActivity()).getSupportActionBar().setDisplayShowHomeEnabled(false);

        //This is for enabling actionbar in different fragments
        ((AppCompatActivity) getActivity()).getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        setHasOptionsMenu(true);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

//TODO Hangi lifeCycle'a koyacağız loading method'u? İleri geri yapınca ilk seferde details gözükmüyor listte
        projektSingleton.setProjektList(loadSavedArrayList());

        View view = inflater.inflate(R.layout.project_todo_layout, container, false);
        addTodoButton = (Button) view.findViewById(R.id.addTodoButton);
        addTodoText = (EditText) view.findViewById(R.id.addTodoText);

//addDetailsButton onClick method

        addTodoButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){

//Checking if nothing is entered in the EditText and gives Error messaga
                if(TextUtils.isEmpty(addTodoText.getText().toString().trim())){
                    Toast.makeText(getActivity(), "Please type details", Toast.LENGTH_SHORT).show();
                    return;
                }

                todoTitle = addTodoText.getText().toString();
                newTodo = new TodoProjekt(todoTitle, 0);
                selectedTodoList = selectedProject.getTodoList();
                selectedTodoList.add(newTodo);
                adapter.notifyDataSetChanged();

                saveArrayList(projektArrayList);
                addTodoText.setText("");

            }


        });

        getActivity().setTitle(selectedProject.getTitle() + " Checklist");

        return view;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        adapter = new CustomTodoAdapter(getActivity(),listOfTodos);
        setListAdapter(adapter);
        getListView().setOnItemLongClickListener(this);

    }

    @Override
    public boolean onItemLongClick(AdapterView<?> adapterView, View view, int i, long l){

        final int num = i;

        AlertDialog.Builder alert = new AlertDialog.Builder(getActivity());
        //alert.setTitle("Alert!!");
        alert.setMessage("Confirm deleting?");
        alert.setPositiveButton("YES", new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {
                listOfTodos.remove(num);
                saveArrayList(projektArrayList);
                adapter.notifyDataSetChanged();

                dialog.dismiss();

            }
        });
        alert.setNegativeButton("NO", new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {

                dialog.dismiss();
            }
        });

        alert.show();

        return true;
    }

    @Override
    public void onStop() {
        super.onStop();
        saveArrayList(projektArrayList);
        projektSingleton.setProjektList(loadSavedArrayList());
    }

    //Actionbar inflating/////////////////////////////////////////////////////////////
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater){
        inflater.inflate(R.menu.submenu, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    public boolean onOptionsItemSelected(MenuItem item){
        int id = item.getItemId();

        if(id == R.id.action_details){
            ProjectTodo frag = new ProjectTodo();
            ProjectDetailsFragmentChanger fragInterface = (ProjectDetailsFragmentChanger) getActivity();
            fragInterface.projectListChangeFragment(frag);

            hideKeyboard(getActivity());

            actionBar_icon_select = 2;

            return true;
        }

        if(id == R.id.action_todo){
            actionBar_icon_select = 3;

            return true;
        }

        if(id == android.R.id.home){
            ProjectTodo frag = new ProjectTodo();
            ProjectMainListFragmentChanger fragInterface = (ProjectMainListFragmentChanger) getActivity();
            fragInterface.projectMainListChangeFragment(frag);

            hideKeyboard(getActivity());

            return true;
        }

        return super.onOptionsItemSelected(item);

    }

    @Override
    public void onPrepareOptionsMenu(Menu menu) {
        // if(actionBar_icon_select == 3){
        menu.findItem(R.id.action_todo).setIcon(R.drawable.ic_check_box_white_24dp);

        getActivity().invalidateOptionsMenu();
        // }
        super.onPrepareOptionsMenu(menu);


    }

    //Save File Method
    public void saveArrayList(ArrayList<Projekt> arrayList) {
        try {
            FileOutputStream fos = getActivity().openFileOutput(FILENAME, Context.MODE_PRIVATE);
            ObjectOutputStream out = new ObjectOutputStream(fos);
            out.writeObject(arrayList);
            out.close();
            fos.close();

        } catch (IOException e) {
        }
    }

    //Load Text File Method

    public ArrayList<Projekt> loadSavedArrayList() {
        ArrayList<Projekt> savedArrayList = null;

        try {
            FileInputStream fis = getActivity().openFileInput(FILENAME);
            ObjectInputStream ois = new ObjectInputStream(fis);
            savedArrayList = (ArrayList<Projekt>) ois.readObject();
            ois.close();
            fis.close();

        } catch (IOException | ClassNotFoundException e) {
        }

        return savedArrayList;

    }

//Method for hiding soft keyboard between fragments////////////////////////////
    public static void hideKeyboard(Context ctx) {
        InputMethodManager inputManager = (InputMethodManager) ctx
                .getSystemService(Context.INPUT_METHOD_SERVICE);

        // check if no view has focus:
        View v = ((Activity) ctx).getCurrentFocus();
        if (v == null)
            return;

        inputManager.hideSoftInputFromWindow(v.getWindowToken(), 0);
    }


    //Interfaces
    public interface ProjectMainListFragmentChanger{

        public void projectMainListChangeFragment(Fragment fragment);

    }

    public interface ProjectDetailsFragmentChanger{

        public void projectListChangeFragment(Fragment fragment);

    }





}
