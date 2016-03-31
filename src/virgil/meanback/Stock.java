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
            String[] s = new String[5];
            List<DayInfo> dl = getBeforeDaysData(this.list.get(i).getDateString(), days);
            f[i] = getMeanValue(days, dl);
            float sub=f[i]-Float.parseFloat(this.list.get(i).getClose());
            s[0] = this.list.get(i).getDateString();
            s[1] = String.valueOf(f[i]);
            s[2]=String.valueOf(sub);
            s[3]=String.valueOf(getBeforeDaysSubValue(this.list.get(i).getDateString(),days));
            l.add(s);
        }
        return l;
    }
    public float getBeforeDaysSubValue(String date,int days){
        List<DayInfo> dl = getBeforeDaysData(date, days);
        int p=getDayIndex(date);
        float error=0;
        for(int i=p;i<p+days;i++){
            float sub=getSubValue(this.getList().get(i).getClose(), days)[1];
            float submean=getSubMean(this.getList().get(i).getClose(), days);
            error += (sub - submean) * (sub - submean);
        }
        error /= days;
        error=(float) Math.sqrt(error);
        return error;
    }
    public float getSubMean(String date,int days){
        int p=getDayIndex(date);
        float submean=0;
        float error=0;
        for(int i=p;i<p+days;i++){
            float[] f=getSubValue(this.getList().get(i).getClose(), days);
            submean+=f[1];
        }
        return submean/days;
    }
    public float[] getSubValue(String date,int days){
        int p=getDayIndex(date);
        float[] f=new float[2];
        List<DayInfo> d = getBeforeDaysData(date, days);
        float mean=getMeanValue(days, d);
        float sub=mean-Float.parseFloat(this.getList().get(p).getClose());
        f[0]=mean;
        f[1]=sub;
        return f;
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
