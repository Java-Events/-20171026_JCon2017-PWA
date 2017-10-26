package org.rapidpm.vaadin.helloworld.server;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;

import org.apache.commons.io.IOUtils;
import org.vaadin.leif.headertags.HeaderTagHandler;
import com.vaadin.annotations.VaadinServletConfiguration;
import com.vaadin.server.BootstrapFragmentResponse;
import com.vaadin.server.BootstrapListener;
import com.vaadin.server.BootstrapPageResponse;
import com.vaadin.server.RequestHandler;
import com.vaadin.server.ServiceException;
import com.vaadin.server.SessionInitEvent;
import com.vaadin.server.SessionInitListener;
import com.vaadin.server.VaadinRequest;
import com.vaadin.server.VaadinResponse;
import com.vaadin.server.VaadinServlet;
import com.vaadin.server.VaadinSession;
import com.vaadin.shared.Registration;

/**
 *
 */

@WebServlet("/*")
@VaadinServletConfiguration(productionMode = false, ui = MyUI.class)
public class MyProjectServlet extends VaadinServlet {

  private Registration pwaRegistration;
  private Registration tagRegistration;

  @Override
  protected void servletInitialized() throws ServletException {
    super.servletInitialized();

    HeaderTagHandler.init(getService());

    pwaRegistration = getService().addSessionInitListener(pwaInitListener());
    tagRegistration = getService().addSessionInitListener(tagInitListener());
  }

  @Override
  public void destroy() {
    super.destroy();
    pwaRegistration.remove();
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
                 .attr("lang" , "en");
           }
         });
  }

  private SessionInitListener pwaInitListener() {
    return new SessionInitListener() {
      @Override
      public void sessionInit(SessionInitEvent event) throws ServiceException {
        event
            .getSession()
            .addRequestHandler(new RequestHandler() {

              @Override
              public boolean handleRequest(VaadinSession session ,
                                           VaadinRequest request ,
                                           VaadinResponse response) throws IOException {

                String pathInfo = request.getPathInfo();

                if (pathInfo.endsWith("sw.js")) {
                  response.setContentType("application/javascript");
                  InputStream in = getClass().getResourceAsStream("/sw.js");

                  if (in != null) {
                    OutputStream out = response.getOutputStream();
                    IOUtils.copy(in , out);
                    in.close();
                    out.close();
                    return true;
                  } else {
                    return false;
                  }
                }
                return false;
              }
            });
      }
    };
  }
}