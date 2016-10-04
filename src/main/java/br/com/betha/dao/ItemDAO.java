/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.betha.dao;

import br.com.betha.model.Item;
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
public class ItemDAO {

    public Item insertItem(Item i) {
        try {     
            Connection conn = ConnectionFactory.connect();
            String sql = "INSERT INTO TB_ITEM (name,description,unit_value) VALUES(?,?,?)";

            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setString(1, i.getName());
            ps.setString(2, i.getDescription());
            ps.setBigDecimal(3, i.getUnitValue());

            ps.execute();
            ps.close();
            i.setIdItem(getLastItem());
            conn.close();
            return i;
        } catch (SQLException ex) {
            throw new RuntimeException(ex.getMessage());
        }

    }

    public Item updateItem(Item i) {
        try {
            ConnectionFactory connectionFactory = new ConnectionFactory();
            Connection conn = connectionFactory.connect();
            String sql = "UPDATE TB_ITEM SET NAME = ?,DESCRIPTION = ?, unit_value = ?  where ID_ITEM = ?";

            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setString(1, i.getName());
            ps.setString(2, i.getDescription());
            ps.setBigDecimal(3, i.getUnitValue());
            ps.setLong(4, i.getIdItem());
            ps.execute();
            ps.close();
            conn.close();
            return i;
        } catch (SQLException ex) {
            throw new RuntimeException(ex.getMessage());
        }

    }

    public List<Item> listItems(ClausulaWhere sClausula) {
        try {
            ConnectionFactory connectionFactory = new ConnectionFactory();
            Connection conn = connectionFactory.connect();
            String sql = "select * from TB_item " + sClausula.montarsClausula();
            System.out.println(sql);
            PreparedStatement ps = conn.prepareStatement(sql);
            ResultSet rs = ps.executeQuery();
            List<Item> items = new ArrayList<Item>();
            while (rs.next()) {
                Item i = new Item();
                i.setName(rs.getString("name"));
                i.setDescription(rs.getString("description"));
                i.setUnitValue(rs.getBigDecimal("unit_value"));
                i.setIdItem(rs.getLong("id_item"));
                items.add(i);
            }
            rs.close();
            ps.close();
            conn.close();
            return items;
        } catch (SQLException ex) {
            throw new RuntimeException(ex.getMessage());
        }
    }

    public boolean deletarItem(Item b) {
        try {
            ConnectionFactory connectionFactory = new ConnectionFactory();
            Connection conn = connectionFactory.connect();
            String sql = "delete from TB_item where id_item = ?";
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setLong(1, b.getIdItem());
            ps.execute();
            ps.close();

            return true;
        } catch (SQLException ex) {
            throw new RuntimeException(ex.getMessage());
        }
    }

    public long getLastItem() {
        try {
            ConnectionFactory connectionFactory = new ConnectionFactory();
            Connection conn = connectionFactory.connect();
            String sql = "select max(id_item) id_item from TB_item ";
            System.out.println(sql);
            PreparedStatement ps = conn.prepareStatement(sql);
            ResultSet rs = ps.executeQuery();
            long id = 0;
            if (rs.next()) {
                id = rs.getLong("id_item");
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
