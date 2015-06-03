package PlanMyTrip.controller;

import PlanMyTrip.action.AActionCredential;
import PlanMyTrip.context.Context;
import PlanMyTrip.error.JwfErrorHandler;
import PlanMyTrip.router.Dispatcher;
import PlanMyTrip.router.RewriteRule;
import PlanMyTrip.router.Rewriter;
import org.esgi.web.framework.core.interfaces.IFrontController;
import org.esgi.web.framework.router.interfaces.IDispatcher;
import org.esgi.web.framework.router.interfaces.IRewriter;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;


public class FrontController extends HttpServlet implements IFrontController {

	private static final long serialVersionUID = 1L;
	
	public static String URIroot = "/PlanMyTrip";
	
	private IRewriter rewriter;
	private IDispatcher dispatcher;
	private Context c;
	
	public void init() {
		rewriter = new Rewriter();
		dispatcher = new Dispatcher();
		rewriter.addRule(new RewriteRule(URIroot + "/css/(.*\\.(css))", "GET|POST", "PlanMyTrip.action.ActionStyle"));
		rewriter.addRule(new RewriteRule(URIroot + "/font/(.*\\.((otf)|(ttf)))", "GET|POST", "PlanMyTrip.action.ActionRetrieveFont"));
		rewriter.addRule(new RewriteRule(URIroot + "/img/(.*\\.((jpe?g)|(png)))", "GET|POST", "PlanMyTrip.action.ActionRetrieveImage"));
		rewriter.addRule(new RewriteRule(URIroot  + "/js/(.*\\.(js))", "GET|POST", "PlanMyTrip.action.ActionRetrieveJavascript"));
		rewriter.addRule(new RewriteRule(URIroot + "/js/vendor/(.*\\.(js))", "GET|POST", "PlanMyTrip.action.ActionRetrieveJavascript"));
		rewriter.addRule(new RewriteRule(URIroot + "$", "GET|POST", "PlanMyTrip.action.ActionIndex"));
		rewriter.addRule(new RewriteRule(URIroot + "(\\/.?(index.html)?$)", "GET|POST", "PlanMyTrip.action.ActionIndex"));
		rewriter.addRule(new RewriteRule(URIroot + "/pages/(.*\\.(html))", "GET|POST", "PlanMyTrip.action.ActionRetrievePage"));
	}

	@Override
	public void service(HttpServletRequest request, HttpServletResponse response) {
		handle(request, response);
	}
	
	@Override
	public void handle(HttpServletRequest request, HttpServletResponse response) {
		try {
			rewriter.rewrite(c = new Context(request, response));
			
			if(checkClass(c)) {
//				if(checkRights(c)) {
				dispatcher.dispatch(c);
//				} else {
//					// 403
//					JwfErrorHandler.displayError(c, 403, "you doesn't have the rights to view this page");
//				}
			} else {
				// 404
				JwfErrorHandler.displayError(c, 404, "the page doesn't exist");
			}
			
			c.removeUploadedFiles();
		} catch (Exception e) {
			e.printStackTrace(); // 500
			JwfErrorHandler.displayError(c, 500, "error while loading page : " + e);
		}
	}
	
	private boolean checkClass(Context c) {
		return c.getActionClass() != null && c.getActionClass().length() > 0;
	}
	
	private boolean checkRights(Context c) throws Exception {
		String[] credentialsNeeded = (String[]) Class.forName(c.getActionClass()).getMethod("getCredentials", null).invoke(null, null);
		return AActionCredential.hasCredential(credentialsNeeded, c.getUserCredentials());
	}
	
}
