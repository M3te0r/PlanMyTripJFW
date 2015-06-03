package PlanMyTrip.database;

import java.sql.*;

/**
 * Created by Mathieu on 03/06/2015.
 */
public class MySQLAccess {

    public static MySQLAccess self;
    private Connection connection;
    private Statement statement;
    private PreparedStatement preparedStatement;
    private ResultSet resultSet;

    private MySQLAccess(){
        try {
            Class.forName("com.mysql.jdbc.Driver");
            connection = DriverManager.getConnection("jdbc:mysql://localhost/planmytrip?user=root&password=");
        }
        catch (ClassNotFoundException cl)
        {
            System.out.println("Class driver not found");
        }
        catch (SQLException sql)
        {
            System.out.println("Error with sqldb");
        }

    }

    public ResultSet searchCityByName(String searched) throws SQLException
    {
        PreparedStatement statement = getInstance().connection.prepareStatement("SELECT Ville FROM guide WHERE Ville LIKE ? ");
        statement.setString(1, searched);
        return statement.executeQuery();
    }

    public ResultSet getUserById(int id) throws  SQLException{
        PreparedStatement statement = getInstance().connection.prepareStatement("SELECT Pseudo FROM user WHERE Id_User  = ?");
        statement.setInt(1, id);
        return statement.executeQuery();
    }

    public ResultSet getLastGuides() throws SQLException
    {
        PreparedStatement statement = getInstance().connection.prepareStatement("SELECT * FROM guide WHERE isValide = 1 ORDER BY Id_Guide DESC LIMIT 0 , 5");
        return statement.executeQuery();
    }

    public ResultSet getVotesForGuide(int guideId) throws SQLException
    {
        PreparedStatement statement = getInstance().connection.prepareStatement("SELECT  `nbDown` ,  `nbUp` FROM  `votes` WHERE  `idGuide` = ?");
        statement.setInt(1, guideId);
        return statement.executeQuery();
    }

    public ResultSet getUserGuidesInfos(int idUser) throws SQLException
    {
        PreparedStatement statement = getInstance().connection.prepareStatement("SELECT Id_Guide, Titre, Contenu, Id_User, Pays, Ville, `Datetime`, duration, isValide FROM Guide WHERE Id_User = ?");
        statement.setInt(1, idUser);
        return statement.executeQuery();
    }








    private static MySQLAccess getInstance(){
        if(self == null)
        {
            self = new MySQLAccess();
        }
        return self;
    }
}
