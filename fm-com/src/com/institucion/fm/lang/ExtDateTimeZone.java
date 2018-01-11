package com.institucion.fm.lang;

import java.io.Serializable;


public class ExtDateTimeZone implements Serializable {
	private static final long serialVersionUID = 1L;
    private String gmtID;
	private Integer dayLightStartMonth;
    private Integer dayLightStartDay;
    private Integer dayLightStartDayOfWeek;
    private Integer dayLightStartTime;
    private Integer dayLightEndMonth;
    private Integer dayLightEndDay;
    private Integer dayLightEndDayOfWeek;
    private Integer dayLightEndTime;
    private Integer year;
    private Boolean hasdaylight;
   
 
    public Integer getDayLightStartMonth() {
        return dayLightStartMonth;
    }
    public void setDayLightStartMonth(Integer dayLightStartMonth) {
        this.dayLightStartMonth = dayLightStartMonth;
    }
    public Integer getDayLightStartDay() {
        return dayLightStartDay;
    }
    public void setDayLightStartDay(Integer dayLightStartDay) {
        this.dayLightStartDay = dayLightStartDay;
    }
    public Integer getDayLightStartDayOfWeek() {
        return dayLightStartDayOfWeek;
    }
    public void setDayLightStartDayOfWeek(Integer dayLightStartDayOfWeek) {
        this.dayLightStartDayOfWeek = dayLightStartDayOfWeek;
    }
    public Integer getDayLightStartTime() {
        return dayLightStartTime;
    }
    public void setDayLightStartTime(Integer dayLightStartTime) {
        this.dayLightStartTime = dayLightStartTime;
    }
    public Integer getDayLightEndMonth() {
        return dayLightEndMonth;
    }
    public void setDayLightEndMonth(Integer dayLightEndMonth) {
        this.dayLightEndMonth = dayLightEndMonth;
    }
    public Integer getDayLightEndDay() {
        return dayLightEndDay;
    }
    public void setDayLightEndDay(Integer dayLightEndDay) {
        this.dayLightEndDay = dayLightEndDay;
    }
    public Integer getDayLightEndDayOfWeek() {
        return dayLightEndDayOfWeek;
    }
    public void setDayLightEndDayOfWeek(Integer dayLightEndDayOfWeek) {
        this.dayLightEndDayOfWeek = dayLightEndDayOfWeek;
    }
    public Integer getDayLightEndTime() {
        return dayLightEndTime;
    }
    public void setDayLightEndTime(Integer dayLightEndTime) {
        this.dayLightEndTime = dayLightEndTime;
    }
    public Integer getYear() {
        return year;
    }
    public void setYear(Integer year) {
        this.year = year;
    }
    public String getGmtID() {
        return gmtID;
    }
    public void setGmtID(String gmtID) {
        this.gmtID = gmtID;
    }
    public Boolean getHasdaylight() {
        return hasdaylight;
    }
    public void setHasdaylight(Boolean hasdaylight) {
        this.hasdaylight = hasdaylight;
    }
}