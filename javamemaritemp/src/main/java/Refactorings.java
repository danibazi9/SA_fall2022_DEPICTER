
import java.io.BufferedReader;
import java.util.Vector;

/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author MWM
 */
public class Refactorings 
{
    
    	//list of candiate refactoring operations to apply
	public static String[] refactring={"addGeneralisation","deleteGeneralisation",
		"addRelationShip","deleteRelationShip","moveAttribute","moveParameter",
		"moveMethod","extractClass","pullUpAttribute","pullUpMethod","pushDownAttribute",
		"pushDownMethod"};
	
	public static Vector detectedClasses=new Vector();
	public static Vector bestRefIter=new Vector();
	public static String [] Ref=new String[4]; 
	public static Vector SolRef=new Vector();
	public static Vector PopSolRef=new Vector();
	public static Vector bestCode=new Vector();
	public static Vector bestCodeDetectedClasses=new Vector();
	public static Vector code=new Vector();
	public static BufferedReader readMatrixFile;
	public static double [][] similarityMatrix;
	
	public static int Effort=0;
	//detectedClasses=Detection.detected;
	
        public static boolean print_executed_refactorings = true ;
	
    
    public static int executeRefactoring(int num)
	{
		if(num==0)
		{
			System.out.println("set add Generalisation");
			return addGeneralisation();
		}
		if(num==1)
		{
			System.out.println("deleteGeneralisation");
			return deleteGeneralisation();
		}
		if(num==2)
		{
			System.out.println("addRelationShip");
			return addRelationShip();
		}
		if(num==3)
		{
			System.out.println("deleteRelationShip");
			return deleteRelationShip();
		}
		if(num==4)
		{
			System.out.println("moveAttribute");
			return moveAttribute();
		}
		if(num==5)
		{
			System.out.println("moveParameter");
			return moveParameter();
		}
		if(num==6)
		{
			System.out.println("moveMethod");
			return moveMethod();
		}
		if(num==7)
		{
			System.out.println("extractClass");
			return extractClass();
		}
		if(num==8)
		{
			System.out.println("pullUpAttribute");
			return pullUpAttribute();
		}
		if(num==9)
		{
			System.out.println("pullUpMethod");
			return pullUpMethod();
		}
		if(num==10)
		{
			System.out.println("pushDownAttribute");
			return pushDownAttribute();
		}
		if(num==11)
		{
			System.out.println("pushDownMethod");
			return pushDownMethod();
		}
		return 0;
	}
	
	public static int addGeneralisation()
	{
		int effort=0;
		String className=Random.randomClass();
		String superClass=Random.randomClass();
		
		effort=ApplyRefactoring.addGeneralisation(className, superClass);
		if (print_executed_refactorings) System.out.println("add Generalisation from "+className+" to "+superClass+" executed");
                
		Ref=new String[3];
		Ref[0]="addGeneralisation()";
		Ref[1]=className;
		Ref[2]=superClass;
		
		return effort;
	}
	
	public static int deleteGeneralisation()
	{
		int effort=0;
		String className=Random.randomClass();		
		int i=0;
		while(!Info.hasSuperClass(className))
		{
			className=Random.randomClass();
			i++;
			if(i==199) {System.out.println("Finding superClass failed! aborting");return 0;}
		}
		
		
		String[] s=Info.getSuperClassesList(className);
		
		int x=Random.random(0, s.length-1);
		if(s.length==1) x=0;
		
		//System.out.println("nbr super class: "+s.length+" "+x);	
		String superClass=(String) s[0];
		
		effort=ApplyRefactoring.deleteGeneralisation(className, superClass);
                if (print_executed_refactorings) System.out.println("delete Generalisation: "+className+" "+superClass+" executed");
		
		Ref=new String[3];
		Ref[0]="deleteGeneralisation()";
		Ref[1]=className;
		Ref[2]=superClass;
		
		return effort;	
	}
	
