import java.util.Vector;


public class Detection
{
	static int nbCorrected=0;
	static float corrPrecision=0;
	static float corrRecall=0;
	static float corrQuality=0;
	
	static String []ruleBlob={"NOC<7", "NOM>24", "NOA>26"};//{"LCOM>50", "NOA>34", "NOM>20","CBO>1"};//new String[sizeBlob]; 
	static String [] ruleSC={"NOA<11","WMC<9","CBO>20","NOP>6","WMC<23","NOM>20"};
	static String [] ruleFD={"NOA>26","NOC<3","WMC>23", "NOM<6"};

	static String [] DefectedClassesExamplesGantt={"GanttGraphicArea", "GanttOptions", "GanttProject blob",
				"GanttTree", "ResourceLoadGraphicArea", "TaskImpl", "GanttTaskPropertiesBean",
				"CSVSettingsPanel", "ColorConvertion", "GregorianTimeUnitStack", "ColorValueParser",
				"DialogAligner", "GanttCalendarDays", "GanttPrintable", "PDFExportProcessor",
				"OpenFileDialog", "NewProjectWizard", "GanttTXTOpen", "AdjustTaskBoundsAlgorithm",
				"FindPossibleDependeesAlgorithmImpl", "RecalculateTaskCompletionPercentageAlgorithm",
				"MonthTextFormatter", "TaskHierarchyManagerImpl", "GraphicPrimitiveContainer",
				"GanttApplet", "OpenFileDialog", "WizardImpl", "CommonPanel", "TaskListenerAdapter",
				"ResourceAction", "AbstractDocument", "AbstractURLDocument"};
	
	static String [] detec={"CSVSettingsPanel", "GanttResourcePanel", "RangeSearchFromKeyImpl",
		"GanttCalendar", "WizardPage", "GanttTaskPropertiesBean", "GanttTaskPropertiesBean",
		"ResourceAssignmentCollectionImpl", "GanttLanguage", "GanttLanguage", "GanttOptions",
		"GanttOptions", "GregorianTimeUnitStack", "GregorianTimeUnitStack", "Task", "ShapePaint",
		"GanttXFIGSaver", "ResourceLoadGraphicArea", "ResourceLoadGraphicArea", "TaskImpl",
		"GanttGraphicArea", "GanttGraphicArea", "TimeUnit", "GanttDialogInfo", "GanttProject",
		"GanttProject"};

	
	public static Vector detected=new Vector();
	public static Vector detectedBlocs=new Vector();
	
