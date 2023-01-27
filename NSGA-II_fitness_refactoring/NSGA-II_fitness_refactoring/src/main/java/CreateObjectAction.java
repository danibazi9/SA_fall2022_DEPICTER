public class CreateObjectAction {

    public String classname;
    private static final int outputEdges = 1;
    private static final int numAction = 1;

    CreateObjectAction(String classname){
        this.classname = classname;
    }

    public int getEdges(){
        return outputEdges;
    }

    public int getAction(){
        return numAction;
    }


}
