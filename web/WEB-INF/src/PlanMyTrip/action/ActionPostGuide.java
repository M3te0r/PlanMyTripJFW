package PlanMyTrip.action;

import PlanMyTrip.context.Context;
import PlanMyTrip.database.MySQLAccess;
import org.apache.velocity.Template;
import org.apache.velocity.VelocityContext;
import org.apache.velocity.app.VelocityEngine;
import org.apache.velocity.runtime.RuntimeConstants;
import org.esgi.web.framework.action.interfaces.IAction;
import org.esgi.web.framework.context.interfaces.IContext;

import java.io.IOException;
import java.io.StringWriter;
import java.sql.SQLException;

/**
 * Created by Mathieu on 05/06/2015.
 */
public class ActionPostGuide implements IAction {

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

        context._getResponse().setContentType("text/html");
        VelocityEngine ve = new VelocityEngine();
        ve.setProperty(RuntimeConstants.FILE_RESOURCE_LOADER_PATH, context._getRequest().getServletContext().getRealPath("/").replace("\\", "/") + "WEB-INF/templates/pages");
        ve.init();
        VelocityContext pageContext = new VelocityContext();
        pageContext.put("userId", context.getSessionAttribute("user-id"));
        pageContext.put("userPseudo", context.getSessionAttribute("user-pseudo"));
        String pays = ((Context) context).getParameterUnique("pays");
        String titre = ((Context) context).getParameterUnique("titre");
        String ville = ((Context) context).getParameterUnique("ville");
        String duration = ((Context) context).getParameterUnique("duration");
        String contenu = ((Context) context).getParameterUnique("input");


        Template t = ve.getTemplate("/new_guide/post.vm");
        StringWriter writer = new StringWriter();
        t.merge(pageContext, writer);

        try {
            if (pays == null || pays.isEmpty() || ville == null || ville.isEmpty() || contenu == null || contenu.isEmpty() || duration == null || duration.isEmpty())
            {
                context._getResponse().sendRedirect(context._getRequest().getHeader("referer") + "?error=1");
            }
            else {
                try {
                    MySQLAccess.InsertNewGuide((Integer) context.getSessionAttribute("user-id"), pays, ville, titre, contenu, Integer.parseInt(duration));
                } catch (SQLException e) {
                    e.printStackTrace();
                }

            }
            context._getResponse().getWriter().write(writer.toString());
        } catch (IOException e) {
            System.out.println("Could not load page searched.html");
            e.printStackTrace();
        }

    }
}
