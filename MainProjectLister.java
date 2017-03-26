package com.example.lenovo.projectlist;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Fragment;
import android.app.ListFragment;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;

/**
 * Created by Lenovo on 29.1.2017.
 */

public class MainProjectLister extends ListFragment implements
        AdapterView.OnItemClickListener,
        AdapterView.OnItemLongClickListener {

    Button addProjectButton;
    EditText addProjectEditText;
    public String projectTitle;
    Projekt newProjekt;
    ProjektSingleton projektSingleton;
    ArrayList<Projekt> projektArrayList;
    ArrayAdapter<String> adapter;
    ArrayList<Projekt> tempProjektArrayList;

    ArrayList<String> ListOfP;

    private final String FILENAME="testfile15.txt";
    private final String FILENAME2="testfile16.txt";


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ListOfP = new ArrayList<String>();
        projektSingleton = ProjektSingleton.getInstance();
        projektSingleton.setProjektList(getSavedArrayList());
        ListOfP = getSavedStringList();

        ((AppCompatActivity) getActivity()).getSupportActionBar().setDisplayShowHomeEnabled(true);
        ((AppCompatActivity) getActivity()).getSupportActionBar().setIcon(R.drawable.action_bar_logo);

        ((AppCompatActivity) getActivity()).getSupportActionBar().setDisplayHomeAsUpEnabled(false);
        setHasOptionsMenu(true);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){

        View view = inflater.inflate(R.layout.main_project_lister_layout, container, false);

        addProjectButton = (Button) view.findViewById(R.id.addProjectButton);
        addProjectEditText = (EditText) view.findViewById(R.id.addProjectEditText);


//addProjectButton onClck method

        addProjectButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){

//Checking if nothing is entered in the EditText and gives Error messaga
               if(TextUtils.isEmpty(addProjectEditText.getText().toString().trim())){
                    Toast.makeText(getActivity(), "Please type details", Toast.LENGTH_SHORT).show();
                    return;
                }

                projectTitle = addProjectEditText.getText().toString();
                newProjekt = new Projekt(projectTitle);
                projektArrayList = projektSingleton.getProjectList();
                projektArrayList.add(newProjekt);

                ListOfP.add(projectTitle);
                adapter.notifyDataSetChanged();
                saveArrayList(projektArrayList);
                saveStringList(ListOfP);
                addProjectEditText.setText("");

            }

        });

        getActivity().setTitle("DearBear Notes App");


        return view;

    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        adapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_list_item_1, ListOfP);
        setListAdapter(adapter);

        getListView().setOnItemClickListener(this);
        getListView().setOnItemLongClickListener(this);
    }


    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l){

        MainProjectLister frag = new MainProjectLister();
        ProjectListFragmentChanger fragInterface = (ProjectListFragmentChanger) getActivity();
        fragInterface.projectListChangeFragment(frag);

        hideKeyboard(getActivity());

        projektSingleton.setProjectNumber(i);

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
                projektArrayList = projektSingleton.getProjectList();
                projektArrayList.remove(num);
                ListOfP.remove(num);
                adapter.notifyDataSetChanged();
                saveArrayList(projektArrayList);
                saveStringList(ListOfP);

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




    //Interface

    public interface ProjectListFragmentChanger{

        public void projectListChangeFragment(Fragment fragment);

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
            e.printStackTrace ();
        }
    }

    public void saveStringList(ArrayList<String> arrayList) {
        try {
            FileOutputStream fos = getActivity().openFileOutput(FILENAME2, Context.MODE_PRIVATE);
            ObjectOutputStream out = new ObjectOutputStream(fos);
            out.writeObject(arrayList);
            out.close();
            fos.close();

        } catch (IOException e) {
            e.printStackTrace ();
        }
    }


//Load Text File Method

    public ArrayList<Projekt> getSavedArrayList() {
        ArrayList<Projekt> savedArrayList = null;

        try {
            // get the file and check if it exists before trying to read it
            File savedFile = new File(getActivity().getFilesDir(), FILENAME);
            if(savedFile.exists()) {
                FileInputStream fis = getActivity().openFileInput(FILENAME);
                ObjectInputStream ois = new ObjectInputStream(fis);
                savedArrayList = (ArrayList<Projekt>) ois.readObject();
                ois.close();
                fis.close();
            } else{
                // if it doesn't exist, return an empty list
                savedArrayList = new ArrayList<Projekt>();
            }

        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace ();
        }

        return savedArrayList;

    }


    public ArrayList<String> getSavedStringList() {
        ArrayList<String> savedArrayList = null;

        try {
            // get the file and check if it exists before trying to read it
            File savedFile2 = new File(getActivity().getFilesDir(), FILENAME2);
            if(savedFile2.exists()) {
                FileInputStream fis = getActivity().openFileInput(FILENAME2);
                ObjectInputStream ois = new ObjectInputStream(fis);
                savedArrayList = (ArrayList<String>) ois.readObject();
                ois.close();
                fis.close();
            } else{
                // if it doesn't exist, return an empty list
                savedArrayList = new ArrayList<String>();
            }

        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace ();
        }

        return savedArrayList;

    }

//Method for hiding soft keyboard between fragments//////////////////////////77
    public static void hideKeyboard(Context ctx) {
        InputMethodManager inputManager = (InputMethodManager) ctx
                .getSystemService(Context.INPUT_METHOD_SERVICE);

        // check if no view has focus:
        View v = ((Activity) ctx).getCurrentFocus();
        if (v == null)
            return;

        inputManager.hideSoftInputFromWindow(v.getWindowToken(), 0);
    }


}

