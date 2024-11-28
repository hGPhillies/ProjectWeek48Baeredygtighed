package org.example.projectweek48baeredygtighed;

import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.chart.PieChart;
import javafx.geometry.Side;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.XYChart;

import java.io.*;
import java.util.ArrayList;
import java.sql.Date;
import java.util.List;


/**
 * A program that is able to show the user different
 */

public class HelloController {
    @FXML
    ComboBox<String> siteIdOneCombo, siteIdTwoCombo;
    @FXML
    BarChart<String, Number> barChart;
    @FXML
    LineChart<String, Number> lineChart;
    @FXML
    PieChart pieChart;
    @FXML
    Button clearButton,goButton;


    @FXML
    private VBox vBox;

    @FXML
    private DatePicker datePicker;

    private ArrayList<Data> dataArrayList;

    //A list of each site ID to be used in the combobox as the option menu to select from
    List<String> siteIds = List.of("298", "2506", "15523", "16008", "16009", "16010", "16011", "16012", "16013", "16014", "16015", "16016",
            "16017", "16018", "16019", "16020", "16021", "16022", "16023", "16024", "16025", "22224", "22229", "41460", "54280", "54282", "54284",
            "54286", "54288", "54290", "54292", "54294", "54374", "54376", "54378", "54384", "54386", "54388", "54390", "54392", "54394", "54396",
            "54399", "54401", "54403", "54405", "54407", "54409", "54411", "54449", "57115", "57116", "57117", "57119", "57120", "57122", "57123",
            "57124", "57126", "60127", "60129", "60131", "82445", "84671", "84673", "87887", "87891", "88062", "88262", "88990", "92029");

    @FXML
    public void initialize()
    {
        // gets the data from the source. Should ALWAYS be first.
        updateDataFromSource();

        addFilterToComboBoxes(siteIdOneCombo);
        addFilterToComboBoxes(siteIdTwoCombo);

        //Adds each site ID from "siteIds" to comboBox one as selection options
        siteIdOneCombo.setItems(FXCollections.observableArrayList(siteIds));
        siteIdOneCombo.setEditable(true); //Enables the ComboBox to allow text input.
        //Adds each site ID from "siteIds" to comboBox one as selection options
        siteIdTwoCombo.setItems(FXCollections.observableArrayList(siteIds));
        siteIdTwoCombo.setEditable(true); //Enables the ComboBox to allow text input.
    }

    @FXML
    public void goButton()
    {
        displayBarChart();
        displayLineChart();
        displayPieChart();
    }

    @FXML
    public void buttonClear()
    {
        lineChart.getData().clear();
        barChart.getData().clear();
        pieChart.getData().clear();
    }

    /**
     * Assigns the value input from each comboBox and checks of the user has
     * made an input with a valid site id.
     */
    @FXML
    public void chooseSiteID()
    {
        String selectedSiteIdOne = siteIdOneCombo.getValue();
        String selectedSiteIdTwo = siteIdTwoCombo.getValue();

        if(selectedSiteIdOne != null && !selectedSiteIdOne.isEmpty())
        {
            System.out.println("Chosen site ID: " + selectedSiteIdOne);
        }

        if(selectedSiteIdTwo != null && !selectedSiteIdTwo.isEmpty())
        {
            System.out.println("Chosen site ID: " + selectedSiteIdTwo);
        }

    }

