package org.example.projectweek48baeredygtighed;


import java.sql.Date;

public class Data {

    private int id; // Record ID
    private int siteId; // ID of the Solar Site
    private int total; // Total lifetime production of the site.
    private int online; // Online production of the site (While connected to the server/monitoring system)
    private int offline; // Offline production of the site (While disconnected from the server/monitoring system)
    private Date date; // Date of the production (year, month, day)
    private int hour; // Hour of the record (What time a day the record is from)


    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getSiteId() {
        return siteId;
    }

    public void setSiteId(int siteId) {
        this.siteId = siteId;
    }

    public int getTotal() {
        return total;
    }

    public void setTotal(int total) {
        this.total = total;
    }

    public int getOnline() {
        return online;
    }

    public void setOnline(int online) {
        this.online = online;
    }

    public int getOffline() {
        return offline;
    }

    public void setOffline(int offline) {
        this.offline = offline;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public int getHour() {
        return hour;
    }

    public void setHour(int hour) {
        this.hour = hour;
    }
}
