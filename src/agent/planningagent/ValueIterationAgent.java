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
	Map<Etat, Double> v ;


	
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
            
            Double vmaxi = 0., vmini=0., current, vmaxi_max = 0., vmini_min = 0.;
            Map<Etat, Double> proba;
            Map<Etat, Double> tmp = new HashMap<>();
            try {
                for(Etat e : getMdp().getEtatsAccessibles()){
                    tmp.put(e, 0.);
                    if(!getMdp().estAbsorbant(e)){
                        for(Action a : getMdp().getActionsPossibles(e)){
                            current = 0.;
                            proba = getMdp().getEtatTransitionProba(e, a);
                            for(Etat s : proba.keySet()){
                                current = current + (proba.get(s) * (getMdp().getRecompense(e, a, s) + (gamma*v.get(s))));
                            }
                            if(current > vmaxi) vmaxi = current;
                            if(current<vmini) vmini = current;
                        }
                        tmp.put(e, vmaxi);
                        if(vmaxi > vmaxi_max) vmaxi_max = vmaxi;
                        if(vmini< vmini_min)    vmini_min = vmini;
                        vmaxi = vmini = 0.;
                    }
                }
                v = tmp;
            } catch (Exception ex) {
                Logger.getLogger(ValueIterationAgent.class.getName()).log(Level.SEVERE, null, ex);
            }

            // mise a jour vmax et vmin pour affichage du gradient de couleur:
            //vmax est la valeur de max pour tout s de V
            //vmin est la valeur de min pour tout s de V
            // ...
            vmax = vmaxi_max;
            vmin = vmini_min;

            //******************* a laisser a la fin de la methode
            this.notifyObs();
	}
	
	
    /**
     * renvoi l'action executee par l'agent dans l'etat e
     * Si aucune actions possibles, renvoi NONE ou null.
     */
	@Override
	public Action getAction(Etat e) {
            List<Action> Politique = getPolitique(e);
            return getMdp().estAbsorbant(e) ? null : Politique.get(new Random().nextInt(Politique.size()));
        }
	@Override
	public double getValeur(Etat _e) {
            if(v.containsKey(_e)){
                return v.get(_e) != null ? v.get(_e) : 0.;
            } else return 0.0;
	}
	/**
	 * renvoi la (les) action(s) de plus forte(s) valeur(s) dans l'etat e 
	 * (plusieurs actions sont renvoyees si valeurs identiques, liste vide si aucune action n'est possible)
	 */
	@Override
	public List<Action> getPolitique(Etat _e) {
		List<Action> politique = new ArrayList<>();
                List<Action> actionsPossibles = this.mdp.getActionsPossibles(_e);
                
                double vmaxi = -Double.MAX_VALUE;
                
                for (Action a : actionsPossibles) {
                    try {
                        HashMap<Etat, Double> hash = (HashMap<Etat, Double>) this.mdp.getEtatTransitionProba(_e, a);
                        double sum = 0d;
                        for (Etat dEtat : hash.keySet()) {
                            sum += hash.get(dEtat) * (this.mdp.getRecompense(_e, a, dEtat) + this.gamma * this.v.get(dEtat));
                        }
                        if (sum > vmaxi) {
                            vmaxi = sum;
                            politique.clear();
                            politique.add(a);
                        } else if (sum == vmaxi) {
                            politique.add(a);
                        }
                    } catch (Exception ex) {
                        Logger.getLogger(ValueIterationAgent.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }
                return politique;
		
	}
	
	@Override
	public void reset() {
		super.reset();
                for (Etat e : this.mdp.getEtatsAccessibles()) {
                    this.v.put(e, 0.0);
                }

                super.vmin = Double.MAX_VALUE;
                super.vmax = Double.MIN_VALUE;

                this.notifyObs();

	}


	@Override
	public void setGamma(double arg0) {
		this.gamma = arg0;
	}

	
}
