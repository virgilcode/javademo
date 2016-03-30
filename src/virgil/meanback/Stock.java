/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package virgil.meanback;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 *
 * @author virgil
 */
public class Stock {

    private String name;
    private String code;
    private List<DayInfo> list;
    private Map<String, DayInfo> map;
    private float meanValue;
    private float subValue;
    private float subMeanvalue;
    private float subBaseError;

    public int getDayIndex(String date) {
        for (int i = 0; i < this.list.size(); i++) {
            if (date == null ? list.get(i).getDateString() == null : date.equals(list.get(i).getDateString())) {
                return i;
            }
        }
        return 0;
    }

    public List<DayInfo> getBeforeDaysData(String date, int days) {
        int p = this.getDayIndex(date);
        List<DayInfo> dayList = new ArrayList<>();
        for (int i = p; i <= p + days; i++) {
            dayList.add(this.list.get(i));
        }
        return dayList;
    }

    public Map<String, String> getMeanGroup(int days) {
        Map<String, String> map = new HashMap<>();
        float[] f = new float[days];
        for (int i = 0; i < days; i++) {
            List<DayInfo> dl = getBeforeDaysData(this.list.get(i).getDateString(), days);
            f[i] = getMeanValue(days, dl);
            System.out.println(f[i]);
            map.put(this.list.get(i).getDateString(), String.valueOf(f[i]));
        }
        return map;
    }

    public float getMeanValue(int days, List<DayInfo> list) {
        float sum = 0;
        for (int i = 0; i < days; i++) {
            sum = sum + Float.parseFloat(list.get(i).getClose());
        }
        return sum / days;
    }

    public Map<String, DayInfo> getMap() {
        return map;
    }

    public void setMap(Map<String, DayInfo> map) {
        this.map = map;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public List<DayInfo> getList() {
        return list;
    }

    public void setList(List<DayInfo> list) {
        this.list = list;
    }

}
