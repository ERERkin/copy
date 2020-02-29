package it.academy.demoBot.Dao;

import it.academy.demoBot.model.Game;

import java.sql.*;

public class GameDao {
    public static boolean addUser(Game game) throws SQLException {
        String SQL = "insert into tic_tac(button1, button2, button3, button4, button5, button6, button7, button8, button9, player_x, player_y)\n" +
                " VALUES (?,?,?,?,?,?,?,?,?,?,?);";
        try (Connection connection = DB.connect();
             PreparedStatement statement = connection.prepareStatement(SQL);) {
            statement.setInt(1,game.getButton1());
            statement.setInt(2,game.getButton2());
            statement.setInt(3,game.getButton3());
            statement.setInt(4,game.getButton4());
            statement.setInt(5,game.getButton5());
            statement.setInt(6,game.getButton6());
            statement.setInt(7,game.getButton7());
            statement.setInt(8,game.getButton8());
            statement.setInt(9,game.getButton9());
            statement.setBoolean(11, game.isPlayerX());
            statement.setBoolean(12, game.isPlayerY());
            statement.executeUpdate();
            return true;
        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
        }
        return false;
    }

    public static boolean updateButton(int button, int update){
        String but = "button" + button;
        String SQL = "update tic_tac set" + but + " = ?;";
        try (Connection connection = DB.connect();
             PreparedStatement statement = connection.prepareStatement(SQL);) {
            statement.setInt(1,update);
            statement.executeUpdate();
            return true;
        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
        }
        return false;
    }

    public static Game getLastGame(){
        Game game = null;
        String SQL = "select * from tic_tac";
        try (Connection connection = DB.connect();
             PreparedStatement statement = connection.prepareStatement(SQL);) {
            try(ResultSet rs = statement.executeQuery()){
                rs.next();
                game = new Game(rs.getInt(1),
                        rs.getInt(2),
                        rs.getInt(3),
                        rs.getInt(4),
                        rs.getInt(5),
                        rs.getInt(6),
                        rs.getInt(7),
                        rs.getInt(8),
                        rs.getInt(9),
                        rs.getInt(10),
                        rs.getBoolean(11),
                        rs.getBoolean(12)
                        );
            }
        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
        }
        return game;
    }

    public boolean finish(){
        boolean ans = false;
        for(int i = 0; i < 9; i++) {
            ans = updateButton(i + 1, 0);
        }
        return ans;
    }
}
