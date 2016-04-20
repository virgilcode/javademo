/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package virgil.meanback;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.gargoylesoftware.htmlunit.BrowserVersion;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.html.HtmlElement;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.logging.Level;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import net.sf.json.JSONObject;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import virgil.eastmoney.tool.StockSearch;

/**
 *
 * @author Virgil
 */
public class StockUtil {

    WebClient wc = new WebClient(BrowserVersion.CHROME);

    public List<Stock> getAll() throws Exception {
        List<Stock> list = new ArrayList<>();
        String url1 = "http://vip.stock.finance.sina.com.cn/q/go.php/vInvestConsult/kind/qgqp/index.phtml?s_i=&s_a=&s_c=&s_t=sh_a&s_z=&num=60&p=";
        List<Stock> list1 = getAllChinaStocks(url1);
        String url2 = "http://vip.stock.finance.sina.com.cn/q/go.php/vInvestConsult/kind/qgqp/index.phtml?s_i=&s_a=&s_c=&s_t=sz_a&s_z=&num=60&p=";
        List<Stock> list2 = getAllChinaStocks(url2);
        ObjectMapper mapper = new ObjectMapper();
        for (Iterator<Stock> it1 = list1.iterator(); it1.hasNext();) {
            list.add(it1.next());
        }
        for (Iterator<Stock> it2 = list2.iterator(); it2.hasNext();) {
            list.add(it2.next());
        }

        return list;
    }

    public List<Stock> getAllChinaStocks(String url) throws Exception {
        List<Stock> list = new ArrayList<>();
        int p = 1;
        label1:
        do {
            Document doc = Jsoup.connect(url + p).userAgent("Mozilla").cookie("auth", "token").timeout(8000).get();
            Elements datael = doc.select("#dataTable tbody tr");
            for (Element data : datael) {
                String code = data.select("td:eq(0)").text();
                if (code.contains("300")) {
                } else {
                    Stock stock = new Stock();
                    stock.setCode(code);
                    String name = data.select("td:eq(1)").text();
                    stock.setName(name);
                    String debate = data.select("td:eq(2)").text();
                    stock.setXinlangDebate(debate);
                    double price = Double.parseDouble(data.select("td:eq(3)").text());
                    stock.setNowPrice(price);
                    double changeAmount = Double.parseDouble(data.select("td:eq(4)").text());
                    stock.setChangeAmount(changeAmount);
                    double changeQuote = Double.parseDouble(data.select("td:eq(5)").text());
                    stock.setChangeQuote(changeQuote);
                    double volume = Double.parseDouble(data.select("td:eq(10)").text());
                    stock.setVolume(volume);
                    list.add(stock);
                }

            }
            Elements el = doc.select(".pages").select("a");
            label2:
            for (Element e : el) {
                if (e.text().contains("下一页") && e.className().contains("nolink")) {
                    break label1;
                }
            }
            p++;
        } while (p > 0);
        return list;
    }

    @SuppressWarnings("")
    public List<Stock> searchStockByPrice(double price_min, double price_max, boolean isErrorPlace, List<Stock> list) throws Exception {
        List<Stock> res = new ArrayList<>();
        ObjectMapper mapper = new ObjectMapper();
        for (Iterator<Stock> it = list.iterator(); it.hasNext();) {
            Stock s = it.next();
            double price = s.getNowPrice();
            if ((price > price_min) && (price < price_max)) {
                if (isErrorPlace == false) {
                    res.add(s);
                } else {
                    System.out.println(mapper.writeValueAsString(s.getName()));
                    HistoryInfo info = new HistoryInfo();
                    String url = "http://q.stock.sohu.com/cn/" + s.getCode() + "/lshq.shtml";
                    Stock ns = info.parse(url);
                    String[] ss = ns.getMeanGroup(10, 10).get(0);
                    float sub = Float.parseFloat(ss[2]);
                    float error = Float.parseFloat(ss[3]);
                    if (sub > error * 2) {
                        System.out.println(mapper.writeValueAsString(s));
                        res.add(s);
                    }
                }
            }
        }
        for (Iterator<Stock> it1 = res.iterator(); it1.hasNext();) {
            System.out.println(mapper.writeValueAsString(it1.next()));
        }
        return res;
    }

