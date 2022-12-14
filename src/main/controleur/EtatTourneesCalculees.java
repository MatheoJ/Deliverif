package controleur;

import javafx.scene.input.*;
import javafx.scene.paint.Color;
import modele.Intersection;
import modele.Livraison;
import vue.FenetrePlusieursLivraisonsAuMemeEndroit;
import vue.VueFenetrePrincipale;

import java.util.ArrayList;

import static controleur.ControleurFenetrePrincipale.LOGGER;

/**
 * Classe implémentant l'état où l'on a calculé une tournée
 */
public class EtatTourneesCalculees extends Etat{
    public EtatTourneesCalculees() {
        super.message = "Ajoutez des demandes, visualisez les "
                + "feuilles de route ou modifiez les tournées";
    }

    public  void sauvegarderDemandes(ControleurFenetrePrincipale c) {
        this.sauvegarderListeDemandes(c);
    }

    public void clicGaucheSurPlan(ControleurFenetrePrincipale c, MouseEvent event) {
        Intersection intersectionTrouvee = this.naviguerSurPlan(c, event, true);
        if(intersectionTrouvee != null) {
            ArrayList<Livraison> livraisonsAssociees = new ArrayList<>();
            for (Livraison livraison : c.vue.comboboxLivreur.getValue().getLivraisons()) {
                if (livraison.getDemandeLivraison().getIntersection().equals(intersectionTrouvee)) {
                    livraisonsAssociees.add(livraison);
                }
            }

            if (livraisonsAssociees.size() == 1) {
                c.vue.tableViewLivraisons.getSelectionModel().select(livraisonsAssociees.get(0));
                this.selectionnerDemande(c, true);
                c.changementEtat(c.etatDemandeLivraisonSelectionneeAvecTournees);
                this.selectionTrajet(c);
            } else if (livraisonsAssociees.size() > 1) {
                FenetrePlusieursLivraisonsAuMemeEndroit.display(c, null,
                        livraisonsAssociees, false);
            }
        }
    }

    public void ajouterDemande(ControleurFenetrePrincipale c) {
        c.vue.comboboxPlageHoraire.setDisable(false);
        c.vue.tableViewDemandesLivraison.setDisable(true);
        c.vue.tableViewLivraisons.setDisable(true);
        c.changementEtat(c.etatSaisieNouvelleDemandeAvecTournees);
    }

    public void clicGaucheSurTableau(ControleurFenetrePrincipale c) {
        boolean demandeSelectionee = this.selectionnerDemande(c,true);

        if (demandeSelectionee) {
            this.selectionTrajet(c);
            c.changementEtat(c.etatDemandeLivraisonSelectionneeAvecTournees);
        }
    }

    public void clicSurLivreur(ControleurFenetrePrincipale c) {
        this.changementLivreur(c);
    }

    public void touchePressee(ControleurFenetrePrincipale c, KeyEvent ke) {
        super.touchePressee(c, ke);
        LOGGER.info(ke.getCode());

        ListeDeCommandes liste = c.getListeCommandes();

        if(ke.getCode() == KeyCode.Z && ke.isControlDown()){
            liste.undoCommande();
        } else if (ke.getCode() == KeyCode.Y && ke.isControlDown()){
            liste.redoCommande();
        }
    }

    public void chargerPlan(ControleurFenetrePrincipale c) throws Exception {
        this.chargerNouveauPlan(c);
    }

    public void zoomScroll(ControleurFenetrePrincipale c, ScrollEvent event) {
        double deltaY = event.getDeltaY();
        if(deltaY>0){
            c.vue.redessinerPlan(true,1.5);
        }
        else{
            c.vue.redessinerPlan(true,0.6667);
        }

        if(c.vue.comboboxLivreur.getValue().getTournee() != null) {
            c.vue.afficherLivraisons(c.vue.comboboxLivreur.getValue(),
                    true);
        } else {
            c.vue.afficherDemandesLivraison(c.vue.comboboxLivreur.getValue(),
                    true);
        }

        if(!c.vue.textfieldIdentifiantIntersection.getText().isEmpty()){
            long idIntersection = Long.parseLong(c.vue.textfieldIdentifiantIntersection.getText());

            c.vue.dessinerIntersection(c.vue.canvasIntersectionsLivraisons.getGraphicsContext2D(),
                    c.journee.getPlan().getIntersections().get(idIntersection),
                    Color.DARKORCHID,
                    c.vue.TAILLE_CERCLE_INTERSECTION_SELECTIONNEE,
                    true,
                    VueFenetrePrincipale.FormeIntersection.CERCLE
            );
        }
    }
}
