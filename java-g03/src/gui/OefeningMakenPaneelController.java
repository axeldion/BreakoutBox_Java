package gui;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXComboBox;
import domein.Groepsbewerking;
import domein.OefeningController;
import domein.Vak;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.StackPane;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.File;
import java.io.IOException;
import java.util.List;

public class OefeningMakenPaneelController extends StackPane {

    @FXML
    private Label lblTitel;

    @FXML
    private Label lblNaam;

    @FXML
    private TextField txfNaam;

    @FXML
    private Label lblOpgave;

    @FXML
    private JFXButton btnOpgaveButton;

    @FXML
    private ImageView img;

    @FXML
    private Label lblAntwoord;

    @FXML
    private TextField txtAntwoord;

    @FXML
    private Label lblFeedback;

    @FXML
    private JFXButton btnFeedbackButton;

    @FXML
    private Label lblGroepsbewerkingen;

    @FXML
    private Label lblVak;

    @FXML
    private JFXComboBox<Vak> vakDropDown;

    @FXML
    private JFXButton btnVoegOefeningToe;

    @FXML
    private JFXButton btnCancel;

    @FXML
    private AnchorPane apGroepsbewerking;

    private ListViewController<Groepsbewerking> lvGroepsbewerking;

    private OefeningController oefeningController;
    private File opgaveFile;
    private File feedbackFile;

    public OefeningMakenPaneelController(OefeningController dc) {
        this.oefeningController = dc;
        FXMLLoader loader = new FXMLLoader(getClass().getResource("OefeningMakenPaneel.fxml"));
        loader.setRoot(this);
        loader.setController(this);
        try {
            loader.load();
        } catch (IOException ex) {
            throw new RuntimeException(ex);
        }
        buildGui();
    }

    private void buildGui(){
        lvGroepsbewerking = new ListViewController<>(oefeningController.geefGroepsbewerkingen(), FXCollections.observableArrayList());
        vakDropDown.setItems(oefeningController.geefVakken());
        apGroepsbewerking.getChildren().add(lvGroepsbewerking);
    }

    @FXML
    void opgaveFileChooser(ActionEvent event) {
        FileChooser fc = new FileChooser();
        fc.getExtensionFilters().add(new FileChooser.ExtensionFilter("PDF file", "*.pdf"));
        opgaveFile = fc.showOpenDialog(null);


    }

    @FXML
    void feedbackFileChooser(ActionEvent event){
        FileChooser fc2 = new FileChooser();
        fc2.getExtensionFilters().add(new FileChooser.ExtensionFilter("PDF file", "*.pdf"));
        feedbackFile = fc2.showOpenDialog(null);


    }



    @FXML
    void VoegOefeningToe(ActionEvent event) {
        try {
            if (opgaveFile == null) {
                throw new IllegalArgumentException("Er is geen opgave geselecteerd.");
            }
            String naam = txfNaam.getText();
            String antwoord = txtAntwoord.getText();
            List<Groepsbewerking> list = lvGroepsbewerking.getLijstRight();
            Vak vak = vakDropDown.getSelectionModel().getSelectedItem();

            try {
                oefeningController.createOefening(naam, opgaveFile, antwoord, feedbackFile, list, vak);
            } catch (IllegalArgumentException ex) {
                AlertBox.showAlertError("Fout maak Oefening", ex.getMessage(), (Stage) this.getScene().getWindow());
            }
            Scene s = this.getScene();
            s.setRoot(new OefeningSchermController(oefeningController));
        }catch (IllegalArgumentException ex) {
            AlertBox.showAlertError("Fout Oefening Toevoegen", ex.getMessage(), (Stage) this.getScene().getWindow());
        }
    }

    @FXML
    void canel(ActionEvent event) {
        Scene s = this.getScene();
        s.setRoot(new OefeningSchermController(oefeningController));
    }
}
