public class Projekt implements Serializable{

//Fields
    private String mTitle;
    private ArrayList<String> mDetails;
    private ArrayList<TodoProjekt> mTodoList;


//Getters and Setters
    public String getTitle() {
        return mTitle;
    }

    public void setTitle(String mTitle) {
        this.mTitle = mTitle;
    }

    public ArrayList<String> getDetails() {
        return mDetails;
    }

    public ArrayList<TodoProjekt> getTodoList(){
        return mTodoList;
    }


//Constructor methods
    public Projekt(String title){
        mTitle = title;
        mDetails = new ArrayList<String>();
        mTodoList = new ArrayList<TodoProjekt>();
    }

}
