import java.util.Vector;

public class Random 
{
			
	public static int random(int min, int max)
	{
		
    	int x=(int)(Math.random()*100);
    	if(max-min<=1 || max==0)
    		return 0;
    	while (x<min || x>max)
    	{
    		x=(int)(Math.random()*100);
    		//System.out.println(x+".");	
    	}
    	return x;
    }
	
	public static String randomClass(Vector classes)
	{
		//Vector detectedBlocs=new Vector();
		
		String res=new String();
		int x=random(0,classes.size()-1);
		res=(String)classes.elementAt(x);
		
		return res;
	}
	
	public static String randomClass()
	{
		String res=new String();
		int x=random(0,Info.getNbrClass()-1);
		res=ApplyRefactoring.getClassName((String)ApplyRefactoring.blocs.elementAt(x));
		
		return res;
	}
	
	public static String randomAttribute(String className)
	{
		String res=new String();
		int x=Info.getNbrAttributes(className);
		int y=random(0,x-1);
		
		String [] s=Info.getAttributesList(className);
		res=s[y];
				
		return res;
	}
	public static String randomMethod(String className)
	{
		String res=new String();
		int x=Info.getNbrMethods(className);
		int y=random(0,x-1);
		
		String [] s=Info.getMethodslist(className);
		//System.out.print("random method: "+className);
		//System.out.println(" "+x+" "+y+" "+s[y]);
		res=s[y];
				
		return res;
	}
	
	/*public static String randomMethod()
	{
		String res=new String();
		String className=randomClass();
		res=randomMethod(className);
				
		return res;
	}
	*/
	public static String randomRelationship(String className)
	{
		String res=new String();
		int x=Info.getNbrRelations(className);
		int y=random(0,x-1);
		//System.out.println("$$$$$$$ randomed: "+y+" max: "+(x-1));
		
		String [] s=Info.getRelationslist(className);
		res=s[y];
				
		return res;
	}
	public static String randomParameter(String className, String methodLine)
	{
		String res=new String();
		String method=Info.getNameMethod(methodLine);
		int x=Info.getNbrParameters(className, method);
		int y=0;
		if(x<1) y=0;
		else	y=random(0,x-1);
		//System.out.println("$$$$$$$ randomed Parameter: "+y+" max: "+(x-1));
		
		String [] s=Info.getParametersList(className, methodLine);
		res=s[y];
				
		return res;
	}
	public static String randomSubClass(String className)
	{
		String res=new String();
		int x=Info.getNbrSubClasses(className);
		int y=random(0,x-1);
		
		String [] s=Info.getSubClassesList(className);
		res=s[y];
				
		return res;
	}
	public static String randomSuperClass(String className)
	{
		String res=new String();
		int x=Info.getNbrSuperClasses(className);
		
		int y=0;
		if(x>01)
			y=random(0,x-1);
		
		String [] s=Info.getSuperClassesList(className);
		res=s[y];
				
		return res;
	}
	

}
