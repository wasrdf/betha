/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.betha.util;

import javax.faces.application.NavigationHandler;
import javax.faces.context.FacesContext;
import javax.faces.event.PhaseEvent;
import javax.faces.event.PhaseId;
import javax.faces.event.PhaseListener;
import javax.servlet.http.HttpSession;

public class AutenticacaoPhaseListener implements PhaseListener {

    Object currentUser = null;

    @Override
    public void afterPhase(PhaseEvent event) {
        FacesContext facesContext = event.getFacesContext();
        String currentPage = facesContext.getViewRoot().getViewId();
        System.out.println(currentPage);
        boolean isLoginPage = (currentPage.lastIndexOf("/login/login.xhtml") > -1)
                ;

        HttpSession session = (HttpSession) facesContext.getExternalContext().getSession(true);
        currentUser = session.getAttribute("currentUser");

        if (!isLoginPage && currentUser == null) {
            NavigationHandler nh = facesContext.getApplication().getNavigationHandler();
            nh.handleNavigation(facesContext, null, "/login/login.xhtml");
        }
        
         if (!isLoginPage && currentUser == null) {
            NavigationHandler nh = facesContext.getApplication().getNavigationHandler();
            nh.handleNavigation(facesContext, null, "/buyer_register/register.xhtml");
        }
    }

    public void beforePhase(PhaseEvent event) {
    }

    public PhaseId getPhaseId() {
        return PhaseId.RESTORE_VIEW;
    }
}