	public static int addRelationShip()
	{
		int effort=1;
		String class1=Random.randomClass();
		String class2=Random.randomClass();
		
		int i=0;
		boolean ok=true;
		while(!Info.hasMethod(class1) && ok)
		{
			class1=Random.randomClass();
			i++;
			if(i==200) {System.out.println("Finding Class with method failed! aborting");return 0;}
		}
		i=0;
		ok=true;
		while(!Info.hasMethod(class2))
		{
			class2=Random.randomClass();
                        i++;
			if(i==200) {System.out.println("Finding Class with method failed! aborting");return 0;}
		}
			
		String method1=Random.randomMethod(class1);
		method1=Info.getNameMethod(method1);
		String method2=Random.randomMethod(class2);
		method2=Info.getNameMethod(method2);
		String line="Relation("+class1+";"+method1+";"+method2+","+class2+","+"type);\n";
		effort=ApplyRefactoring.addRelationShip(class1, line);
                if (print_executed_refactorings) System.out.println("add Relationship: between"+class1+" "+line+" "+class2+" executed");
		
		Ref=new String[3];
		Ref[0]="addRelationShip()";
		Ref[1]=class1;
		Ref[2]=line;
		
		return effort;
	}
	public static int deleteRelationShip()
	{
		int effort=3;
		String class1=Random.randomClass();
		
		int i=0;
		boolean ok=true;
		while(!Info.hasRelationShip(class1) && ok)
		{
			class1=Random.randomClass();
			i++;
			if(i==200) {System.out.println("Finding Class with method failed! aborting");ok=false;return 0;}
		}
				
		String line=Random.randomRelationship(class1);
		
		effort=ApplyRefactoring.deleteRelationShip(class1, line);
		if (print_executed_refactorings) System.out.println("delete Relationship: "+class1+" "+line+" executed");
		
		Ref=new String[3];
		Ref[0]="deleteRelationShip()";
		Ref[1]=class1;
		Ref[2]=line;
		
		return effort;
	}
	
	public static int moveParameter()
	{
		int effort=1;
		String class1=Random.randomClass();
		String class2=Random.randomClass();
		
                int i = 0 ;
		while(!Info.hasMethod(class1) || !Info.hasParameter(class1))
                {
                    class1=Random.randomClass();
                    i++;
                    if(i==200) {System.out.println("Finding Class with method failed! aborting");return 0;}
                    
                }
			
		i = 0 ;
		while(!Info.hasMethod(class2))
                {
                    class2=Random.randomClass();
                    i++;
                    if(i==200) {System.out.println("Finding Class with method failed! aborting");return 0;}
                }
			
		
		String method1=Random.randomMethod(class1);
		while(!Info.hasParameter(class1, Info.getNameMethod(method1)))
                {
                    method1=Random.randomMethod(class1);
                    i++;
                    if(i==200) {System.out.println("Finding Parameter in method failed! aborting");return 0;}
                }
					
				
		String method2=Random.randomMethod(class2);
		
		String parameter=Random.randomParameter(class1, method1);
		effort+=ApplyRefactoring.deleteParameter(class1, parameter);
		effort+=ApplyRefactoring.addParameter(class2, parameter);
                if (print_executed_refactorings) System.out.println("move from Class "+class1+" the Parameter "+parameter+" to the Class "+class2+" executed");
		
		Ref=new String[4];
		Ref[0]="moveParameter()";
		Ref[1]=class1;
		Ref[2]=parameter;
		Ref[3]=class2;
		
		return effort;		
	}
	
	public static int moveMethod()
	{
		int effort=0;
		String class1=Random.randomClass();
		String class2=Random.randomClass();
		String method;
		
		int i=0;
		boolean ok=true;
		while(!Info.hasMethod(class1) && ok)
		{
			class1=Random.randomClass();
			i++;
			if(i==200) {System.out.println("Finding Class with method failed! aborting");ok=false;return 0;}
		}
		if(ok==false) return 0;
		
		method=Random.randomMethod(class1);
		effort+=ApplyRefactoring.moveMethod(class1, method, class2);
                if (print_executed_refactorings) System.out.println("move from Class "+class1+" the Method "+method+" to the Class "+class2+" executed");
		
		Ref=new String[4];
		Ref[0]="moveMethod()";
		Ref[1]=class1;
		Ref[2]=method;
		Ref[3]=class2;
		
		return effort;
	}
	public static int moveAttribute()
	{
		int effort=0;
		String class1=Random.randomClass();
		String class2=Random.randomClass();
		int i = 0 ;
                while(!Info.hasAttribute(class1))
                {
                    class1=Random.randomClass();
                    i++;
                    if(i==200) {System.out.println("Finding Class with attribute failed! aborting");return 0;}
		}

		String attribute=Random.randomAttribute(class1);
		effort+=ApplyRefactoring.moveAttribute(class1, attribute, class2);
                if (print_executed_refactorings) System.out.println("Move from Class "+class1+" Attribute "+attribute+" to Class "+class2+" executed");
		Ref=new String[4];
		Ref[0]="moveAttribute()";
		Ref[1]=class1;
		Ref[2]=attribute;
		Ref[3]=class2;
		return effort;
	}

