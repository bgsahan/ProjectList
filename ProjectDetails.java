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
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

/**
 * Created by Lenovo on 28.1.2017.
 */

public class ProjectDetails extends ListFragment implements AdapterView.OnItemLongClickListener {

    ArrayAdapter<String> adapter;
    ArrayList<Projekt> projektArrayList;
    int projectNumber;
    ArrayList<String> listOfDetail;
    ProjektSingleton projektSingleton;
    Projekt selectedProject;
    TextView textView;

    Button addDetailsButton;
    EditText addDetailsEditText;
    String detailsTitle;
    ArrayList<String> selectedDetails;

    long currentTime;
    DateFormat dateFormat;
    Date date;
    String currentTimeString;

//for Save & Load
    private final String FILENAME="testfile15.txt";

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        projektSingleton = ProjektSingleton.getInstance();
        projectNumber = projektSingleton.getProjectNumber();
        projektArrayList = projektSingleton.getProjectList();
        selectedProject = projektArrayList.get(projectNumber);
        listOfDetail = selectedProject.getDetails();

        currentTime = System.currentTimeMillis();
        dateFormat = new SimpleDateFormat("dd/MM/yy");
        date = new Date(currentTime);
        currentTimeString = dateFormat.format(date);

        ((AppCompatActivity) getActivity()).getSupportActionBar().setDisplayShowHomeEnabled(false);

//This is for enabling actionbar in different fragments
        ((AppCompatActivity) getActivity()).getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

//TODO Hangi lifeCycle'a koyacağız loading method'u? İleri geri yapınca ilk seferde details gözükmüyor listte
        projektSingleton.setProjektList(getSavedArrayList());

        View view = inflater.inflate(R.layout.project_details_layout, container, false);
        addDetailsButton = (Button) view.findViewById(R.id.addDetailsButton);
        addDetailsEditText = (EditText) view.findViewById(R.id.addDetailsEditText);

//addDetailsButton onClick method

        addDetailsButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){

//Checking if nothing is entered in the EditText and gives Error messaga
                if(TextUtils.isEmpty(addDetailsEditText.getText().toString().trim())){
                    Toast.makeText(getActivity(), "Please type details", Toast.LENGTH_SHORT).show();
                    return;
                }

                detailsTitle = addDetailsEditText.getText().toString();
                selectedDetails = selectedProject.getDetails();
                selectedDetails.add(detailsTitle + " - (" + currentTimeString + ")");
                adapter.notifyDataSetChanged();

                saveArrayList(projektArrayList);
                addDetailsEditText.setText("");

            }


        });

        getActivity().setTitle(selectedProject.getTitle() + " Notes");

        return view;
    }



    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        adapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_list_item_1, listOfDetail);
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
                listOfDetail.remove(num);
                adapter.notifyDataSetChanged();
                saveArrayList(projektArrayList);

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
        //projektSingleton.setProjektList(getSavedArrayList());
        saveArrayList(projektArrayList);
        projektSingleton.setProjektList(getSavedArrayList());
    }

    //Actionbar inflating
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater){
        inflater.inflate(R.menu.submenu, menu);
    }

    public boolean onOptionsItemSelected(MenuItem item){
        int id = item.getItemId();

        if(id == R.id.action_details){
            return true;
        }

        if(id == R.id.action_todo){
            ProjectDetails frag = new ProjectDetails();
            ProjectTodoFragmentChanger fragInterface = (ProjectTodoFragmentChanger) getActivity();
            fragInterface.projectTodoChangeFragment(frag);

            hideKeyboard(getActivity());

            return false;
        }

        if(id == android.R.id.home){
            ProjectDetails frag = new ProjectDetails();
            ProjectMainListFragmentChanger fragInterface = (ProjectMainListFragmentChanger) getActivity();
            fragInterface.projectMainListChangeFragment(frag);

            hideKeyboard(getActivity());

            return true;
        }

        return super.onOptionsItemSelected(item);

    }

    @Override
    public void onPrepareOptionsMenu(Menu menu) {
        menu.findItem(R.id.action_details).setIcon(R.drawable.ic_create_white_24dp);

        getActivity().invalidateOptionsMenu();

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

    public ArrayList<Projekt> getSavedArrayList() {
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

//Method for hiding soft keyboard between fragments//////////////////////////
    public static void hideKeyboard(Context ctx) {
        InputMethodManager inputManager = (InputMethodManager) ctx
                .getSystemService(Context.INPUT_METHOD_SERVICE);

        // check if no view has focus:
        View v = ((Activity) ctx).getCurrentFocus();
        if (v == null)
            return;

        inputManager.hideSoftInputFromWindow(v.getWindowToken(), 0);
    }


    //Interface

    public interface ProjectTodoFragmentChanger{

        public void projectTodoChangeFragment(Fragment fragment);

    }

    public interface ProjectMainListFragmentChanger{

        public void projectMainListChangeFragment(Fragment fragment);

    }

}
