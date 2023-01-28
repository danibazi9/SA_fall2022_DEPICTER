import java.io.*;
import java.util.Vector;


public class ApplyRefactoring{
	public static BufferedReader lecture;
	public static BufferedWriter ecriture;
	public static Vector blocs=readCode();
	public static Vector saveDefectedCode=readCode();
	
	public static String getClassName(String s)
	{
		String name=new String();
		int d=s.indexOf("Class(")+6;
		int f=s.indexOf(",", d);
    	name=s.substring(d,f);
		return name;
    }
	
	public static int createClass(String name, String b1, String b2, String visibility)
	{
		int effort=1;
		String s=new String();
		s="start b"+(blocs.size()+1)+"\n";
		s+="Class("+name+","+b1+","+b2+","+visibility+");"+"\n";
		s+="end b"+(blocs.size()+1)+"\n";
		blocs.addElement(s);
		updateFile(blocs);
		
		return effort;
	}
	
	public static int deleteClass(String name)
	{
		int effort=3;
		String bloc=new String();
		int numBloc=-1;
		String res="Class can not be deleted";
		for (int i=0;i<blocs.size();i++)
		{
			if(getClassName((String)blocs.elementAt(i)).equals(name))
			{
				numBloc=i;
				bloc=(String)blocs.elementAt(i);
				res="Class deleted successfully";
			}
		}
		if(numBloc==-1) return effort;
		for (int i=numBloc;i<blocs.size();i++)
		{
			String s=(String)blocs.elementAt(i);
			//bloc.replaceAll("b"+i, "b"+(i-2));
			String s1=s.substring(0, s.indexOf("start b")+7);
			String s2=s.substring(s.indexOf('\n'), s.indexOf("end b")+5);
			
			s=s1+(i)+s2+(i)+'\n';
			blocs.setElementAt(s, i);
		}
		blocs.remove(numBloc);
		updateFile(blocs);
		
		return effort;	
	}
	
	public static int addAttribute(String className, String line)
	{
		int effort=1;
		
		if(!line.contains(");\n"))
			
		line+=");\n";
		String bloc=new String();
		int numBloc=-1;
		String res="Add attribute failed";
		for (int i=0;i<blocs.size();i++)
		{
			if(getClassName((String)blocs.elementAt(i)).equals(className))
			{
				numBloc=i;
				bloc=(String)blocs.elementAt(i);
				res="Attribute added successfully";
			}
		}
		if(numBloc==-1) return effort;
		String s1=bloc.substring(0, bloc.indexOf("\n")+1);
		String s2=bloc.substring(bloc.indexOf("\n")+1,bloc.length());
		bloc=s1+line+s2;
		blocs.setElementAt(bloc, numBloc);
		
		updateFile(blocs);
		return effort;
	}

	public static int deleteAttribute(String className, String line)
	{
		int effort=2;
		String s=new String();
		
		String bloc=new String();
		int numBloc=-1;
		String res="delete attribute failed";
		for (int i=0;i<blocs.size();i++)
		{
			if(getClassName((String)blocs.elementAt(i)).equals(className))
			{
				numBloc=i;
				bloc=(String)blocs.elementAt(i);
				res="Attribute deleted successfully";
			}
		}
		
		
		int d=bloc.indexOf(line);
		int f=bloc.indexOf(");\n", d)+3;
		//System.out.println("index: "+d);
		if(d==-1) return 0;
		String s1=bloc.substring(0, d);
		String s2=bloc.substring(f,bloc.length());
		s=s1+s2;
		blocs.setElementAt(s, numBloc);
		
		updateFile(blocs);
		return effort;
	}
	
	public static int moveAttribute(String className, String line, String targetClass)
	{
		int effort=0;
		String s=new String();
		
		String bloc=new String();
		int numBloc=-1;
		String res="Move Attribute failed";
		for (int i=0;i<blocs.size();i++)
		{
			if(getClassName((String)blocs.elementAt(i)).equals(className))
			{
				numBloc=i;
				bloc=(String)blocs.elementAt(i);
				res="Attribute moved successfully";
			}
		}
		if(numBloc==-1) return effort;
				
		effort+=ApplyRefactoring.deleteAttribute(className, line);
		if(line.contains(className))
			line=line.replace(className, targetClass);
		effort+=ApplyRefactoring.addAttribute(targetClass, line);
				
		updateFile(blocs);
		return effort;
	}
	
