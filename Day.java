package com.common.problem;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * author zhengjg
 */
public class Day {

    /**
     * 日期
     */
    private Date date;
    /**
     * 日期字符串
     */
    private String dateStr;

    /**
     * 是否是工作日标志，1：是,0，否
     */
    private boolean isTridingDay;


    private String nextTridingDateStr;

    private String prevTridingDateStr;

    public String getNextTridingDateStr() {
        return nextTridingDateStr;
    }

    public void setNextTridingDateStr(String nextTridingDateStr) {
        this.nextTridingDateStr = nextTridingDateStr;
    }

    public String getPrevTridingDateStr() {
        return prevTridingDateStr;
    }

    public void setPrevTridingDateStr(String prevTridingDateStr) {
        this.prevTridingDateStr = prevTridingDateStr;
    }

    public Date getDate() {
        return date;
    }

    public String getDateStr() {
        return dateStr;
    }

    public boolean isTridingDay() {
        return isTridingDay;
    }


    Day(Date date, String dateStr, boolean isTridingDay){
        this.date = date;
        this.dateStr = dateStr;
        this.isTridingDay = isTridingDay;
    }

    private Day nextTridingDay;

    private Day prevTridingDay;

    public Day getNextTridingDay() {
        return nextTridingDay;
    }

    public void setNextTridingDay(Day nextTridingDay) {
        this.nextTridingDay = nextTridingDay;
        this.nextTridingDateStr = new SimpleDateFormat("yyyy-MM-dd").format(nextTridingDay.getDate());
    }

    public Day getPrevTridingDay() {
        return prevTridingDay;
    }

    public void setPrevTridingDay(Day prevTridingDay) {
        this.prevTridingDay = prevTridingDay;
        this.prevTridingDateStr = new SimpleDateFormat("yyyy-MM-dd").format(prevTridingDay.getDate());
    }

    @Override
    public String toString() {
        return "Day{" +
//                "date=" + date +
                ", dateStr='" + dateStr + '\'' +
                ", isTridingDay=" + isTridingDay +
//                ", position=" + position +
                ", nextTridingDateStr='" + nextTridingDateStr + '\'' +
                ", prevTridingDateStr='" + prevTridingDateStr + '\'' +
//                ", nextTridingDay=" + nextTridingDay +
//                ", prevTridingDay=" + prevTridingDay +
                '}';
    }
}
