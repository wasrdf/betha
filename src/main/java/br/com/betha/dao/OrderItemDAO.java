/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.betha.dao;

import br.com.betha.model.Order;
import br.com.betha.model.OrderItem;
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
public class OrderItemDAO {

    public OrderItem insertOrder(OrderItem oi) {
        try {

            Connection conn = ConnectionFactory.connect();
            String sql = "INSERT INTO TB_ORDER_ITEM (ID_ORDER,ID_ITEM,quantity,total_cost,unit_cost) VALUES(?,?,?,?,?)";

            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setLong(1, oi.getIdOrder());
            ps.setLong(2, oi.getIdItem());
            ps.setInt(3, oi.getQuantity());
            ps.setBigDecimal(4, oi.getTotalCost());
            ps.setBigDecimal(5, oi.getUnitCost());

            ps.execute();
            ps.close();
            conn.close();
            return oi;
        } catch (SQLException ex) {
            throw new RuntimeException(ex.getMessage());
        }

    }

    public List<OrderItem> listOrderItem(ClausulaWhere sClausula) {
        try {
        
            Connection conn = ConnectionFactory.connect();
            String sql = "select TB_order_item.*,TB_item.name itemName from TB_order_item inner join TB_item on TB_item.id_item = TB_order_item.id_item" + sClausula.montarsClausula();
            System.out.println(sql);
            PreparedStatement ps = conn.prepareStatement(sql);
            ResultSet rs = ps.executeQuery();
            List<OrderItem> orderItemList = new ArrayList<>();
            while (rs.next()) {
                OrderItem orderItem = new OrderItem();
                orderItem.setIdOrderItem(rs.getLong("id_order_item"));
                orderItem.setIdOrder(rs.getLong("id_order"));
                orderItem.setIdItem(rs.getLong("id_item"));
                orderItem.setQuantity(rs.getInt("quantity"));
                orderItem.setTotalCost(rs.getBigDecimal("total_cost"));
                orderItem.setUnitCost(rs.getBigDecimal("unit_cost"));
                orderItem.setItemName(rs.getString("itemName"));
                orderItemList.add(orderItem);
            }
            rs.close();
            ps.close();
            conn.close();
            return orderItemList;
        } catch (SQLException ex) {
            throw new RuntimeException(ex.getMessage());
        }
    }

   

}