	public static int addGeneralisation(String className, String superClassName)
	{
		int effort=1;
		String s=new String();
		
		String bloc=new String();
		int numBloc=-1;
		String res="Add generalization failed";
		for (int i=0;i<blocs.size();i++)
		{
			if(getClassName((String)blocs.elementAt(i)).equals(className))
			{
				numBloc=i;
				bloc=(String)blocs.elementAt(i);
				res="Generalization added successfully";
			}
		}
		if(numBloc==-1) return effort;
		int c=bloc.indexOf("Class("+className);
		int d=bloc.indexOf(");", c);
		
		String s1=bloc.substring(0, d+3);
		String s2=bloc.substring(d+3,bloc.length());
		s=s1+"Generalisation("+className+','+superClassName+");\n"+s2;
		blocs.setElementAt(s, numBloc);
		
		updateFile(blocs);
		return effort;
	}
	
	public static int deleteGeneralisation(String className, String superClassName)
	{
		int effort=2;
		String s=new String();
		
		String bloc=new String();
		int numBloc=0;
		String res="delete generalisation failed";
		for (int i=0;i<blocs.size();i++)
		{
			if(getClassName((String)blocs.elementAt(i)).equals(className))
			{
				numBloc=i;
				bloc=(String)blocs.elementAt(i);
				res="Generalisation deleted successfully";
			}
		}
		int d=bloc.indexOf("Generalisation("+className+','+superClassName);
		int f=bloc.indexOf(");\n", d)+3;
		if(d==-1)return 0;
		String s1=bloc.substring(0, d);
		String s2=bloc.substring(f,bloc.length());
		s=s1+s2;
		blocs.setElementAt(s, numBloc);
		
		updateFile(blocs);
		return effort;
	}
	
	public static int addRelationShip(String className, String line)
	{
		int effort=1;
		String s=new String();
		
		String bloc=new String();
		int numBloc=-1;
		String res="Add RelationShip failed";
		for (int i=0;i<ApplyRefactoring.blocs.size();i++)
		{
			if(Info.getClassName((String)ApplyRefactoring.blocs.elementAt(i)).equals(className))
			{
				numBloc=i;
				bloc=(String)ApplyRefactoring.blocs.elementAt(i);
				res="RelationShip added successfully";
			}
		}
		if(numBloc==-1) return 0;
		int d=bloc.indexOf("end b");
				
		String s1=bloc.substring(0, d);
		String s2=bloc.substring(d,bloc.length());
		s=s1+line+s2;
		ApplyRefactoring.blocs.setElementAt(s, numBloc);
						
		updateFile(blocs);
		return effort;
	}
	
	public static int deleteRelationShip(String className, String line)
	{
		int effort=3;
		String s=new String();
		
		String bloc=new String();
		int numBloc=-1;
		String res="delete RelationShip failed";
		for (int i=0;i<ApplyRefactoring.blocs.size();i++)
		{
			if(Info.getClassName((String)ApplyRefactoring.blocs.elementAt(i)).equals(className))
			{
				numBloc=i;
				bloc=(String)ApplyRefactoring.blocs.elementAt(i);
				res="RelationShip deleted successfully";
			}
		}
		if(numBloc==-1) return 0;
		int d=bloc.indexOf(line);
		if(d==-1) return 0;
		int f=bloc.indexOf(");\n", d);		
		String s1=bloc.substring(0, d);
		String s2=bloc.substring(f+3,bloc.length());
		s=s1+s2;
		ApplyRefactoring.blocs.setElementAt(s, numBloc);
		
		updateFile(blocs);
		return effort;
	}
	
