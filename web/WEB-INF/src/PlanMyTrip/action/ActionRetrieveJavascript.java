package PlanMyTrip.action;

import PlanMyTrip.error.JwfErrorHandler;
import org.apache.commons.io.FileUtils;
import org.esgi.web.framework.action.interfaces.IAction;
import org.esgi.web.framework.context.interfaces.IContext;

import java.io.File;
import java.io.IOException;
import java.net.URL;

/**
 * Created by Mathieu on 02/06/2015.
 */
public class ActionRetrieveJavascript implements IAction {

    @Override
    public int setPriority(int i) {
        return 0;
    }

    @Override
    public int getPriority() {
        return 0;
    }

    @Override
    public void addCredential(String s) {

    }

    @Override
    public boolean needsCredentials() {
        return false;
    }

    @Override
    public boolean hasCredential(String[] strings) {
        return false;
    }

    @Override
    public void proceed(IContext iContext) {
        String resource  = iContext._getRequest().getRequestURI();
        resource = resource.substring(resource.lastIndexOf("/"));
        try{
            URL resourceURL = this.getClass().getResource("/../js/" + resource);
            if (resourceURL != null)
            {
                String resourcePath = resourceURL.getFile();
                File resourceFile = new File(resourcePath);

                if(resourceFile.exists())
                {
                    String resourceContent = FileUtils.readFileToString(resourceFile);
                    iContext._getResponse().getWriter().println(resourceContent);
                }
            }
            else {
                JwfErrorHandler.displayError(iContext, 404, "There is no such script");
            }


        }catch (IOException err)
        {
            JwfErrorHandler.displayError(iContext, 404, "There is no such scrip");
        }

    }
}