	public static void main(String [] args)
	{
		//System.out.println(getNum("CBO>20"));
		System.out.println("nb detected classes: "+" "+quality());
		
		System.out.println("Recall: "+Recall(DefectedClassesExamplesGantt,detec));
		System.out.println("Precision: "+Precision(DefectedClassesExamplesGantt,detec));
	}
	public static float quality()
	{
		float res=1;
		int nbClass=ApplyRefactoring.blocs.size();
		int nbdetected=0;
		int [][] metric=Metric.getValues();
		String []Class=new String[nbClass];
		detected=new Vector();
		for(int i=0;i<ApplyRefactoring.blocs.size();i++)
		{
			String bloc=(String)ApplyRefactoring.blocs.elementAt(i);
			Class[i]=Info.getClassName(bloc);
		}
		
		for(int i=0;i<nbClass;i++)
		{
			String bloc=(String)ApplyRefactoring.blocs.elementAt(i);
			Class[i]=Info.getClassName(bloc);
			
			boolean ok=true;
			for(int j=0;j<ruleBlob.length;j++)
			{
				ok=true;
				ok=ok && rule(metric[i][getNum(ruleBlob[j])],ruleBlob[j]);
				//System.out.print(rule(metric[i][getNum(ruleBlob[j])],ruleBlob[j])+" ");
				//System.out.println(metric[i][getNum(ruleBlob[j])]+" "+ruleBlob[j]+" "+getNum(ruleBlob[j]));
			}
			
			if(ok==true)
			{
				detected.add(Class[i]);
				nbdetected++;			
			}
			
			ok=true;
			for(int j=0;j<ruleSC.length;j++)
			{
				ok=true;
				ok=ok && rule(metric[i][getNum(ruleSC[j])],ruleSC[j]);
			}
			
			if(ok==true)
			{
				detected.add(Class[i]);
				nbdetected++;
			}
			
			ok=true;
			for(int j=0;j<ruleFD.length;j++)
			{
				ok=true;
				ok=ok && rule(metric[i][getNum(ruleFD[j])],ruleFD[j]);
			}
			
			if(ok==true)
			{
				detected.add(Class[i]);
				nbdetected++;
			}
			
			MOOptimization.bestCodeDetectedClasses.add(detected);
		}
		String [] ddet=new String[detected.size()];
		
		for(int i=0;i<detected.size();i++)
		{
			System.out.println((String)detected.elementAt(i));
			ddet[i]=(String)detected.elementAt(i);
			detectedBlocs.add(Info.getBloc((String)detected.elementAt(i)));
		}
		
		corrPrecision=1-Precision(DefectedClassesExamplesGantt,ddet);
		corrRecall=1-Recall(DefectedClassesExamplesGantt,ddet);
		res=1-Precision(DefectedClassesExamplesGantt,ddet);
		float a=nbdetected;
		float b=nbClass;
		res=(float) 1-(a/(b*3));
		return res;
	}
	
	public static float Recall(String[] examples, String [] det)
	{
		float res=0;
		int dd=0;
		int dc=examples.length;
		
		for(int i=0;i<examples.length;i++)
		{
			for(int j=0;j<det.length;j++)
			{
				if(examples[i].equals(det[j]))
					dd++;
			}
		}
		
		res=(float) dd/dc;
		return res;
	}
	
	public static float Precision(String[] examples, String [] det)
	{
		float res=0;
		int dd=0;
		int de=det.length;
		
		for(int i=0;i<examples.length;i++)
		{
			for(int j=0;j<det.length;j++)
			{
				if(examples[i].equals(det[j]))
					dd++;
			}
		}
		nbCorrected=examples.length-de;
		res=(float) dd/de;
		return res;
	}
	
	public static String [] getDetectedClasses()
	{
		if(detected.isEmpty())
			quality();
		int nb=detected.size();
		String res[]=new String[nb];
		String ch=new String();
		for(int i=0;i<nb;i++)
		{
			ch=(String)detected.elementAt(i);
			res[i]=ch;
		}
		//detected=res;
		return res;
	}

	
	public static boolean rule(int val, String rul)
	{
		boolean res=false;
		String supinf;
		if(rul.contains(">"))
			supinf=">";
		else supinf="<";
		String metric=rul.substring(0, rul.indexOf(supinf));
		int threshold=Integer.parseInt(rul.substring(rul.indexOf(supinf)+1));
		//System.out.println(metric+supinf+threshold);
		
		if(supinf==">" && val>threshold)
			return true;
		
		if(supinf=="<" && val<threshold)
			return true;
				
		return false;
	}
	
	public static int getNum(String rul)
	{
		int num=0;
		String supinf;
		if(rul.contains(">"))
			supinf=">";
		else supinf="<";
		//System.out.println("num: "+rul.indexOf(supinf));
		String m=rul.substring(0, rul.indexOf(supinf));
		//System.out.println(m);
		if(m.equals("NOM")) return num=0;
		if(m.equals("NOA")) return num=1;
		if(m.equals("WMC")) return num=2;
		if(m.equals("LCOM")) return num=3;
		if(m.equals("CBO")) return num=4;
		if(m.equals("CBO")) return num=5;
		if(m.equals("NOC")) return num=6;
		
		
		return num;
		
	}

}
