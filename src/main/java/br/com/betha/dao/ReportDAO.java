/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.betha.dao;

import br.com.betha.model.OrderItem;
import br.com.betha.model.Report;
import br.com.betha.util.ConnectionFactory;
import br.com.br.betha.ClausulaSQL.ClausulaWhere;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author crossystem
 */
public class ReportDAO {

    public List<Report> getSalesNumberByItem() {
        try {
            Connection conn = ConnectionFactory.connect();
            String sql = "select tb_item.name,sum(tb_order_item.quantity) quantity from tb_order_item\n"
                    + "inner join tb_item on tb_item.id_item = tb_order_item.id_item\n"
                    + "group by tb_item.name ";
            System.out.println(sql);
            PreparedStatement ps = conn.prepareStatement(sql);
            ResultSet rs = ps.executeQuery();
            List<Report> listReport = new ArrayList<>();
            while (rs.next()) {
                Report report = new Report();
                report.setItemName(rs.getString("name"));
                report.setQuantity(rs.getInt("quantity"));
                listReport.add(report);
            }
            rs.close();
            ps.close();
            conn.close();
            return listReport;
        } catch (SQLException ex) {
            throw new RuntimeException(ex.getMessage());
        }
    }

    public List<Report> getSalesByMonth(ClausulaWhere pClausula) {
        try {
            Connection conn = ConnectionFactory.connect();
            String sql = "select tb_order.id_order,tb_item.name,tb_order_item.quantity,tb_order_item.total_cost,tb_order.date_order from tb_order_item \n"
                    + "	inner join tb_order on tb_order.id_order = tb_order_item.id_order\n"
                    + "	inner join tb_item on tb_item.id_item = tb_order_item.id_item \n" + pClausula.montarsClausula()
                    + "	order by tb_order.id_order ";
            System.out.println(sql);
            PreparedStatement ps = conn.prepareStatement(sql);
            ResultSet rs = ps.executeQuery();
            List<Report> listReport = new ArrayList<>();
            while (rs.next()) {
                Report report = new Report();
                report.setIdOrder(rs.getLong("id_order"));
                report.setItemName(rs.getString("name"));
                report.setQuantity(rs.getInt("quantity"));
                report.setTotalCost(rs.getBigDecimal("total_cost"));
                report.setDateOrder(rs.getDate("date_order"));
                listReport.add(report);
            }
            rs.close();
            ps.close();
            conn.close();
            return listReport;
        } catch (SQLException ex) {
            throw new RuntimeException(ex.getMessage());
        }
    }

}