	public static int addParameter(String className, String line)
	{
		int effort=1;
		String res="addParameter Failed";
		if(!line.contains(");\n"))
			line+=");\n";
		String bloc=new String();
		int numBloc=-1;
		for (int i=0;i<blocs.size();i++)
		{
			if(getClassName((String)blocs.elementAt(i)).equals(className))
			{
				numBloc=i;
				bloc=(String)blocs.elementAt(i);
				res="Parameter added successfully";
			}
		}
		if(numBloc==-1) return 0;
		int c=bloc.lastIndexOf("Method("+className);
		int d=bloc.indexOf(");", c);
		if(d==-1) return 0;		
		String s1=bloc.substring(0, d+3);
		String s2=bloc.substring(d+3,bloc.length());
		bloc=s1+line+s2;
		blocs.setElementAt(bloc, numBloc);
		
		updateFile(blocs);
		return effort;
	}
	
	public static int deleteParameter(String className, String line)
	{
		int effort=2;
		String s=new String();
		
		String bloc=new String();
		int numBloc=-1;
		String res="delete parameter failed";
		for (int i=0;i<blocs.size();i++)
		{
			if(getClassName((String)blocs.elementAt(i)).equals(className))
			{
				numBloc=i;
				bloc=(String)blocs.elementAt(i);
				res="Parameter deleted successfully";
			}
		}
		if(numBloc==-1) return 0;
		int d=bloc.indexOf(line);
		int f=bloc.indexOf(");\n", d)+3;
		if(d==-1) return 0;
		
		String s1=bloc.substring(0, d);
		String s2=bloc.substring(f,bloc.length());
		s=s1+s2;
		blocs.setElementAt(s, numBloc);
		
		updateFile(blocs);
		return effort;
	}
	
	public static int moveParameter(String className, String line, String targetClass)
	{
		int effort=0;
		try
		{
			String s=new String();
			
			String bloc=new String();
			int numBloc=-1;
			String res="Move Attribute failed";
			for (int i=0;i<blocs.size();i++)
			{
				if(getClassName((String)blocs.elementAt(i)).equals(className))
				{
					numBloc=i;
					bloc=(String)blocs.elementAt(i);
					res="Parameter moved successfully";
				}
			}
			if(numBloc==-1) return 0;
			effort+=ApplyRefactoring.deleteParameter(className, line);
			//line=line.replace(className, "____"+targetClass);
			int d=line.indexOf("(")+1;
			int f=line.indexOf(",", d);
			line=line.substring(0,d)+targetClass+line.substring(f, line.length());
			effort+=ApplyRefactoring.addParameter(targetClass, line);
			
			updateFile(blocs);
		}
		catch(ArrayIndexOutOfBoundsException e)
		{
			System.out.println("Exception move Paramater: ");
		}
		
		return effort;
	}
	public static int addMethod(String className, String MethodLine)
	{
		int effort=1;
		String res="addClass Failed";
		if(!MethodLine.contains(");\n"))
			MethodLine+=");\n";
			
		String bloc=new String();
		int numBloc=-1;
		for (int i=0;i<ApplyRefactoring.blocs.size();i++)
		{
			if(ApplyRefactoring.getClassName((String)ApplyRefactoring.blocs.elementAt(i)).equals(className))
			{
				numBloc=i;
				bloc=(String)ApplyRefactoring.blocs.elementAt(i);
				res="Method added successfully";
			}
		}
		if(numBloc==-1) return 0;;
		int c=bloc.indexOf("Class("+className);
		int d=bloc.indexOf(");", c);
		
		
		String s1=bloc.substring(0, d+3);
		String s2=bloc.substring(d+3,bloc.length());
		bloc=s1+MethodLine+s2;
		ApplyRefactoring.blocs.setElementAt(bloc, numBloc);
		
		updateFile(blocs);
		return effort;
	}
	
