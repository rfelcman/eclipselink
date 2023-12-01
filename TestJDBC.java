//package com.oracle.eclipselink.bugtest;


//import org.junit.jupiter.api.Test;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;

public class TestJDBC {

    //    @Test
    public void connectionIsValidTest() throws Exception {
        Connection conn = null;
        Statement stmt = null;
        ResultSet rset = null;

        //MySQL
//        TEST_DB_URL=jdbc:mysql://localhost:3306/ecltests?allowPublicKeyRetrieval=true
//        TEST_DB_USERNAME=root
//        TEST_DB_PASSWORD=root


        String dbURL = System.getProperty("TEST_DB_URL", "jdbc:mysql://localhost:3306/ecltests?allowPublicKeyRetrieval=true");
        String dbUsr = System.getProperty("TEST_DB_USERNAME", "root");
        String dbPwd = System.getProperty("TEST_DB_PASSWORD", "root");
        System.out.println(dbURL + "\t" + dbUsr + "\t" + dbPwd);
        conn = DriverManager.getConnection(dbURL,dbUsr,dbPwd);
        try {
            conn.prepareStatement("DROP TABLE TEST_JDBC_TAB").execute();
        } catch (Exception e) {
            System.out.println(e);
        }
        conn.prepareStatement("CREATE TABLE TEST_JDBC_TAB (ID INTEGER NOT NULL, NAME VARCHAR(50), PRIMARY KEY (ID));").execute();
        conn.prepareStatement("INSERT INTO TEST_JDBC_TAB (ID, NAME) VALUES (1, 'aaa')").execute();
        stmt = conn.createStatement();
        if (!conn.isValid(10)) {
            System.out.println("DB connection is not valid!");
            return;
        }
        boolean isResultSet = stmt.execute("SELECT * FROM TEST_JDBC_TAB");
        if (isResultSet) {
            rset = stmt.getResultSet();
            if (rset.next()) {
                System.out.println(rset.getInt("ID") + " " + rset.getString("NAME"));
            }
            rset.close();
        }
        stmt.close();
        conn.close();

    }

    public static void main(String[] args) throws Exception{
        TestJDBC testJDBC = new TestJDBC();
        testJDBC.connectionIsValidTest();
    }

}
