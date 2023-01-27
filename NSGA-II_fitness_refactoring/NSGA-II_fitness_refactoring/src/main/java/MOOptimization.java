import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.Vector;


public class MOOptimization 
{

	/**
	 * @param args
	 */
	
	//list of candiate refactoring operations to apply
	public static String[] refactring={"addGeneralisation","deleteGeneralisation",
		"addRelationShip","deleteRelationShip","moveAttribute","moveParameter",
		"moveMethod","extractClass","pullUpAttribute","pullUpMethod","pushDownAttribute",
		"pushDownMethod","extractSubClass","extractSuperClass","inlineClass",
		"inlineMethod"};
	
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
	private static double thresholdSimilarity=0.3;
	
	public static int Effort=0;
	//detectedClasses=Detection.detected;
	
	
	public static void main(String[] args) 
	{
		//ApplyRefactoring.blocs=ApplyRefactoring.readCode();
		int effort=-1;
		Detection.quality();
		SolRef=new Vector();
		code=new Vector();
		
		similarityMatrix=readSimilarityMatrix("GanttSimilarityMatrix3.txt");
		System.out.println(similarityMatrix[292][1]+" "+similarityMatrix[192][1]+" "+similarityMatrix[1][1]);
		
		System.out.println("______________");
		int nbSol=40;
		int nbOp=25;
		//int nbOp=Random.random(1, 120);
		double [] eff=new double[nbSol];
		double [] qual=new double[nbSol];

		int [] corr=new int[nbSol];
		float [] precision=new float[nbSol];
		float [] recall=new float[nbSol];
		
		detectedClasses=new Vector();
		//for(int j=0;j<100;j++)
		for(int i=0;i<nbSol;i++)
		{
			ApplyRefactoring.blocs=new Vector();
			ApplyRefactoring.blocs=ApplyRefactoring.saveDefectedCode;
			
			//ApplyRefactoring.blocs=generateSolutionApplyRefactoring.readCode();
			double [] val=generateSolution(nbOp);
			System.out.println("Solution effort: "+Effort);
			eff[i]=val[0];
			qual[i]=val[1];
			corr[i]=Detection.nbCorrected;
			precision[i]=Detection.corrPrecision;
			recall[i]=Detection.corrRecall;
			
			PopSolRef.add(SolRef);
			detectedClasses.add(Detection.detected);
			
			code.add(ApplyRefactoring.blocs);
			System.out.println("=====================================================");
		}
					
		System.out.println("\n\n#######################################################\n\n");
		
		double [] best=Pareto.bestParetoSol(eff, qual);
		//System.out.println("Pareto: Effort: "+best[0]+" "+best[1]);
		
		/*
		ApplyRefactoring.saveBestCode(bestCode, "bestCode.txt");
		ApplyRefactoring.saveBestSolution(bestRefIter, "bestSolution.txt");
		
		System.out.println("+----------------------------------------------------------------------------------------------+");
		System.out.println("  N  "+"\t | Quality"+"\t | Effort"+"\t | CorrectedClases");//\t | Precision: "+"\t | Recall: ");
		System.out.println("+----------------------------------------------------------------------------------------------+");
		
		for(int i=0;i<nbSol;i++)
		{
			//System.out.println("quality: "+(qual[i]+0.5)+" effort: "+eff[i]);
			System.out.println(" "+i+"\t | "+(qual[i])+"\t | "+eff[i]+"\t | "+corr[i]+"/"+Detection.DefectedClassesExamplesGantt.length);//\t | Precision: "+precision[i]+"\t | Recall: "+recall[i]);
			System.out.println("+----------------------------------------------------------------------------------------------+");
		}
		*/
		System.out.println("\n\n");
		
		for(int i=0;i<nbSol;i++)
		{
			System.out.println(" "+i+"  "+(qual[i])+"  "+eff[i]+"  "+corr[i]+"/"+Detection.DefectedClassesExamplesGantt.length);//+" Precision: "+precision[i]+" Recall: "+recall[i]);
		}
		
		System.out.println("^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^");
		
		for(int i=0;i<nbSol;i++)
		{
			System.out.println(qual[i]+"  "+eff[i]);
		}
		
		ApplyRefactoring.updateFile(ApplyRefactoring.blocs); 
	}
	
