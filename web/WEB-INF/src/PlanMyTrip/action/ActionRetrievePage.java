package PlanMyTrip.action;

import org.apache.velocity.Template;
import org.apache.velocity.VelocityContext;
import org.apache.velocity.app.VelocityEngine;
import org.apache.velocity.runtime.RuntimeConstants;
import org.esgi.web.framework.action.interfaces.IAction;
import org.esgi.web.framework.context.interfaces.IContext;

import java.io.IOException;
import java.io.StringWriter;

/**
 * Created by Mathieu on 02/06/2015.
 */
public class ActionRetrievePage implements IAction {

    @Override
    public int setPriority(int priority) {
        return 0;
    }

    @Override
    public int getPriority() {
        return 0;
    }

    @Override
    public void addCredential(String role) {

    }

    @Override
    public boolean needsCredentials() {
        return false;
    }

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
        String requested = context._getRequest().getRequestURI();
        requested = requested.substring(requested.lastIndexOf("/pages/") + 1).replace(".html", ".vm");
        requested = requested.substring(requested.indexOf("/"));
        VelocityContext pageContext = new VelocityContext();

        Template t = ve.getTemplate(requested);
        StringWriter writer = new StringWriter();
        t.merge(pageContext, writer);

        try {
            context._getResponse().getWriter().write(writer.toString());
        } catch (IOException e) {
            System.out.println("Could not load page " + requested);
            e.printStackTrace();
        }


    }
}
