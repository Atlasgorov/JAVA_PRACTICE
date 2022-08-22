package com.common.problem;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.TimeUnit;

public class OpenDay {
    // 工作日list
    public static List<String> workDayList = new ArrayList<>();
    // 非工作日list
    public static List<String> holidayList = new ArrayList<>();

    public static List<Day> dayList = new ArrayList<>(1024 * 8 * 8);
    public static Map<String, Day> dayPropertyMap = new HashMap<>();

    public static volatile CopyOnWriteArrayList<String> TOPENDAYLIST = new CopyOnWriteArrayList<String>();
    public static volatile ConcurrentHashMap<String, String> TOPENDAYMAP = new ConcurrentHashMap<String, String>();

    public static void main(String[] args) throws ParseException {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
        Date parse = null;
        try {
            parse = simpleDateFormat.parse("2022-01-01");
        } catch (ParseException e) {
            throw new RuntimeException(e);
        }
        Calendar instance = Calendar.getInstance();
        instance.setTime(parse);
        long startTime1 = System.nanoTime();
        for (int i = 0; i < 3651; i++) {
            String format = simpleDateFormat.format(instance.getTime());
            if (instance.get(Calendar.DAY_OF_WEEK) == Calendar.SATURDAY
                    || instance.get(Calendar.DAY_OF_WEEK) == Calendar.SUNDAY) {
                holidayList.add(format);
                Day holiday = new Day(instance.getTime(), format, false);
                dayPropertyMap.put(format, holiday);
                dayList.add(holiday);
                TOPENDAYLIST.add(format);
                TOPENDAYMAP.put(format,"0");
                int daySizeUntilNow = dayList.size();
                int targetIndex = daySizeUntilNow - 2;
                while (targetIndex >= 0) {
                    Day day = dayList.get(targetIndex);
                    if (day.isTridingDay()) {
                        holiday.setPrevTridingDay(day);
                        break;
                    }
                    targetIndex--;
                }
            } else {
                Day workDay = new Day(instance.getTime(), format, true);
                workDayList.add(format);
                dayPropertyMap.put(format, workDay);
                dayList.add(workDay);
                TOPENDAYLIST.add(format);
                TOPENDAYMAP.put(format,"1");
                // 找到自己的上一个交易日
                int daySizeUntilNow = dayList.size();
                int targetIndex = daySizeUntilNow - 2;
                while (targetIndex >= 0) {
                    Day day = dayList.get(targetIndex);
                    if (day.isTridingDay()) {
                        workDay.setPrevTridingDay(day);
                        day.setNextTridingDay(workDay);
                        break;
                    } else {
                        day.setNextTridingDay(workDay);
                    }
                    targetIndex--;
                }
                //
            }
            instance.add(Calendar.DAY_OF_MONTH, 1);
        }
        long endTime1 = System.nanoTime();

        Date date = new Date();
        startTime1 = System.nanoTime();
        for (int i = 0; i < 1000000; i++) {
            Date tridingDayMyVersion = getTridingDayMyVersion(date, "-100");
        }
        Date tridingDayMyVersion = getTridingDayMyVersion(date, "-100");
        endTime1 = System.nanoTime();
        System.out.println(new SimpleDateFormat("yyyy-MM-dd").format(tridingDayMyVersion)+",共耗时："
                +(TimeUnit.MILLISECONDS.convert(endTime1-startTime1,TimeUnit.NANOSECONDS)));
    }

    /**
     * @param startDay
     * @param interval
     * @return
     * @throws ParseException
     */
    public static Date getTridingDayMyVersion(Date startDay, String interval) throws ParseException {
        Objects.requireNonNull(startDay);
        Objects.requireNonNull(interval);
        int i = Integer.parseInt(interval);
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
        String format = simpleDateFormat.format(startDay);
        Day day = dayPropertyMap.get(format);
        Objects.requireNonNull(day);
        if (i == 0) {
            return day.getDate();
        }
        if (i > 0) {
            Day x = day;
            for (int j = 0; j < i; j++) {
                if(Objects.isNull(x.getNextTridingDay())){
                    throw new RuntimeException("There is no valid next triding day.");
                }
                x = x.getNextTridingDay();
            }
            return x.getDate();
        }
        if (i < 0) {
            int limit = -i;
            Day x = day;
            for (int j = 0; j < limit; j++) {
                if(Objects.isNull(x.getPrevTridingDay())){
                    throw new RuntimeException("There is no valid previous triding day.");
                }
                x = x.getPrevTridingDay();
            }
            return x.getDate();
        }
        throw new RuntimeException("impossible error!");
    }
}
