/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package virgil.eastmoney.tool;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.gargoylesoftware.htmlunit.BrowserVersion;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import com.steadystate.css.parser.Token;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.logging.Level;
import net.sf.json.JSONObject;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import virgil.meanback.Stock;

/**
 *
 * @author virgil
 */
public class StockSearch {

    WebClient wc = new WebClient(BrowserVersion.CHROME);

    public StockSearch(WebClient wc) {
        wc.getOptions().setUseInsecureSSL(true);
        wc.getOptions().setJavaScriptEnabled(true); // 启用JS解释器，默认为true  
        wc.getOptions().setCssEnabled(false); // 禁用css支持  
        wc.getOptions().setThrowExceptionOnScriptError(false); // js运行错误时，是否抛出异常  
        wc.getOptions().setTimeout(100000); // 设置连接超时时间 ，这里是10S。如果为0，则无限期等待  
        wc.getOptions().setDoNotTrackEnabled(false);
    }

    private static final String riseBaseUrl = "http://hqdigi2.eastmoney.com/EM_Quote2010NumericApplication/index.aspx?type=s&sortType=C&sortRule=-1&pageSize=20&page=";

    //搜寻涨幅榜
    public List<Stock> searchStockByRise(double minPrice, double maxPrice, int num) throws Exception {
        java.util.logging.Logger.getLogger("com.gargoylesoftware.htmlunit").setLevel(Level.OFF);
        List<Stock> list = new ArrayList<>();
        String[] urls = new String[2 * num];
        for (int i = 0; i < num; i++) {
            urls[i] = riseBaseUrl + (i + 1) + "&jsName=quote_123&style=10";//上证
        }
        for (int i = num; i < 2 * num; i++) {
            urls[i] = riseBaseUrl + (i - 9) + "&jsName=quote_123&style=20";//深证
        }
        for (String url : urls) {
            //System.out.println(url);
            HtmlPage page = wc.getPage(url);
            //System.out.println(page.asText());
            //Document doc = Jsoup.connect(url).timeout(10000).get();
            // String json = doc.body().text();
            String json = page.asText();
            json = json.substring(json.indexOf("{"), json.length());
            JSONObject jo = JSONObject.fromObject(json);
            String data = jo.getString("rank");
            String[] arrays = data.split("\"");
            for (String a : arrays) {
                if (a.length() > 5) {
                    String[] aa = a.split(",");
                    String code = aa[1];
                    String name = aa[2];
                    String nowPrice = aa[5];
                    if (nowPrice.length() > 2) {
                        Double price = Double.parseDouble(nowPrice);
                        if (price > minPrice && price < maxPrice) {
                            Stock stock = new Stock();
                            stock.setCode(code);
                            stock.setName(name);
                            stock.setNowPrice(Double.parseDouble(nowPrice));
                            list.add(stock);
                        }
                    }

                }
            }
        }
        ObjectMapper mapper = new ObjectMapper();
        for (Iterator it = list.iterator(); it.hasNext();) {
            System.out.println(mapper.writeValueAsString(it.next()));
        }
        return list;
    }

    private static final String changeBaseUrl = "http://hqdigi2.eastmoney.com/EM_Quote2010NumericApplication/index.aspx?type=s&sortType=J&sortRule=-1&pageSize=20&page=";

    //搜寻换手率
    public List<Stock> searchStockByChange(double minPrice, double maxPrice, int num) throws Exception {
        java.util.logging.Logger.getLogger("com.gargoylesoftware.htmlunit").setLevel(Level.OFF);
        List<Stock> list = new ArrayList<>();
        String[] urls = new String[2 * num];
        for (int i = 0; i < num; i++) {
            urls[i] = changeBaseUrl + (i + 1) + "&jsName=quote_123&style=10";//上证
        }
        for (int i = num; i < 2 * num; i++) {
            urls[i] = changeBaseUrl + (i - 9) + "&jsName=quote_123&style=20";//深证
        }
        for (String url : urls) {
            //System.out.println(url);
            HtmlPage page = wc.getPage(url);
            //System.out.println(page.asText());
            //Document doc = Jsoup.connect(url).timeout(10000).get();
            // String json = doc.body().text();
            String json = page.asText();
            json = json.substring(json.indexOf("{"), json.length());
            JSONObject jo = JSONObject.fromObject(json);
            String data = jo.getString("rank");
            String[] arrays = data.split("\"");
            for (String a : arrays) {
                if (a.length() > 5) {
                    String[] aa = a.split(",");
                    String code = aa[1];
                    String name = aa[2];
                    String nowPrice = aa[5];
                    if (nowPrice.length() > 2) {
                        Double price = Double.parseDouble(nowPrice);
                        if (price > minPrice && price < maxPrice) {
                            Stock stock = new Stock();
                            stock.setCode(code);
                            stock.setName(name);
                            stock.setNowPrice(Double.parseDouble(nowPrice));
                            list.add(stock);
                        }
                    }

                }
            }
        }
        ObjectMapper mapper = new ObjectMapper();
        for (Iterator it = list.iterator(); it.hasNext();) {
            System.out.println(mapper.writeValueAsString(it.next()));
        }
        return list;
    }

    //搜寻主力增仓
    private static final String buysBaseUrl = "http://nufm.dfcfw.com/EM_Finance2014NumericApplication/JS.aspx/JS.aspx?type=ct&st=(FFARank)&sr=1&p=";
    private static final String attach = "&ps=50&js=var%20TVAigeSB={pages:(pc),data:[(x)]}&token=894050c76af8597a853f5b408b759f5d&cmd=C._A&sty=DCFFITAMA";

    public List<Stock> searchStockByBuys(double minPrice, double maxPrice, int num) throws Exception {
        List<Stock> list = new ArrayList<>();
        String[] urls = new String[num];
        for (int i = 0; i < num; i++) {
            urls[i] = buysBaseUrl + (i + 1) + attach;
        }
        for (String url : urls) {
            HtmlPage page = wc.getPage(url);
            String json = page.asText();
            json = json.substring(json.indexOf("{"), json.length());
            JSONObject jo = JSONObject.fromObject(json);
            String data = jo.getString("data");
            String[] arrays = data.split("\"");
            for (String a : arrays) {
                if (a.length() > 5) {
                    String[] aa = a.split(",");
                    String code = aa[1];
                    String name = aa[2];
                    String nowPrice = aa[3];
                    if (nowPrice.length() > 2) {
                        Double price = Double.parseDouble(nowPrice);
                        if (price > minPrice && price < maxPrice) {
                            Stock stock = new Stock();
                            stock.setCode(code);
                            stock.setName(name);
                            stock.setNowPrice(Double.parseDouble(nowPrice));
                            list.add(stock);
                        }
                    }

                }
            }
        }
        ObjectMapper mapper = new ObjectMapper();
        for (Iterator it = list.iterator(); it.hasNext();) {
            System.out.println(mapper.writeValueAsString(it.next()));
        }
        return list;
    }

    public static void main(String[] args) throws Exception {
        StockSearch search = new StockSearch(new WebClient(BrowserVersion.CHROME));
        //search.searchStockByRise(0, 10, 5);
        // search.searchStockByChange(0, 10, 5);
        search.searchStockByBuys(0, 10, 5);
    }
}
