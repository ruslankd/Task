package ruslan.kabirov.server.networkserver.auth;

import java.sql.*;

public class BaseAuthService implements AuthService {

    private Connection connection;
    private Statement statement;

    @Override
    public String getNickByLoginAndPassword(String login, String password) throws SQLException {
        statement = connection.createStatement();
        ResultSet rs = statement.executeQuery("SELECT nickname FROM users WHERE (login = '" +
                login + "' AND password = '" + password + "');");
        if (!rs.next()) return null;
        return rs.getString(1);
    }

    @Override
    public int changeNickname(String nickname, String newNickname) throws SQLException {
        return statement.executeUpdate("UPDATE users SET nickname = '" + newNickname +
                "' WHERE nickname = '" + nickname + "';");
    }

    @Override
    public void start() throws ClassNotFoundException, SQLException {
        System.out.println("Auth service has been started");
        Class.forName("org.sqlite.JDBC");
        connection = DriverManager.getConnection("jdbc:sqlite:userDB");
    }

    @Override
    public void stop() throws SQLException {
        System.out.println("Auth service has been stopped");
        connection.close();
    }


}
