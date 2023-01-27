import java.io.BufferedWriter;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Vector;

public class Semantic 
{
	int [] tf_idt;
	public static double similarityMatrix [][];
	public static BufferedWriter matrixFile;
	public static Vector save=new Vector();
	
	public static void main(String [] args)
	{
		String bloc=(String)ApplyRefactoring.blocs.elementAt(18);
		//bloc="aaa(jbmjm,jhghj,jhghjg,ghfhg,jhgfj,jhgfhj,kjghhj);a";
		
		Vector v=blocVocabulary(bloc);
		for(int i=0; i<v.size();i++)
		{
			String s=(String)v.elementAt(i);
			System.out.println(s);
		}
		Vector v1=generateVocabulary(blocVocabulary(bloc));
		int [] t=TF_IDF(bloc, v1);
		for(int i=0; i<v1.size();i++)
		{
			String s=(String)v1.elementAt(i);
			System.out.println(s+" "+t[i]);
		}
		
		String bloc2=(String)ApplyRefactoring.blocs.elementAt(40);
		//bloc2="aaa(ali,ouni,jhghjg,aaaa);a";
		//Vector v2=generateVocabulary(blocVocabulary(bloc));
		System.out.println("__________________________________");
		int [] t2=TF_IDF(bloc2, v1);
		for(int i=0; i<v1.size();i++)
		{
			String s=(String)v1.elementAt(i);
			System.out.println(s+" "+t2[i]);
		}
		
		//bloc="aaa(ali,ouni,ali,ali,ouni,ouni,ok,ok,loi);a";
		//bloc2="aaa(abc,def,ali);a";
		
		System.out.println("Similarity "+similarity(bloc,bloc2)+" "+similarity(bloc2,bloc));
		/*
		int a=0;
		double b=0;
		for(int i=50;i<ApplyRefactoring.blocs.size();i++)
		{
			bloc2=(String)ApplyRefactoring.blocs.elementAt(i);
			//double val=(similarity(bloc,bloc2)+similarity(bloc2,bloc))/2;
			double val=similarity(bloc,bloc2);
			System.out.println(i);
			if(b<similarity(bloc,bloc2))
			{
				a=i;
				b=val;
			}
		}
		System.out.println("classe "+a+" similarity "+b);
		*/
		double matrix[][]=generateSimilarityMatrix(ApplyRefactoring.blocs);
		String s=new String();
		s="\t";
		for (int i=1;i<matrix.length+1;i++)
			s+=i+"\t";
		save.add(s);
		for(int i=0;i<matrix.length;i++)
		{
			s=new String(); 
			s=""+(i+1)+"\t";
			for(int j=0;j<matrix.length;j++)
			{
				//java.text.DecimalFormat df = new java.text.DecimalFormat("########.00");
				//s+="0"+df.format(matrix[i][j])+"\t";
				s+=matrix[i][j]+"\t";
				
			}
			save.add(s);
		}
		saveMatrixFile(save, "GanttSimilarityMatrix.txt");
		System.out.println(matrix[2][2]);
	}
	public static double similarity(String class1, String class2)
	{
		double cos=0;
		Vector v1=generateVocabulary(blocVocabulary(class1));
		int t1[]=TF_IDF(class1, v1);
		int t2[]=TF_IDF(class2, v1);
		
		double pScalaire=0;
		double nT1=0;
		double nT2=0;
		for(int i=0;i<v1.size();i++)
		{
			pScalaire+=t1[i]*t2[i];
			nT1+=(t1[i]*t1[i]);
			nT2+=(t2[i]*t2[i]);
		}
		double normeT1=Math.sqrt(nT1);
		double normeT2=Math.sqrt(nT2);
		
		if(normeT1*normeT2==0 || pScalaire==0)
			return 0;
		//cos=Math.cos(pScalaire/(normeT1*normeT2));
		
		cos=(pScalaire/(normeT1*normeT2));
		
		return cos;
	}
	
	public static Vector generateVocabularyAll(Vector v)
	{
		Vector res=new Vector();
				
		for (int i=0;i<v.size();i++)
		{
			String s=(String)v.elementAt(i);
			int a=s.indexOf("(");
			int b=s.indexOf(")");
			int c=s.indexOf(",");
			int d=s.indexOf(";");
			
			while((c>-1) || (d>-1))
			{
				if(c>0 && c<d)
				{
					String ss=s.substring(a+1, c+1);
					String sss=s.substring(a+1, c);
					s=s.replace(ss, "");
					b=s.indexOf(")");
					c=s.indexOf(",");
					d=s.indexOf(";");
					res.add(sss);
				}
				else
				{
					if(d>-1 && d<c)
					{
						String ss=s.substring(a+1, d+1);
						String sss=s.substring(a+1, d);
						s=s.replace(ss, "");
						b=s.indexOf(")");
						c=s.indexOf(",");
						d=s.indexOf(";");
						
						res.add(sss);
					}
				}
				if(c==-1 || d==-1)
				{
					break;
				}
			}
			String ss=s.substring(a+1, b);
			res.add(ss);
				
		}
		return res;
	}
	
