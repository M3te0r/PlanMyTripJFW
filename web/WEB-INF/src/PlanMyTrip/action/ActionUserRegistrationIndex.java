package PlanMyTrip.action;

import PlanMyTrip.context.Context;
import org.apache.velocity.Template;
import org.apache.velocity.VelocityContext;
import org.apache.velocity.app.VelocityEngine;
import org.apache.velocity.runtime.RuntimeConstants;
import org.esgi.web.framework.action.interfaces.IAction;
import org.esgi.web.framework.context.interfaces.IContext;

import java.io.IOException;
import java.io.StringWriter;

/**
 * Created by Mathieu on 04/06/2015.
 */
public class ActionUserRegistrationIndex implements IAction {

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
        String errorString = ((Context) context).getParameterUnique("error");
        pageContext.put("err", errorString);
        Template t = ve.getTemplate("/registration/index.vm");
        StringWriter writer = new StringWriter();
        t.merge(pageContext, writer);

        try {
            context._getResponse().getWriter().write(writer.toString());
        } catch (IOException e) {
            System.out.println("Could not load page index.html");
            e.printStackTrace();
        }

    }
}
