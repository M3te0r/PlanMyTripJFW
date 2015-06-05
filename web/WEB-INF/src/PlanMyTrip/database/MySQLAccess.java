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
        statement.setString(1, "%" + searched + "%");

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
        statement.setString(1, "%" + search + "%");
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
        statement.setString(1, "%" + cityString + "%");
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

    public static List getUserGuidesInfos(int idUser) throws SQLException
    {
        PreparedStatement statement = getInstance().connection.prepareStatement("SELECT Id_Guide, Titre, Contenu, Pays, Ville, `Datetime`, duration, isValide FROM Guide WHERE Id_User = ?");
        statement.setInt(1, idUser);
        ResultSet resultSet = statement.executeQuery();
        List<Object[]> userGuides = new ArrayList<>();
        while (resultSet.next())
        {
            Object []tmp = new Object[8];
            tmp[0] = resultSet.getInt("Id_Guide");
            tmp[1] = resultSet.getString("Titre");
            tmp[2] = resultSet.getString("Contenu");
            tmp[3] = resultSet.getString("Pays");
            tmp[4] = resultSet.getString("Ville");
            tmp[5] = resultSet.getString("Datetime");
            tmp[6] = resultSet.getInt("duration");
            tmp[7] = resultSet.getInt("isValide");
            userGuides.add(tmp);
        }
        return userGuides;
    }

    public static List getUserByLoginAndPassword(String login, String password) throws SQLException
    {
        PreparedStatement statement = getInstance().connection.prepareStatement("SELECT Id_User,Pseudo, Password, IsValidate FROM user " +
                " WHERE Pseudo = ? AND Password = ?");
        statement.setString(1, login);
        statement.setString(2, password);
        List<Object> userList = new ArrayList<>(4);
        ResultSet resultSet = statement.executeQuery();
        while(resultSet.next())
        {
            userList.add(resultSet.getInt("Id_User"));
            userList.add(resultSet.getString("Pseudo"));
            userList.add(resultSet.getString("Password"));
            userList.add(resultSet.getInt("IsValidate"));
        }
        return userList;

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

    public static void registerUser(String realname, String pseudo, String mail, String password) throws SQLException
    {
        PreparedStatement statement = getInstance().connection.prepareStatement("INSERT INTO User(FullName, Pseudo, Mail, Password, ValidateKey, IsValidate) VALUES(?,?,?,?,?,?)");
        statement.setString(1, realname);
        statement.setString(2, pseudo);
        statement.setString(3, mail);
        statement.setString(4, password);
        statement.setString(5, "");
        statement.setInt(6, 1);
        statement.executeUpdate();

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

    public static void voteForUserGuideByUpdate(int idGuide, int idUser, int up, int down) throws SQLException
    {
        PreparedStatement statement = getInstance().connection.prepareStatement("UPDATE votes SET nbDown = nbDown+?, nbUp = nbUp+? WHERE idGuide = " +
                "(SELECT idGuide FROM votesByUser WHERE idGuide= ? AND idUser = ? AND hasVoted=0);" +
                " UPDATE votesbyuser SET hasvoted=1 WHERE idUser = ? AND idGuide = ? AND hasVoted=0;");
        statement.setInt(1, down);
        statement.setInt(2, up);
        statement.setInt(3, idGuide);
        statement.setInt(4, idUser);
        statement.setInt(5, idUser);
        statement.setInt(6, idGuide);
        statement.executeUpdate();
    }

    public static void voteForUserGuideByCreate(int idGuide, int idUser, int up, int down) throws SQLException
    {
        PreparedStatement statement = getInstance().connection.prepareStatement(("INSERT INTO votesbyuser (id,idUser, idGuide, hasVoted) VALUES (?,?,?,?);" +
                "UPDATE votes SET nbDown = nbDown+ ?, nbUp=nbUp+ ? WHERE idGuide = (SELECT idGuide FROM votesByUser WHERE idGuide = ? AND idUser = ? AND hasVoted=1)"));

        statement.setNull(1, Types.NULL);
        statement.setInt(2, idUser);
        statement.setInt(3, idGuide);
        statement.setInt(4, 1);
        statement.setInt(5, down);
        statement.setInt(6, up);
        statement.setInt(7, idGuide);
        statement.setInt(8, idUser);
        statement.executeUpdate();
    }

    public static int getNbLike(int idGuide) throws SQLException
    {
        PreparedStatement statement = getInstance().connection.prepareStatement("SELECT SUM(nbUP) as total FROM votes WHERE idGuide = ?");

        statement.setInt(1, idGuide);
        ResultSet result = statement.executeQuery();
        if(result.next())
            return result.getInt("total");
        else
            return 0;
    }

    public static int getNbDislike(int idGuide) throws SQLException
    {
        PreparedStatement statement = getInstance().connection.prepareStatement("SELECT SUM(nbDown) as total FROM votes WHERE idGuide = ?");

        statement.setInt(1, idGuide);
        ResultSet result = statement.executeQuery();
        if(result.next())
            return result.getInt("total");
        else
            return 0;
    }

    public static int updateUserInfo(String fullName, String newPassword, String oldPassword, int userId) throws SQLException
    {
        PreparedStatement statement = getInstance().connection.prepareStatement("UPDATE User SET FullName = ?, Password = ? WHERE Id_User = ? AND Password = ?");
        statement.setString(1, fullName);
        statement.setString(2, newPassword);
        statement.setInt(3, userId);
        statement.setString(4, oldPassword);
        return statement.executeUpdate();
    }

    public static void InsertNewGuide(int userId, String pays, String ville, String titre, String contenu, int duration) throws SQLException
    {
        PreparedStatement statement = getInstance().connection.prepareStatement("INSERT INTO guide(Titre, Contenu, Id_User, Pays, Ville, duration, isValide) VALUES(?,?,?,?,?,?,?)");
        statement.setString(1, titre);
        statement.setString(2, contenu);
        statement.setInt(3, userId);
        statement.setString(4, pays);
        statement.setString(5, ville);
        statement.setInt(6, duration);
        statement.setInt(7, 1);
        statement.executeUpdate();
        PreparedStatement statement2 = getInstance().connection.prepareStatement("INSERT INTO votes(idGuide, nbDown, nbUp) VALUES ((SELECT Id_Guide FROM guide WHERE Titre = ? AND Id_User = ?),?,?)");
        statement2.setString(1, titre);
        statement2.setInt(2, userId);
        statement2.setInt(3, 0);
        statement2.setInt(4, 0);
        statement2.executeUpdate();
    }

    public static String getBaseUrl()
    {
        return "http://localhost:8081/PlanMyTrip";
    }


    private static MySQLAccess getInstance(){
        if(self == null)
        {
            self = new MySQLAccess();
        }
        return self;
    }


}
