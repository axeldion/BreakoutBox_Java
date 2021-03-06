package gui;

import com.jfoenix.controls.JFXButton;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.ListView;
import javafx.scene.layout.HBox;

import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

public class ListViewController<T> extends HBox {
    private ObservableList<T> lijstLeft;
    private ObservableList<T> lijstRight;

    @FXML
    private ListView<T> left;

    @FXML
    private JFXButton toRight;

    @FXML
    private JFXButton toLeft;

    @FXML
    private ListView<T> right;

    public ListViewController(ObservableList<T> lijstLeft, List<T> lijstRight) {

        this.lijstLeft = lijstLeft;
        this.lijstRight = FXCollections.observableArrayList(lijstRight);
        lijstLeft.removeAll(lijstRight);
        FXMLLoader loader
                = new FXMLLoader(getClass().getResource("ListView.fxml"));
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

        left.setItems(lijstLeft);
        left.getSelectionModel().selectFirst();
        right.setItems(lijstRight);
        right.getSelectionModel().selectFirst();
        toLeft.setDisable(lijstRight.isEmpty());
        toRight.setDisable(lijstLeft.isEmpty());

        lijstLeft.addListener(new ListChangeListener<T>() {
            @Override
            public void onChanged(Change<? extends T> c) {
                toRight.setDisable(lijstLeft.isEmpty());
            }
        });
        lijstRight.addListener(new ListChangeListener<T>() {
            @Override
            public void onChanged(Change<? extends T> c) {
                toLeft.setDisable(lijstRight.isEmpty());
            }
        });

    }

    @FXML
    void toLeft(ActionEvent event) {
        T obj = right.getSelectionModel().getSelectedItem();
        if (obj != null) {
            lijstLeft.add(obj);
            lijstRight.remove(obj);
        }
    }

    @FXML
    void toRight(ActionEvent event) {
        T obj = left.getSelectionModel().getSelectedItem();
        if (obj != null) {
            lijstRight.add(obj);
            lijstLeft.remove(obj);
        }

    }

    public List<T> getLijstRight() {
        return lijstRight.stream().collect(Collectors.toList());
    }
}
