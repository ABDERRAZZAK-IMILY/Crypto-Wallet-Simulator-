package com.cryptoRepository;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public abstract class GenericRepositoryImpl<T, ID> implements GenericRepository<T, ID> {
    protected Connection connection;
    protected String tableName;

    public GenericRepositoryImpl(Connection connection, String tableName) {
        this.connection = connection;
        this.tableName = tableName;
    }

    @Override
    public void delete(ID id) {
        try {
            PreparedStatement ps = connection.prepareStatement(
                "DELETE FROM " + tableName + " WHERE id = ?"
            );
            ps.setObject(1, id);
            ps.executeUpdate();
            System.out.println("Entity deleted successfully!");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public abstract void save(T entity);

    @Override
    public abstract T findById(ID id);

    @Override
    public abstract void update(T entity);

    @Override
    public abstract java.util.List<T> findAll();
}
