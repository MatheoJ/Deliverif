package controleur;

import javafx.scene.input.MouseEvent;
import modele.DemandeLivraison;
import vue.VueFenetrePrincipale;

/**
 * Classe implémentant l'état où aucune tournée n'a été calculée, et où on effectue
 * une modification d'une demande de livraison
 */
public class EtatModifierDemandeLivraisonSansTournees extends Etat {

    public EtatModifierDemandeLivraisonSansTournees() {
        super.message = "Validez ou annulez les modifications";
    }

    public void clicGaucheSurPlan(ControleurFenetrePrincipale c, MouseEvent event) {
        this.naviguerSurPlan(c, event, false);
        DemandeLivraison ligne = c.vue.tableViewDemandesLivraison
                .getSelectionModel().getSelectedItem();

        if (ligne != null) {
            c.vue.dessinerIntersection(c.vue.canvasIntersectionsLivraisons.getGraphicsContext2D(),
                    ligne.getIntersection(),
                    c.vue.COULEUR_POINT_LIVRAISON_SELECTIONNE,
                    c.vue.TAILLE_RECT_PT_LIVRAISON_SELECTIONNE,
                    true,
                    VueFenetrePrincipale.FormeIntersection.RECTANGLE
            );
        }
    }

    public void validerAjouterOuModifier(ControleurFenetrePrincipale c) {
        this.effectuerModification(c);
        c.changementEtat(c.etatDemandeLivraisonSelectionneeSansTournees);
    }

    public void annulerAjouterOuModifier(ControleurFenetrePrincipale c) {
        this.annulerModification(c);
        c.changementEtat(c.etatDemandeLivraisonSelectionneeSansTournees);
    }
}
