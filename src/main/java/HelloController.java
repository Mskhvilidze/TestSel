import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.TextFieldListCell;
import javafx.scene.paint.Color;
import javafx.stage.DirectoryChooser;
import model.Client;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;


public class HelloController implements Initializable {
    public ToolBar test;
    @FXML
    private Label invoice;
    @FXML
    private ComboBox clients;
    @FXML
    private Label report;
    @FXML
    private ListView listView;
    @FXML
    private TextField numbers;
    @FXML
    private Button search;
    List<String> list = new ArrayList<>();
    private WebSelenium web;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        this.web = new WebSelenium();
        this.search.disableProperty().bind(numbers.textProperty().isEmpty());
        listView.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
        listView.setCellFactory(TextFieldListCell.forListView());
        clients.getItems().addAll(FXCollections.observableArrayList(Client.getInstance().getClients()));
        int length = Client.getInstance().getClients().length;
        clients.getSelectionModel().select(Client.getInstance().getClients()[length - 2]);
        invoice.setTooltip(new Tooltip("Z.B. 57618476, 57618377, 57618376"));
    }

    @FXML
    private void onSearch() throws InterruptedException, IOException {
        listView.getItems().clear();
        list.clear();
        for (int i = 0; i < numbers.getText().split(",").length; i++) {
            list.add(numbers.getText().split(",")[i].replaceAll("'", "").trim());
        }
        if (list != null) {
            report.setText("Alle Dateien wurden gefuden!");
            report.setTextFill(Color.GREEN);
            web.downloadPDFFile(list, (String) clients.getValue());
        }

        if (web.getNotFoundNumbers().size() > 0) {
            report.setText("Überprüfen Mandanten und Rechnungsnummer");
            report.setTextFill(Color.color(1, 0, 0));
            listView.getItems().addAll(FXCollections.<String>observableArrayList(web.getNotFoundNumbers()));
        }

        this.numbers.setText("");
    }
}