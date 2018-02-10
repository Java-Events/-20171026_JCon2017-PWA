package org.rapidpm.vaadin.helloworld.server;

import com.vaadin.annotations.VaadinServletConfiguration;
import com.vaadin.server.*;
import com.vaadin.shared.Registration;
import org.rapidpm.dependencies.core.logger.HasLogger;
import org.vaadin.leif.headertags.HeaderTagHandler;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;

/**
 *
 */

@WebServlet(value = "/*", asyncSupported = true)
@VaadinServletConfiguration(productionMode = false, ui = MyUI.class)
public class MyProjectServlet extends VaadinServlet implements HasLogger {

  private Registration tagRegistration;

  @Override
  protected void servletInitialized() throws ServletException {
    super.servletInitialized();

    HeaderTagHandler.init(getService());

    tagRegistration = getService().addSessionInitListener(tagInitListener());
  }

  @Override
  public void destroy() {
    super.destroy();
    tagRegistration.remove();
  }

  private SessionInitListener tagInitListener() {
    return e ->
        e.getSession()
         .addBootstrapListener(new BootstrapListener() {
           @Override
           public void modifyBootstrapFragment(BootstrapFragmentResponse response) {
             // NOP, this is for portlets etc
           }

           @Override
           public void modifyBootstrapPage(BootstrapPageResponse response) {
             response
                 .getDocument()
                 .child(0)
                 .attr("lang", "en");
           }
         });
  }

}