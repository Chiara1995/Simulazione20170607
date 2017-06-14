/**
 * Sample Skeleton for 'SerieA.fxml' Controller Class
 */

package it.polito.tdp.seriea;

import java.net.URL;
import java.util.ResourceBundle;

import it.polito.tdp.seriea.model.Model;
import it.polito.tdp.seriea.model.Punteggio;
import it.polito.tdp.seriea.model.Season;
import it.polito.tdp.seriea.model.Team;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.TextArea;

public class SerieAController {
	
	private Model model;

    @FXML // ResourceBundle that was given to the FXMLLoader
    private ResourceBundle resources;

    @FXML // URL location of the FXML file that was given to the FXMLLoader
    private URL location;

    @FXML // fx:id="boxSeason"
    private ChoiceBox<Season> boxSeason; // Value injected by FXMLLoader

    @FXML // fx:id="boxTeam"
    private ChoiceBox<?> boxTeam; // Value injected by FXMLLoader

    @FXML // fx:id="txtResult"
    private TextArea txtResult; // Value injected by FXMLLoader
    
    public void setModel(Model m){
    	this.model=m;
    	this.boxSeason.getItems().addAll(model.getSeasons());
    	this.boxSeason.setValue(model.getSeasons().get(0));
    }

    @FXML
    void handleCarica(ActionEvent event) {
    	this.txtResult.clear();
    	if(this.boxSeason.getValue()==null){
    		this.txtResult.setText("Selezionare una stagione.");
    		return;
    	}
    	int s=this.boxSeason.getValue().getSeason();
    	model.creaGrafo(s);
    	for(Punteggio p : model.getClassifica(s)){
    		txtResult.appendText(p.toString()+"\n");
    	}
    	return;
    }

    @FXML
    void handleDomino(ActionEvent event) {
    	this.txtResult.clear();
    	if(this.boxSeason.getValue()==null){
    		this.txtResult.setText("Selezionare una stagione.");
    		return;
    	}
    	int s=this.boxSeason.getValue().getSeason();
    	model.creaGrafo(s);
    	this.txtResult.setText("Cammino:\n");
    	for(Team t : model.risolvi(s)){
    		this.txtResult.appendText(t.toString()+"\n");
    	}
    	return;
    }

    @FXML // This method is called by the FXMLLoader when initialization is complete
    void initialize() {
        assert boxSeason != null : "fx:id=\"boxSeason\" was not injected: check your FXML file 'SerieA.fxml'.";
        assert boxTeam != null : "fx:id=\"boxTeam\" was not injected: check your FXML file 'SerieA.fxml'.";
        assert txtResult != null : "fx:id=\"txtResult\" was not injected: check your FXML file 'SerieA.fxml'.";
    }
}