	public static double [] generateSolution(int nbOp)
	{
		ApplyRefactoring.blocs=new Vector();
		ApplyRefactoring.blocs=ApplyRefactoring.readCode();
		
		//ApplyRefactoring.blocs=ApplyRefactoring.saveDefectedCode;
		
		int []solution=new int[nbOp];
		double []val=new double[2];
		SolRef=new Vector();
		
		double qual=0;
		Effort=0;
		String op=new String();
		int numRefactoring;
		
		for(int i=0;i<nbOp;i++)
		{
			numRefactoring=Random.random(1, 15);
			solution[i]=numRefactoring;
			System.out.println("____________________________________________________________");
			int eff=executeRefactoring(numRefactoring);
			Effort+=eff;
			SolRef.add(Ref);
			System.out.println("Execute Refactoring: "+refactring[numRefactoring-1]+" \tEffort= "+eff);
		}
		
		val[0]=Effort;
		val[1]=Detection.quality();
		return val;
	}
	
	public static double[] crossover(int[] Sol1, int[] Sol2)
	{
		ApplyRefactoring.blocs=new Vector();
		ApplyRefactoring.blocs=ApplyRefactoring.readCode();
		int min=Math.min(Sol1.length, Sol2.length);
		int max=Math.max(Sol1.length, Sol2.length);
		
		int[] child1=new int [Sol2.length];
		int[] child2=new int [Sol1.length];
		
		//int []solution=new int[nbOp];		
		int pointCut=Random.random(2, min-1);
		
		double []val=new double[2];
		SolRef=new Vector();
		
		double qual=0;
		Effort=0;
		String op=new String();
		int numRefactoring;
		
		child1=Sol1;
		child2=Sol2;
		for(int i=pointCut;i<Sol2.length;i++)
		{
			child1[i]=Sol2[i];
		}
		for(int i=pointCut;i<Sol1.length;i++)
		{
			child2[i]=Sol1[i];
		}
		
		for(int i=0;i<child1.length;i++)
		{
			numRefactoring=child1[i];
			System.out.println("____________________________________________________________");
			int eff=executeRefactoring(numRefactoring);
			Effort+=eff;
			SolRef.add(Ref);
			System.out.println("Execute Refactoring: +++++++++ "+refactring[numRefactoring-1]+" \tEffort= "+eff);
		}
		val[0]=Effort;
		val[1]=Detection.quality();
		return val;
	}
	
	public static double[] mutation(int[] Sol)
	{
		ApplyRefactoring.blocs=new Vector();
		ApplyRefactoring.blocs=ApplyRefactoring.readCode();
		
		int[] child= Sol;
		int nbchanges=Random.random(1, child.length/2);
		int pointChange;
		
		double []val=new double[2];
		SolRef=new Vector();
		
		double qual=0;
		Effort=0;
		String op=new String();
		int numRefactoring;
		
		for(int i=0;i<nbchanges;i++)
		{
			pointChange=Random.random(1, child.length);
			numRefactoring=Random.random(1, 15);
			child[i]=numRefactoring;
		}
				
		for(int i=0;i<child.length;i++)
		{
			numRefactoring=child[i];
			System.out.println("____________________________________________________________");
			int eff=executeRefactoring(numRefactoring);
			Effort+=eff;
			SolRef.add(Ref);
			System.out.println("Execute Refactoring: "+refactring[numRefactoring-1]+" \tEffort= "+eff);
		}
		
		val[0]=Effort;
		val[1]=Detection.quality();
		return val;
	}
	
	public static int executeRefactoring(int num)
	{
		if(num==1)
		{
			return addGeneralisation();
		}
		if(num==2)
		{
			return deleteGeneralisation();
		}
		if(num==3)
		{
			return addRelationShip();
		}
		if(num==4)
		{
			return deleteRelationShip();
		}
		if(num==5)
		{
			return moveAttribute();
		}
		if(num==6)
		{
			return moveParameter();
		}
		if(num==7)
		{
			return moveMethod();
		}
		if(num==8)
		{
			return extractClass();
		}
		if(num==9)
		{
			return pullUpAttribute();
		}
		if(num==10)
		{
			return pullUpMethod();
		}
		if(num==11)
		{
			return pushDownAttribute();
		}
		if(num==12)
		{
			return pushDownMethod();
		}
		if(num==13)
		{
			return extractSubClass();
		}
		if(num==14)
		{
			return extractSuperClass();
		}
		if(num==15)
		{
			return inlineClass();
		}
		
		return 0;
	}
	
