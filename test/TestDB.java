package test;

import main.persistence.DBConn;
import java.sql.Connection;

public class TestDB {
    public static void main(String[] args) {
        Connection conn = DBConn.getConnectionFromInstance();
        
        System.out.println(conn != null ? "Conexiune OK!" : "Conexiune esuata!");
    }
}

