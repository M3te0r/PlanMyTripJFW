package PlanMyTrip.action;

import PlanMyTrip.error.JwfErrorHandler;
import org.apache.commons.io.FileUtils;
import org.esgi.web.framework.action.interfaces.IAction;
import org.esgi.web.framework.context.interfaces.IContext;

import java.io.File;
import java.io.IOException;
import java.net.URL;

/**
 * Created by Mathieu on 05/06/2015.
 */
public class ActionUserAccountJavascript implements IAction {
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
        try{
            URL resourceURL = this.getClass().getResource("/../templates/pages/user/account/script.js");
            if (resourceURL != null)
            {
                String resourcePath = resourceURL.getFile();
                File resourceFile = new File(resourcePath);

                if(resourceFile.exists())
                {
                    String resourceContent = FileUtils.readFileToString(resourceFile);
                    context._getResponse().getWriter().println(resourceContent);
                }
            }
            else {
                JwfErrorHandler.displayError(context, 404, "There is no such script");
            }


        }catch (IOException err)
        {
            JwfErrorHandler.displayError(context, 404, "There is no such scrip");
        }

    }
}