    public boolean findMatcherStockByCode(String code) throws Exception {
        HistoryInfo info = new HistoryInfo();
        String url = "http://q.stock.sohu.com/cn/" + code + "/lshq.shtml";
        Stock ns = info.parse(url);
        try {
            String[] ss = ns.getMeanGroup(10, 10).get(0);
            float sub = Float.parseFloat(ss[2]);
            float error = Float.parseFloat(ss[3]);
            if (sub > error * 2) {
                return true;
            }
        } catch (Exception e) {
        }
        return false;
    }

    //主力资金流入
    public void getMeanBackStockByBuys(double price_min, double price_max, int pages) throws Exception {
        WebClient wc = new WebClient(BrowserVersion.CHROME);
        StockSearch search = new StockSearch(wc);
        java.util.logging.Logger.getLogger("com.gargoylesoftware.htmlunit").setLevel(Level.OFF);
        List<Stock> stocks = search.searchStockByBuys(price_min, price_max, pages);
        List<Stock> list = new ArrayList<>();
        for (Iterator<Stock> it = stocks.iterator(); it.hasNext();) {
            boolean res = findMatcherStockByCode(it.next().getCode());
            if (res) {
                list.add(it.next());
            }
        }
        ObjectMapper mapper = new ObjectMapper();
        System.out.println("主力查询结果：");
        for (Iterator<Stock> it = list.iterator(); it.hasNext();) {
            System.out.println(mapper.writeValueAsString(it.next()));
        }

    }

    //换手率
    public void getMeanBackStockByChange(double price_min, double price_max, int pages) throws Exception {
        WebClient wc = new WebClient(BrowserVersion.CHROME);
        StockSearch search = new StockSearch(wc);
        java.util.logging.Logger.getLogger("com.gargoylesoftware.htmlunit").setLevel(Level.OFF);
        List<Stock> stocks = search.searchStockByChange(price_min, price_max, pages);
        List<Stock> list = new ArrayList<>();
        for (Iterator<Stock> it = stocks.iterator(); it.hasNext();) {
            boolean res = findMatcherStockByCode(it.next().getCode());
            if (res) {
                list.add(it.next());
            }
        }
        ObjectMapper mapper = new ObjectMapper();
        System.out.println("换手率查询结果：");
        for (Iterator<Stock> it = list.iterator(); it.hasNext();) {
            System.out.println(mapper.writeValueAsString(it.next()));
        }
    }

    public void isSuspension(String url) throws IOException {
        wc.getOptions().setUseInsecureSSL(true);
        wc.getOptions().setJavaScriptEnabled(true); // 启用JS解释器，默认为true
        wc.getOptions().setCssEnabled(false); // 禁用css支持
        wc.getOptions().setThrowExceptionOnScriptError(false); // js运行错误时，是否抛出异常
        wc.getOptions().setTimeout(50000); // 设置连接超时时间 ，这里是10S。如果为0，则无限期等待
        wc.getOptions().setDoNotTrackEnabled(false);
        HtmlPage page = wc.getPage(url);
        HtmlElement documentElement = page.getDocumentElement();
        Document ndoc = Jsoup.parse(documentElement.asXml());
        System.out.println(ndoc);
    }

    //涨幅
    public void getMeanBackStockByRise(double price_min, double price_max, int pages) throws Exception {
        WebClient wc = new WebClient(BrowserVersion.CHROME);
        StockSearch search = new StockSearch(wc);
        java.util.logging.Logger.getLogger("com.gargoylesoftware.htmlunit").setLevel(Level.OFF);
        List<Stock> stocks = search.searchStockByRise(price_min, price_max, pages);
        List<Stock> list = new ArrayList<>();
        for (Iterator<Stock> it = stocks.iterator(); it.hasNext();) {
            boolean res = findMatcherStockByCode(it.next().getCode());
            if (res) {
                list.add(it.next());
            }
        }
        ObjectMapper mapper = new ObjectMapper();
        System.out.println("涨幅查询结果：");
        for (Iterator<Stock> it = list.iterator(); it.hasNext();) {
            System.out.println(mapper.writeValueAsString(it.next()));
        }

    }

    public static void main(String[] args) throws Exception {
        StockUtil util = new StockUtil();
//        List<Stock> list = util.getAll();
//        util.searchStockByPrice(0.0, 10.0, true, list);
        util.getMeanBackStockByBuys(0, 10, 8);
        //util.getMeanBackStockByChange(0, 20, 5);
        //util.getMeanBackStockByRise(0, 10, 5);
    }
}
