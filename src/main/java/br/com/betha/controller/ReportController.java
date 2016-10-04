/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.betha.controller;

import br.com.betha.dao.ReportDAO;
import br.com.betha.model.Report;
import br.com.br.betha.ClausulaSQL.ClausulaWhere;
import br.com.br.betha.ClausulaSQL.GeneroCondicaoWhere;
import br.com.br.betha.ClausulaSQL.OperacaoCondicaoWhere;
import br.com.br.betha.ClausulaSQL.TipoCondicaoWhere;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import org.primefaces.model.chart.BarChartModel;
import org.primefaces.model.chart.ChartSeries;
import org.primefaces.model.chart.LegendPlacement;

@ManagedBean
@ViewScoped
public class ReportController {

    private List<Report> reportList;
    private List<Report> salesByMonth;
    private BarChartModel barChartItem;
    
    public ReportController() {
        this.reportList = new ArrayList<>();
        this.salesByMonth = new ArrayList<>();
    }

    public void init() {
        createBarChartSalesNumberByItem();
        listSalesByMonth();
    }

    public void createBarChartSalesNumberByItem() {
        ReportDAO reportDAO = new ReportDAO();
        reportList = reportDAO.getSalesNumberByItem();

        barChartItem = new BarChartModel();

        for (Report r : reportList) {
            ChartSeries products = new ChartSeries();
            products.setLabel(r.getItemName());
            products.set("Produtos", r.getQuantity());
            barChartItem.addSeries(products);

        }

        barChartItem.setLegendPosition("e");
        barChartItem.setAnimate(true);
        barChartItem.setBarMargin(10);
        barChartItem.setLegendPlacement(LegendPlacement.OUTSIDEGRID);
    }

    public void listSalesByMonth() {
        ReportDAO reportDAO = new ReportDAO();
        ClausulaWhere condition = new ClausulaWhere();
        Date data = new Date();
        Calendar cal = Calendar.getInstance();
        cal.setTime(data);
        int month = cal.get(Calendar.MONTH) + 1;
        int year = cal.get(Calendar.YEAR);
        ClausulaWhere condicao = new ClausulaWhere();
        condicao.AdicionarCondicao(OperacaoCondicaoWhere.and, "MONTH(tb_order.date_order)", GeneroCondicaoWhere.igual, String.valueOf(month), TipoCondicaoWhere.Numero);
        condicao.AdicionarCondicao(OperacaoCondicaoWhere.and, "YEAR(tb_order.date_order)", GeneroCondicaoWhere.igual, String.valueOf(year), TipoCondicaoWhere.Numero);
        salesByMonth = reportDAO.getSalesByMonth(condicao);
    }

    //get and set
    public List<Report> getReportList() {
        return reportList;
    }

    public void setReportList(List<Report> reportList) {
        this.reportList = reportList;
    }

    public BarChartModel getBarChartItem() {
        return barChartItem;
    }

    public void setBarChartItem(BarChartModel barChartItem) {
        this.barChartItem = barChartItem;
    }

    public List<Report> getSalesByMonth() {
        return salesByMonth;
    }

    public void setSalesByMonth(List<Report> salesByMonth) {
        this.salesByMonth = salesByMonth;
    }

    
    
}