	public static int deleteMethod(String className, String methodLine)
	{
		int effort=3;
		String bloc=new String();
		int numBloc=-1;
		String res="delete method failed";
		for (int i=0;i<blocs.size();i++)
		{
			if(getClassName((String)blocs.elementAt(i)).equals(className))
			{
				numBloc=i;
				bloc=(String)blocs.elementAt(i);
				res="Method deleted successfully";
			}
		}
		
		if(numBloc==-1) return 0;
		String method=Info.getNameMethod(methodLine);
		int d=bloc.indexOf(methodLine);
		int f=bloc.indexOf(";\n", d)+2;
		
		String s1=bloc.substring(0, d);
		String s2=bloc.substring(f,bloc.length());
		String s=s1+s2;
		blocs.setElementAt(s, numBloc);
		
		if(Info.hasParameter(className, method))
		{
			int nb=Info.getNbrParameters(className, method);
			String []parameters=Info.getParametersList(className, methodLine);
			for(int i=0;i<nb;i++)
			{
				effort+=deleteParameter(className, parameters[i]);
			}
		}
		
		for (int i=0;i<blocs.size();i++)
		{
			//if(getClassName((String)blocs.elementAt(i)).equals(className))
			{
				numBloc=i;
				bloc=(String)blocs.elementAt(i);
				String nameOfClass=Info.getNameClass(bloc);
				if(Info.hasRelationShip(nameOfClass))
				{
					String[] relations=Info.getRelationslist(nameOfClass);
					int nbRelations=Info.getNbrRelations(nameOfClass);
					//System.out.println("nb relations: "+nbRelations);
					for(int j=0;j<nbRelations;j++)
					{
						if(relations[j].contains("("+className+";"+method) || relations[j].contains(";"+method+","+className+","))
						{
							effort+=deleteRelationShip(className, relations[j]);
							//System.out.println("deleted: "+relations[j]);
						}
					}
				}
			}
		}
		
		updateFile(blocs);
		return effort;
	}
	
	public static int deleteMethodOnly(String className, String methodLine)
	{
		int effort=3;
		String bloc=new String();
		int numBloc=-1;
		String res="delete method failed";
		for (int i=0;i<blocs.size();i++)
		{
			if(getClassName((String)blocs.elementAt(i)).equals(className))
			{
				numBloc=i;
				bloc=(String)blocs.elementAt(i);
				res="Method deleted successfully";
			}
		}
		
		if(numBloc==-1) return 0;
		String method=Info.getNameMethod(methodLine);
		//System.out.println(methodLine);
		
		if(bloc.contains(methodLine))
		{
			int d=bloc.indexOf(methodLine);
			int f=bloc.indexOf(";\n", d)+2;
			
			String s1=bloc.substring(0, d);
			String s2=bloc.substring(f,bloc.length());
			String s=s1+s2;
			blocs.setElementAt(s, numBloc);
		}
		
		
		updateFile(blocs);
		return effort;
	}
	
