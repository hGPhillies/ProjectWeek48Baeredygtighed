package org.example.projectweek48baeredygtighed;

import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.chart.PieChart;
import javafx.geometry.Side;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.layout.VBox;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.XYChart;
import javafx.scene.control.*;
import javafx.geometry.Side;
import javafx.scene.layout.VBox;
import javafx.scene.control.TextArea;

import java.awt.*;
import java.io.*;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.sql.Date;
import java.util.List;


/**
 * A program that is able to show the user different
 */

public class HelloController {
    @FXML
    ComboBox<String> siteIdOneCombo; // Removed siteIdTwoCombo
    @FXML
    BarChart<String, Number> barChart;
    @FXML
    LineChart<String, Number> lineChart;
    @FXML
    PieChart pieChart;
    @FXML
    Button clearButton, goButton;
    @FXML
    TextArea textArea;
    @FXML
    DatePicker datePicker;
    @FXML
    private VBox vBox;

    //A list of each site ID to be used in the combobox as the option menu to select from
    List<String> siteIds = List.of("298", "2506", "15523", "16008", "16009", "16010", "16011", "16012", "16013", "16014", "16015", "16016",
            "16017", "16018", "16019", "16020", "16021", "16022", "16023", "16024", "16025", "22224", "22229", "41460", "54280", "54282", "54284",
            "54286", "54288", "54290", "54292", "54294", "54374", "54376", "54378", "54384", "54386", "54388", "54390", "54392", "54394", "54396",
            "54399", "54401", "54403", "54405", "54407", "54409", "54411", "54449", "57115", "57116", "57117", "57119", "57120", "57122", "57123",
            "57124", "57126", "60127", "60129", "60131", "82445", "84671", "84673", "87887", "87891", "88062", "88262", "88990", "92029");

    @FXML
    public void initialize() {
        // Gets the data from the source. Should ALWAYS be first.
        DataHandler.updateDataFromSource();

        addFilterToComboBoxes(siteIdOneCombo);

        // Adds each site ID from "siteIds" to comboBox one as selection options
        siteIdOneCombo.setItems(FXCollections.observableArrayList(siteIds));
        siteIdOneCombo.setEditable(true); // Enables the ComboBox to allow text input
    }

    @FXML
    public void goButton() {
        displayBarChart();
        displayLineChart();
        displayTextArea();
        displayPieChart();
        getMonthCalc();
    }

    @FXML
    public void buttonClear() {
        lineChart.getData().clear();
        barChart.getData().clear();
        pieChart.getData().clear();
    }

    /**
     * Assigns the value input from each comboBox and checks of the user has
     * made an input with a valid site id.
     */
    @FXML
    public void chooseSiteID() {
        String selectedSiteIdOne = siteIdOneCombo.getValue();
        if (selectedSiteIdOne != null && !selectedSiteIdOne.isEmpty()) {
            System.out.println("Chosen site ID: " + selectedSiteIdOne);
        }
    }

