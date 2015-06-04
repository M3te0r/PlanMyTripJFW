package PlanMyTrip.action;

import PlanMyTrip.context.Context;
import PlanMyTrip.database.MySQLAccess;
import PlanMyTrip.error.JwfErrorHandler;
import PlanMyTrip.message.JwfMessage;
import PlanMyTrip.renderer.Renderer;
import org.esgi.web.framework.action.interfaces.IAction;
import org.esgi.web.framework.context.interfaces.IContext;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

/**
 * Created by Mathieu on 04/06/2015.
 */
public class ActionUserLogin implements IAction {

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

        String login = ((Context)context).getParameterUnique("pseudo");
        String pass  = ((Context)context).getParameterUnique("password");
        context._getResponse().setContentType("text/html");
        if(login != null && !login.isEmpty() && pass != null && !pass.isEmpty()) {
            // The user wants to connect
            List userInfo = null;
            try {
                userInfo = MySQLAccess.getUserByLoginAndPassword(login, pass);
            } catch (SQLException e) {
                e.printStackTrace();
            }
            if (userInfo != null) {
                if(!userInfo.isEmpty()) {
                    // Save session
                    context.setSessionAttribute("user-id", userInfo.get(0));
                    context.setSessionAttribute("user-pseudo", userInfo.get(1));
                    // Display message
                    try {
                        context.setAttribute("model", new JwfMessage("Connected with user " + login));
                        context._getResponse().getWriter().write(new Renderer().render(context));
                        context._getResponse().sendRedirect(context._getRequest().getHeader("referer"));
                    } catch (IOException e) {
                        JwfErrorHandler.displayError(context, 500, "error while writting response : " + e.getMessage());
                        e.printStackTrace();
                    }

                } else
                {
                    JwfErrorHandler.displayError(context, 403, "no user with the login/password provided");
                try {
                    context._getResponse().sendRedirect(context._getRequest().getHeader("referer"));
                } catch (IOException e) {
                    e.printStackTrace();
                }}

            }
        }
    }

    }