	public static int moveMethod(String className, String methodLine, String targetClass) throws StringIndexOutOfBoundsException
	{
		int effort=0;
		String bloc=new String();
		int numBloc=-1;
		String res="delete method failed";
		for (int i=0;i<blocs.size();i++)
		{
			if(getClassName((String)blocs.elementAt(i)).equals(className))
			{
				numBloc=i;
				bloc=(String)blocs.elementAt(i);
				res="Method deleted successfully";
			}
		}
		
		if(numBloc==-1) return 0;
		effort+=deleteMethodOnly(className, methodLine);
		if(methodLine.contains("("+className+","))
			methodLine=methodLine.replace("("+className+",","("+targetClass+",");
		effort+=addMethod(targetClass, methodLine);
		
		String method=Info.getNameMethod(methodLine);
		
		if(Info.hasParameter(className, method))
		{
			int nb=Info.getNbrParameters(className, method);
			String []parameters=Info.getParametersList(className, methodLine);
			for(int i=0;i<nb;i++)
			{
				effort+=moveParameter(className, parameters[i], targetClass);
			}
		}
		
		if(Info.hasRelationShip(className))
		{
			String[] relations=Info.getRelationslist(className);
			int nbRelations=Info.getNbrRelations(className);
			//System.out.println("nb relations: "+nbRelations);
			for(int j=0;j<nbRelations;j++)
			{
				if(relations[j].contains("("+className+";"+method) || relations[j].contains(";"+method+","+className+","))
				{
					effort+=deleteRelationShip(className, relations[j]);
					//System.out.println(relations[j]);
					if(relations[j].contains("("+className+";"+method))
						relations[j]=relations[j].replace("("+className, "("+targetClass);
				
					if(relations[j].contains(";"+method+","+className+","))
						relations[j]=relations[j].replace(";"+method+","+className+",", ";"+method+","+targetClass+",");
				
				effort+=addRelationShip(targetClass, relations[j]);
					
				}
			}
		}
		/*
		for (int i=0;i<blocs.size();i++)
		{
			if(i!=numBloc)
			{
				bloc=(String)blocs.elementAt(i);
				String nameOfClass=Info.getClassName(bloc);
				int nb=Info.getNbrRelations(nameOfClass);
				for(int j=0;j<nb;j++)
				{
					//effort+=deleteRelationShip(className, relations[j]);
					if(bloc.contains("("+className+";"+method))
						bloc=bloc.replace("("+className, "("+targetClass);
						
					if(bloc.contains(";"+method+","+className+","))
						bloc=bloc.replace(";"+method+","+className+",", ";"+method+","+targetClass+",");
					
				}
				blocs.setElementAt(bloc,i);
			}
		}
		*/
		
		for (int i=0;i<blocs.size();i++)
		{
			if(i!=numBloc)
			{
				bloc=(String)blocs.elementAt(i);
				String nameOfClass=Info.getClassName(bloc);
				//int nb=Info.getNbrRelations(nameOfClass);
				//for(int j=0;j<nb;j++)
				{
					//effort+=deleteRelationShip(className, relations[j]);
					if(bloc.contains("("+className+";"+method))
						bloc=bloc.replace("("+className, "("+targetClass);
						
					if(bloc.contains(";"+method+","+className+","))
						bloc=bloc.replace(";"+method+","+className+",", ";"+method+","+targetClass+",");
					
				}
				blocs.setElementAt(bloc,i);
			}
		}
		
		updateFile(blocs);
		return effort;
	}
	public static int extractClass(String className, String newClassName)
	{
		int effort=0;
		String s=new String();
		
		String bloc=new String();
		int numBloc=-1;
		String res="Extract Class failed";
		for (int i=0;i<blocs.size();i++)
		{
			if(getClassName((String)blocs.elementAt(i)).equals(className))
			{
				numBloc=i;
				bloc=(String)blocs.elementAt(i);
				res="Class extracted successfully";
			}
		}
		if(numBloc==-1) return 0;
		String [] a=new String[4];
		a=Info.getClassInfo(className);
		effort+=ApplyRefactoring.createClass(newClassName, a[1], a[2], a[3]);
		//System.out.println("CreateClass: "+a[0]+" "+a[1]+" "+ a[2]+" "+ a[3]);
		int nbA=Info.getNbrAttributes(className);
		String []attributes=Info.getAttributesList(className);
		if(nbA>1)
		{
			int nb=nbA/2;
			
			for(int i=0;i<nb;i++)
			{
				effort+=ApplyRefactoring.moveAttribute(className, attributes[i], newClassName);
	
			}
		}
		
		int nbM=Info.getNbrMethods(className);
		String []methods=Info.getMethodslist(className);
		if(nbM>1)
		{
			int nb=nbM/2;
			
			for(int i=0;i<nb;i++)
			{
				effort+=ApplyRefactoring.moveMethod(className, methods[i], newClassName);
			}
		}
		
		for (int i=0;i<blocs.size();i++)
		{
			if(getClassName((String)blocs.elementAt(i)).equals(newClassName))
			{
				numBloc=i;
				bloc=(String)blocs.elementAt(i);
				if(bloc.contains(";\n;\n"))
					bloc=bloc.replace(";\n;\n", ";\n");
				blocs.setElementAt(bloc, numBloc);
			}
		}
		
		updateFile(blocs);
		return effort;
	}
	
	public static int pullUpAttribute(String className, String attribute, String superClass)
	{
		int effort=0;
		String s=new String();
		
		String bloc=new String();
		int numBloc=-1;
		String res="pullUpAttribute failed";
		for (int i=0;i<blocs.size();i++)
		{
			if(getClassName((String)blocs.elementAt(i)).equals(className))
			{
				numBloc=i;
				bloc=(String)blocs.elementAt(i);
				res="Attribute pulled Up successfully";
			}
		}
		
		if(numBloc==-1) return 0;
		
		effort=ApplyRefactoring.moveAttribute(className, attribute, superClass);
				
		updateFile(blocs);
		return effort;
	}
	public static int pullUpMethod(String className, String method, String superClass)
	{
		int effort=0;
		String s=new String();
		
		String bloc=new String();
		int numBloc=-1;
		String res="pullUpMethod failed";
		for (int i=0;i<blocs.size();i++)
		{
			if(getClassName((String)blocs.elementAt(i)).equals(className))
			{
				numBloc=i;
				bloc=(String)blocs.elementAt(i);
				res="Attribute pulled Up successfully";
			}
		}
		if(numBloc==-1) return 0;
		
		effort=ApplyRefactoring.moveMethod(className, method, superClass);
						
		updateFile(blocs);
		return effort;
	}
	
