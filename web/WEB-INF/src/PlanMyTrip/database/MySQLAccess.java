package PlanMyTrip.database;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Mathieu on 03/06/2015.
 */
public class MySQLAccess {

    public static MySQLAccess self;
    private Connection connection;
    private Statement statement;
    private PreparedStatement preparedStatement;
    private ResultSet resultSet;

    public MySQLAccess(){
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

    public static ResultSet searchCityByName(String searched) throws SQLException
    {
        PreparedStatement statement = getInstance().connection.prepareStatement("SELECT Ville FROM guide WHERE Ville LIKE ? ");
        statement.setString(1, searched);

        return statement.executeQuery();

    }

    public static String getUserPseudoById(int userId) throws SQLException
    {
        System.out.println(userId);
        PreparedStatement statement = getInstance().connection.prepareStatement("SELECT Pseudo FROM user WHERE Id_User = ?");
        statement.setInt(1, userId);
        ResultSet resultSet = statement.executeQuery();
        String userPseudo = null;
        while (resultSet.next())
        {
            userPseudo =  resultSet.getString("Pseudo");
        }
        System.out.println(userPseudo);
        return null;
    }

    public static List searchGuideWithSearchAndDuration(String search, int duration) throws SQLException
    {
        PreparedStatement statement = getInstance().connection.prepareStatement("SELECT * FROM guide WHERE ville LIKE ? AND duration >= ? AND `isValide` = 1 ORDER BY Id_Guide");
        statement.setString(1, search);
        statement.setInt(2, duration);
        ResultSet resultSet = statement.executeQuery();
        List<Object[]> r = new ArrayList<>();
        while (resultSet.next())
        {
            Object tmp[] = new Object[6];
            tmp[0] = resultSet.getInt("Id_Guide");
            tmp[1] = resultSet.getString("Titre");
            tmp[2] = resultSet.getString("Contenu");
            tmp[3] = resultSet.getInt("Id_User");
            tmp[4] = resultSet.getString("Pays");
            tmp[5] = resultSet.getString("Ville");
            r.add(tmp);

        }
        return r;
    }

    public static List getSearchedByCity(String cityString) throws SQLException
    {
        PreparedStatement statement = getInstance().connection.prepareStatement("SELECT * FROM guide " +
                " INNER JOIN user as u ON u.Id_User=guide.Id_User " +
                " INNER JOIN votes as v ON v.idGuide=guide.Id_Guide WHERE ville LIKE ? AND `isValide` = 1 ORDER BY Id_Guide ");
        statement.setString(1, cityString);
        ResultSet resultSet = statement.executeQuery();
        List<Object[]> r = new ArrayList<>();
        while(resultSet.next())
        {
            Object []tmp = new Object[8];
            tmp[0] = Integer.toString(resultSet.getInt("Id_Guide"));
            tmp[1] = resultSet.getString("Titre");
            tmp[2] = resultSet.getString("Contenu");
            tmp[3] = resultSet.getString("Pseudo");
            tmp[4] = resultSet.getString("Pays");
            tmp[5] = resultSet.getString("Ville");
            tmp[6] = resultSet.getInt("nbDown");
            tmp[7] = resultSet.getInt("nbUp");
            r.add(tmp);

        }
        return r;
    }

    public static ResultSet getUserById(int id) throws  SQLException{
        PreparedStatement statement = getInstance().connection.prepareStatement("SELECT Pseudo FROM user WHERE Id_User  = ?");
        statement.setInt(1, id);
        return statement.executeQuery();
    }

    public static ResultSet getLastGuides() throws SQLException
    {
        PreparedStatement statement = getInstance().connection.prepareStatement("SELECT * FROM guide WHERE isValide = 1 ORDER BY Id_Guide DESC LIMIT 0 , 5");
        return statement.executeQuery();
    }

    public static ResultSet getVotesForGuide(int guideId) throws SQLException
    {
        PreparedStatement statement = getInstance().connection.prepareStatement("SELECT  `nbDown` ,  `nbUp` FROM  `votes` WHERE  `idGuide` = ?");
        statement.setInt(1, guideId);
        return statement.executeQuery();
    }

    public static ResultSet getUserGuidesInfos(int idUser) throws SQLException
    {
        PreparedStatement statement = getInstance().connection.prepareStatement("SELECT Id_Guide, Titre, Contenu, Id_User, Pays, Ville, `Datetime`, duration, isValide FROM Guide WHERE Id_User = ?");
        statement.setInt(1, idUser);
        return statement.executeQuery();
    }

    public static List getGuideById(int idGuide) throws SQLException
    {
        PreparedStatement statement = getInstance().connection.prepareStatement("SELECT * FROM guide " +
                " INNER JOIN user as u  ON u.Id_User=guide.Id_User " +
                " INNER JOIN votes as v on v.idGuide=guide.Id_Guide WHERE Id_Guide = ?");
        statement.setInt(1, idGuide);
        ResultSet resultSet = statement.executeQuery();
        List<Object[]> result = new ArrayList<>();
        while (resultSet.next())
        {
            Object []tmp = new Object[8];
            tmp[0] = resultSet.getInt("Id_Guide");
            tmp[1] = resultSet.getString("Titre");
            tmp[2] = resultSet.getString("Contenu");
            tmp[3] = resultSet.getString("Pseudo");
            tmp[4] = resultSet.getString("Pays");
            tmp[5] = resultSet.getString("Ville");
            tmp[6] = resultSet.getInt("nbDown");
            tmp[7] = resultSet.getInt("nbUp");
            result.add(tmp);
        }
        return result;
    }

    public static ResultSet hasUserVoted(int idGuide, int idUser) throws SQLException
    {
        PreparedStatement statement = getInstance().connection.prepareStatement("SELECT hasVoted FROM votesByUser WHERE idGuide= ? AND idUser= ?");
        statement.setInt(1, idGuide);
        statement.setInt(2, idUser);
        return statement.executeQuery();
    }

    public static void updateVoteForUserGuide(int idGuide, int idUser) throws SQLException
    {
        PreparedStatement statement = getInstance().connection.prepareStatement("UPDATE votes SET nbDown = nbDown+1 WHERE idGuide = " +
                "(SELECT idGuide FROM votesByUser WHERE idGuide= ? AND idUser = ? AND hasVoted=0);" +
                " UPDATE votesbyuser SET hasvoted=1 WHERE idUser = ? AND idGuide = ? AND hasVoted=0;");
        statement.setInt(1, idGuide);
        statement.setInt(2, idUser);
        statement.setInt(3, idUser);
        statement.setInt(4, idGuide);
        statement.executeUpdate();

    }



    private static MySQLAccess getInstance(){
        if(self == null)
        {
            self = new MySQLAccess();
        }
        return self;
    }
}
