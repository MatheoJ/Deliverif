package modele;

import java.util.List;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class GrapheComplet implements Graphe {
    private static final int COUT_MAX = 40;
    private static final int COUT_MIN = 10;
    
    private Map<DemandeLivraison, Integer> idDemandeLivraisonToIndex = new HashMap<DemandeLivraison, Integer>();
    private Map<Integer, DemandeLivraison > idIndexToDemandeLivraison = new HashMap<Integer, DemandeLivraison>();
    int nbSommets;
    float[][] couts;
    
    /**
     * Create a complete directed graph such that each edge has a weight within
     * [MIN_COST,MAX_COST]
     */
    public GrapheComplet(List<DemandeLivraison> demandesLivraisons, Plan plan,
                         Intersection entrepot) {

        nbSommets = demandesLivraisons.size() + 1;
        couts = new float[demandesLivraisons.size() + 1][demandesLivraisons.size() + 1];

        for(float[] row : couts) {
            Arrays.fill(row, -1.0f);
        }
         
        Integer index = 1;      
        ArrayList<Intersection> listIntersection = new ArrayList<Intersection>();

        for(DemandeLivraison dl: demandesLivraisons) {
            idDemandeLivraisonToIndex.put(dl, index);
            idIndexToDemandeLivraison.put(index, dl);
            listIntersection.add(dl.getIntersection());
            index++;
        }

        listIntersection.add(entrepot);
        HashMap<Intersection, Float> plusCourtsChemins =
                plan.calculerPlusCourtsChemins(listIntersection, entrepot);

        for(DemandeLivraison dl : demandesLivraisons) {
            index = idDemandeLivraisonToIndex.get(dl);
            couts[0][index] = plusCourtsChemins.get(dl.getIntersection());
        } 
        
        /*
            Pour chaque point de livraison, on calcule les plus courts chemins
            à tous les autres points de livraisons
        */
        for(DemandeLivraison currentDl: demandesLivraisons) {
            plusCourtsChemins =
                    plan.calculerPlusCourtsChemins(listIntersection, currentDl.getIntersection());
            Integer currentIndex = idDemandeLivraisonToIndex.get(currentDl);

            for(DemandeLivraison dl : demandesLivraisons) {
                if(dl != currentDl) {
                    if(currentDl.getPlageHoraire().getFin() <= dl.getPlageHoraire().getDebut()
                            || currentDl.getPlageHoraire().getDebut() == dl.getPlageHoraire().getDebut()) {
                        index = idDemandeLivraisonToIndex.get(dl);
                        couts[currentIndex][index] = plusCourtsChemins.get(dl.getIntersection());
                    }   
                }
            }

            couts[currentIndex][0] = plusCourtsChemins.get(entrepot);
        }
    }

    @Override
    public int getNbSommets() {
        return nbSommets;
    }

    @Override
    public float getCout(int i, int j) {
        if (i < 0 || i >= nbSommets || j < 0 || j >= nbSommets)
            return -1;
        return couts[i][j];
    }

    @Override
    public boolean estUnArc(int i, int j) {
        if (couts[i][j] < 0) {
            return false;
        }
        return true;
    }

    /*
    Fonction servant au débugage
     */
    public void afficherGraphe() {
        for(int i = 0; i < nbSommets; i++) {
            for(int j = 0; j < nbSommets; j++) {
                System.out.print(couts[i][j] + " ");
            } 
            System.out.println();
        }
    }
    @Override
    public float[][] getMatriceCouts() {
        return this.couts;
    }
}