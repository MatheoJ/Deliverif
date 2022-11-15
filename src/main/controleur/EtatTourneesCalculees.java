package controleur;

import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import modele.DemandeLivraison;
import modele.Intersection;
import modele.Livraison;
import modele.Livreur;
import vue.FenetrePlusieursLivraisonsAuMemeEndroit;

import java.util.ArrayList;
import java.util.Arrays;

import static controleur.ControleurFenetrePrincipale.LOGGER;

public class EtatTourneesCalculees extends Etat{
    public EtatTourneesCalculees() {
        super.message = "Ajoutez des demandes, visualisez les " +
                "feuilles de route ou modifiez les tournées";
    }

    public  void sauvegarderDemandes(ControleurFenetrePrincipale c){
        this.sauvegarderListeDemandes(c);
    }
    public void clicGaucheSurPlan(ControleurFenetrePrincipale c, MouseEvent event) {
        Intersection intersectionTrouvee = this.naviguerSurPlan(c, event, true);
        ArrayList<Livraison> livraisonsAssociees = new ArrayList<>();
        for(Livraison livraison : c.vue.comboboxLivreur.getValue().getLivraisons()){
            if(intersectionTrouvee == livraison.getDemandeLivraison().getIntersection()){
                livraisonsAssociees.add(livraison);
            }
        }
        if (livraisonsAssociees.size() == 1) {
            c.vue.tableViewLivraisons.getSelectionModel().select(livraisonsAssociees.get(0));
            this.selectionnerDemande(c, true);
            c.changementEtat(c.etatDemandeLivraisonSelectionneeAvecTournees);
        } else if (livraisonsAssociees.size() > 1) {
            FenetrePlusieursLivraisonsAuMemeEndroit.display(c, null, livraisonsAssociees);
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
        if (demandeSelectionee){
            this.selectionTrajet(c);
            c.changementEtat(c.etatDemandeLivraisonSelectionneeAvecTournees);
        }
    }

    public void clicSurLivreur(ControleurFenetrePrincipale c) {
        this.changementLivreur(c);
    }

    @Override
    public void redo(ListeDeCommandes liste) {
        liste.redoCommande();
    }

    public void undo(ListeDeCommandes liste) {
        liste.undoCommande();
    }

    public void touchePressee(ControleurFenetrePrincipale c, KeyEvent ke){
        super.touchePressee(c,ke);
        LOGGER.info(ke.getCode());
        ListeDeCommandes liste = c.getListeCommandes();
        switch(ke.getCode()) {
            case Z: //undo
                liste.undoCommande();
            case R: //redo
                liste.redoCommande();
        }
    }

    public void chargerPlan(ControleurFenetrePrincipale c) throws Exception{
        this.chargerNouveauPlan(c);
    }
}
