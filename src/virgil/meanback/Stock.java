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
import java.util.LinkedHashMap;
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

    /*
     * @days 几日均值
     * @listDays 判断的时间长度数据
     */
    public List<String[]> getMeanGroup(int days, int listDays) {
        List<String[]> l = new ArrayList<>();
        float[] f = new float[listDays];
        for (int i = 0; i < listDays; i++) {
            String[] s = new String[2];
            List<DayInfo> dl = getBeforeDaysData(this.list.get(i).getDateString(), days);
            f[i] = getMeanValue(days, dl);
            s[0] = this.list.get(i).getDateString();
            s[1] = String.valueOf(f[i]);
            l.add(s);
        }
        return l;
    }

    public float getMeanValue(int days, List<DayInfo> list) {
        float sum = 0;
        for (int i = 0; i < days; i++) {
            sum += Float.parseFloat(list.get(i).getClose());
        }
        float a = sum / days;
        return (float) (Math.round(a * 100)) / 100;
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
