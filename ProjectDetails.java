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
    private final String FILENAME="testfile13.txt";

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        projektSingleton = ProjektSingleton.getInstance();
        projectNumber = projektSingleton.getProjectNumber();
        projektArrayList = projektSingleton.getProjectList();
        selectedProject = projektArrayList.get(projectNumber);
        listOfDetail = selectedProject.getDetails();

        currentTime = System.currentTimeMillis();
        dateFormat = new SimpleDateFormat("yy/MM/dd");
        date = new Date(currentTime);
        currentTimeString = dateFormat.format(date);

//This is for enabling actionbar in different fragments
        ((AppCompatActivity) getActivity()).getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

//TODO Hangi lifeCycle'a koyacağız loading method'u? İleri geri yapınca ilk seferde details gözükmüyor listte
        projektSingleton.setProjektList(getSavedArrayList());

        View view = inflater.inflate(R.layout.project_details_layout, container, false);
        textView = (TextView) view.findViewById(R.id.editText);
        textView.setText("SingletonProjectNo: " + Integer.toString(projectNumber) + " ProjectArrayList: " + Integer.toString(projektArrayList.size()));
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

        listOfDetail.remove(i);
        adapter.notifyDataSetChanged();
        saveArrayList(projektArrayList);
        Toast.makeText(getActivity(), "LongClick works!", Toast.LENGTH_SHORT).show();

        return true;
    }

//Actionbar inflating
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater){
        inflater.inflate(R.menu.submenu, menu);
    }

    public boolean onOptionsItemSelected(MenuItem item){
        int id = item.getItemId();

        if(id == R.id.action_details){
            Toast.makeText(getActivity(), "Action Details", Toast.LENGTH_SHORT).show();
            return true;
        }

        if(id == R.id.action_todo){
            ProjectDetails frag = new ProjectDetails();
            ProjectTodoFragmentChanger fragInterface = (ProjectTodoFragmentChanger) getActivity();
            fragInterface.projectTodoChangeFragment(frag);

            Toast.makeText(getActivity(), "Action Todo", Toast.LENGTH_SHORT).show();
            return false;
        }

        if(id == android.R.id.home){
            ProjectDetails frag = new ProjectDetails();
            ProjectMainListFragmentChanger fragInterface = (ProjectMainListFragmentChanger) getActivity();
            fragInterface.projectMainListChangeFragment(frag);
            return true;
        }

        return super.onOptionsItemSelected(item);

    }

    @Override
    public void onPrepareOptionsMenu(Menu menu) {
        menu.findItem(R.id.action_details).setIcon(R.drawable.ic_create_white_24dp);
        Toast.makeText(getActivity(), "2", Toast.LENGTH_SHORT).show();

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
            Toast.makeText(getActivity(), "Details Success!", Toast.LENGTH_SHORT).show();

        } catch (IOException e) {
            Toast.makeText(getActivity(), e.getMessage(), Toast.LENGTH_SHORT).show();
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
            Toast.makeText(getActivity(), "Details Loaded!", Toast.LENGTH_SHORT).show();

        } catch (IOException | ClassNotFoundException e) {
            Toast.makeText(getActivity(), e.getMessage(), Toast.LENGTH_SHORT).show();
        }

        return savedArrayList;

    }

    //Interface

    public interface ProjectTodoFragmentChanger{

        public void projectTodoChangeFragment(Fragment fragment);

    }

    public interface ProjectMainListFragmentChanger{

        public void projectMainListChangeFragment(Fragment fragment);

    }

}
