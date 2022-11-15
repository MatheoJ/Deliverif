package controleur;

import modele.DemandeLivraison;
import modele.Intersection;
import modele.Livraison;
import modele.Livreur;
import vue.FenetrePlusieursLivraisonsAuMemeEndroit;
import vue.VueFenetrePrincipale;

import javafx.scene.input.MouseEvent;

import java.util.ArrayList;

public class EtatSelectionLivraisonPourNouvelleDemande extends Etat{
    public EtatSelectionLivraisonPourNouvelleDemande() {
        super.message = "Sélectionner la livraison qui viendra avant la livraison correspondant à la nouvelle deamnde";
    }
    public void clicGaucheSurPlan(ControleurFenetrePrincipale c, MouseEvent event){
        Intersection intersectionTrouvee = this.naviguerSurPlan(c, event, true);
        Livraison livraisonAssociee = null;



        ArrayList<Livraison> livraisonsAssociees = new ArrayList<>();
        for(Livraison livraison : c.vue.comboboxLivreur.getValue().getLivraisons()){
            if(intersectionTrouvee == livraison.getDemandeLivraison().getIntersection()){
                livraisonsAssociees.add(livraison);
            }
        }
        if (livraisonsAssociees.size() == 1) {
            selectionPourNouvelleDemande(c, livraisonsAssociees.get(0));
        } else if (livraisonsAssociees.size() > 1) {
            FenetrePlusieursLivraisonsAuMemeEndroit.display(c, null, livraisonsAssociees, true);
        }
    }
    public void clicGaucheSurTableau(ControleurFenetrePrincipale c) {
        this.selectionnerDemande(c,false);
    }
    public boolean selectionnerDemande(ControleurFenetrePrincipale c, boolean livraison) {
        Livraison ligne;
        ligne = c.vue.tableViewLivraisons.getSelectionModel().getSelectedItem();
        return selectionPourNouvelleDemande(c, ligne);
    }
    public boolean selectionPourNouvelleDemande(ControleurFenetrePrincipale c, Livraison ligne){
        if (ligne != null) {
            c.vue.afficherLivraisons(c.vue.comboboxLivreur.getValue(), true);
            c.vue.dessinerIntersection(c.vue.canvasIntersectionsLivraisons.getGraphicsContext2D(),
                    ligne.getDemandeLivraison().getIntersection(),
                    c.vue.COULEUR_POINT_LIVRAISON_SELECTIONNE,
                    c.vue.TAILLE_RECT_PT_LIVRAISON_SELECTIONNE,
                    true,
                    VueFenetrePrincipale.FormeIntersection.RECTANGLE);

            c.vue.titlePaneSelectionDemande.setVisible(true);
            c.vue.textfieldIdentifiantIntersection.setText(ligne.getDemandeLivraison().getIdIntersection().toString());
            remplirLabelRuesIntersection(c, ligne.getDemandeLivraison().getIntersection());
            c.vue.textfieldPlageHoraire.setText(ligne.getDemandeLivraison().getPlageHoraire().toString());

            Livreur livreur = c.vue.comboboxLivreur.getValue();
            DemandeLivraison derniereDemande = livreur.getDemandeLivraisons().get(livreur.getDemandeLivraisons().size()-1);
            Commande commandeAjout = new CommandeAjouter(c, livreur, ligne, derniereDemande);
            c.getListeCommandes().ajouterCommande(commandeAjout);
            c.changementEtat(c.etatTourneesCalculees);
            return true;
        }
        return false;
    }
}
