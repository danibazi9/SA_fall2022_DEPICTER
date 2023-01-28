//public class CallBehaviorAction {
//
//    private int numOfEdges ;
//    private int numOfAction ;
//
//    public String classname;
//    public String methodLine;
//
//    CallBehaviorAction(String classname , String methodLine){
//        this.classname = classname;
//        this.methodLine = methodLine;
//        numOfAction = 0;
//        numOfEdges = 0;
//    }
//
//    public int getEdges(){
//        String []rels = Info.getRelationslist(this.classname);
//        String method = Info.getNameMethod(this.methodLine);
//        for (int i=0;i<rels.length;i++){
//            if (rels[i].split(";")[1] == method){
//                numOfEdges++;
//            }
//        }
//        return numOfEdges;
//    }
//
//    public int getActions(){
//        String []rels = Info.getRelationslist(this.classname);
//        String method = Info.getNameMethod(this.methodLine);
//        for (int i=0;i<rels.length;i++){
//            if (rels[i].split(";")[1] == method){
//                numOfAction++;
//            }
//        }
//        return numOfAction;
//    }
//}
