package it.polito.tdp.porto;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

import it.polito.tdp.porto.model.Author;
import it.polito.tdp.porto.model.Model;
import it.polito.tdp.porto.model.Paper;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextArea;

public class PortoController {
	
	Model model;

    @FXML
    private ResourceBundle resources;

    @FXML
    private URL location;

    @FXML
    private ComboBox<Author> boxPrimo;

    @FXML
    private ComboBox<Author> boxSecondo;

    @FXML
    private TextArea txtResult;

    @FXML
    void handleCoautori(ActionEvent event) {
    	
    	Author autore = boxPrimo.getValue();
    	txtResult.clear();
    	txtResult.appendText("I coautori dell'autore selezioneato sono: \n");
    	for(Author tmp: this.model.getCoAutori(autore)) {
    		txtResult.appendText(tmp.toString()+"\n");
    	}
    	
    	//scrivo nella seconda tendina 
    	List<Author> noCoAutori = model.getNoCoAutori(autore);
    	noCoAutori.remove(autore);
    	
    	boxSecondo.getItems().clear() ;
    	boxSecondo.getItems().addAll(noCoAutori);
    	//abilito la seconda tendina
    	boxSecondo.setDisable(false); 

    }

    @FXML
    void handleSequenza(ActionEvent event) {
    	txtResult.clear();
    	
    	Author a1 = boxPrimo.getValue() ;
    	Author a2 = boxSecondo.getValue() ;
    	
    	if( a1==null || a2==null ) {
    		txtResult.appendText("Errore: selezionare due autori\n");
    		return ;
    	}
    	
    	List<Paper> paper = model.sequenzaArticoli(a1, a2);
    	txtResult.clear();
    	txtResult.appendText("Articoli che collegano "+a1.toString()+" e "+a2.toString()+":\n");
    	for(Paper tmp: paper) {
    		txtResult.appendText(tmp.getEprintid() + "\n");
    	}

    }

    @FXML
    void initialize() {
        assert boxPrimo != null : "fx:id=\"boxPrimo\" was not injected: check your FXML file 'Porto.fxml'.";
        assert boxSecondo != null : "fx:id=\"boxSecondo\" was not injected: check your FXML file 'Porto.fxml'.";
        assert txtResult != null : "fx:id=\"txtResult\" was not injected: check your FXML file 'Porto.fxml'.";

    }
    
    public void setModel(Model model) {
    	this.model = model;
    	boxPrimo.getItems().addAll(this.model.getAllAuthor());
    	boxSecondo.getItems().clear() ;
    }
}
