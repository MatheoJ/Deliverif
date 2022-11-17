package controleur;

import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import modele.Intersection;
import modele.Livraison;
import vue.FenetrePlusieursLivraisonsAuMemeEndroit;

import java.util.ArrayList;

import static controleur.ControleurFenetrePrincipale.LOGGER;

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

        switch(ke.getCode()) {
            case E: //undo
                liste.undoCommande();
                break;
            case R: //redo
                liste.redoCommande();
                break;
        }
    }

    public void chargerPlan(ControleurFenetrePrincipale c) throws Exception {
        this.chargerNouveauPlan(c);
    }
}
