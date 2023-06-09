package lk.ijse.pos.dao.custom.impl;

import lk.ijse.pos.dao.custom.ItemDAO;
import lk.ijse.pos.dao.custom.impl.util.SQLUtil;
import lk.ijse.pos.entity.Item;

import java.sql.*;
import java.util.ArrayList;

public class ItemDAOImpl implements ItemDAO {

    @Override
    public ArrayList<Item> loadAll() throws SQLException, ClassNotFoundException {
        ArrayList<Item> allItems = new ArrayList<>();
        String sql = "SELECT * FROM item";
        ResultSet resultSet = SQLUtil.execute(sql);
        while (resultSet.next()) {
            Item item = new Item(resultSet.getString(1), resultSet.getString(2), resultSet.getInt(3), resultSet.getBigDecimal(4));
            allItems.add(item);
        }
        return allItems;
    }

    @Override
    public boolean save(Item item) throws SQLException, ClassNotFoundException {
        String sql = "INSERT INTO item (code, description, qtyOnHand, unitPrice) VALUES (?,?,?,?)";
        return SQLUtil.execute(sql, item.getItemCode(), item.getDescription(), item.getQtyOnHand(), item.getUnitPrice());
    }

    @Override
    public boolean update(Item item) throws SQLException, ClassNotFoundException {
        String sql = "UPDATE item SET description=?, qtyOnHand=?, unitPrice=? WHERE code=?";
        return SQLUtil.execute(sql, item.getDescription(), item.getQtyOnHand(), item.getUnitPrice(), item.getItemCode());
    }

    @Override
    public boolean delete(String code) throws SQLException, ClassNotFoundException {
        String sql = "DELETE FROM item WHERE code=?";
        return SQLUtil.execute(sql, code);
    }

    @Override
    public boolean exist(String code) throws SQLException, ClassNotFoundException {
        String sql = "SELECT code FROM item WHERE code=?";
        ResultSet resultSet = SQLUtil.execute(sql, code);
        return resultSet.next();
    }

    @Override
    public String generateNextId() throws SQLException, ClassNotFoundException {
        String sql = "SELECT code FROM item ORDER BY code DESC LIMIT 1;";
        ResultSet resultSet = SQLUtil.execute(sql);
        if (resultSet.next()) {
            String id = resultSet.getString("code");
            int newCustomerId = Integer.parseInt(id.replace("I00-", "")) + 1;
            return String.format("I00-%03d", newCustomerId);
        } else {
            return "I00-001";
        }
    }

    @Override
    public Item search(String newItemCode) throws SQLException, ClassNotFoundException {
        String sql = "SELECT * FROM Item WHERE code=?";
        ResultSet resultSet = SQLUtil.execute(sql, newItemCode + "");
        if (resultSet.next()) {
            Item item = new Item(newItemCode + "", resultSet.getString("description"), resultSet.getInt("qtyOnHand"), resultSet.getBigDecimal("unitPrice"));
            return item;
        }
        return null;
    }
}
