import java.io.BufferedWriter;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;


public class Metric
{
	public static BufferedWriter ecriture;
	
	public static void main(String [] args)
	{
		int nbClass=Info.getNbrClass();
		int [][] tab=getValues();
		for(int i=0;i<nbClass;i++)
			{
				for(int j=0;j<7;j++)
					System.out.print(tab[i][j]+" ");
				System.out.println("");
			}
				
		String ch=new String();
		String bloc=new String();
		
		
		String [] base=new String[nbClass];
		
		for(int i=0;i<nbClass;i++)
		{
			bloc=(String)ApplyRefactoring.blocs.elementAt(i);
			String className=Info.getClassName(bloc);
			ch=className+" ";
			ch+=NOM(className)+" "+NOA(className)+" "+WMC(className)+" "+LCOM(className);
			ch+=" "+CBO(className)+" "+NOP(className)+" "+NOC(className);
			base[i]=ch;
		}
		
		generateSystem(base, "SystemeAEvaluer.txt");
		
		for(int i=0;i<100;i++)
		{
			int x=Random.random(70, 98);
			int y=Random.random(8, 40);
			float a=(float)x/100;
			System.out.println(a+" "+y);
		}
		//System.out.println(+" "+);
		
		//ApplyRefactoring.blocs=ApplyRefactoring.readCode();
		
	}
	
	public static int[][] getValues()
	{
		String ch=new String();
		String bloc=new String();
		int []val=new int[7];
		int [][]res=new int [ApplyRefactoring.blocs.size()][7];
		
		int nbClass=Info.getNbrClass();
				
		for(int i=0;i<nbClass;i++)
		{
			bloc=(String)ApplyRefactoring.blocs.elementAt(i);
			String className=Info.getClassName(bloc);
			res[i][0]=Metric.NOM(className);
			res[i][1]=Metric.NOA(className);
			res[i][2]=Metric.WMC(className);
			res[i][3]=Metric.LCOM(className);
			res[i][4]=Metric.CBO(className);
			res[i][5]=Metric.NOP(className);
			res[i][6]=Metric.NOC(className);
			
		}
		
		return res;
		//generateSystem(base, "SystemeAEvaluer.txt");
	}
	
	public static int NOM(String className) // Number of methods
	{
		int val=0;
		val=Info.getNbrMethods(className);
		
		return val;
	}
	
	public static int NOA(String className) // Number of attributes
	{
		int val=0;
		val=Info.getNbrAttributes(className);
		
		return val;
	}
	
	public static int SIZE(String className) // Number of methods and attributes
	{
		int val=0;
		val=NOM(className)+NOA(className);
		
		return val;
	}
	
	public static int CBO(String className) // Coupling Between Objects 
	{
		int val=0;
		val=Info.getNbrRelations(className);
		
		return val;
	}
	
	public static float DAM(String className)
	{
		float res=0;
		int nbAtt=Info.getNbrAttributes(className);
		int nbProtected=0;
		String []att=Info.getAttributesList(className);
		
		for(int i=0;i<nbAtt;i++)
		{
			if(att[i].contains("private);") || att[i].contains("package);"))
				nbProtected++;
		}
		res=nbProtected/nbAtt;
		return res;
	}
	
	public static int NOP(String className) // Number of Parents
	{
		int res=0;
		if(Info.hasSuperClass(className))
			res=Info.getNbrSuperClasses(className);
		
		return res;
	}
	
	public static int NOC(String className) // Number of Childrens
	{
		int res=0;
		if(Info.hasSubClass(className))
			res=Info.getClassNbrSubClasses(className);
		
		return res;
	}
	
	public static int DIT(String className) // Depth of Inheritance Tree
	{
		int max=-1;
		int h=0;
		try
		{
			if(Info.hasSuperClass(className))
			{
				int nb=Info.getNbrSuperClasses(className);
				String [] superClass=Info.getSuperClassesList(className); 	
				for(int i=0;i<nb;i++)
				{
					h=DIT(superClass[i]);
					if(h>max)
						max=h;
				}
			}
		}
		catch(ArrayIndexOutOfBoundsException e)
		{
			System.out.println(e);
		}
		return max+1;
	}
	
	public static int LCOM(String className)
	{
		int res=0;
		String bloc=Info.getBloc(className);
		for(int i=0;i<bloc.length();i++)
		{
			if(bloc.charAt(i)=='\n')
				res++;	
		}
		return res;
	}
	
	public static int WMC(String className)
	{
		float res=0;
		int methods=Info.getNbrMethods(className);
		String bloc=Info.getBloc(className);
		int parameters=0;
		while(bloc.contains("Parameter("))
		{
			parameters++;
			bloc=bloc.replaceFirst("Parameter", "__");
		}
		if(methods<=1)
			return 0;
		else
		res=parameters/(methods/2);
		int a=(int)res;
		return a;		
	}
	
	
	public static void generateSystem(String [] ch, String name)
	{
		try
		{
			ecriture=new BufferedWriter(new FileWriter(name));
			for(int i=0;i<ch.length;i++)
			{
				String c=ch[i];
				ecriture.append(ch[i]);
				ecriture.newLine();
			}
			ecriture.close();
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
