import java.util.ArrayList;

public class Execution{
    //--- User parameters ---
    // these values represents the chosen reference point and sigma value
    
    static double aspiration_values[] = {0.5,0.5,0.5,0.5,0.5,0.5,0.5,0.5,0.5,0.5,0.5};
    static ReferencePoint ref = new ReferencePoint(aspiration_values) ;
    
    static boolean coupling_metric = true;
    static boolean cohesion_metric= true;
    static boolean complexity_metric= true;
    static boolean stability_metric= true;
    static boolean deviance_metric= true;
    static boolean encapsulation_metric= true;
    static boolean interfacing_metric= true;

    static Sigma s = new Sigma(1);
    static int objectives_number = 7 ;
    static int generations = 2 ;
    static int population_size = 50 ;
    static int division = 3;
    
    public static void main(String[] args){
        Population p = new Population(population_size,s,ref,generations); 
        ArrayList<Solution> parents = new ArrayList<>();
        p.create_poplulation();
        for(int i=0; i<generations; i++){
            p.update_sigma_value(i, generations);
            parents = p.random_selection();
            p.generate_next_popluation(parents,i);
            p.print_popluation_metrics(i);
        }
    }
}