	public static int addGeneralisation()
	{
		int effort=0;
		String className=Random.randomClass(Detection.detected);
		String superClass=Random.randomClass();
		
		System.out.println("add Generalisation: "+className+" "+superClass);
		effort=ApplyRefactoring.addGeneralisation(className, superClass);
		
		Ref=new String[3];
		Ref[0]="addGeneralisation()";
		Ref[1]=className;
		Ref[2]=superClass;
		
		return effort;
	}
	
	public static int deleteGeneralisation()
	{
		int effort=0;
		String className=Random.randomClass(Detection.detected);
				
		int i=0;
		while(!Info.hasSuperClass(className))
		{
			className=Random.randomClass(Detection.detected);
			i++;
			if(i==199) return 0;
		}
		
		
		String[] s=Info.getSuperClassesList(className);
		
		int x=Random.random(0, s.length-1);
		if(s.length==1) 
			x=0;
		
		//System.out.println("nbr super class: "+s.length+" "+x);	
		String superClass=(String) s[0];
		System.out.println("delete Generalisation: "+className+" "+superClass);	
		
		effort=ApplyRefactoring.deleteGeneralisation(className, superClass);
		
		Ref=new String[3];
		Ref[0]="deleteGeneralisation()";
		Ref[1]=className;
		Ref[2]=superClass;
		
		return effort;	
	}
	
	public static int addRelationShip()
	{
		int effort=1;
		String class1=Random.randomClass(Detection.detected);
		String class2=Random.randomClass();
		
		int i=0;
		boolean ok=true;
		while(!Info.hasMethod(class1) && ok)
		{
			class1=Random.randomClass(Detection.detected);
			i++;
			if(i==200)
				ok=false;
		}
		if(ok==false) return 0;
		
		while(!Info.hasMethod(class2))
		{
			class2=Random.randomClass();
		}
			
		String method1=Random.randomMethod(class1);
		method1=Info.getNameMethod(method1);
		String method2=Random.randomMethod(class2);
		method2=Info.getNameMethod(method2);
		String line="Relation("+class1+";"+method1+";"+method2+","+class2+","+"type);\n";
		System.out.println("add Relationship: "+class1+" "+line+" "+class2);
		effort=ApplyRefactoring.addRelationShip(class1, line);
		
		Ref=new String[3];
		Ref[0]="addRelationShip()";
		Ref[1]=class1;
		Ref[2]=line;
		
		return effort;
	}
	public static int deleteRelationShip()
	{
		int effort=3;
		String class1=Random.randomClass(Detection.detected);
		
		int i=0;
		boolean ok=true;
		while(!Info.hasRelationShip(class1) && ok)
		{
			class1=Random.randomClass(Detection.detected);
			i++;
			if(i==200)
				ok=false;
		}
		if(ok==false) return 0;
				
		String line=Random.randomRelationship(class1);
		
		effort=ApplyRefactoring.deleteRelationShip(class1, line);
		System.out.println("add Relationship: "+class1+" "+line);
		
		Ref=new String[3];
		Ref[0]="deleteRelationShip()";
		Ref[1]=class1;
		Ref[2]=line;
		
		return effort;
	}
	
