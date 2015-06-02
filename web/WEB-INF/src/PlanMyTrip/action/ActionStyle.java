package PlanMyTrip.action;

import org.esgi.web.framework.action.interfaces.IAction;
import org.esgi.web.framework.context.interfaces.IContext;

import java.io.*;

/**
 * Created by Mathieu on 02/06/2015.
 */
public class ActionStyle implements IAction {

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
        String resource = iContext._getRequest().getRequestURI();
        resource = resource.substring(resource.lastIndexOf("/"));
        File resourceFile;
        try {
            resourceFile = new File(this.getClass().getResource("/../css/" + resource).getFile());
            if( resourceFile.exists())
            {
                String a;
                try (BufferedReader out = new BufferedReader(new InputStreamReader(new BufferedInputStream(new FileInputStream(resourceFile))))){
                    PrintWriter writer = iContext._getResponse().getWriter();
                    while ((a = out.readLine()) != null)
                    {
                        writer.println(a);
                    }
                } catch (IOException er)
                {
                    System.out.println("404 - File doesn't exists");

                }
            }
        }
        catch (NullPointerException ed)
        {
            System.out.println("404 - File doesn't exists");
        }
    }
}
