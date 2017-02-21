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

    private final String FILENAME="testfile13.txt";
    private final String FILENAME2="testfile14.txt";

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ListOfP = new ArrayList<String>();
        projektArrayList = new ArrayList<Projekt>();
        projektSingleton = ProjektSingleton.getInstance();
        projektSingleton.setProjektList(getSavedArrayList());
        ListOfP = getSavedStringList();

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

            }

        });

        getActivity().setTitle("Bulut Note");

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

        projektSingleton.setProjectNumber(i);

    }

    @Override
    public boolean onItemLongClick(AdapterView<?> adapterView, View view, int i, long l){

        projektArrayList.remove(i);
        ListOfP.remove(i);
        adapter.notifyDataSetChanged();
        saveArrayList(projektArrayList);
        saveStringList(ListOfP);
        Toast.makeText(getActivity(), "LongClick works!", Toast.LENGTH_SHORT).show();

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
            Toast.makeText(getActivity(), "Success!", Toast.LENGTH_SHORT).show();

        } catch (IOException e) {
            Toast.makeText(getActivity(), e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }

    public void saveStringList(ArrayList<String> arrayList) {
        try {
            FileOutputStream fos = getActivity().openFileOutput(FILENAME2, Context.MODE_PRIVATE);
            ObjectOutputStream out = new ObjectOutputStream(fos);
            out.writeObject(arrayList);
            out.close();
            fos.close();
            Toast.makeText(getActivity(), "2 x Success!", Toast.LENGTH_SHORT).show();

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
            Toast.makeText(getActivity(), "Loaded!", Toast.LENGTH_SHORT).show();

        } catch (IOException | ClassNotFoundException e) {
            Toast.makeText(getActivity(), e.getMessage(), Toast.LENGTH_SHORT).show();
        }

        return savedArrayList;

    }

    public ArrayList<String> getSavedStringList() {
        ArrayList<String> savedArrayList = null;

        try {
            FileInputStream fis = getActivity().openFileInput(FILENAME2);
            ObjectInputStream ois = new ObjectInputStream(fis);
            savedArrayList = (ArrayList<String>) ois.readObject();
            ois.close();
            fis.close();
            Toast.makeText(getActivity(), "2 x Loaded!", Toast.LENGTH_SHORT).show();

        } catch (IOException | ClassNotFoundException e) {
            Toast.makeText(getActivity(), e.getMessage(), Toast.LENGTH_SHORT).show();
        }

        return savedArrayList;

    }

}