	public static int moveParameter()
	{
		int effort=1;
		String class1=Random.randomClass(Detection.detected);
		String class2=Random.randomClass();
		
		while(!Info.hasMethod(class1) || !Info.hasParameter(class1))
			class1=Random.randomClass(Detection.detected);
		
		while(!Info.hasMethod(class2))
			class2=Random.randomClass();
		
		String method1=Random.randomMethod(class1);
		while(!Info.hasParameter(class1, Info.getNameMethod(method1)))
					method1=Random.randomMethod(class1);
				
		String method2=Random.randomMethod(class2);
		
		String parameter=Random.randomParameter(class1, method1);
		System.out.println("move Parameter: "+class1+" "+parameter+" "+class2);
		effort+=ApplyRefactoring.deleteParameter(class1, parameter);
		effort+=ApplyRefactoring.addParameter(class2, parameter);
		
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
		String class1=Random.randomClass(Detection.detected);
		String class2=Random.randomClass();
		String method;
		
		int i=0;
		boolean ok=true;
		while(!Info.hasMethod(class1) && ok)
		{
			class1=Random.randomClass(Detection.detected);
			i++;
			if(i==200)
				ok=false;
		}
		if(ok==false) return 0;
		int exit=0;
		int numClass1=Info.getClassNum(class1);
		int numClass2=Info.getClassNum(class2);
		
		while((class1.equals(class2) || (similarityMatrix[numClass1+1][numClass2+1]<thresholdSimilarity)) && (exit<ApplyRefactoring.blocs.size()-1))
		{
			class2=Random.randomClass();
			numClass2=Info.getClassNum(class2);
			System.out.println("################################ "+similarityMatrix[numClass1+1][numClass2+1]+" "+(numClass1+1)+" "+(numClass2+1));
			exit++;
		}
		
		method=Random.randomMethod(class1);
		System.out.println("move Method: "+class1+" "+method+" "+class2);
		effort+=ApplyRefactoring.moveMethod(class1, method, class2);
		
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
		String class1=Random.randomClass(Detection.detected);
		String class2=Random.randomClass();
		while(!Info.hasAttribute(class1))
			class1=Random.randomClass(Detection.detected);
		
		int exit=0;
		int numClass1=Info.getClassNum(class1);
		int numClass2=Info.getClassNum(class2);
		
		while((class1.equals(class2) || (similarityMatrix[numClass1+1][numClass2+1]<thresholdSimilarity)) && (exit<ApplyRefactoring.blocs.size()-1))
		{
			class2=Random.randomClass();
			numClass2=Info.getClassNum(class2);
			System.out.println("###########_____################ "+similarityMatrix[numClass1+1][numClass2+1]+" "+(numClass1+1)+" "+(numClass2+1));
			exit++;
		}
				
		String attribute=Random.randomAttribute(class1);
		System.out.println("Move Attribute: "+class1+" "+attribute+" "+class2);
		effort+=ApplyRefactoring.moveAttribute(class1, attribute, class2);
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
		String className=Random.randomClass(Detection.detected);
		System.out.println("extract Class: "+className+" "+"new"+className);
		effort=ApplyRefactoring.extractClass(className, "new"+className);
		
		Ref=new String[2];
		Ref[0]="extractClass()";
		Ref[1]=className;
		
		return effort;
	}
	
	public static int pullUpAttribute()
	{
		System.out.println("pullUpAttribute()");
		int effort=0;
		String class1=Random.randomClass(Detection.detected);
		
		int i=0;
		boolean ok=true;
		while((!Info.hasSuperClass(class1) || !Info.hasAttribute(class1)) && ok)
		{
			class1=Random.randomClass(Detection.detected);
			i++;
			if(i==200)
				ok=false;
		}
		if(ok==false) return 0;
		String class2=Random.randomSuperClass(class1);
		String attribute=Random.randomAttribute(class1);
		System.out.println("PullUp Attribute: "+class1+" "+attribute+" "+class2);
		effort=ApplyRefactoring.pullUpAttribute(class1, attribute, class2);
		
		Ref=new String[4];
		Ref[0]="pullUpAttribute()";
		Ref[1]=class1;
		Ref[2]=attribute;
		Ref[3]=class2;
		
		return effort;
	}
	
	public static int pullUpMethod()
	{
		System.out.println("pullUpMethod()");
		int effort=0;
		String class1=Random.randomClass(Detection.detected);
				
		int i=0;
		boolean ok=true;
		while((!Info.hasSuperClass(class1) || !Info.hasMethod(class1)) && ok)
		{
			class1=Random.randomClass(Detection.detected);
			i++;
			if(i==200)
				ok=false;
		}
		if(ok==false) return 0;
		
		String method=Random.randomMethod(class1);
		String class2=Random.randomSuperClass(class1);
		System.out.println("PullUp Method: "+class1+" "+method+" "+class2);
		effort=ApplyRefactoring.pullUpMethod(class1, method, class2);
		
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
		String class1=Random.randomClass(Detection.detected);
		System.out.println("pushDownAttribute()"+"classe: "+class1);
		int i=0;
		while(!Info.hasSubClass(class1) || !Info.hasAttribute(class1))
		{
			class1=Random.randomClass(Detection.detected);
			i++;
			if(i==199) return 0;
		}
		
		String class2=Random.randomSubClass(class1);
		String attribute=Random.randomAttribute(class1);
		System.out.println("pushDown Attribute: "+class1+" "+attribute+" "+class2);
		effort=ApplyRefactoring.pushDownAttribute(class1, attribute, class2);
		
		Ref=new String[4];
		Ref[0]="pushDownAttribute()";
		Ref[1]=class1;
		Ref[2]=attribute;
		Ref[3]=class2;
		
		return effort;
	}
	
