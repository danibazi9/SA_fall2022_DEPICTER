import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.Vector;

public class Metrics
{

	/**
	 * @param args
	 */
	
	
	public static void main(String[] args) 
	{
		BufferedReader lecture;
		//BufferedWriter ecriture;
		Vector bloc=new Vector();
		
		int nbr_bloc=0;
		try
		  {
		      lecture=new BufferedReader(new FileReader("1guehene-dpl-GanttProject v1.10.2.blocks"));
		      
	          	      
		      String ch;
		      String b="";
		      int i=0;
		      
		      
		      while((ch=lecture.readLine())!=null)
		      {
		    	  
		    	  //System.out.println(ch);
		    	  b+=ch+"\n";
		    	  if(ch.startsWith("end"))
		    	  {
		    		  //System.out.println("start OK");
		    		  
			    	  bloc.add(b);
			    	  b="";
			    	  nbr_bloc++;
		    	  }
		    		  
			  } 
		      		   
		      lecture.close();
		      //ecriture.close();
		 }
	    catch(FileNotFoundException e)
	    {
	    	System.out.println("fichier introuvable");
	    }
	    catch(IOException e1)
	    {
	    	System.out.println("Erreur E/S");
	    }
	    
	   //--------------------------------------------------------------------
	    /* Metric DSC*/
	    System.out.println("\n--- Metric DSC ");
	    System.out.println("Total number of classes : "+ bloc.size());
	    
	    
	  //--------------------------------------------------------------------
	    /* Metric NOM*/
	    
	    System.out.println("\n--- Metric NOM ");
	    int i=0;
	    String bloc1=new String();
	    
	    while(i<bloc.size())
	    {
	    	int nbMethods=0;
	    	bloc1=bloc.get(i).toString();
	    	for(int j=0;j<bloc1.length()-1;j++)
	    	{
		    	if(bloc1.substring(j).startsWith("Method"))
	    			nbMethods++;
		    }
	    	String cName=new String("");
	    	String s=bloc1.substring(bloc1.indexOf("Class"));
	    	cName=bloc1.substring(bloc1.indexOf("Class")+6, bloc1.indexOf("Class")+s.indexOf(","));
	    	System.out.println("Class "+ (i+1) +" : "+ cName+ " \n\t nb Methods : "+nbMethods+" \n");
	    	i++;
	    	nbMethods=0;
	    }
	    
	    //----------------------------------------------------------------------
	    /* Metric DCC*/
	    System.out.println("\n--- Metric DCC "); 
	    i=0;
	    bloc1=new String();
	    
	    while(i<bloc.size())
	    {
	    	int nbRelations=0;
	    	bloc1=bloc.get(i).toString();
	    	for(int j=0;j<bloc1.length()-1;j++)
	    	{
		    	if(bloc1.substring(j).startsWith("Relation("))
	    			nbRelations++;
		    	
		    }
	    	String cName=new String("");
	    	String s=bloc1.substring(bloc1.indexOf("Class"));
	    	cName=bloc1.substring(bloc1.indexOf("Class")+6, bloc1.indexOf("Class")+s.indexOf(","));
	    	System.out.println("Class "+ (i+1) +" : "+ cName+ " \n\t nb Relations : "+nbRelations+" \n");
	    	i++;
	    	nbRelations=0;
	    }
	    	    
	    //----------------------------------------------------------------------
	    /* Metric DAM*/
	    
	    System.out.println("\n--- Metric DAM ");
	    i=0;
	    
	    bloc1=new String();
	    
	    while(i<bloc.size())
	    {
	    	int nbAttributes=0;
	    	int nbProtectedAttributes=0;
	    	float ratio=0;
	    	bloc1=bloc.get(i).toString();
	    	
	    	for(int j=0;j<bloc1.indexOf("Class");j++)
	    	{
		    	if(bloc1.substring(j).startsWith("Attribute"))
		    		nbAttributes++;
		    	if(bloc1.substring(j).startsWith("private);") || bloc1.substring(j).startsWith("package);"))
		    		nbProtectedAttributes++;
		    }
	    	ratio=(float)nbProtectedAttributes/nbAttributes;
	    	String cName=new String("");
	    	String s=bloc1.substring(bloc1.indexOf("Class"));
	    	cName=bloc1.substring(bloc1.indexOf("Class")+6, bloc1.indexOf("Class")+s.indexOf(","));
	    	System.out.println("Class "+ (i+1) +" :  "+ cName+ "  Ratio = "+ ratio +"\n\t(nbAttributes : "+nbAttributes+ "\tnbProtectedAttributes : "+nbProtectedAttributes+") \n");
	    	i++;
	    	nbAttributes=0;
	    	nbProtectedAttributes=0;
	    }
	    
	    //----------------------------------------------------------------------
	    /* Metric NOP*/
	    System.out.println("\n--- Metric NOP "); 
	    i=0;
	    bloc1=new String();
	    
	    while(i<bloc.size())
	    {
	    	int nbParents=0;
	    	bloc1=bloc.get(i).toString();
	    	String cName=new String("");
	    	String s=bloc1.substring(bloc1.indexOf("Class"));
	    	cName=bloc1.substring(bloc1.indexOf("Class")+6, bloc1.indexOf("Class")+s.indexOf(","));
	    	
	    	for(int j=0;j<bloc1.length()-1;j++)
	    	{
		    	if(bloc1.substring(j).startsWith(("Generalisation(")+cName))
		    		nbParents++;
		    	
		    }
	    	
	    	//cName=bloc1.substring(bloc1.indexOf("Class")+6, bloc1.indexOf("Class")+s.indexOf(","));
	    	System.out.println("Class "+ (i+1) +" : "+ cName+ " \n\t nb Parents = "+nbParents+" \n");
	    	i++;
	    	nbParents=0;
	    }
	    
	    //----------------------------------------------------------------------
	    /* Metric ANA*/
	    System.out.println("\n--- Metric ANA "); 
	    i=0;
	    bloc1=new String();
	    int nbHeritage=0;
	    
	    while(i<bloc.size())
	    {
	    	
	    	bloc1=bloc.get(i).toString();
	    	String cName=new String("");
	    	String s=bloc1.substring(bloc1.indexOf("Class"));
	    	cName=bloc1.substring(bloc1.indexOf("Class")+6, bloc1.indexOf("Class")+s.indexOf(","));
	    	
	    	for(int j=0;j<bloc1.length()-1;j++)
	    	{
		    	if(bloc1.substring(j).startsWith("Generalisation("))
		    		nbHeritage++;
		    }
	    	
	    	//System.out.println("Class "+ (i+1) +" : "+ cName+ " \n\t nb Parents = "+nbParents+" \n");
	    	i++;
	    	//nbHeritage=0;
	    }
	    float ANA=(float) nbHeritage/bloc.size();
	    System.out.println("ANA = "+ANA+"  (nbHeritage = "+nbHeritage+ " nbrClasses = "+ bloc.size()+")");
	    
	    
	}

}
