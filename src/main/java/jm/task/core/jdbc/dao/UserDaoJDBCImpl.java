package jm.task.core.jdbc.dao;

import jm.task.core.jdbc.model.User;
import jm.task.core.jdbc.util.Util;

import java.sql.*;

import java.util.ArrayList;
import java.util.List;



public class UserDaoJDBCImpl implements UserDao {
    public UserDaoJDBCImpl() {

    }

    public void createUsersTable() {
        try(Connection con = Util.getConnection()) {
            Statement state = con.createStatement();
            state.executeUpdate("CREATE TABLE IF NOT EXISTS Users" +
                    "(Id SERIAL PRIMARY KEY," +
                    "Name VARCHAR(50) NOT NULL," +
                    "LastName VARCHAR(50)," +
                    "Age SMALLINT NOT NULL)");
        }
        catch(SQLException e) {
            e.printStackTrace();
        }
    }

    public void dropUsersTable() {
        try(Connection con = Util.getConnection()) {
            Statement state = con.createStatement();
            state.executeUpdate("DROP TABLE IF EXISTS Users");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void saveUser(String name, String lastName, byte age) {
        try(Connection con = Util.getConnection()){
            PreparedStatement state = con.prepareStatement(
                    "INSERT INTO Users (name, lastName, age) VALUES (?,?,?)");
            state.setString(1, name);
            state.setString(2, lastName);
            state.setByte(3, age);
            state.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void removeUserById(long id) {
        try(Connection con = Util.getConnection()) {
            PreparedStatement state = con.prepareStatement(
                    "DELETE FROM Users WHERE Id = ?");
            state.setLong(1, id);
            state.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public List<User> getAllUsers() {
        List<User> users = new ArrayList<>();
        try(Connection con = Util.getConnection()) {
            Statement state = con.createStatement();
            ResultSet rs = state.executeQuery("SELECT * FROM Users");

            while(rs.next()) {
                User user = new User(
                        rs.getString("name"),
                        rs.getString("lastName"),
                        rs.getByte("age")
                );
                users.add(user);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return users;
    }

    public void cleanUsersTable() {
        try(Connection con = Util.getConnection()) {
            Statement state = con.createStatement();
            state.executeUpdate("TRUNCATE TABLE Users");
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
