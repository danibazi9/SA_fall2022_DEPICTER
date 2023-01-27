public class CallOperationAction {

    private int numOfEdges = 0;
    private int numOfAction = 1;

    public String classname;
    public String methodLine;

    private final String []params ;

    CallOperationAction(String classname , String methodLine){
        this.classname = classname;
        this.methodLine = methodLine;
        this.params = Info.getParametersList(this.classname , this.methodLine);
    }

    public int getEdges(){
        for (int i=0;i<this.params.length;i++){
            if (this.params[i].contains("declaration")){
                numOfEdges++;
            }
        }
        return numOfEdges;
    }

    public int getActions(){
        return numOfAction;
    }
}
