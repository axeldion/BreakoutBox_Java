package gui;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXComboBox;
import com.jfoenix.controls.JFXTextField;
import domein.*;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.util.List;

public class OefeningKopiePaneelController extends BorderPane {


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

    @FXML
    private AnchorPane apGroepsbewerking;

    @FXML
    private Label lblDoelstellingen;

    @FXML
    private AnchorPane apDoelstellingen;

    @FXML
    private Label lblTijdslimiet;

    @FXML
    private TextField txfTijdslimiet;

    private OefeningController oefeningController;
    private File opgaveFile;
    private File feedbackFile;
    private Oefening oefening;

    private ListViewController<Groepsbewerking> lvGroepsbewerking;
    private ListViewController<Doelstellingscode> lvDoelstellingen;

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

    private void buildGui() {
        this.setTop(new TopBarController());
        try {
            lvGroepsbewerking = new ListViewController<>(oefeningController.geefGroepsbewerkingen(), oefening.getLijstGroepsbewerkingen());
            lvDoelstellingen = new ListViewController<>(oefeningController.geefDoelstelingscodes(), oefening.getDoelstellingscodes());

        } catch (IllegalArgumentException e) {
            AlertBox.showAlertError("Toevoegen breakout box", e.getMessage(), (Stage) this.getScene().getWindow());
        }
        apGroepsbewerking.getChildren().add(lvGroepsbewerking);
        apDoelstellingen.getChildren().add(lvDoelstellingen);
        vakDropDown.setItems(oefeningController.geefVakken());
        txfNaam.setText(oefening.getNaam());
        txtAntwoord.setText(oefening.getAntwoord());
        vakDropDown.getSelectionModel().select(oefening.getVak());
        opgaveFile = oefeningController.geefFile(oefening.getOpgave());
        if (oefening.getFeedback() != null) {
            feedbackFile = oefeningController.geefFile(oefening.getFeedback());

        }
        txfTijdslimiet.setText(Integer.toString(oefening.getTijdsLimiet()));
        txfTijdslimiet.textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue,
                                String newValue) {
                if (!newValue.matches("\\d*")) {
                    txfTijdslimiet.setText(newValue.replaceAll("[^\\d]", ""));
                }
                if (newValue.isEmpty()){
                    txfTijdslimiet.setText(Integer.toString(0));
                }
            }
        });

        txtAntwoord.textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue,
                                String newValue) {
                if (!newValue.matches("\\d*")) {
                    txtAntwoord.setText(newValue.replaceAll("[^\\d]", ""));
                }


            }
        });
        txfTijdslimiet.setText(Integer.toString(oefening.getTijdsLimiet()));


    }

    @FXML
    void opgaveFileChooser(ActionEvent event) {
        FileChooser fc = new FileChooser();
        fc.getExtensionFilters().add(new FileChooser.ExtensionFilter("PDF file", "*.pdf"));
        opgaveFile = fc.showOpenDialog(null);


    }

    @FXML
    void feedbackFileChooser(ActionEvent event) {
        FileChooser fc2 = new FileChooser();
        fc2.getExtensionFilters().add(new FileChooser.ExtensionFilter("PDF file", "*.pdf"));
        feedbackFile = fc2.showOpenDialog(null);


    }


    @FXML
    void VoegOefeningToe(ActionEvent event) {

        String naam = txfNaam.getText();
        String antwoord = txtAntwoord.getText();
        List<Groepsbewerking> list = lvGroepsbewerking.getLijstRight();
        List<Doelstellingscode> listDoelstellingen = lvDoelstellingen.getLijstRight();
        Vak vak = vakDropDown.getSelectionModel().getSelectedItem();
        int tijdslimiet = Integer.parseInt(txfTijdslimiet.getText());

        try {
            if (oefening.getNaam().equals(naam)) {
                oefeningController.createOefening(naam +" kopie", opgaveFile, antwoord, feedbackFile, list, listDoelstellingen, vak, tijdslimiet);
            }
            else {
                oefeningController.createOefening(naam, opgaveFile, antwoord, feedbackFile, list, listDoelstellingen, vak, tijdslimiet);
            }
            oefeningController.createOefening(naam +" kopie", opgaveFile, antwoord, feedbackFile, list, listDoelstellingen, vak, tijdslimiet);
            Scene s = this.getScene();
            s.setRoot(new OefeningSchermController(oefeningController));
        } catch (IllegalArgumentException ex) {
            AlertBox.showAlertError("Fout Oefening Kopieren", ex.getMessage(), (Stage) this.getScene().getWindow());

        }

    }

    @FXML
    void cancel(ActionEvent event) {
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
