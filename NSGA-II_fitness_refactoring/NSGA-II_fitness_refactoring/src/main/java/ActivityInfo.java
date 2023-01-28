//public class ActivityInfo {
//
//    // number of parameters:
//    public static int NP(String className, String methodLine){
//        String method = Info.getNameMethod(methodLine);
//        return Info.getNbrParameters(className , method);
//    }
//    // number of edges :
//    public static int NED(String className, String method){
//        int faze1 = new CreateObjectAction(className).getEdges();
//        int faze2 = new ReadStructuralFeatureAction(className , method).getEdges();
//        int faze3 = new CallOperationAction(className , method).getEdges();
//        int faze4 = new CallBehaviorAction(className , method).getEdges();
//        return faze1 + faze2 + faze3 + faze4;
//    }
//
//    // number of actions :
//    public static int NAC(String className, String method){
//        int faze1 = new CreateObjectAction(className).getAction();
//        int faze2 = new ReadStructuralFeatureAction(className , method).getActions();
//        int faze3 = new CallOperationAction(className , method).getActions();
//        int faze4 = new CallBehaviorAction(className , method).getActions();
//        return faze1 + faze2 + faze3 + faze4;
//    }
//
//    // Locality :
//    public static double LO(String className, String method){
//        int faze1 = new CreateObjectAction(className).getAction();
//        int faze2 = new ReadStructuralFeatureAction(className , method).getActions();
//        int faze3 = new CallOperationAction(className , method).getActions();
//        int faze4 = new CallBehaviorAction(className , method).getActions();
//        int readInReferenced = faze1 + faze2 + faze3;
//        int allRead = faze1 + faze2 + faze3 + faze4;
//        return (double)readInReferenced/allRead;
//    }
//}
