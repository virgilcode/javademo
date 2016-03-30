/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package virgil.meanback;

import com.gargoylesoftware.htmlunit.BrowserVersion;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.html.HtmlElement;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import org.jfree.data.time.TimeSeries;
import org.jfree.data.xy.XYDataset;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

/**
 *
 * @author virgil
 */
public class HistoryInfo {

    public Stock parse(String url) throws Exception {
        Stock stock = new Stock();
        List<DayInfo> list = new ArrayList<>();
        /**
         * HtmlUnit请求web页面
         */
        WebClient wc = new WebClient(BrowserVersion.CHROME);
        wc.getOptions().setUseInsecureSSL(true);
        wc.getOptions().setJavaScriptEnabled(true); // 启用JS解释器，默认为true  
        wc.getOptions().setCssEnabled(false); // 禁用css支持  
        wc.getOptions().setThrowExceptionOnScriptError(false); // js运行错误时，是否抛出异常  
        wc.getOptions().setTimeout(30000); // 设置连接超时时间 ，这里是10S。如果为0，则无限期等待  
        wc.getOptions().setDoNotTrackEnabled(false);
        HtmlPage page = wc.getPage(url);
        HtmlElement documentElement = page.getDocumentElement();
        Document doc = Jsoup.parse(documentElement.asXml());
        String name = doc.select("#BIZ_IS_Name").text();
        String code = doc.select(".BIZ_IS_price_id span").text();
        code = code.substring(code.indexOf("(") + 2, code.length() - 1);
        Elements els = doc.select("#BIZ_hq_historySearch tbody tr");
        stock.setCode(code);
        stock.setName(name);
        int count = 0;
        for (Element el : els) {
            if (!el.html().contains("sum")) {
                DayInfo dayInfo = new DayInfo();
                String dateString = el.select("td.e1").text();
                SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
                Date date = format.parse(dateString);
                String open = el.select("td").eq(1).text();
                String close = el.select("td").eq(2).text();
                String low = el.select("td").eq(5).text();
                String high = el.select("td").eq(6).text();
                String volume = el.select("td").eq(7).text();
                dayInfo.setClose(close);
                dayInfo.setDateString(dateString);
                dayInfo.setHigh(high);
                dayInfo.setLow(low);
                dayInfo.setOpen(open);
                dayInfo.setVolume(volume);
                dayInfo.setDate(date);
                list.add(dayInfo);
                count++;
                if (list.size() > 79) {
                    break;
                }
            }
        }
        stock.setList(list);
        return stock;
    }

    public void printChart(Map<String, String> map) {
        TimeSeries timeseries = new TimeSeries("20日均线",org.jfree.data.time.Month.class);
        for(Map.Entry<String,String> entry:map.entrySet()){
            
        }
        XYDataset xydataset;
    }

    public static void main(String[] args) throws Exception {
        String url = "http://q.stock.sohu.com/cn/600636/lshq.shtml";
        HistoryInfo hq = new HistoryInfo();
        Stock stock = hq.parse(url);
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        Date date = format.parse("2016-03-30");
        System.out.println(stock.getList().get(0).getDate().toString());
        stock.getBeforeDaysData("2016-03-29", 20);
        System.out.println(stock.getMeanGroup(20));

    }
}
