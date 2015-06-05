package PlanMyTrip.action;

import PlanMyTrip.context.Context;
import PlanMyTrip.database.MySQLAccess;
import org.esgi.web.framework.action.interfaces.IAction;
import org.esgi.web.framework.context.interfaces.IContext;

import java.io.IOException;
import java.sql.SQLException;

/**
 * Created by Mathieu on 04/06/2015.
 */
public class ActionUserRegistration implements IAction {

    /**
     * the highest priority is 0, the lower is Integer.MAX_VALUE
     * used in the Dispatcher priority queue.
     *
     * @param priority
     */
    @Override
    public int setPriority(int priority) {
        return 0;
    }

    @Override
    public int getPriority() {
        return 0;
    }

    /**
     * may only be used in implementation constructor scope.
     *
     * @param role
     */
    @Override
    public void addCredential(String role) {

    }

    /**
     * @return true if credentials are needed for this action.
     */
    @Override
    public boolean needsCredentials() {
        return false;
    }

    /**
     * @param roles
     * @return true if at least one given roles matches with require roles.
     */
    @Override
    public boolean hasCredential(String[] roles) {
        return false;
    }

    @Override
    public void proceed(IContext context) {
        String passOne = ((Context) context).getParameterUnique("mdp");
        String passTwo = ((Context) context).getParameterUnique("mdp2");

        if (passOne.length() < 3)
        {
            try {
                context._getResponse().sendRedirect(context._getRequest().getHeader("referer") + "?error=1");
            } catch (IOException e) {
                e.printStackTrace();
            }

        }
        else if(!passOne.equals(passTwo))
        {
            try {
                context._getResponse().sendRedirect(context._getRequest().getHeader("referer") + "?error=2");
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        else {
            String realname = ((Context) context).getParameterUnique("realname");
            String pseudo = ((Context) context).getParameterUnique("pseudo");
            String mail = ((Context) context).getParameterUnique("mail");

            try {
                MySQLAccess.registerUser(realname, pseudo, mail, passOne);
                context._getResponse().sendRedirect("/PlanMyTrip/index.html");
            } catch (SQLException e) {
                e.printStackTrace();
            }
            catch (IOException io)
            {
                io.printStackTrace();
            }

        }

    }
}
