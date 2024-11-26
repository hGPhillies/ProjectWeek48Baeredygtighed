package org.example.projectweek48baeredygtighed;

import javafx.fxml.FXML;
import javafx.scene.control.Label;

import java.io.*;
import java.util.ArrayList;
import java.sql.Date;
import java.util.Scanner;

public class HelloController {

    private ArrayList<Data> dataArrayList;

    @FXML
    public void initialize() throws IOException
    {
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
                for (char c : lineItems[1].toCharArray())
                {
                    if (c == 'T')
                    {
                        isDate = false;
                    }
                    else if (isDate)
                    {
                        date += c;
                    }
                    else
                    {
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

        for (Data dataItem : dataArrayList)
        {
            System.out.println(dataItem.getId() + "\t" + dataItem.getDate() + "\t" + dataItem.getHour() + "\t" + dataItem.getSiteId() +
                    "\t" + dataItem.getTotal() + "\t" + dataItem.getOnline() + "\t" + dataItem.getOffline());
        }
    }
}