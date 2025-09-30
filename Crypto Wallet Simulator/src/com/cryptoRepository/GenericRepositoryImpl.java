package com.cryptoRepository;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.logging.Logger;

import com.util.AppLogger;

public abstract class GenericRepositoryImpl<T, ID> implements GenericRepository<T, ID> {
    protected Connection connection;
    protected String tableName;
    protected static final Logger logger = AppLogger.getLogger(GenericRepositoryImpl.class.getName());

    public GenericRepositoryImpl(Connection connection, String tableName) {
        this.connection = connection;
        this.tableName = tableName;
    }

    @Override
    public void delete(ID id) {
        String sql = "DELETE FROM " + tableName + " WHERE id = ?";
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setObject(1, id);
            ps.executeUpdate();
            logger.info("Entity deleted successfully: " + id);
        } catch (SQLException e) {
            logger.severe("Error deleting entity: " + e.getMessage());
        }
    }
}