
import java.math.BigInteger;
import java.util.ArrayList;

/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author mwm
 */
public class Hyperplane 
{
    static double spacing;
    static int objectives_number;
    static int height;
    static double p ;
    
    ArrayList<Double> initial ;
    
    static ArrayList<ArrayList<Double>> results ;
    static ArrayList<ArrayList<Double>> ref_points ;
    static ArrayList<ArrayList<Integer>> associated_solutions ;
    
    static int leaves;
    
    Hyperplane()
    {
        objectives_number = 3 ;
        height = 0 ;
        p = 4.0;
        spacing = 1.0/p;
        
        this.initial = new ArrayList<Double>();
        results = new ArrayList<ArrayList<Double>>();
        ref_points = new ArrayList<ArrayList<Double>>();
        associated_solutions =new ArrayList<ArrayList<Integer>>();
        double temp = 0 ;
        
        do
        {
            initial.add(temp);
            temp += spacing ;
        }while(temp <= 1);
        
        leaves = 0 ;
        
    }
    Hyperplane(int objectives_number, int division)
    {
        this.objectives_number = objectives_number ;
        height = 0 ;
        p = division;
        spacing = 1.0/p;
        
        this.initial = new ArrayList<Double>();
        results = new ArrayList<ArrayList<Double>>();
        ref_points = new ArrayList<ArrayList<Double>>();
        associated_solutions =new ArrayList<ArrayList<Integer>>();
        double temp = 0 ;
        
        do
        {
            initial.add(temp);
            temp += spacing ;
        }while(temp <= 1);
        
        leaves = 0 ;
        
    }
    
    Hyperplane(Execution e)
    {
        this.objectives_number = e.objectives_number ;
        height = 0 ;
        p = e.division;
        spacing = 1.0/p;
        
        this.initial = new ArrayList<Double>();
        results = new ArrayList<ArrayList<Double>>();
        ref_points = new ArrayList<ArrayList<Double>>();
        associated_solutions =new ArrayList<ArrayList<Integer>>();
        double temp = 0 ;
        
        do
        {
            initial.add(temp);
            temp += spacing ;
        }while(temp <= 1);
        
        leaves = 0 ;
        
    }
    
    static BigInteger binomial(final int N, final int K) 
    {
    BigInteger ret = BigInteger.ONE;
    for (int k = 0; k < K; k++) 
    {
        ret = ret.multiply(BigInteger.valueOf(N-k)).divide(BigInteger.valueOf(k+1));
    }
    return ret;
    }
    
    void print_initial_values()
    {
        System.out.print(" initial values : ");
        for(int i =0; i<initial.size();i++)
        {
            System.out.print(initial.get(i)+" ");
        }
        System.out.print("\n");
    }
    
    void create_hyperplane()
    {
        int initial_size = initial.size();
        int current_level = 0 ;
        ArrayList<Double> line = new ArrayList<Double>();
        ArrayList<Double> temp = new ArrayList(initial) ;
        
        //BigInteger exprected_results_size = binomial( ( objectives_number+(int)p - 1 ) , (int)p );
        //System.out.print("\n exprected reference points number is : "+exprected_results_size);
        
        for(int i=0 ; i < initial_size ; i++ )
        {
            create_subtree(temp,line,current_level);
            if ( temp.size() > 0 ) temp.remove(0);
        }
        this.clean_results();
        //System.out.println(" and the actual reference points number is : "+ref_points.size());
        
    }
    // only used in create_hyperplance
    void create_subtree(ArrayList<Double> temp2, ArrayList<Double> line, int upper_level)
    {
        int current_level = upper_level +1 ;
        
        ArrayList<Double> temp = new ArrayList<Double>(temp2);
        
        ArrayList<Double> line_result = new ArrayList(line) ;
        
        // we are on the leaf-1 level
        if (current_level == objectives_number - 1)
        {
            
            
            if(temp.size() == 0)
            {
                //System.out.println(" size empty adding 0");
                line_result.add(0.0);
            }
            else
            {
                //System.out.println(" one still, adding its value");
                leaves++;
                line_result.add(temp.get(0));
            }
            
            Double line_sum = 0.0 ;
                
            for(int i=0 ; i < line_result.size() ; i++ )
            {
                line_sum += line_result.get(i);
            }
            // adding the last value
            line_result.add(1-line_sum);
            
            results.add(line_result);
        }
        // we are on intermidiate level, so we need to know how much recuurrences to start
        else
        {           
            if(temp.size() == 0)
            {
                //System.out.println(" intermediate level with size 0");
                line_result.add(0.0);
                create_subtree(temp,line_result,current_level);
            }
            else
            {
                //System.out.println(" normal intermidiate level");
                
                Double recurrences_number = (1 - temp.get(0))/spacing + 1 ;
                
                line_result.add(temp.get(0));
                
                ArrayList<Double> temp3 = new ArrayList<Double>(initial);
            
                for(int i=0 ; i < recurrences_number ; i++ )
                {
                    create_subtree(temp3,line_result,current_level);
                    if ( temp3.size() > 0 ) temp3.remove(0);
                }
            }
            
        }
    }
    // only used in create_hyperplance
    void clean_results()
    {
        ArrayList<Integer> keep_list = new ArrayList<Integer>();
        
        for(int i=0 ; i < results.size() ; i++ )
        {
            boolean keep_this = true;
            
            for(int j=0 ; j < results.get(i).size() ; j++ )
            {
                if(results.get(i).get(j)<0) keep_this = false;
            }
            if(keep_this == true) keep_list.add(i);
        }
        for(int i=0 ; i < keep_list.size() ; i++ )
        {
            ref_points.add(results.get(keep_list.get(i)));
        }
        for(int i=0 ; i < ref_points.size() ; i++ )
        {
            associated_solutions.add(new ArrayList<Integer>());
        }
    }
    
