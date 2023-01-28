public class Info{
    
    
    public static String getNameClass(String ch){
        String s=new String();
        int d=ch.indexOf("(")+1;
        int f=ch.indexOf(",", d);
        s=ch.substring(d,f);
        return s;
    }
	
    public static String getClassName(String s){
        String name=new String();
        int d=s.indexOf("Class(")+6;
        int f=s.indexOf(",", d);
        name=s.substring(d,f);
        return name;
    }
        
    public static boolean isInterface(String s)
    {
        String name=new String();
        int d=s.indexOf("Class(");
        int f=s.indexOf(");", d);
        name=s.substring(d,f);
        if(name.contains(",abstract,")) 
            return true;
        return false;
    }
        
    public static int getClassNum(String s){
        int res=0;
        for(int i=0;i<ApplyRefactoring.blocs.size();i++){
            String ss=(String)ApplyRefactoring.blocs.elementAt(i);
            ss=ApplyRefactoring.getClassName((String)ApplyRefactoring.blocs.elementAt(i));
            if(s.equals(ss)){
                res=i;
                return i;
            }
        }
        return res;
    }
	
    public static int getNbrClass(){
        return ApplyRefactoring.blocs.size();
    }
	
    public static String [] getAttributesList(String className){
        int nb=Info.getNbrAttributes(className);
        String [] res=new String[nb];
        String bloc=new String();
        for(int i=0;i<ApplyRefactoring.blocs.size();i++){
            String s=(String)ApplyRefactoring.blocs.elementAt(i);
            if(s.contains("Class("+className))
                bloc=s;
        }
        for(int i=0;i<nb;i++){
            int d=bloc.indexOf("Attribute(");
            int f=bloc.indexOf(");\n",d)+3;
            String ss=bloc.substring(d,f);
            res[i]=ss;
            String s1=bloc.substring(0,d);
            String s2=bloc.substring(f,bloc.length());
            bloc=s1+s2;
        }
        return res;
    }
	
    public static String [] getParametersList(String className, String methodLine){
        String method=Info.getNameMethod(methodLine);
        int nb=Info.getNbrParameters(className, method);
        String [] res=new String[nb];
        String bloc=new String();
        for(int i=0;i<ApplyRefactoring.blocs.size();i++){
            String s=(String)ApplyRefactoring.blocs.elementAt(i);
            if(s.contains("Class("+className))
                bloc=s;
        }
        for(int i=0;i<nb;i++){
            int d=bloc.indexOf("Parameter("+className+","+method+",");
            int f=bloc.indexOf(");\n",d)+3;
            String ss=bloc.substring(d,f);
            res[i]=ss;
            String s1=bloc.substring(0,d);
            String s2=bloc.substring(f,bloc.length());
            bloc=s1+s2;
        }
        return res;
    }
	
    public static int getNbrAttributes(String className){
        int nb=0;
        String bloc=new String();
        for(int i=0;i<ApplyRefactoring.blocs.size();i++){
            String s=(String)ApplyRefactoring.blocs.elementAt(i);
            if(s.contains("Class("+className))
                    bloc=s;
        }
        while(bloc.contains("Attribute(")){
            nb++;
            int d=bloc.indexOf("Attribute(");
            int f=bloc.indexOf(");",d);
            String s1=bloc.substring(0,d);
            String s2=bloc.substring(f,bloc.length());
            bloc=s1+s2;
        }
        return nb;
    }
        
    public static int getNbrPublicAttributes(String className){
        int nb=0;
        String bloc=new String();
        for(int i=0;i<ApplyRefactoring.blocs.size();i++){
            String s=(String)ApplyRefactoring.blocs.elementAt(i);
            if(s.contains("Class("+className))
                bloc=s;
        }
        while(bloc.contains("Attribute(")){
            if (!bloc.contains("private"))nb++;
            int d=bloc.indexOf("Attribute(");
            int f=bloc.indexOf(");",d);
            String s1=bloc.substring(0,d);
            String s2=bloc.substring(f,bloc.length());
            bloc=s1+s2;
        }
        return nb;
    }
	
    public static int getNbrMethods(String className){
        int nb=0;
        String bloc=new String();
        for(int i=0;i<ApplyRefactoring.blocs.size();i++){
            String s=(String)ApplyRefactoring.blocs.elementAt(i);
            if(s.contains("Class("+className))
                bloc=s;
        }
        while(bloc.contains("Method(")){
            nb++;
            //bloc.replace("Attribute(", "__");
            int d=bloc.indexOf("Method(");
            int f=bloc.indexOf(");",d);
            String s1=bloc.substring(0,d);
            String s2=bloc.substring(f,bloc.length());
            bloc=s1+s2;
        }
        return nb;
    }
	
    public static int getNbrParameters(String className, String method){
        int nb=0;
        String bloc=new String();
        for(int i=0;i<ApplyRefactoring.blocs.size();i++){
            String s=(String)ApplyRefactoring.blocs.elementAt(i);
            if(s.contains("Class("+className))
                bloc=s;
        }
        while(bloc.contains("Parameter("+className+","+method+",")){
            nb++;
            int d=bloc.indexOf("Parameter("+className+","+method+",");
            int f=bloc.indexOf(");",d);
            String s1=bloc.substring(0,d);
            String s2=bloc.substring(f,bloc.length());
            bloc=s1+s2;
        }
        return nb;
    }
	
    public static int getNbrRelations(String className){
        int nb=0;
        String bloc=new String();
        for(int i=0;i<ApplyRefactoring.blocs.size();i++){
            String s=(String)ApplyRefactoring.blocs.elementAt(i);
            if(s.contains("Class("+className))
                bloc=s;
        }
        while(bloc.contains("Relation(")){
            nb++;
            //bloc.replace("Attribute(", "__");
            int d=bloc.indexOf("Relation(");
            int f=bloc.indexOf(");",d);
            String s1=bloc.substring(0,d);
            String s2=bloc.substring(f,bloc.length());
            bloc=s1+s2;
        }
        return nb;
    }
	
    public static int getNbrRelations(String bloc,String className){
        int nb=0;
        int cohesion =0;
        int coupling =0;
        while(bloc.contains("Relation(")){
            nb++;
            //bloc.replace("Attribute(", "__");
            int d=bloc.indexOf("Relation(");
            int f=bloc.indexOf(");",d);
            String relation = bloc.substring(d,f);
            String source = relation.substring((relation.indexOf("(")+1),relation.indexOf(";"));
            String target = relation.substring((relation.indexOf(",")+1),relation.lastIndexOf(","));
            if (source.equals(target)) 
                cohesion ++ ;
            else 
                coupling++;
            String s1=bloc.substring(0,d);
            String s2=bloc.substring(f,bloc.length());
            bloc=s1+s2;
        }
        return nb;
    }
	
    public static String [] getMethodslist(String className){
        int nb=Info.getNbrMethods(className);
        String [] res=new String[nb];
        String bloc=new String();
        for(int i=0;i<ApplyRefactoring.blocs.size();i++){
            String s=(String)ApplyRefactoring.blocs.elementAt(i);
            if(s.contains("Class("+className))
                bloc=s;
        }
        for(int i=0;i<nb;i++){
            int d=bloc.indexOf("Method(");
            int f=bloc.indexOf(");\n",d)+3;
            String ss=bloc.substring(d,f);
            res[i]=ss;
            //System.out.println("res[i]: "+i+res[i]);
            String s1=bloc.substring(0,d);
            String s2=bloc.substring(f,bloc.length());
            bloc=s1+s2;
        }
        return res;
    }
	
    public static boolean hasRelationShip(String className){
        String bloc=new String();
        String s;
        int numBloc=0;
        for(int i=0;i<ApplyRefactoring.blocs.size();i++){
            s=(String)ApplyRefactoring.blocs.elementAt(i);
            if(s.contains("Class("+className)){
                bloc=s;
                numBloc=i;
            }
        }
        if(bloc.contains("Relation("))
            return true;
        return false;
    }
	
    public static boolean hasRelationShip(String bloc, String className){
        if(bloc.contains("Relation("))
            return true;
        return false;
    }

    public static String[] getClassInfo(String className){
        String res[]=new String[4];
        String bloc=new String();
        for(int i=0;i<ApplyRefactoring.blocs.size();i++){
            String s=(String)ApplyRefactoring.blocs.elementAt(i);
            if(s.contains("Class("+className))
                bloc=s;
        }
        int d=bloc.indexOf("Class(");
        int f=bloc.indexOf(");", d)+2;
        String s1=bloc.substring(d, f);

        d=s1.indexOf("(")+1;
        f=s1.indexOf(",", d);
        res[0]=s1.substring(d,f);
        s1=s1.substring(0, d)+s1.substring(f, s1.length());

        d=s1.indexOf(",",s1.indexOf(res[0]))+1;
        f=s1.indexOf(",", d);
        res[1]=s1.substring(d,f);
        s1=s1.substring(d);

        d=s1.indexOf(",",s1.indexOf(res[1]))+1;
        f=s1.indexOf(",", d);
        res[2]=s1.substring(d,f);
        s1=s1.substring(d);

        d=s1.indexOf(",",s1.indexOf(res[2]))+1;
        f=s1.indexOf(")", d);
        res[3]=s1.substring(d,f);
        //s1=s1.substring(d);
        //System.out.println("getClassInfo: "+res[0]+" "+res[1]+" "+res[2]+" "+res[3]+" ");
        return res;
    }

    public static boolean hasAttribute(String className){
        String bloc=new String();
        String s;
        for(int i=0;i<ApplyRefactoring.blocs.size();i++){
            s=(String)ApplyRefactoring.blocs.elementAt(i);
            if(s.contains("Class("+className))
                bloc=s;
        }
        if(bloc.contains("Attribute("))
                return true;
        return false;
    }

    public static boolean hasMethod(String className){
        String bloc=new String();
        String s;
        int numBloc=0;
        for(int i=0;i<ApplyRefactoring.blocs.size();i++){
            s=(String)ApplyRefactoring.blocs.elementAt(i);
            if(s.contains("Class("+className)){
                bloc=s;
                numBloc=i;
            }
        }
        if(bloc.contains("Method("))
            return true;
        return false;
    }

    public static boolean hasSuperClass(String className){
        String bloc=new String();
        String s;
        for(int i=0;i<ApplyRefactoring.blocs.size();i++){
            s=(String)ApplyRefactoring.blocs.elementAt(i);
            if(s.contains("Class("+className))
                bloc=s;
            
        }
        if(bloc.contains("Generalisation("+className))
            return true;
        return false;
    }

    public static String [] getRelationslist(String className){
        int nb=Info.getNbrRelations(className);
        String [] res=new String[nb];
        String bloc=new String();
        for(int i=0;i<ApplyRefactoring.blocs.size();i++){
            String s=(String)ApplyRefactoring.blocs.elementAt(i);
            if(s.contains("Class("+className))
            {
                    bloc=s;
            }
        }
        for(int i=0;i<nb;i++){
            int dd=bloc.indexOf("Relation(");
            int ff=bloc.indexOf(");\n",dd)+3;
            String ss=bloc.substring(dd,ff);
            res[i]=ss;
            int d=bloc.indexOf("Relation(");
            int f=bloc.indexOf(");",d);
            String s1=bloc.substring(0,d);
            String s2=bloc.substring(f,bloc.length());
            bloc=s1+s2;
        }
        return res;
    }
    public static String [] getRelationslist(String bloc, String className){
        int nb=Info.getNbrRelations(className);
        String [] res=new String[nb];
        for(int i=0;i<nb;i++){
            int dd=bloc.indexOf("Relation(");
            int ff=bloc.indexOf(");\n",dd)+3;
            String ss=bloc.substring(dd,ff);
            res[i]=ss;
            int d=bloc.indexOf("Relation(");
            int f=bloc.indexOf(");",d);
            String s1=bloc.substring(0,d);
            String s2=bloc.substring(f,bloc.length());
            bloc=s1+s2;
        }
        return res;
    }

    public static String [] getSuperClassesList(String className){
        int nb=Info.getNbrSuperClasses(className);
        String [] res=new String[nb];
        String bloc=new String();
        for(int i=0;i<ApplyRefactoring.blocs.size();i++){
            String s=(String)ApplyRefactoring.blocs.elementAt(i);
            if(s.contains("Class("+className))
                bloc=s;
        }
        int i=0;
        while(i<nb){
            int dd=bloc.indexOf("Generalisation("+className);
            int ff=bloc.indexOf(");\n",dd)+3;
            String ss=bloc.substring(dd,ff);
            int c=ss.indexOf("Generalisation("+className);
            int d=ss.indexOf(",",c)+1;
            int f=ss.indexOf(");",d);
            res[i]=ss.substring(d,f);
            String s1=bloc.substring(0,dd);
            String s2=bloc.substring(ff,bloc.length());
            bloc=s1+s2;
            i++;
        }
        return res;
    }

    public static int getNbrSuperClasses(String className){
        int nb=0;
        String bloc=new String();
        for(int i=0;i<ApplyRefactoring.blocs.size();i++){
            String s=(String)ApplyRefactoring.blocs.elementAt(i);
            if(s.contains("Class("+className))
                bloc=s;
        }
        while(bloc.contains("Generalisation("+className)){
            nb++;
            int d=bloc.indexOf("Generalisation("+className);
            int f=bloc.indexOf(");",d);
            String s1=bloc.substring(0,d);
            String s2=bloc.substring(f,bloc.length());
            bloc=s1+s2;
        }
        return nb;
    }

    public static boolean isLocalParameter(String chaine){
        if(chaine.contains("local"))
            return true;
        else
            return false;
    }

    public static boolean hasParameter(String className, String methodName){
        String bloc=new String();
        String s;
        int numBloc=0;
        for(int i=0;i<ApplyRefactoring.blocs.size();i++){
            s=(String)ApplyRefactoring.blocs.elementAt(i);
            if(s.contains("Class("+className)){
                bloc=s;
                numBloc=i;
            }
        }
        if(bloc.contains("Parameter("+className+","+methodName+","))
            return true;

        return false;
    }

    public static boolean hasParameter(String className){
        String bloc=new String();
        String s;
        int numBloc=0;
        for(int i=0;i<ApplyRefactoring.blocs.size();i++){
            s=(String)ApplyRefactoring.blocs.elementAt(i);
            if(s.contains("Class("+className)){
                bloc=s;
                numBloc=i;
            }
        }
        if(bloc.contains("Parameter("+className+","))
            return true;
        return false;
    }

    public static String getNameMethod(String ch){
        String s=new String();
        int c=ch.indexOf("(")+1;
        int d=ch.indexOf(",", c)+1;
        int f=ch.indexOf(",", d);
        s=ch.substring(d,f);
        return s;
    }

    public static int getNbrSubClasses(String className){
        int nb=0;
        String bloc=new String();
        String name=new String();
        for(int i=0;i<ApplyRefactoring.blocs.size();i++){
            bloc=(String)ApplyRefactoring.blocs.elementAt(i);
            name=ApplyRefactoring.getClassName(bloc);			
            while(bloc.contains("Generalisation("+name+","+className+");")){
                nb++;
                int d=bloc.indexOf("Generalisation("+name+","+className+");");
                int f=bloc.indexOf(");",d)+1;
                String s1=bloc.substring(0,d);
                String s2=bloc.substring(f,bloc.length());
                bloc=s1+s2;
            }
        }
        return nb;
    }

    public static boolean hasSubClass(String className){
        String bloc=new String();
        String name=new String();
        int numBloc=0;
        for(int i=0;i<ApplyRefactoring.blocs.size();i++){
            bloc=(String)ApplyRefactoring.blocs.elementAt(i);
            name=ApplyRefactoring.getClassName(bloc);			
            if(bloc.contains("Generalisation("+name+","+className+");")){
                int d=bloc.indexOf("Generalisation("+name+","+className+");");
                int f=bloc.indexOf(");",d)+2;
                String ch=bloc.substring(d, f);
                if(ch.contains(","+className+")"))
                        return true;
            }
        }
        return false;
    }

    public static String [] getSubClassesList(String className){
        int nb=Info.getNbrSubClasses(className);
        String [] res=new String[nb];
        String bloc=new String();
        int x=0;
        for(int i=0;i<ApplyRefactoring.blocs.size();i++){
            bloc=(String)ApplyRefactoring.blocs.elementAt(i);
            String name=ApplyRefactoring.getClassName(bloc);
            while(bloc.contains("Generalisation("+name+","+className+");")){
                int dd=bloc.indexOf("Generalisation("+name+","+className+");");
                int ff=bloc.indexOf(");\n",dd)+3;
                String ss=bloc.substring(dd,ff);
                res[x]=name;
                String s1=bloc.substring(0,dd);
                String s2=bloc.substring(ff,bloc.length());
                bloc=s1+s2;
                x++;
            }
        }
        return res;
    }

    public static int getClassNbrSubClasses(String className){
        int nb=0;
        String bloc=new String();
        String name=new String();
        for(int i=0;i<ApplyRefactoring.blocs.size();i++){
            bloc=(String)ApplyRefactoring.blocs.elementAt(i);
            name=ApplyRefactoring.getClassName(bloc);			
            while(bloc.contains("Generalisation("+name+","+className+");")){
                nb++;
                int d=bloc.indexOf("Generalisation("+name+","+className+");");
                int f=bloc.indexOf(");",d)+1;
                String s1=bloc.substring(0,d);
                String s2=bloc.substring(f,bloc.length());
                bloc=s1+s2;
            }
        }
        return nb;
    }

    public static String getBloc(String className){
        String bloc=new String();
        for (int i=0;i<ApplyRefactoring.blocs.size();i++){
                if(ApplyRefactoring.getClassName((String)ApplyRefactoring.blocs.elementAt(i)).equals(className))
                    bloc=(String)ApplyRefactoring.blocs.elementAt(i);
        }
        return bloc;
    }
    // for activity diagram :
}
