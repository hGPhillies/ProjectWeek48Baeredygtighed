package org.example.projectweek48baeredygtighed;

import java.io.BufferedReader;
import java.io.FileReader;
import java.sql.Date;
import java.util.ArrayList;

public class DataHandler {
    public static ArrayList<Data> dataArrayList;

    /**
     * Reads the SolarData.tsv file and adds all records to the dataArrayList ArrayList.
     * Gives error "Something went wrong" if the file is not found.
     */
    public static void updateDataFromSource() {
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
                for (char c : lineItems[1].toCharArray()) {
                    if (c == 'T') {
                        isDate = false;
                    } else if (isDate) {
                        date.append(c);
                    } else {
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
    private void printAllData() {
        for (Data dataItem : DataHandler.dataArrayList) {
            System.out.println(dataItem.getId() + "\t" + dataItem.getDate() + "\t" + dataItem.getHour() + "\t" + dataItem.getSiteId() +
                    "\t" + dataItem.getTotal() + "\t" + dataItem.getOnline() + "\t" + dataItem.getOffline());
        }
    }
}
