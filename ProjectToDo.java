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

    private final String FILENAME="testfile13.txt";

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        projektSingleton = ProjektSingleton.getInstance();
        projectNumber = projektSingleton.getProjectNumber();
        projektArrayList = projektSingleton.getProjectList();
        selectedProject = projektArrayList.get(projectNumber);
        listOfTodos = selectedProject.getTodoList();

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

        listOfTodos.remove(i);
        saveArrayList(projektArrayList);
        adapter.notifyDataSetChanged();
        Toast.makeText(getActivity(), "LongClick works!", Toast.LENGTH_SHORT).show();

        return true;
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

            actionBar_icon_select = 2;

            Toast.makeText(getActivity(), "Action Details", Toast.LENGTH_SHORT).show();
            return true;
        }

        if(id == R.id.action_todo){
            actionBar_icon_select = 3;
            Toast.makeText(getActivity(), "Action Bar", Toast.LENGTH_SHORT).show();

            return true;
        }

        if(id == android.R.id.home){
            ProjectTodo frag = new ProjectTodo();
            ProjectMainListFragmentChanger fragInterface = (ProjectMainListFragmentChanger) getActivity();
            fragInterface.projectMainListChangeFragment(frag);
            return true;
        }

        return super.onOptionsItemSelected(item);

    }

    @Override
    public void onPrepareOptionsMenu(Menu menu) {
      // if(actionBar_icon_select == 3){
            menu.findItem(R.id.action_todo).setIcon(R.drawable.ic_check_box_white_24dp);
            Toast.makeText(getActivity(), "3", Toast.LENGTH_SHORT).show();

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
            Toast.makeText(getActivity(), "Todo Saved!", Toast.LENGTH_SHORT).show();

        } catch (IOException e) {
            Toast.makeText(getActivity(), e.getMessage(), Toast.LENGTH_SHORT).show();
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
            Toast.makeText(getActivity(), "Todo Loaded!", Toast.LENGTH_SHORT).show();

        } catch (IOException | ClassNotFoundException e) {
            Toast.makeText(getActivity(), e.getMessage(), Toast.LENGTH_SHORT).show();
        }

        return savedArrayList;

    }


//Interfaces
    public interface ProjectMainListFragmentChanger{

    public void projectMainListChangeFragment(Fragment fragment);

    }

    public interface ProjectDetailsFragmentChanger{

        public void projectListChangeFragment(Fragment fragment);

    }
