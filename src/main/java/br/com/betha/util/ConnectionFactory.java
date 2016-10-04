/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.betha.util;

import java.sql.Connection;
import java.sql.DriverManager;

/**
 *
 * @author Was
 */
public class ConnectionFactory {

    static Connection conn;

    public static Connection connect() {
        try {
            String usuario = "sa";
            String url = "jdbc:sqlserver://192.168.0.10:1433;" + "databaseName=SGM;";
            String senha = "//jmncbb100";
            String DRIVER = "com.microsoft.sqlserver.jdbc.SQLServerDriver";
            Class.forName(DRIVER);
            Connection con = null;
            con = DriverManager.getConnection(url, usuario, senha);
            conn = con;
            return con;
        } catch (Exception e) {
            System.out.println(e.getMessage());
            return null;
        }
    }

}