	public static int extractClass()
	{
		int effort=0;
		String className=Random.randomClass();
		effort=ApplyRefactoring.extractClass(className, "new"+className);
		if (print_executed_refactorings) System.out.println("extract Class: "+className+" "+" new "+className+" executed ");		
		Ref=new String[2];
		Ref[0]="extractClass()";
		Ref[1]=className;
		
		return effort;
	}
	
	public static int pullUpAttribute()
	{
		int effort=0;
		String class1=Random.randomClass();
		
		int i=0;
		boolean ok=true;
		while((!Info.hasSuperClass(class1) || !Info.hasAttribute(class1)) && ok)
		{
			class1=Random.randomClass();
			i++;
                        if(i==200) {System.out.println("Finding Class with super-Class and attribute failed! aborting");return 0;}
		}
		if(ok==false) return 0;
		String class2=Random.randomSuperClass(class1);
		String attribute=Random.randomAttribute(class1);
		effort=ApplyRefactoring.pullUpAttribute(class1, attribute, class2);
                if (print_executed_refactorings) System.out.println("PullUp Attribute "+attribute+" from class "+class1+" to super-Class "+class2+" executed");
		
		Ref=new String[4];
		Ref[0]="pullUpAttribute()";
		Ref[1]=class1;
		Ref[2]=attribute;
		Ref[3]=class2;
		
		return effort;
	}
	
	public static int pullUpMethod()
	{
		int effort=0;
		String class1=Random.randomClass();
				
		int i=0;
		boolean ok=true;
		while((!Info.hasSuperClass(class1) || !Info.hasMethod(class1)) && ok)
		{
			class1=Random.randomClass();
			i++;
                        if(i==200) {System.out.println("Finding Class with super-Class and method failed! aborting");return 0;}
		}
		if(ok==false) return 0;
		
		String method=Random.randomMethod(class1);
		String class2=Random.randomSuperClass(class1);
		effort=ApplyRefactoring.pullUpMethod(class1, method, class2);
                if (print_executed_refactorings) System.out.println("PullUp method "+method+" from class "+class1+" to super-Class "+class2+" executed");
		
		Ref=new String[4];
		Ref[0]="pullUpMethod()";
		Ref[1]=class1;
		Ref[2]=method;
		Ref[3]=class2;
		
		return effort;
	}
	
	public static int pushDownAttribute()
	{		
		int effort=0;
		String class1=Random.randomClass();
		int i=0;
		while(!Info.hasSubClass(class1) || !Info.hasAttribute(class1))
		{
			class1=Random.randomClass();
			i++;
                        if(i==200) {System.out.println("Finding Class with sub-Class and attribute failed! aborting");return 0;}
		}
		
		String class2=Random.randomSubClass(class1);
		String attribute=Random.randomAttribute(class1);
		effort=ApplyRefactoring.pushDownAttribute(class1, attribute, class2);
                if (print_executed_refactorings) System.out.println("PushDown Attribute "+attribute+" from class "+class1+" to super-Class "+class2+" executed");
		
		Ref=new String[4];
		Ref[0]="pushDownAttribute()";
		Ref[1]=class1;
		Ref[2]=attribute;
		Ref[3]=class2;
		
		return effort;
	}
	
	public static int pushDownMethod()
	{
		int effort=0;
		String class1=Random.randomClass();
		
		int i=0;
		while(!Info.hasSubClass(class1) || !Info.hasMethod(class1))
		{
			class1=Random.randomClass();
			i++;
                        if(i==200) {System.out.println("Finding Class with sub-Class and method failed! aborting");return 0;}
		}
		
		String class2=Random.randomSubClass(class1);
		String method=Random.randomMethod(class1);

		effort=ApplyRefactoring.pushDownMethod(class1, method, class2);
		if (print_executed_refactorings) System.out.println("Pushdown method "+method+" from class "+class1+" to super-Class "+class2+" executed");
                
		Ref=new String[4];
		Ref[0]="pushDownMethod()";
		Ref[1]=class1;
		Ref[2]=method;
		Ref[3]=class2;
		
		return effort;
	}
	/*
	public static int inlineClass()
	{
		int effort=0;
		String class1=Random.randomClass();
		String class2=Random.randomClass();
		
		effort=ApplyRefactoring.inlineClass(class2, class1);
                System.out.println("inline Class: "+class1+" "+class2+" executed");
                
		for(int i=0;i<ApplyRefactoring.blocs.size();i++)
		{
			String ch;
			ch=(String)ApplyRefactoring.blocs.elementAt(i);
			if(ch.equals(class2))
			ApplyRefactoring.blocs.remove(class2);
		}
		
		Ref=new String[3];
		Ref[0]="inlineClass()";
		Ref[1]=class1;
		Ref[2]=class2;
		
		return effort;
	}
        */
    
}
