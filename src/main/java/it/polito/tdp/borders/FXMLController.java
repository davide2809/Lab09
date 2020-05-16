package it.polito.tdp.borders;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

import it.polito.tdp.borders.model.Country;
import it.polito.tdp.borders.model.Model;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;

public class FXMLController {

	private Model model;

    @FXML
    private ResourceBundle resources;

    @FXML
    private URL location;

    @FXML
    private TextField txtAnno;

    @FXML
    private ComboBox<Country> CmbCountry;

    @FXML
    private TextArea txtResult;

    @FXML
    void doCalcolaConfini(ActionEvent event) {
    	String s=txtAnno.getText();
    	txtResult.clear();
    	if(s.isBlank()) {
    		txtResult.appendText("Inserire qualcosa");
    		return;
    	}
    	int anno;
    	try {
    		anno=Integer.parseInt(s);
    	} catch (Exception e) {
    		txtResult.appendText("Inserire un numero!");
    		return;
    	}
    	if(anno < 1816 || anno >2016) {
    		txtResult.appendText("Inserisci un numero tra 1816 e 2016");
    		return;
    	}
    	model.caricaGrafo(anno);
    	int numeroVertici=model.vertexNumber();
    	int numeroArchi=model.edgeNumber();
    	txtResult.appendText("Grafo con "+numeroArchi+" archi e con "+numeroVertici+" vertici \n");
    	CmbCountry.getItems().addAll(this.model.getCountry());
    	for(Country c: model.getCountry()) {
    		int numeroVicini= model.numeroVicini(c);
    		txtResult.appendText(c.toString()+" Grado vertice: "+numeroVicini+"\n");
    	}
    	return;
    }

    @FXML
    void doVicini(ActionEvent event) {
    	txtResult.clear();
    	Country c= CmbCountry.getValue();
    	if(c.equals(null)) {
    		txtResult.appendText("Inserisci uno stato!");
    	}
    	List<Country> raggiungibili=new ArrayList<Country>(this.model.paesiRaggiungibili(c));
    	for(Country co:raggiungibili) {
    		txtResult.appendText(co.toString()+"\n");
    	}
    	

    }

    @FXML
    void initialize() {
        assert txtAnno != null : "fx:id=\"txtAnno\" was not injected: check your FXML file 'Scene.fxml'.";
        assert CmbCountry != null : "fx:id=\"CmbCountry\" was not injected: check your FXML file 'Scene.fxml'.";
        assert txtResult != null : "fx:id=\"txtResult\" was not injected: check your FXML file 'Scene.fxml'.";
    }
    
    public void setModel(Model model) {
    	this.model = model;
    }
}
