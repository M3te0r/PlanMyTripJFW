package PlanMyTrip.action;

import PlanMyTrip.error.JwfErrorHandler;
import org.esgi.web.framework.action.interfaces.IAction;
import org.esgi.web.framework.context.interfaces.IContext;

import javax.imageio.ImageIO;
import javax.servlet.ServletOutputStream;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.net.URL;

/**
 * Created by Mathieu on 02/06/2015.
 */
public class ActionRetrieveImage implements IAction {


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
            URL resourceURL = this.getClass().getResource("/../img/" + resource);
            if (resourceURL != null)
            {
                String resourcePath = resourceURL.getFile();
                File resourceFile = new File(resourcePath);
                if(resourceFile.exists())
                {
                    try{
                        BufferedImage bufferedImage = ImageIO.read(resourceFile);
                        ServletOutputStream outputStream = iContext._getResponse().getOutputStream();

                        ImageIO.write(bufferedImage, resourceFile.getName().substring(resourceFile.getName().lastIndexOf(".") + 1), outputStream);
                        outputStream.close();

                    }catch (IOException io)
                    {
                        JwfErrorHandler.displayError(iContext, 404, "Could not find image");
                    }
                }
            }
    }
}
