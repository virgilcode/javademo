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
import java.awt.BasicStroke;
import java.awt.Font;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.logging.Level;
import org.jfree.chart.ChartColor;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartFrame;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.StandardChartTheme;
import org.jfree.chart.axis.CategoryAxis;
import org.jfree.chart.axis.CategoryLabelPositions;
import org.jfree.chart.axis.ValueAxis;
import org.jfree.chart.labels.StandardCategoryItemLabelGenerator;
import org.jfree.chart.plot.CategoryPlot;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.plot.XYPlot;
import org.jfree.chart.renderer.category.LineAndShapeRenderer;
import org.jfree.data.category.DefaultCategoryDataset;
import org.jfree.data.time.Day;
import org.jfree.data.time.Month;
import org.jfree.data.time.TimeSeries;
import org.jfree.data.time.TimeSeriesCollection;
import org.jfree.data.time.Week;
import org.jfree.data.xy.XYDataset;
import org.jfree.data.xy.XYSeries;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

/**
 *
 * @author virgil
 */
public class HistoryInfo {

    /**
     * 
     * @param url
     * @return
     * @throws Exception
     */
    @SuppressWarnings("")
    public Stock parse(String url) throws Exception {
        java.util.logging.Logger.getLogger("com.gargoylesoftware.htmlunit").setLevel(Level.OFF);
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
        wc.getOptions().setTimeout(50000); // 设置连接超时时间 ，这里是10S。如果为0，则无限期等待  
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
                double cd = Double.parseDouble(close);
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
        ;
        stock.setList(list);
        return stock;
    }

    public double[] getAxiasThresold(Stock stock, List<String[]> list) {
        double[] d = new double[2];
        double min = 1000.0;
        double max = 0.0;
        List<DayInfo> l = stock.getList();
        for (int i = 0; i < list.size(); i++) {
            DayInfo info = l.get(i);
            double dd = Double.parseDouble(info.getClose());
            if (dd > max) {
                max = dd;
            }
            if (dd < min) {
                min = dd;
            }
        }
        d[0] = min;
        d[1] = max;
        return d;
    }

    public XYDataset getDataSet(Stock stock, List<String[]> list) throws Exception {
        TimeSeriesCollection timeseriescollection = new TimeSeriesCollection();
        TimeSeries time1 = new TimeSeries("MD20");
        TimeSeries time2 = new TimeSeries("日K");
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        for (int i = 0; i < list.size(); i++) {
            String[] s = (String[]) list.get(i);
            Date d = format.parse(s[0]);
            Calendar cc = Calendar.getInstance();
            cc.setTime(d);
            double db = Double.parseDouble(stock.getList().get(i).getClose());
            Day day = new Day(d);
            time1.addOrUpdate(day, db);
            double dayclose = Double.parseDouble(stock.getList().get(i).getClose());
            time2.addOrUpdate(day, dayclose);
        }
        timeseriescollection.addSeries(time1);
        timeseriescollection.addSeries(time2);
        return timeseriescollection;
    }

    public void printChart(Stock stock, List<String[]> list, int days) throws Exception {
        //创建主题样式
        StandardChartTheme standardChartTheme = new StandardChartTheme("CN");
        //设置标题字体
        standardChartTheme.setExtraLargeFont(new Font("隶书", Font.BOLD, 20));
        //设置图例的字体
        standardChartTheme.setRegularFont(new Font("隶书", Font.BOLD, 12));
        //设置轴向的字体
        standardChartTheme.setLargeFont(new Font("隶书", Font.BOLD, 18));
        //应用主题样式
        ChartFactory.setChartTheme(standardChartTheme);
        DefaultCategoryDataset dataSet = new DefaultCategoryDataset();
        for (int i = list.size() - 1; i >= 0; i--) {
            String[] s = (String[]) list.get(i);
            dataSet.addValue(Double.parseDouble(s[1]), days + "日趋势线", s[0]);
            dataSet.addValue(Double.parseDouble(stock.getList().get(i).getClose()), "日收", s[0]);
            float sub = Float.parseFloat(s[2]);
            float error = Float.parseFloat(s[3]);
            if (sub > error * 2) {
                dataSet.addValue(Double.parseDouble(stock.getList().get(i).getClose()), "偏离点", s[0]);
            }
        }

        //第一个参数是标题，第二个参数是一个数据集，第三个参数表示是否显示Legend
        //第四个参数表示是否显示提示
        //第五个参数表示图中是否存在URL
        JFreeChart chart = ChartFactory.createLineChart(stock.getName() + "(" + stock.getCode() + ")股价走势图 ", "时间", "价格",
                dataSet, PlotOrientation.VERTICAL, true, true, false);
        CategoryPlot cp = chart.getCategoryPlot();
        cp.setBackgroundPaint(ChartColor.WHITE); // 背景色设置
        CategoryAxis categoryAxis = cp.getDomainAxis();
        // 横轴上的 Lable 90度倾斜 
        Font labelFont = new Font("SansSerif", Font.TRUETYPE_FONT, 10);
        categoryAxis.setCategoryLabelPositions(CategoryLabelPositions.UP_90);
        categoryAxis.setTickLabelFont(labelFont);//X轴坐标上数值字体 
        ValueAxis yAxis = cp.getRangeAxis();
        yAxis.setAutoRange(true);
        double[] d = getAxiasThresold(stock, list);
        yAxis.setLowerBound(d[0] - 0.15);
        yAxis.setUpperBound(d[1] + 0.15);
        LineAndShapeRenderer lasp = (LineAndShapeRenderer) cp.getRenderer();
        lasp.setBaseFillPaint(ChartColor.RED);
        lasp.setDrawOutlines(true);
        lasp.setSeriesShape(0, new java.awt.geom.Ellipse2D.Double(-5D, -5D, 10D, 10D));
        LineAndShapeRenderer renderer = (LineAndShapeRenderer) cp.getRenderer(1);//获取折线对象
        DecimalFormat decimalformat1 = new DecimalFormat("##.##");//数据点显示数据值的格式
        renderer.setItemLabelGenerator(new StandardCategoryItemLabelGenerator("{2}", decimalformat1));
        //上面这句是设置数据项标签的生成器
        renderer.setItemLabelsVisible(true);//设置项标签显示
        renderer.setBaseItemLabelsVisible(true);//基本项标签显示
        //上面这几句就决定了数据点按照设定的格式显示数据值
        renderer.setShapesFilled(Boolean.TRUE);//在数据点显示实心的小图标
        renderer.setShapesVisible(true);//设置显示小图标
        ChartFrame chartFrame = new ChartFrame("均值回归量化模型", chart, true);
        //chart要放在Java容器组件中，ChartFrame继承自java的Jframe类。该第一个参数的数据是放在窗口左上角的，不是正中间的标题。
        chartFrame.pack(); //以合适的大小展现图形
        chartFrame.setVisible(true);//图形是否可见
    }

    public static void main(String[] args) throws Exception {
        String url = "http://q.stock.sohu.com/cn/600636/lshq.shtml";
        HistoryInfo hq = new HistoryInfo();
        Stock stock = hq.parse(url);
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        Date date = format.parse("2016-03-30");
        System.out.println(stock.getList().get(0).getDate().toString());
        hq.printChart(stock, stock.getMeanGroup(20, 30), 20);
        //hq.test();
    }
}
