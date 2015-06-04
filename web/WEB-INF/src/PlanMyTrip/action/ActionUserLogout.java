package PlanMyTrip.action;

import PlanMyTrip.error.JwfErrorHandler;
import PlanMyTrip.message.JwfMessage;
import PlanMyTrip.renderer.Renderer;
import org.esgi.web.framework.action.interfaces.IAction;
import org.esgi.web.framework.context.interfaces.IContext;

import java.io.IOException;

/**
 * Created by Mathieu on 04/06/2015.
 */
public class ActionUserLogout implements IAction {

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

        context.resetSession();

        try {
            context.setAttribute("model", new JwfMessage("You are now disconnected"));
            context._getResponse().setContentType("text/html");
            context._getResponse().getWriter().write(new Renderer().render(context));
            context._getResponse().sendRedirect(context._getRequest().getHeader("referer"));
        } catch (IOException e) {
            JwfErrorHandler.displayError(context, 500, "error while writting response : " + e.getMessage());
            e.printStackTrace();
        }

    }
}
