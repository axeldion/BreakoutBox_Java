package gui;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXComboBox;
import com.jfoenix.controls.JFXTextField;
import domein.Groepsbewerking;
import domein.Oefening;
import domein.OefeningController;
import domein.Vak;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.stage.FileChooser;

import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class OefeningKopiePaneelController extends AnchorPane {


    @FXML
    private Label lblTitel;

    @FXML
    private Label lblNaam;

    @FXML
    private JFXTextField txfNaam;

    @FXML
    private Label lblOpgave;

    @FXML
    private JFXButton btnOpgaveButton;

    @FXML
    private Label lblAntwoord;

    @FXML
    private JFXTextField txtAntwoord;

    @FXML
    private Label lblFeedback;

    @FXML
    private JFXButton btnFeedbackButton;

    @FXML
    private Label lblGroepsbewerkingen;

    @FXML
    private ListView<Groepsbewerking> left;

    @FXML
    private JFXButton toRight;

    @FXML
    private JFXButton toLeft;

    @FXML
    private ListView<Groepsbewerking> right;

    @FXML
    private Label lblVak;

    @FXML
    private JFXComboBox<Vak> vakDropDown;

    @FXML
    private JFXButton btnVoegOefeningToe;

    @FXML
    private JFXButton btnCancel;

    @FXML
    private JFXButton btnOpgave;

    @FXML
    private JFXButton btnFeedback;

    private ObservableList<Groepsbewerking> lijstLeft;
    private ObservableList<Groepsbewerking> lijstRight;
    private OefeningController oefeningController;
    private File opgaveFile;
    private File feedbackFile;
    private Oefening oefening;

    public OefeningKopiePaneelController(OefeningController dc, Oefening oef) {
        this.oefeningController = dc;
        this.oefening = oef;
        FXMLLoader loader = new FXMLLoader(getClass().getResource("OefeningKopiePaneel.fxml"));
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
        lijstLeft = oefeningController.geefGroepsbewerkingen();
        lijstRight = FXCollections.observableArrayList(oefening.getLijstGroepsbewerkingen());
        lijstLeft.removeAll(lijstRight);
        left.setItems(lijstLeft);
        left.getSelectionModel().selectFirst();
        right.setItems(lijstRight);
        right.getSelectionModel().selectFirst();
        vakDropDown.setItems(oefeningController.geefVakken());
        toLeft.setDisable(true);
        txfNaam.setText(oefening.getNaam());
        txtAntwoord.setText(oefening.getAntwoord());
        vakDropDown.getSelectionModel().select(oefening.getVak());
        opgaveFile = oefeningController.geefFile(oefening.getOpgave());
        feedbackFile = oefeningController.geefFile(oefening.getFeedback());
        btnOpgave.setText(oefening.getOpgave());
        btnFeedback.setText(oefening.getFeedback());

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
    void toLeft(ActionEvent event) {
        Groepsbewerking groep = right.getSelectionModel().getSelectedItem();
        if (groep != null){
            lijstLeft.add(groep);
            lijstRight.remove(groep);
            if (lijstRight.isEmpty())
                toLeft.setDisable(true);
            if (!lijstLeft.isEmpty())
                toRight.setDisable(false);
        }


    }

    @FXML
    void toRight(ActionEvent event) {
        Groepsbewerking groep = left.getSelectionModel().getSelectedItem();
        if (groep != null){
            lijstRight.add(groep);
            lijstLeft.remove(groep);
            if (lijstLeft.isEmpty())
                toRight.setDisable(true);
            if (!lijstRight.isEmpty())
                toLeft.setDisable(false);
        }

    }

    @FXML
    void VoegOefeningToe(ActionEvent event) {
        if (opgaveFile == null || feedbackFile == null){
            throw new IllegalArgumentException("Er is geen opgave of feedback geselecteerd.");
        }
        String naam = txfNaam.getText();
        String antwoord = txtAntwoord.getText();
        List<Groepsbewerking> list = new ArrayList<>();
        lijstRight.stream().forEach(g -> list.add(g));
        Vak vak = vakDropDown.getSelectionModel().getSelectedItem();

        oefeningController.createOefening(naam, opgaveFile, antwoord, feedbackFile, list, vak );
        Scene s = this.getScene();
        s.setRoot(new OefeningSchermController(oefeningController));
    }

    @FXML
    void canel(ActionEvent event) {
        Scene s = this.getScene();
        s.setRoot(new OefeningSchermController(oefeningController));
    }

    @FXML
    void toonFeedback(ActionEvent event) {
        try {
            Desktop.getDesktop().open(feedbackFile);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    void toonOpgave(ActionEvent event) {
        try {
            Desktop.getDesktop().open(opgaveFile);
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
}
