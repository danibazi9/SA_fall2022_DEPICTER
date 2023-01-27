import java.util.Vector;


public class Pareto 
{
	public static void main(String[] args)
	{
		double [] effort= new double[25];
		double [] quality=new double [25];
		
		Vector v=pareto(effort, quality);
		Vector det=new Vector();
		double [] best_eff=(double [])v.elementAt(0);
		double [] best_qual=(double [])v.elementAt(1);
		
		System.out.println("Pareto Sol: "+best_eff[0]+" "+best_qual[1]);
		
		double res[]=bestParetoSol(best_eff, best_qual);
		System.out.println("best Sol: "+res[0]+" "+res[1]);
		
	}
	
	public static Vector pareto(double [] effort, double [] quality)
	{
		Vector res=new Vector();
		boolean dominated=false;
		
		double []eff=effort;
		double []qual=quality;
		double []temp_eff=new double[25];
		double []temp_qual=new double[25];
		
		int x=0;
		for(int i=0;i<effort.length;i++)
		{	
			dominated=false;
			for(int j=0;j<effort.length;j++)
			{
				if(eff[i]>effort[j] && qual[i]<qual[j])
				{
					dominated=true;
					break;
				}
			}
			if(dominated==false)
			{
				temp_eff[x]=eff[i];
				temp_qual[x]=qual[i];
				x++;
			}
		}
		double [] b_eff=new double [x];
		double [] b_qual=new double [x];
		for(int i=0;i<x;i++)
		{
			b_eff[i]=temp_eff[i];
			b_qual[i]=temp_qual[i];
		}
		
		res.add(b_eff);
		res.add(b_qual);
		
		return res;
	}
	
	public static double [] bestParetoSol(double [] effort, double [] quality)
	{
		double res[]=new double [2];
		double best=10000;
		double best_eff;
		double best_qual;
		for(int i=0;i<effort.length;i++)
		{
			//System.out.println("Val: "+Math.sqrt(((1-quality[i])*(1-quality[i]))+(effort[i]*effort[i])));
			double val=Math.sqrt(((1-quality[i])*(1-quality[i]))+(effort[i]*effort[i]));
			if(val<best && val>0)
			{
				best=Math.sqrt(((1-quality[i])*(1-quality[i]))+(effort[i]*effort[i]));
				res[0]=effort[i];
				res[1]=quality[i];
				MOOptimization.bestRefIter=new Vector();
				MOOptimization.bestRefIter=(Vector)MOOptimization.PopSolRef.elementAt(i);
				MOOptimization.bestCode=(Vector)MOOptimization.code.elementAt(i);
				MOOptimization.bestCodeDetectedClasses=(Vector)MOOptimization.detectedClasses.elementAt(i);
				
			}
		}
		System.out.println("Pareto best solution: "+res[1]+" "+res[0]);
		String [] s=new String[4];
		System.out.println("+++++++++++++++++++++++++++++++++++\nOptimal Solution:\n");
		
		for(int i=0;i<MOOptimization.bestRefIter.size();i++)
		{
			s=(String[])MOOptimization.bestRefIter.elementAt(i);
			for(int j=0;j<s.length;j++)
			{
				System.out.print(s[j]+" ");
			}
			System.out.println("");
		}
		System.out.println("\n+++++++++++++++++++++++++++++++++++");
		for(int i=0;i<MOOptimization.bestCodeDetectedClasses.size();i++)
		{
			String ch=(String)MOOptimization.bestCodeDetectedClasses.elementAt(i);
			//for(int j=0;j<size();j++)
			{
				System.out.print(ch+" ");
			}
			System.out.println("");
		}
		System.out.println("\n+++++++++++++++++++++++++++++++++++");
		return res;
	}
	

}
