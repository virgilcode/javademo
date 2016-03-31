/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package virgil.meanback;

/**
 *
 * @author Virgil
 */
public class StockSearch {

    /*
     * @Param code 股票代号
     * @days       均线
     * @listdays   待显示的天数，默认为30天
     */
    public void stockMeanBack(String code,int days,int listdays) throws Exception {
        HistoryInfo info=new HistoryInfo();
        String url = "http://q.stock.sohu.com/cn/"+code+"/lshq.shtml";
        Stock stock = info.parse(url);
        info.printChart(stock, stock.getMeanGroup(days, listdays),days);
    }

    public static void main(String[] args) throws Exception {
        StockSearch stockSearch=new StockSearch();
        String code="000981";
        stockSearch.stockMeanBack(code,10,20);
    }
}
