/*
 * LanceurApplication
 * 
 * Version 1.0
 */

package demarrer;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import vue.VueFenetrePrincipale;


/**
 * Ouvre la première vue de l'application.
 * @author H4113
 *
 */
public class LanceurApplication extends Application {
	@Override
	public void start(Stage primaryStage) {
		try {
			
			FXMLLoader loader = new FXMLLoader(getClass()
			        .getResource("/FenetrePrincipale.fxml"));
	        Parent root = loader.load();
			VueFenetrePrincipale controleur = loader.getController();
			controleur.setStage(primaryStage);
	        primaryStage.setTitle("Deliver'IF");
	        primaryStage.setScene(new Scene(root));
	        primaryStage.show();
			
	    } catch(Exception e) {
	        e.printStackTrace();
	    }
	}
	
}
