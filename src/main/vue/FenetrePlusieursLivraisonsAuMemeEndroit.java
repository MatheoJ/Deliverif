package vue;

import controleur.ControleurFenetrePrincipale;
import javafx.collections.FXCollections;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.Button;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;
import modele.*;

import java.awt.*;
import java.util.ArrayList;
import java.util.Arrays;

/**
 * Fenêtre spécifique pour le cas où plusieurs demandes de trouvent au même
 * endroit, afin de pouvoir en sélectionner une
 */
public class FenetrePlusieursLivraisonsAuMemeEndroit {

    public static void display (ControleurFenetrePrincipale c,
                                ArrayList<DemandeLivraison> listeDemandes,
                                ArrayList<Livraison> listeLivraisons,
                                boolean etatSelectionPourNouvelleDemande) {
        Stage window = new Stage();

        window.initModality(Modality.APPLICATION_MODAL);
        window.setTitle("Choix multiple de demande de livraison");
        window.setMinWidth(0);
        window.setMinHeight(0);
        window.setMaxHeight(1000);
        window.setMaxWidth(1000);

        VBox layout = new VBox(10);
        Button selectionnerDemande = new Button("Sélectionner");

        if(listeDemandes != null) {
            TableView<DemandeLivraison> tableViewLivraisons = new TableView<>();
            TableColumn<DemandeLivraison, Long> columnIdentifiantLivraison = new TableColumn<>("Identifiant livraison");
            TableColumn<DemandeLivraison, PlageHoraire> columnPlageHoraireLivraison = new TableColumn<>("Plage horaire");

            tableViewLivraisons.setItems(FXCollections.observableArrayList());
            columnIdentifiantLivraison.setCellValueFactory(
                    new PropertyValueFactory<>("idIntersection"));
            columnPlageHoraireLivraison.setCellValueFactory(
                    new PropertyValueFactory<>("plageHoraire"));
            columnPlageHoraireLivraison.setComparator(new ComparateurPlageHoraire());

            tableViewLivraisons.getItems().addAll(listeDemandes);
            tableViewLivraisons.refresh();
            tableViewLivraisons.getColumns().addAll(Arrays.asList(columnIdentifiantLivraison,
                    columnPlageHoraireLivraison
            ));

            selectionnerDemande.setOnAction(event -> {
                DemandeLivraison demande = tableViewLivraisons.getSelectionModel()
                        .getSelectedItem();

                if (demande != null) {
                    c.getVue().tableViewDemandesLivraison.getSelectionModel().select(demande);
                    c.getEtatCourant().selectionnerDemande(c, false);
                    c.changementEtat(c.getEtatDemandeLivraisonSelectionneeSansTournees());

                    window.close();
                }
            });

            layout.getChildren().addAll(tableViewLivraisons, selectionnerDemande);
        } else {
            TableView<Livraison> tableViewLivraisons = new TableView<>();
            TableColumn<Livraison, Long> columnIdentifiantLivraison = new TableColumn<>("Identifiant intersection");
            TableColumn<Livraison, PlageHoraire> columnPlageHoraireLivraison = new TableColumn<>("Plage horaire");
            TableColumn<Livraison, String> columnHeure = new TableColumn<>("Heure de passage");

            tableViewLivraisons.setItems(FXCollections.observableArrayList());

            columnIdentifiantLivraison.setCellValueFactory(
                    new PropertyValueFactory<>("idIntersectionLivraison"));
            columnPlageHoraireLivraison.setCellValueFactory(
                    new PropertyValueFactory<>("plageHoraireLivraison"));
            columnPlageHoraireLivraison.setComparator(new ComparateurPlageHoraire());
            columnHeure.setCellValueFactory(new PropertyValueFactory<>("heureAffichee"));

            tableViewLivraisons.getItems().addAll(listeLivraisons);
            tableViewLivraisons.refresh();
            tableViewLivraisons.getColumns().addAll(Arrays.asList(columnIdentifiantLivraison,
                    columnPlageHoraireLivraison, columnHeure));

            selectionnerDemande.setOnAction(event -> {
                Livraison demande = tableViewLivraisons.getSelectionModel().getSelectedItem();

                if (demande != null) {
                    if(etatSelectionPourNouvelleDemande) {
                        c.getEtatCourant().selectionPourNouvelleDemande(c, demande);
                    } else {
                        c.getVue().tableViewLivraisons.getSelectionModel().select(demande);
                        c.getEtatCourant().selectionnerDemande(c, true);
                        c.changementEtat(c.getEtatDemandeLivraisonSelectionneeAvecTournees());
                    }

                    window.close();
                }
            });

            layout.getChildren().addAll(tableViewLivraisons, selectionnerDemande);
        }

        layout.setAlignment(Pos.CENTER);

        Dimension size = Toolkit.getDefaultToolkit().getScreenSize();
        int width = (int)size.getWidth();
        int height = (int)size.getHeight();
        Scene scene = new Scene(layout, 300,120);

        window.setScene(scene);
        window.show();

    }
}
