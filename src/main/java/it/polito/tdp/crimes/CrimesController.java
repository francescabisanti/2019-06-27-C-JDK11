/**
 * Sample Skeleton for 'Crimes.fxml' Controller Class
 */

package it.polito.tdp.crimes;

import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

import org.jgrapht.graph.DefaultWeightedEdge;

import it.polito.tdp.crimes.model.Model;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextArea;

public class CrimesController {
	
	private Model model;

    @FXML // ResourceBundle that was given to the FXMLLoader
    private ResourceBundle resources;

    @FXML // URL location of the FXML file that was given to the FXMLLoader
    private URL location;

    @FXML // fx:id="boxCategoria"
    private ComboBox<String> boxCategoria; // Value injected by FXMLLoader

    @FXML // fx:id="boxGiorno"
    private ComboBox<Integer> boxGiorno; // Value injected by FXMLLoader

    @FXML // fx:id="btnAnalisi"
    private Button btnAnalisi; // Value injected by FXMLLoader

    @FXML // fx:id="boxArco"
    private ComboBox<DefaultWeightedEdge> boxArco; // Value injected by FXMLLoader

    @FXML // fx:id="btnPercorso"
    private Button btnPercorso; // Value injected by FXMLLoader

    @FXML // fx:id="txtResult"
    private TextArea txtResult; // Value injected by FXMLLoader

    @FXML
    void doCreaGrafo(ActionEvent event) {
    	this.txtResult.clear();
    	String categoria= this.boxCategoria.getValue();
    	Integer giorno= this.boxGiorno.getValue();
    	if(giorno==null || categoria==null) {
    		this.txtResult.setText("Seleziona un giorno e una categoria!");
    		return;
    	}
    	this.model.creaGrafo(categoria, giorno);
    	this.txtResult.appendText("Grafo creato con "+model.getNVertici()+" vertici e "+model.getNArchi()+" archi\n");
    	List<DefaultWeightedEdge> result= model.puntoD();
    	for(DefaultWeightedEdge e: result) {
    		this.txtResult.appendText(model.getGrafo().getEdgeSource(e)+"  -  "+model.getGrafo().getEdgeTarget(e)+" = "+model.getGrafo().getEdgeWeight(e)+"\n");
    	}
    	this.txtResult.appendText("Peso mediano: "+model.getMediano());
    	this.boxArco.getItems().clear();
    	this.boxArco.getItems().addAll(result);
    }

    @FXML
    void doCalcolaPercorso(ActionEvent event) {
    	this.txtResult.clear();
    	if(model.getGrafo()==null) {
    		this.txtResult.setText("Crea prima il grafo!");
    		return;
    	}
    	DefaultWeightedEdge e = this.boxArco.getValue();
    	List <String> percorso= model.trovaPercorso(e);
    	this.txtResult.appendText("Il percorso da "+model.getGrafo().getEdgeSource(e)+" a "+this.model.getGrafo().getEdgeTarget(e)+ " è: \n");
    	for(String s: percorso) {
    		this.txtResult.appendText(s+"\n");
    	}
    }

    @FXML // This method is called by the FXMLLoader when initialization is complete
    void initialize() {
        assert boxCategoria != null : "fx:id=\"boxCategoria\" was not injected: check your FXML file 'Crimes.fxml'.";
        assert boxGiorno != null : "fx:id=\"boxGiorno\" was not injected: check your FXML file 'Crimes.fxml'.";
        assert btnAnalisi != null : "fx:id=\"btnAnalisi\" was not injected: check your FXML file 'Crimes.fxml'.";
        assert boxArco != null : "fx:id=\"boxArco\" was not injected: check your FXML file 'Crimes.fxml'.";
        assert btnPercorso != null : "fx:id=\"btnPercorso\" was not injected: check your FXML file 'Crimes.fxml'.";
        assert txtResult != null : "fx:id=\"txtResult\" was not injected: check your FXML file 'Crimes.fxml'.";

    }
    
    public void setModel(Model model) {
    	this.model = model;
    	this.boxCategoria.getItems().addAll(model.getCategorie());
    	this.boxGiorno.getItems().addAll(model.getAnni());
    }
}
