package org.example.projectweek48baeredygtighed;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import org.controlsfx.control.textfield.TextFields;
import javafx.application.Application;

import java.util.List;
import java.io.*;
import java.util.ArrayList;
import java.sql.Date;
import java.util.Scanner;


public class HelloController {

    @FXML
    private Button go;

    @FXML
    private VBox vBox;

    @FXML
    private TextField siteId1, siteId2;


    private ArrayList<Data> dataArrayList;

    @FXML
    public void initialize() throws IOException {
        dataArrayList = new ArrayList<>();

        ArrayList<String[]> data = new ArrayList<>(); //initializing a new ArrayList out of String[]'s
        try (BufferedReader TSVReader = new BufferedReader(new FileReader("src/main/resources/org/example/projectweek48baeredygtighed/SolarData.tsv"))) {
            String line = null;
            while ((line = TSVReader.readLine()) != null) {
                String[] lineItems = line.split("\t");
                data.add(lineItems);

                Data entry = new Data();
                entry.setId(Integer.parseInt(lineItems[0]));

                String date = "";
                String time = "";
                boolean isDate = true;
                for (char c : lineItems[1].toCharArray()) {
                    if (c == 'T') {
                        isDate = false;
                    } else if (isDate) {
                        date += c;
                    } else {
                        time += c;
                    }

                }

                Date dateObj = Date.valueOf(date);

                entry.setDate(dateObj);

                entry.setHour(Integer.parseInt(time.substring(0, 2)));

                entry.setSiteId(Integer.parseInt(lineItems[2]));

                entry.setTotal(Integer.parseInt(lineItems[3]));

                entry.setOnline(Integer.parseInt(lineItems[4]));

                entry.setOffline(Integer.parseInt(lineItems[5]));

                dataArrayList.add(entry);

                System.out.println(line);
            }
        } catch (Exception e) {
            System.out.println("Something went wrong");
            System.out.println(e.getMessage());
        }

        for (Data dataItem : dataArrayList) {
            System.out.println(dataItem.getId() + "\t" + dataItem.getDate() + "\t" + dataItem.getHour() + "\t" + dataItem.getSiteId() +
                    "\t" + dataItem.getTotal() + "\t" + dataItem.getOnline() + "\t" + dataItem.getOffline());
        }

        List<String> siteIds = List.of("298", "2506", "15523", "16008", "16009", "16010", "16011", "16012", "16013", "16014", "16015", "16016", "16017", "16018", "16019", "16020", "16021", "16022", "16023", "16024", "16025", "22224", "22229", "41460", "54280", "54282", "54284", "54286", "54288", "54290", "54292", "54294", "54374", "54376", "54378", "54384", "54386", "54388", "54390", "54392", "54394", "54396", "54399", "54401", "54403", "54405", "54407", "54409", "54411", "54449", "57115", "57116", "57117", "57119", "57120", "57122", "57123", "57124", "57126", "60127", "60129", "60131", "82445", "84671", "84673", "87887", "87891", "88062", "88262", "88990", "92029"
        );

        TextFields.bindAutoCompletion(siteId1, siteIds);
        TextFields.bindAutoCompletion(siteId2, siteIds);
    }
}