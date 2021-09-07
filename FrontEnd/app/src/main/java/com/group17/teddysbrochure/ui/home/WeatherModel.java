package com.group17.teddysbrochure.ui.home;

public class WeatherModel {
    String day;
    String high;
    String low;

    public WeatherModel(String day, String high, String low) {
        this.day = day;
        this.high = high;
        this.low = low;
    }

    public String getDay() {
        return this.day;
    }

    public String getHigh() {
        return this.high;
    }

    public String getLow() {
        return this.low;
    }
}
