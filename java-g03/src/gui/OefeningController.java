package gui;

import domein.DomeinController;
import javafx.scene.layout.HBox;

public class OefeningController extends HBox {
    private OefeningenOverzichtPaneelController overzichtPanel;
    private OefeningenDetailPaneelController detailPanelController;

    private OefeningenFilterPaneelController filterPaneelController;

    private DomeinController dc;

    public OefeningController(DomeinController domeinController) {
        this.dc = domeinController;
        overzichtPanel = new OefeningenOverzichtPaneelController(dc);
        detailPanelController = new OefeningenDetailPaneelController();
        filterPaneelController = new OefeningenFilterPaneelController();

        getChildren().addAll(overzichtPanel, detailPanelController, filterPaneelController);

        dc.addObserver(detailPanelController);
    }
}