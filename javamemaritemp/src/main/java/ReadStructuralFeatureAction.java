public class ReadStructuralFeatureAction {
    private int numOfEdges = 0;
    private int numOfAction = 0;

    private String []attrs ;
    private String []attrReturnType;

    public String classname;
    public String methodLine;

    ReadStructuralFeatureAction(String classname , String methodLine){
        this.classname = classname;
        this.methodLine = methodLine;
        int numOfAttr = Info.getNbrAttributes(this.classname);
        this.attrs = new String[numOfAttr];
        this.attrs = Info.getAttributesList(this.classname);
    }

    private String[] getAttrReturnType(String[] sens){
        String []res = new String[sens.length];
        for (int i=0;i< sens.length;i++){
            res[i] = sens[i].split(",")[2];
        }
        return res;
    }

    public int getEdges(){
        String []params = Info.getParametersList(this.classname , this.methodLine);
        String []returnType = getAttrReturnType(this.attrs);
        for (int i=0;i< params.length;i++){
            for (int j=0;j< returnType.length;j++){
                if (params[i].contains(returnType[j]) && params[i].contains("declaration")){
                    numOfEdges++;
                    break;
                }
            }
        }
        return ++numOfEdges;
    }

    public int getActions(){
        String []params = Info.getParametersList(this.classname , this.methodLine);
        String []returnType = getAttrReturnType(this.attrs);
        for (int i=0;i< params.length;i++){
            for (int j=0;j< returnType.length;j++){
                if (params[i].contains(returnType[j]) && params[i].contains("declaration")){
                    numOfAction++;
                    break;
                }
            }
        }
        return numOfAction;
    }
}
