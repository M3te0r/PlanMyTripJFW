package PlanMyTrip.action;

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
 * Created by Mathieu on 02/06/2015.
 */
public class ActionIndex implements IAction {

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
        ve.setProperty(RuntimeConstants.FILE_RESOURCE_LOADER_PATH, context._getRequest().getServletContext().getRealPath("/").replace("\\", "/") + "WEB-INF/templates");
        ve.init();
        VelocityContext indexContext = new VelocityContext();
        indexContext.put("userId", context.getSessionAttribute("user-id"));
        indexContext.put("userPseudo", context.getSessionAttribute("user-pseudo"));
        Template  t = ve.getTemplate("index.vm");

        try {
            indexContext.put("lastGuides", MySQLAccess.getLastGuides());
        } catch (SQLException e) {
            e.printStackTrace();
        }
        StringWriter writer = new StringWriter();
        t.merge(indexContext, writer);
        try {
            context._getResponse().getWriter().write(writer.toString());
        } catch (IOException e) {
            System.out.println("Erreur lors de l'écriture sur la Page Web");
            e.printStackTrace();
        }

    }
}
