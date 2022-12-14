package modele;

import java.util.Comparator;

/**
 * Classe permettant de comparer des plages horaires
 */
public class ComparateurPlageHoraire implements Comparator<PlageHoraire> {

    /**
     * Compare les heures de début de deux plages horaires.
     * @param o1 La prmeière plage horaire
     * @param o2 La seconde plage horaire
     * @return La différence entre les heures de début des plages horaires.
     */
    @Override
    public int compare(PlageHoraire o1, PlageHoraire o2) {
        return o1.getDebut() - o2.getDebut();
    }
}
