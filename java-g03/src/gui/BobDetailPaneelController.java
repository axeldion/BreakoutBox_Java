package gui;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXDecorator;
import com.jfoenix.controls.JFXTextField;
import domein.Actie;
import domein.Bob;
import domein.Oefening;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.ListView;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.Observable;
import java.util.Observer;

public class BobDetailPaneelController extends VBox implements Observer {

    @FXML
    private JFXTextField txfNaam;

    @FXML
    private ListView<Oefening> lvOefeningen;

    @FXML
    private ListView<Actie> lvActies;

    @FXML
    private JFXButton btnGenerateBobOverzicht;

    private Bob bob;

    public BobDetailPaneelController() {
        FXMLLoader loader
                = new FXMLLoader(getClass().getResource("BobDetailPaneel.fxml"));
        loader.setRoot(this);
        loader.setController(this);

        try {
            loader.load();
        } catch (IOException ex) {
            throw new RuntimeException(ex);
        }

        btnGenerateBobOverzicht.setDisable(true);
    }

    @Override
    public void update(Observable o, Object arg) {
        if (arg != null){
            bob = (Bob)arg;
            txfNaam.setText(bob.getNaam());
            lvOefeningen.setItems(FXCollections.observableArrayList(bob.getLijstOefeningen()));
            lvActies.setItems(FXCollections.observableArrayList(bob.getLijstActies()));
            btnGenerateBobOverzicht.setDisable(false);
        }else {
            btnGenerateBobOverzicht.setDisable(true);
        }


    }

    @FXML
    void generateBobOverzicht(ActionEvent event) {
        bob.generateBobOverzichtPdf();
    }
}