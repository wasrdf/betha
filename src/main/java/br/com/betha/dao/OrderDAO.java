/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.betha.dao;

import br.com.betha.model.Order;
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
 * @author Was
 */
public class OrderDAO {

    public Order insertOrder(Order o) {
        try {
     
            Connection conn = ConnectionFactory.connect();
            String sql = "INSERT INTO TB_ORDER (DATE_ORDER) VALUES(?)";

            PreparedStatement ps = conn.prepareStatement(sql);
            try {
                ps.setDate(1, new java.sql.Date(o.getDateOrder().getTime()));
            } catch (NullPointerException e) {
                ps.setDate(1, null);
            }

            ps.execute();
            ps.close();
            o.setIdOrder(getLastOrder());
            conn.close();
            return o;
        } catch (SQLException ex) {
            throw new RuntimeException(ex.getMessage());
        }

    }

    public List<Order> requestList(ClausulaWhere sClausula) {
        try {
            ConnectionFactory connectionFactory = new ConnectionFactory();
            Connection conn = connectionFactory.connect();
            String sql = "select * from TB_order " + sClausula.montarsClausula();
            System.out.println(sql);
            PreparedStatement ps = conn.prepareStatement(sql);
            ResultSet rs = ps.executeQuery();
            List<Order> requestList = new ArrayList<>();
            while (rs.next()) {
                Order order = new Order();
                order.setIdOrder(rs.getLong("id_order"));
                order.setDateOrder(rs.getDate("date_order"));                   
                requestList.add(order);
            }
            rs.close();
            ps.close();
            conn.close();
            return requestList;
        } catch (SQLException ex) {
            throw new RuntimeException(ex.getMessage());
        }
    }

    public long getLastOrder() {
        try {
            ConnectionFactory connectionFactory = new ConnectionFactory();
            Connection conn = connectionFactory.connect();
            String sql = "select max(id_order) id_order from TB_order ";
            System.out.println(sql);
            PreparedStatement ps = conn.prepareStatement(sql);
            ResultSet rs = ps.executeQuery();
            long id = 0;
            if (rs.next()) {
                id = rs.getLong("id_order");
            }
            rs.close();
            ps.close();
            conn.close();
            return id;
        } catch (SQLException ex) {
            throw new RuntimeException(ex.getMessage());
        }
    }
}