    void print_results()
    {
        System.out.println(" created reference points : ");
        for(int i=0 ; i < results.size() ; i++ )
        {
            System.out.print(" Ref Point "+(i+1)+" : "); 
            for(int j=0 ; j < results.get(i).size() ; j++ )
            {
                System.out.print(results.get(i).get(j)+", ");
            }
            System.out.print("\n");
        }
    }
    
    void print_ref_points()
    {
        System.out.println(" created reference points : ");
        for(int i=0 ; i < ref_points.size() ; i++ )
        {
            System.out.print(" Ref Point "+i+" : "); 
            for(int j=0 ; j < ref_points.get(i).size() ; j++ )
            {
                System.out.print(ref_points.get(i).get(j)+", ");
            }
            System.out.print("\n");
        }
    }
    
    void print_ref_points_associations()
    {
        System.out.println(" created reference points : ");
        for(int i=0 ; i < associated_solutions.size() ; i++ )
        {
            System.out.print(" Ref Point "+i+" : is associated to : "); 
            for(int j=0 ; j < associated_solutions.get(i).size() ; j++ )
            {
                System.out.print(associated_solutions.get(i).get(j)+", ");
            }
            System.out.print("\n");
        }
    }
    
    ArrayList<Solution> calculate_normal_distance(ArrayList<Solution> front)
    {
        ArrayList<Solution> updated_front = new ArrayList<Solution>(front);
        
        for(int i=0 ; i < updated_front.size() ; i++ )
        {
            updated_front.get(i).ref_lines_distance = new ArrayList<Double>();
            
            for(int j=0 ; j < ref_points.size() ; j++ )
            {
                Double dot_product = 0.0 ;
                
                for(int k=0 ; k < updated_front.get(i).objectives.size() ; k++ )
                {
                    dot_product += updated_front.get(i).objectives.get(k) * ref_points.get(j).get(k);
                }
                Double quotion = dot_product / (updated_front.get(i).objectives.size() * ref_points.get(j).size());
                
                Double temporary_result = (double)updated_front.get(i).objectives.size() * Math.sqrt( 1 - Math.pow(quotion,2) );
                        
                updated_front.get(i).ref_lines_distance.add(temporary_result);
                
            }
            // associate the solution i to a reference point
            int min_distance_index = 0 ;
      
            for(int j=1;j<updated_front.get(i).ref_lines_distance.size();j++)
            {
                if(updated_front.get(i).ref_lines_distance.get(min_distance_index) > updated_front.get(i).ref_lines_distance.get(j)) min_distance_index = j;
            }
            updated_front.get(i).associated_ref_point_index = min_distance_index ;
            associated_solutions.get(min_distance_index).add(i);
        }
        return updated_front ;
    }
    
    ArrayList<Solution> niching(ArrayList<Solution> front, int needed_solutions)
    {
        ArrayList<Solution> next_generation = new ArrayList<Solution>();
        // first of all make sure the current front is larger than the needed solutions
        if (front.size() < needed_solutions)
        {
            System.out.println(" ---------- ERROR! the front is smaller than the needed elements for next generation ---------- ");
            System.exit(0);
        }
        else
        {
            int selected_elements = 0 ;
            do
            {
                // determine the min index in the front rather than 0
                int min_index = Integer.MAX_VALUE ;
                boolean has_smaller_value = false ;
                
                for(int i=0 ; i < associated_solutions.size() ; i++ )
                {
                    if( (associated_solutions.get(i).size() > 0) && (associated_solutions.get(i).size() <= min_index))
                    {
                        has_smaller_value = true ;
                        min_index = associated_solutions.get(i).size();
                    }
                }
                for(int i=0 ; i < associated_solutions.size() ; i++ )
                {
                    if((associated_solutions.get(i).size() == min_index)&&(selected_elements < needed_solutions))
                    {
                        next_generation.add(front.get(associated_solutions.get(i).get(0)));
                        selected_elements++;
                        associated_solutions.get(i).remove(0);
                    }
                }
                
            }while(selected_elements < needed_solutions);
        }
        return next_generation;
    }
    
    public static void main(String[] args) 
    {
        Execution e = new Execution();
        
        Hyperplane h = new Hyperplane();
        
        h.print_initial_values();
        
        BigInteger exprected_results_size = binomial( ( objectives_number+(int)h.p - 1 ) , (int)h.p );
        
        System.out.print(" exprected ref points : "+exprected_results_size);
        
        h.create_hyperplane();
        
        System.out.println(" created ref points : "+ref_points.size());
        
        h.print_ref_points();
        
        System.out.println(" associated_points list size : "+associated_solutions.size());
        
        // now testing with population
        
        Population p = new Population(Execution.population_size,Execution.s,Execution.ref,Execution.generations); 
        ArrayList<Solution> parents = new ArrayList<Solution>();
        
        p.create_poplulation();
        System.out.println("Population created");
        parents = p.random_selection();
        System.out.println("Evaluation process started...");
        
        
        for(int i=0;i<parents.size();i++)
        {
            p.solutions.add(new Solution(parents.get(i)));
        }
        // re-evaluate the population with the offsprings
        p.evaluate_poplulation();
        System.out.println("Evaluation current population + offsprings Evaluation... done");
        System.out.print("\n --- starting new distance calculation...");
        parents = h.calculate_normal_distance(p.solutions);
        System.out.println("done!");
        System.out.println("printing current population associations with reference points");
        for(int i=0;i<parents.size();i++)
        {
            parents.get(i).print_solution_reference_distances(i);
        }
        h.print_ref_points_associations();
        
    }
    
    
    
}
