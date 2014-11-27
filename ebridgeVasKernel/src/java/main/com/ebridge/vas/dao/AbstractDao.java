package com.ebridge.vas.dao;

import com.ebridge.vas.util.DatabaseConnectionPooling;

import java.sql.Connection;

/**
 * david@ebridgevas.com
 *
 */
public class AbstractDao {

    protected Connection connection;

    public AbstractDao() {
        try {
            connection = DatabaseConnectionPooling.getConnection();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    protected Connection getConnection() throws Exception {

        if (connection == null) {
            connection = DatabaseConnectionPooling.getConnection();
        }
        return connection;
    }
}