	public static int pushDownAttribute(String superClassName, String attribute, String className)
	{
		int effort=0;
		String s=new String();
		
		String bloc=new String();
		int numBloc=-1;
		String res="push down attribute failed";
		for (int i=0;i<blocs.size();i++)
		{
			if(getClassName((String)blocs.elementAt(i)).equals(superClassName))
			{
				numBloc=i;
				bloc=(String)blocs.elementAt(i);
				res="Attribute pushed down successfully";
			}
		}
		
		if(numBloc==-1) return 0;
		effort=ApplyRefactoring.moveAttribute(superClassName, attribute, className);
				
		updateFile(blocs);
		return effort;
	}
	
	public static int pushDownMethod(String superClassName, String method, String className)
	{
		int effort=0;
		String s=new String();
		
		String bloc=new String();
		int numBloc=-1;
		String res="pullUpMethod failed";
		for (int i=0;i<blocs.size();i++)
		{
			if(getClassName((String)blocs.elementAt(i)).equals(superClassName))
			{
				numBloc=i;
				bloc=(String)blocs.elementAt(i);
				res="Attribute pulled Up successfully";
			}
		}
		
		if(numBloc==-1) return 0;
		effort=ApplyRefactoring.moveMethod(superClassName, method, className);
						
		updateFile(blocs);
		return effort;
	}
	
	public static int extractSubClass(String className, String subClassName)
	{
		int effort=0;
				
		effort+=ApplyRefactoring.extractClass(className, subClassName);
		effort+=ApplyRefactoring.addGeneralisation(subClassName,className);
		
		return effort;
	}
	
	public static int extractSuperClass(String className, String superClassName)
	{
		int effort=0;
		String res="Extract superClass";
		
		effort+=ApplyRefactoring.extractClass(className, superClassName);
		effort+=ApplyRefactoring.addGeneralisation(className, superClassName);
		
		return effort;
	}
	
	public static int inlineClass(String class1, String class2)
	{
		int effort=0;
		String [] att2=Info.getAttributesList(class2);
		String [] methods2=Info.getMethodslist(class2);
		String [] relations2=Info.getRelationslist(class2);
		
		int nbAtt2=Info.getNbrAttributes(class2);
		int nbMethos2=Info.getNbrMethods(class2);
		
		String s=new String();
		String res="Inline Class failed";
		
		String [] a=new String[4];
		
		int nbA=Info.getNbrAttributes(class2);
		if(nbA>=1)
		{
			int nb=nbA;
			for(int i=0;i<nb;i++)
			{
				effort+=ApplyRefactoring.moveAttribute(class2, att2[i], class1);
			}
		}
		
		int nbM=Info.getNbrMethods(class2);
		if(nbM>=1)
		{
			int nb=nbM;
			for(int i=0;i<nb;i++)
			{
				effort+=ApplyRefactoring.moveMethod(class2, methods2[i],class1);
			}
		}
		/*
		int nbR=Info.getNbrRelations(class2);
		if(nbR>0)
		{
			for(int i=0;i<nbR;i++)
			{
				String ch=relations2[i];
				ch=ch.replace(class2, class1);
				relations2[i]=ch;
								
				effort+=ApplyRefactoring.moveRelationShip(class1, relations2[i],class2);
			}
		}
		*/
		effort+=ApplyRefactoring.deleteClass(class2);
		int numBloc=0;
		String bloc;
		for (int i=0;i<blocs.size();i++)
		{
			numBloc=i;
			bloc=(String)blocs.elementAt(i);
			while(bloc.contains(class2))
			{
				bloc=bloc.replace(class2, class1);
			}
			blocs.setElementAt(bloc, numBloc);
		}
		
		updateFile(blocs);
		return effort;
	}
	