    /**
     * Ensures that the user can filter the predefined site IDs based on the input entered in the combobox.
     *
     * @param comboFilter Where the filtering functionality and event handling will be added.
     */
    public void addFilterToComboBoxes(ComboBox<String> comboFilter) {
        List<String> originalSiteIDs = new ArrayList<>(siteIds); //Provides a new list with the original site ids

        //Adds a listener to the comboboxes that checks user input and uses it for filtering.
        comboFilter.getEditor().textProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue == null || newValue.isEmpty()) {
                comboFilter.setItems(FXCollections.observableArrayList(originalSiteIDs)); //The full list of site ids are shown when the comboboxes are empty.
            } else //When the user makes input in the combobox a new list with the filtered site ids are made and displayed to the user
            {
                String input = newValue.toLowerCase();
                List<String> filteredSiteIDs = new ArrayList<>();
                for (String site : originalSiteIDs) {
                    if (site.toLowerCase().startsWith(input)) {
                        filteredSiteIDs.add(site);
                    }
                }
                comboFilter.setItems(FXCollections.observableArrayList(filteredSiteIDs)); //Filters the site ids based upon the input
            }
            comboFilter.show(); //Displays the filtered site ids to the user.
        });

        comboFilter.setOnAction(event -> { //Displays in the console whether the user has entered a valid site id or not.
            String selectedItem = comboFilter.getValue();
            if (!originalSiteIDs.contains(selectedItem)) {
                System.out.println("Invalid choice: " + selectedItem);
                comboFilter.setValue(null);
            } else {
                System.out.println("Valid choice from " + comboFilter.getId() + ": " + selectedItem);
            }
        });
    }

    /**
     * A method that grey scales the dates where the site has not been active.
     */
    @FXML
    public void getMonthCalc()
    {
        //Stores the input of the combobox in selectedSite ID and if the combobox is empty a String is returned and stops the method if so.
        String selectedSiteId = siteIdOneCombo.getValue();
        if (selectedSiteId == null || selectedSiteId.isEmpty())
        {
            System.out.println("No site selected");
            return;
        }

        int siteId = Integer.parseInt(selectedSiteId); //Stores the siteId as Integers to be held against the dataArrayList.

        //Stores another arraylist with the filtered results from the site ids based on the dataArrayList.
        ArrayList<Data> filteredData = new ArrayList<>();
        for (Data resultFilter : DataHandler.dataArrayList) {
            if (resultFilter.getSiteId() == siteId) {
                filteredData.add(resultFilter);
            }
        }

        //Checks filtered data, if the site ID doesn't exists a String is returned and stops the method if so.
        if (filteredData.isEmpty()) {
            System.out.println("No data found for Site ID: " + siteId);
            return;
        }

        /*
        Finds and stores the first and last date for the chosen site id.
        Whenever a new site id is chosen it checks again whether the date has changed and stores new dates if so.
         */
        Date minDate = filteredData.getFirst().getDate();
        Date maxDate = filteredData.getFirst().getDate();
        for (Data dates : filteredData)
        {
            if (dates.getDate().before(minDate))
            {
                minDate = dates.getDate();
            }
            if(dates.getDate().after(maxDate))
            {
                maxDate = dates.getDate();
            }
        }

        LocalDate localMinDate = minDate.toLocalDate();
        LocalDate localMaxDate = maxDate.toLocalDate();

        //Makes sure that datePicker grey scales all dates out of the range from minDate and maxDate.
        datePicker.setDayCellFactory(_ ->
                new DateCell() {
                    @Override
                    public void updateItem(LocalDate item, boolean empty) {
                        super.updateItem(item, empty);
                        setDisable(item.isAfter(localMaxDate) || item.isBefore(localMinDate));
                    }
                });

        //Stores the selected date from the datePicker in selectedDate if empty a String is returned
        LocalDate selectedDate = datePicker.getValue();
        if (selectedDate == null) {
            System.out.println("No date selected in DatePicker.");
            return;
        }

        int selectedYear = selectedDate.getYear(); //Stores the selected year in a variable.
        int selectedMonth = selectedDate.getMonthValue(); //Stores the selected month in a variable.
        int totalForMonth = 0; //A variable that holds the sum for the month

        //A loop that sorts through the filtered data for the chosen site, month and year and adds all the data together.
        for (Data monthFilter : DataHandler.dataArrayList) {
            if (monthFilter.getSiteId() == siteId) {
                LocalDate dataDate = monthFilter.getDate().toLocalDate();
                if (dataDate.getYear() == selectedYear && dataDate.getMonthValue() == selectedMonth)
                {
                    totalForMonth += monthFilter.getOnline();
                }
            }
        }
        /*
        Clears the date in the barChart and adds data to the barChart.
         */
        XYChart.Series<String, Number> series = new XYChart.Series<>();
        series.setName("Total for " + selectedDate.getMonth() + " " + selectedYear);
        series.getData().add(new XYChart.Data<>("Site ID: " + siteId, totalForMonth));
        barChart.getData().add(series);

        System.out.println("Total for Site " + siteId + " in " + selectedDate.getMonth() + " " + selectedYear + "is: " + totalForMonth + "kWh");
    }


    //Method to display Bar Chart
    @FXML
    public void displayBarChart() {
        XYChart.Series<String, Number> series = new XYChart.Series<>();
        series.setName("Total Data");

        int largestNum = 0;

        for (Data dataItem : DataHandler.dataArrayList) {
            if (dataItem.getSiteId() == Integer.parseInt(siteIdOneCombo.getValue())) {
                if (dataItem.getDate().toString().equalsIgnoreCase("2023-02-13")) {
                    if (dataItem.getTotal() > largestNum) {
                        largestNum = dataItem.getTotal();
                    }
                }
            }
        }

        series.getData().add(new XYChart.Data<>(String.valueOf(Integer.parseInt(siteIdOneCombo.getValue())), largestNum));
        barChart.setAnimated(false);
        barChart.getData().add(series);
    }

    /**
     * Adds data to the LineChart based on the selected Date and Site ID.
     */
    @FXML
    public void displayLineChart() {
        XYChart.Series<String, Number> series = new XYChart.Series<>();
        series.setName("Production of " + siteIdOneCombo.getValue());

        boolean dataExists = false;

        try {
            for (Data dataItem : DataHandler.dataArrayList) {
                if (dataItem.getSiteId() == Integer.parseInt(siteIdOneCombo.getValue())) {
                    if (dataItem.getDate().toString().equalsIgnoreCase(String.valueOf(datePicker.getValue()))) {
                        series.getData().add(new XYChart.Data<>(String.valueOf(dataItem.getHour()), dataItem.getOnline()));
                        dataExists = true;
                    }
                }
            }
        }
        catch (Exception e) {
            System.out.println("Something went wrong");
            System.out.print(e.getMessage());
        }


        if (dataExists) {
            lineChart.setAnimated(false);
            lineChart.getData().add(series);
        }
        else
        {
            System.out.println("The given Site does not have this date: " + datePicker.getValue());
        }
    }

    @FXML
    public void displayTextArea() {
        textArea.clear();
        String selectedSiteIdOne = siteIdOneCombo.getValue();
        if (selectedSiteIdOne != null) {
            int sumProduction = 0;
            int count = 0;
            for (Data dataItem : DataHandler.dataArrayList) {
                if (dataItem.getSiteId() == Integer.parseInt(selectedSiteIdOne)) {
                    if (dataItem.getDate().toString().equalsIgnoreCase(String.valueOf(datePicker.getValue()))) {
                        sumProduction += dataItem.getOnline();
                        count++;
                    }
                }
            }
            textArea.appendText("Average production: " + sumProduction/count);
        }
    }

    @FXML
    public void displayPieChart() {
        // Get the selected site ID from ComboBox
        String selectedSiteIdOne = siteIdOneCombo.getValue();

        if (selectedSiteIdOne != null) {
            // Parse the selected site ID to an integer
            int selectedSiteId = Integer.parseInt(selectedSiteIdOne);

            // Initialize the total for the selected site
            int totalForSelectedSite = 0;

            // Iterate through dataArrayList and calculate the total for the selected site
            for (Data dataItem : DataHandler.dataArrayList) {
                if (dataItem.getSiteId() == selectedSiteId) {
                    totalForSelectedSite += dataItem.getTotal(); // Sum the 'total' for each data item
                }
            }

            // Create PieChart data for the total value of the selected site
            PieChart.Data siteData = new PieChart.Data("Total for Site " + selectedSiteId, totalForSelectedSite);

            // Clear existing pie chart data
            //pieChart.getData().clear();

            // Set the legend position (optional)
            pieChart.setLegendSide(Side.BOTTOM);

            // Add the new data to the pie chart
            pieChart.getData().add(siteData);
        }
    }

}