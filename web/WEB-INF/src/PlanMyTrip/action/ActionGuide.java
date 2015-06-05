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
 * Created by Mathieu on 04/06/2015.
 */
public class ActionGuide implements IAction {

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

        Object userId  = context.getSessionAttribute("user-id");
        int intUserId = -1;
        if(userId != null) {
            intUserId = Integer.parseInt(userId.toString());
        }

        int id_guide = Integer.parseInt(((Context) context).getParameterUnique("Id_Guide"));
        String votes = ((Context)context).getParameterUnique("votes");

        try {
            if (votes != null && intUserId != -1) {
                boolean hasUserVoted = MySQLAccess.hasUserVoted(id_guide, intUserId);
                if (votes.equals("like") && hasUserVoted){
                    MySQLAccess.voteForUserGuideByUpdate(id_guide, intUserId, 1, 0);
                }
                else if(votes.equals("like") && !hasUserVoted){
                    MySQLAccess.voteForUserGuideByCreate(id_guide, intUserId, 1, 0);
                }
                else if(votes.equals("dislike") && hasUserVoted){
                    MySQLAccess.voteForUserGuideByUpdate(id_guide, intUserId, 0, 1);
                }
                else if(votes.equals("dislike") && !hasUserVoted){
                    MySQLAccess.voteForUserGuideByCreate(id_guide, intUserId, 0, 1);
                }
            }
        }
        catch(SQLException e)
        {
            System.out.println("SQL error");
            e.printStackTrace();
        }

        pageContext.put("votes", votes);
        pageContext.put("Id_Guide", id_guide);
        pageContext.put("userId", userId);
        String search = ((Context) context ).getParameterUnique("search");
        String duration = ((Context) context).getParameterUnique("duration");
        pageContext.put("s", search);
        pageContext.put("d", duration);

        pageContext.put("SQL", new MySQLAccess());
        Template t = ve.getTemplate("/guide/index.vm");
        StringWriter writer = new StringWriter();
        t.merge(pageContext, writer);

        try {
            context._getResponse().getWriter().write(writer.toString());
        } catch (IOException e) {
            System.out.println("Could not load page searched.html");
            e.printStackTrace();
        }

    }
}
