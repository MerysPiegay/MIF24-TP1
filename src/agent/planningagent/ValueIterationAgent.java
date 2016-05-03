package agent.planningagent;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Random;

import environnement.Action;
import environnement.Etat;
import environnement.MDP;
import environnement.gridworld.ActionGridworld;
import java.util.HashMap;
import java.util.logging.Level;
import java.util.logging.Logger;


/**
 * Cet agent met a jour sa fonction de valeur avec value iteration 
 * et choisit ses actions selon la politique calculee.
 * @author laetitiamatignon
 *
 */
public class ValueIterationAgent extends PlanningValueAgent{
	/**
	 * discount facteur
	 */
	protected double gamma;
	HashMap<Etat, Double> v ;


	
	/**
	 * 
	 * @param gamma
	 * @param mdp
	 */
	public ValueIterationAgent(double gamma,MDP mdp) {
		super(mdp);
		this.gamma = gamma;
		v = new HashMap<>();
                for(Etat e : mdp.getEtatsAccessibles()) {
                    v.put(e, 0.0);
                }
	
	
	}
	
	
	public ValueIterationAgent(MDP mdp) {
		this(0.9,mdp);

	}
	
	/**
	 * 
	 * Mise a jour de V: effectue UNE iteration de value iteration 
	 */
	@Override
	public void updateV(){
		//delta est utilise pour detecter la convergence de l'algorithme
		//lorsque l'on planifie jusqu'a convergence, on arrete les iterations lorsque
		//delta < epsilon 
		this.delta=0.0;
		HashMap<Etat, Double> v_clone = (HashMap<Etat, Double>) v.clone() ;
                for(Etat e : mdp.getEtatsAccessibles()) {
                    double max = 0 ;
                    for(Action a : mdp.getActionsPossibles(e)) {
                        double somme = 0 ;
                        try {
                            Map<Etat,Double> m = mdp.getEtatTransitionProba(e, a);
                            for(Etat e2 : m.keySet()) {
                                somme += m.get(e2) *(mdp.getRecompense(e, a, e2)+gamma*v_clone.get(e));
                            }
                        } catch (Exception ex) {
                            Logger.getLogger(ValueIterationAgent.class.getName()).log(Level.SEVERE, null, ex);
                        }
                        max = Math.max(max, somme) ;
                    }
                    v.put(e, max);
                }
                
                double maxDelta = 0 ;
                for(Etat e : v.keySet()) {
                    Math.abs(v.get(e) - v_clone.get(e));
                    //TODO
                }
		
	
		
		
		// mise a jour vmax et vmin pour affichage du gradient de couleur:
		//vmax est la valeur de max pour tout s de V
		//vmin est la valeur de min pour tout s de V
		// ...
		
		//******************* a laisser a la fin de la methode
		this.notifyObs();
	}
	
	
    /**
     * renvoi l'action executee par l'agent dans l'etat e
     * Si aucune actions possibles, renvoi NONE ou null.
     */
	@Override
	public Action getAction(Etat e) {
		//*** VOTRE CODE
		
	
		return null;
	}
	@Override
	public double getValeur(Etat _e) {
		//*** VOTRE CODE
		
		return 0.0;
	}
	/**
	 * renvoi la (les) action(s) de plus forte(s) valeur(s) dans l'etat e 
	 * (plusieurs actions sont renvoyees si valeurs identiques, liste vide si aucune action n'est possible)
	 */
	@Override
	public List<Action> getPolitique(Etat _e) {
		List<Action> l = new ArrayList<Action>();
		//*** VOTRE CODE
		
		
		return l;
		
	}
	
	@Override
	public void reset() {
		super.reset();
		//*** VOTRE CODE
		
		
		
		
		
		/*-----------------*/
		this.notifyObs();

	}


	@Override
	public void setGamma(double arg0) {
		this.gamma = arg0;
	}

	
}