	public static int pushDownMethod()
	{
		System.out.println("pushDownMethod()");
		int effort=0;
		String class1=Random.randomClass(Detection.detected);
		
		int i=0;
		while(!Info.hasSubClass(class1) || !Info.hasMethod(class1))
		{
			class1=Random.randomClass(Detection.detected);
			i++;
			if(i==199) return 0;
		}
		
		String class2=Random.randomSubClass(class1);
		String method=Random.randomMethod(class1);
		System.out.println("PushDown Method: "+class1+" "+method+" "+class2);
		effort=ApplyRefactoring.pushDownMethod(class1, method, class2);
		
		Ref=new String[4];
		Ref[0]="pushDownMethod()";
		Ref[1]=class1;
		Ref[2]=method;
		Ref[3]=class2;
		
		return effort;
	}
	
	public static int extractSubClass()
	{
		int effort=0;
		String class1=Random.randomClass(Detection.detected);
		System.out.println("extract subClass: "+class1+" subClass"+class1);
		effort=ApplyRefactoring.extractSubClass(class1, "subClass"+class1);
		
		Ref=new String[2];
		Ref[0]="extractSubClass()";
		Ref[1]=class1;
		
		return effort;
	}
	
	public static int extractSuperClass()
	{
		int effort=0;
		String class1=Random.randomClass(Detection.detected);
		System.out.println("extract superClass: "+class1+" superClass"+class1);
		effort=ApplyRefactoring.extractSuperClass(class1, "superClass"+class1);
		
		Ref=new String[2];
		Ref[0]="extractSuperClass()";
		Ref[1]=class1;
		
		return effort;
	}
	
	public static int inlineClass()
	{
		int effort=0;
		String class1=Random.randomClass(Detection.detected);
		String class2=Random.randomClass();
						
		int exit=0;
		int numClass1=Info.getClassNum(class1);
		int numClass2=Info.getClassNum(class2);
		System.out.println("###########_____################ "+similarityMatrix[numClass1+1][numClass2+1]+" "+(numClass1+1)+" "+(numClass2+1));
		
		while((class1.equals(class2) || (similarityMatrix[numClass1+1][numClass2+1]<thresholdSimilarity)) && (exit<ApplyRefactoring.blocs.size()-1))
		{
			class2=Random.randomClass();
			numClass2=Info.getClassNum(class2);
			System.out.println("###########_____################ "+similarityMatrix[numClass1+1][numClass2+1]+" "+(numClass1+1)+" "+(numClass2+1));
			exit++;
		}
		
		System.out.println("inline Class: "+class1+" "+class2);
		effort=ApplyRefactoring.inlineClass(class2, class1);
		
		for(int i=0;i<Detection.detected.size();i++)
		{
			String ch;
			ch=(String)Detection.detected.elementAt(i);
			if(ch.equals(class2))
			Detection.detected.remove(class2);
		}
		
		Ref=new String[3];
		Ref[0]="inlineClass()";
		Ref[1]=class1;
		Ref[2]=class2;
		
		return effort;
	}
	public static double [][] readSimilarityMatrix(String fileName)
	{
		double [][] res=new double[ApplyRefactoring.blocs.size()+1][ApplyRefactoring.blocs.size()+1];
		int i=0;
		try
		  {
		      readMatrixFile=new BufferedReader(new FileReader(fileName));
		      	      
		      String ch;
		      String b="";
		      		      
		      while((ch=readMatrixFile.readLine())!=null)
		      {
		    	  System.out.println(ch);
		    	  for(int j=0;j<ApplyRefactoring.blocs.size();j++)
		    	  {
		    		  b=ch.substring(0,ch.indexOf('\t'));
			    	  ch=ch.replaceFirst(b, "");
			    	  ch=ch.replaceFirst("\t", "");
			    	  //System.out.println(b);
			    	  if(!b.isEmpty())
			    	  res[i][j]=(double)Double.parseDouble(b);
			    	  else
			    		  res[i][j]=0;
		    	  }
		    	  i++;
			  }   
		      readMatrixFile.close();
		 }
	    catch(FileNotFoundException e)
	    {
	    	System.out.println("fichier introuvable");
	    }
	    catch(IOException e1)
	    {
	    	System.out.println("Erreur E/S");
	    }
		
	    
		return res;
	}
	

}