	public static Vector generateVocabulary(Vector v)
	{
		Vector res=new Vector();
				
		
		for (int i=0;i<v.size();i++)
		{
			String s=(String)v.elementAt(i);
			int a=s.indexOf("(");
			int b=s.indexOf(")");
			int c=s.indexOf(",");
			int d=s.indexOf(";");
			
			//System.out.println(a+" "+b+" "+c+" "+d);
			while((c>-1) || (d>-1))
			{
				if(c>0 && c<d)
				{
					String ss=s.substring(a+1, c+1);
					String sss=s.substring(a+1, c);
					s=s.replace(ss, "");
					b=s.indexOf(")");
					c=s.indexOf(",");
					d=s.indexOf(";");
					if((!res.contains(sss)) && (!sss.equals("Y")) && (!sss.equals("N")) && (!sss.equals("public")) && (!sss.equals("private")) && (!sss.equals("String")) && (!sss.equals("declaration")))
					{
						if(sss.contains("[")) sss=sss.replace("[", "");
						if(sss.contains("]")) sss=sss.replace("]", "");
						res.add(sss);
					}
				}
				else
				{
					if(d>-1 && d<c)
					{
						String ss=s.substring(a+1, d+1);
						String sss=s.substring(a+1, d);
						s=s.replace(ss, "");
						b=s.indexOf(")");
						c=s.indexOf(",");
						d=s.indexOf(";");
						if((!res.contains(sss)) && (!sss.equals("Y")) && (!sss.equals("N")) && (!sss.equals("public")) && (!sss.equals("private")) && (!sss.equals("String")) && (!sss.equals("declaration")))
						{
							if(sss.contains("[")) sss=sss.replace("[", "");
							if(sss.contains("]")) sss=sss.replace("]", "");
							res.add(sss);
						}
					}
				}
				if(c==-1 || d==-1)
				{
					break;
				}
			}
			String ss=s.substring(a+1, b);
			if((!res.contains(ss)) && (!ss.equals("Y")) && (!ss.equals("N")) && (!ss.equals("public")) && (!ss.equals("private")) && (!ss.equals("String")) && (!ss.equals("declaration")))
			{
				if(ss.contains("[")) ss=ss.replace("[", "");
				if(ss.contains("]")) ss=ss.replace("]", "");
				res.add(ss);
			}
		}
		return res;
	}
	public static Vector blocVocabulary(String s)
	{
		Vector v=new Vector();
		
		String ss;
		while(s.contains("("))
		{
			ss=s.substring(s.indexOf("(")-2, s.indexOf(");")+2);
			v.add(ss);
			s=s.replace(ss, "_");	
		}
	return v;
	}
	
	public static int [] TF_IDF(String bloc, Vector v) //throws  java.util.regex.PatternSyntaxException
	{
		int []res=new int[v.size()];
		//Vector v_all=generateVocabularyAll(v);
		
		String copie=bloc;
		String s;
		//try
		{
		for(int i=0;i<v.size();i++)
		{
			res[i]=0;
			s=(String)v.elementAt(i);
			
			while(bloc.contains(s+","))
			{
				bloc=bloc.replaceFirst(s+",","");
				res[i]++;
			}
			while(bloc.contains(s+";"))
			{
				bloc=bloc.replaceFirst(s+";","");
				res[i]++;
			}
			while(bloc.contains(","+s+");"))
			{
				bloc=bloc.replaceFirst(","+s,"");
				res[i]++;
			}
			bloc=copie;
		}
		}
		//catch(java.util.regex.PatternSyntaxException e)
		{
			//System.out.println("________Exception________"+e.getDescription());
		}
				
		return res;
	}
	
	public static double [][] generateSimilarityMatrix(Vector blocs)
	{
		double res [][] = new double[blocs.size()][blocs.size()];
		String class1;
		String class2;
				
		for(int i=0;i<ApplyRefactoring.blocs.size();i++)
		{
			class1=(String)ApplyRefactoring.blocs.elementAt(i);
			System.out.println("ligne "+i);
			for(int j=0;j<ApplyRefactoring.blocs.size();j++)
			{
				class2=(String)ApplyRefactoring.blocs.elementAt(j);
				//double val=(similarity(class1,class2)+similarity(class2,class1))/2;
				double val=similarity(class1,class2);
								
				res[i][j]=val;
				java.text.DecimalFormat df = new java.text.DecimalFormat("########.00");
				//System.out.print(i+" "+j+" "+df.format(val));
				System.out.print(" "+df.format(val));
			}
			System.out.println("");
		}
		
		return res;
	}
	
	public static void saveMatrixFile(Vector v, String name)
	{
		try
		{
			matrixFile=new BufferedWriter(new FileWriter(name));
			
			for(int i=0;i<v.size();i++)
			{
				String s=(String)v.elementAt(i);
				matrixFile.append(s+" ");
				matrixFile.newLine();
			}
			
			matrixFile.close();
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