	public static int inlineMethod(String class1, String method1, String class2, String method2)
	{
		int effort=0;
		String res="inline Method failed";
		int numBloc=-1;
		String bloc=new String();
		
		if(class1.equals(class2))
		{
			for (int i=0;i<blocs.size();i++)
			{
				if(getClassName((String)blocs.elementAt(i)).equals(class1))
				{
					numBloc=i;
					bloc=(String)blocs.elementAt(i);
					res="method inlined successfully";
				}
			}
			if(numBloc==-1) return 0;
			
			numBloc=0;
			effort+=ApplyRefactoring.deleteMethod(class1, method2);
			for (int i=0;i<blocs.size();i++)
			{
				numBloc=i;
				bloc=(String)blocs.elementAt(i);
				while(bloc.contains(method2))
				{
					bloc=bloc.replace(method2, method1);
				}
				blocs.setElementAt(bloc, numBloc);
			}
		}
		else
		{
			
		}
		return effort;
	}
	
	public static void updateFile(Vector v)
	{
		try
		{
			ecriture=new BufferedWriter(new FileWriter("Result.txt"));
			for(int i=0;i<v.size();i++)
			{
				String c=(String)v.elementAt(i);
				for(int j=0;j<c.length();j++)
				{
					if(c.charAt(j)!='\n')
						ecriture.append(c.charAt(j));
					else 
						ecriture.newLine();
				}
			}
			ecriture.close();
			//blocs=readCode();
		}
		 catch(FileNotFoundException e)
		 {
		 	System.out.println("fichier introuvable");
		 }
		 catch(IOException e)
		 {
		 	System.out.println("fichier introuvable");
		 }
	}
	
	public static Vector readCode()
	{
		Vector bloc=new Vector();
		try
		  {
		      lecture=new BufferedReader(new FileReader("GanttProject.blocks"));
		      	      
		      String ch;
		      String b="";
		      		      
		      while((ch=lecture.readLine())!=null)
		      {
		    	  b+=ch+"\n";
		    	  if(ch.startsWith("end"))
		    	  {
		    		  bloc.add(b);
			    	  b="";
		    	  }  
			  }   
		      lecture.close();
			  System.out.println(bloc.toString());
		 }
	    catch(FileNotFoundException e)
	    {
	    	System.out.println("fichier introuvable");
	    }
	    catch(IOException e1)
	    {
	    	System.out.println("Erreur E/S");
	    }
	    saveDefectedCode=blocs;
		return bloc;
	}
	
	public static void saveBestCode(Vector v, String name)
	{
		try
		{
			ecriture=new BufferedWriter(new FileWriter(name));
			for(int i=0;i<v.size();i++)
			{
				String c=(String)v.elementAt(i);
				for(int j=0;j<c.length();j++)
				{
					if(c.charAt(j)!='\n')
						ecriture.append(c.charAt(j));
					else 
						ecriture.newLine();
				}
			}
			ecriture.close();
			//blocs=readCode();
		}
		 catch(FileNotFoundException e)
		 {
		 	System.out.println("fichier introuvable");
		 }
		 catch(IOException e)
		 {
		 	System.out.println("fichier introuvable");
		 }
	}
	
	public static void saveBestSolution(Vector v, String name)
	{
		try
		{
			ecriture=new BufferedWriter(new FileWriter(name));
			
			for(int i=0;i<v.size();i++)
			{
				String [] s=(String[])v.elementAt(i);
				for(int j=0;j<s.length;j++)
				{
					String c=(String) s[j];
					ecriture.append(c+" ");
					/*
					for(int k=0;k<c.length();k++)
					{
						if(c.charAt(k)!='\n')
							ecriture.append(c.charAt(k));
						else 
							ecriture.newLine();
					}
					*/
				}
				ecriture.newLine();
				ecriture.newLine();
			}
			
			ecriture.close();
			//blocs=readCode();
		}
		 catch(FileNotFoundException e)
		 {
		 	System.out.println("fichier introuvable");
		 }
		 catch(IOException e)
		 {
		 	System.out.println("fichier introuvable");
		 }
	}

}
