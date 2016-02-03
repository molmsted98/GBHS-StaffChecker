package com.grandblanchs.staffchecker;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.SystemUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.ArrayList;

public class Controller {

    public String[] input;
    public ArrayList<String> fetch = new ArrayList<>();

    public TextArea txt_input;

    public RadioButton rdo_name;
    public RadioButton rdo_email;
    public RadioButton rdo_phone;

    public Button btn_check;

    @FXML
    void initialize() {

        ToggleGroup group = new ToggleGroup();
        rdo_name.setToggleGroup(group);
        rdo_email.setToggleGroup(group);
        rdo_phone.setToggleGroup(group);
        rdo_name.setSelected(true);
    }

    public void check() {

        input = txt_input.getText().split("</item>\n");

        for (int i = 0; i < input.length; i++) {
            input[i] = input[i].replace("<item>", "");
            input[i] = input[i].replace("</item>", "");
        }

        fetch = getWeb();

        TableColumn<Item, Integer> numColumn = new TableColumn<>("Number");
        numColumn.setCellValueFactory(new PropertyValueFactory<>("number"));

        TableColumn<Item, String> hcColumn = new TableColumn<>("Hardcoded");
        hcColumn.setCellValueFactory(new PropertyValueFactory<>("hardcoded"));

        TableColumn<Item, String> webColumn = new TableColumn<>("Web");
        webColumn.setCellValueFactory(new PropertyValueFactory<>("web"));

        TableView<Item> table = new TableView<>();
        table.setItems(getItem());

        //noinspection unchecked
        table.getColumns().addAll(numColumn, hcColumn, webColumn);

        Stage stage = new Stage();
        stage.setTitle("Mismatched Entries");
        stage.initModality(Modality.APPLICATION_MODAL);

        VBox vbox = new VBox();
        vbox.getChildren().addAll(table);

        Scene scene = new Scene(vbox, 720, 480);
        stage.setScene(scene);
        stage.setResizable(false);
        stage.show();

        if (table.getItems().size() == 0) {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Info");
            alert.setHeaderText("No mismatched entries found.");
            alert.setContentText("You're good to go!");
            alert.show();
        }
    }

    public ObservableList<Item> getItem() {
        ObservableList<Item> items = FXCollections.observableArrayList();

        for (int i = 0; i < fetch.size() - 1; i++){
            if (rdo_phone.isSelected()) {
                if (fetch.get(i).equals("810")) {
                    fetch.set(i, "NONE");
                }
            }
            if (!input[i].toUpperCase().equals(fetch.get(i))) {
                items.add(new Item(i + 1, input[i], fetch.get(i)));
            }
        }

        return items;
    }


    private ArrayList<String> getWeb(){
        Document staff;
        ArrayList<String> checkArray = new ArrayList<>();

        try {
            staff = Jsoup.connect("http://grandblanc.high.schoolfusion.us/modules/cms/pages.phtml?pageid=120116").get();

            Element table = staff.select("table").last();
            Elements rows = table.select("tr");

            for (int i = 3; i < rows.size(); i++) {  //Skip first three rows
                Element row = rows.get(i);
                if (rdo_name.isSelected()) {
                    checkArray.add(row.select("td").get(0).text());
                }else if (rdo_email.isSelected()) {
                    checkArray.add(StringUtils.substringBetween(
                            row.select("td").get(1).toString().toUpperCase(), "MAILTO:", "\">"));
                    System.out.println(checkArray.get(checkArray.size() - 1));
                }else if (rdo_phone.isSelected()) {
                    checkArray.add("810" + row.select("td").get(2).text().replace("-", "").replace("\u00a0", ""));
                }
            }

            for (int i = 0; i < checkArray.size(); i ++) {
                String item = checkArray.get(i).toLowerCase();
                if (rdo_name.isSelected()) {
                    int loc = item.indexOf(" ");
                    item = item.substring(0, 1).toUpperCase() + item.substring(1, loc).toLowerCase() + " " +
                            item.substring(loc + 1, loc + 2).toUpperCase() + item.substring(loc + 2).toLowerCase();
                }
                System.out.println("<item>" + item + "</item>");
            }

            return checkArray;

        }catch(IOException | NullPointerException e){
            e.printStackTrace();
        }

        return null;
    }
}