    /**
     * Ensures that the user can filter the predefined site IDs based on the input entered in the combobox.
     * @param comboFilter Where the filtering functionality and event handling will be added.
     */
    public void addFilterToComboBoxes(ComboBox<String> comboFilter)
    {
        List<String> originalSiteIDs = new ArrayList<>(siteIds); //Provides a new list with the original site ids

        //Adds a listener to the comboboxes that checks user input and uses it for filtering.
        comboFilter.getEditor().textProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue == null || newValue.isEmpty())
            {
                comboFilter.setItems(FXCollections.observableArrayList(originalSiteIDs)); //The full list of site ids are shown when the comboboxes are empty.
            }
            else //When the user makes input in the combobox a new list with the filtered site ids are made and displayed to the user
            {
                String input = newValue.toLowerCase();
                List<String> filteredSiteIDs = new ArrayList<>();
                for (String site : originalSiteIDs)
                {
                    if (site.toLowerCase().startsWith(input))
                    {
                        filteredSiteIDs.add(site);
                    }
                }
                comboFilter.setItems(FXCollections.observableArrayList(filteredSiteIDs)); //Filters the site ids based upon the input
            }
            comboFilter.show(); //Displays the filtered site ids to the user.
        });

        comboFilter.setOnAction(event -> { //Displays in the console whether the user has entered a valid site id or not.
            String selectedItem = comboFilter.getValue();
            if (!originalSiteIDs.contains(selectedItem))
            {
                System.out.println("Invalid choice: " + selectedItem);
                comboFilter.setValue(null);
            } else
            {
                System.out.println("Valid choice from " + comboFilter.getId() + ": " + selectedItem);
            }
        });
    }


    /**
     * Reads the SolarData.tsv file and adds all records to the dataArrayList ArrayList.
     * Gives error "Something went wrong" if the file is not found.
     */
    private void updateDataFromSource()
    {
        dataArrayList = new ArrayList<>();

        // tries to get the file.
        try (BufferedReader TSVReader = new BufferedReader(new FileReader("src/main/resources/org/example/projectweek48baeredygtighed/SolarData.tsv"))) {
            String line;
            while ((line = TSVReader.readLine()) != null) { // if there are more lines/records it continues.
                String[] lineItems = line.split("\t"); // splits the record into its column.

                Data entry = new Data(); // makes a Data object for adding the data to.

                entry.setId(Integer.parseInt(lineItems[0])); // Sets the ID of the entry to the first column in the record.

                // Converts the Date column to two separate strings, representing the date and time.
                StringBuilder date = new StringBuilder();
                StringBuilder time = new StringBuilder();
                boolean isDate = true;
                for (char c : lineItems[1].toCharArray())
                {
                    if (c == 'T')
                    {
                        isDate = false;
                    }
                    else if (isDate)
                    {
                        date.append(c);
                    }
                    else
                    {
                        time.append(c);
                    }

                }

                Date dateObj = Date.valueOf(date.toString()); // Converts the date string into a Date object.

                entry.setDate(dateObj); // sets the date of the entry to the found date.

                entry.setHour(Integer.parseInt(time.substring(0, 2))); // sets the hour from the time string.

                entry.setSiteId(Integer.parseInt(lineItems[2])); // sets SiteId from the third column.

                entry.setTotal(Integer.parseInt(lineItems[3])); // sets total production to the data from the fourth column

                entry.setOnline(Integer.parseInt(lineItems[4])); // sets Online production to the data from the fifth column

                entry.setOffline(Integer.parseInt(lineItems[5])); // sets Offline production to the data from the sixth column

                dataArrayList.add(entry); // Adds the entry to the ArrayList containing all records.
            }
        } catch (Exception e) {
            System.out.println("Something went wrong");
            System.out.println(e.getMessage());
        }
    }

    /**
     * Prints all data information in the console.
     */
    private void printAllData()
    {
        for (Data dataItem : dataArrayList)
        {
            System.out.println(dataItem.getId() + "\t" + dataItem.getDate() + "\t" + dataItem.getHour() + "\t" + dataItem.getSiteId() +
                    "\t" + dataItem.getTotal() + "\t" + dataItem.getOnline() + "\t" + dataItem.getOffline());
        }
    }
    //Method to display Bar Chart
    @FXML
    public void displayBarChart() {
        XYChart.Series<String, Number> series = new XYChart.Series<>();
        series.setName("Total Data");

        int largestNum = 0;

        for (Data dataItem : dataArrayList) {
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

    @FXML
    public void displayLineChart() {

        XYChart.Series<String, Number> series = new XYChart.Series<>();
        series.setName("Production for " + datePicker.getValue());

        boolean dataExists = false;

        try {
            for (Data dataItem : dataArrayList) {
                if (dataItem.getSiteId() == Integer.parseInt(siteIdTwoCombo.getValue())) {
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
            lineChart.setTitleSide(Side.RIGHT);
            lineChart.setTitle("W/h");
            lineChart.setTitleSide(Side.BOTTOM);
            lineChart.setTitle("Hours");
        }
        else
        {
            System.out.println("The given Site does not have this date: " + datePicker.getValue());
        }
    }
    @FXML
    public void displayPieChart() {
        // Get selected site IDs
        String selectedSiteIdOne = siteIdOneCombo.getValue();
        String selectedSiteIdTwo = siteIdTwoCombo.getValue();


        // Variables to store total values for the selected site IDs
        int totalSiteOne = 0;
        int totalSiteTwo = 0;

        // Calculate totals for each site
        for (Data dataItem : dataArrayList) {
            if (dataItem.getSiteId() == Integer.parseInt(selectedSiteIdOne)) {
                totalSiteOne += dataItem.getTotal();
            }
            if (dataItem.getSiteId() == Integer.parseInt(selectedSiteIdTwo)) {
                totalSiteTwo += dataItem.getTotal();
            }
        }

        // Create PieChart data
        PieChart.Data siteOneData = new PieChart.Data("Site " + selectedSiteIdOne, totalSiteOne);
        PieChart.Data siteTwoData = new PieChart.Data("Site " + selectedSiteIdTwo, totalSiteTwo);

        // Add data to the PieChart
        pieChart.getData().addAll(siteOneData, siteTwoData);
    }


}