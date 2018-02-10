package org.rapidpm.microservice;


import io.undertow.Handlers;
import io.undertow.Undertow;
import io.undertow.UndertowOptions;
import io.undertow.server.HttpHandler;
import io.undertow.server.handlers.PathHandler;
import io.undertow.servlet.Servlets;
import io.undertow.servlet.api.DeploymentInfo;
import io.undertow.servlet.api.DeploymentManager;
import org.rapidpm.vaadin.helloworld.server.MyProjectServlet;
import org.rapidpm.vaadin.helloworld.server.PWAServlet;

import javax.servlet.ServletException;
import java.util.Optional;

import static io.undertow.Handlers.redirect;
import static io.undertow.servlet.Servlets.servlet;

/**
 *
 */
public class Main {

  public static final String CONTEXT_PATH = "/";

  public static void start() {
    main(new String[0]);
  }

  public static void shutdown() {
    undertowOptional.ifPresent(Undertow::stop);
  }

  private static Optional<Undertow> undertowOptional;

  public static void main(String[] args) {

    // grap it here
    // http://bit.ly/undertow-servlet-deploy
    DeploymentInfo servletBuilder
        = Servlets.deployment()
                  .setClassLoader(Main.class.getClassLoader())
                  .setContextPath(CONTEXT_PATH)
                  .setDeploymentName("ROOT.war")
                  .setDefaultEncoding("UTF-8")
                  .addServlets(
                      servlet(
                          PWAServlet.class.getSimpleName(),
                          PWAServlet.class
                      ).addMappings("/sw.js", "/manifest.json", "/VAADIN/app.js")

                       .setAsyncSupported(true)
                       .setEnabled(true),
                      servlet(
                          MyProjectServlet.class.getSimpleName(),
                          MyProjectServlet.class
                      ).addMapping("/*")
                       .setAsyncSupported(true)
                       .setEnabled(true)

                  );

    DeploymentManager manager = Servlets
        .defaultContainer()
        .addDeployment(servletBuilder);

    manager.deploy();

    try {
      HttpHandler httpHandler = manager.start();
      PathHandler path = Handlers.path(redirect(CONTEXT_PATH))
                                 .addPrefixPath(CONTEXT_PATH, httpHandler);

      Undertow undertowServer = Undertow.builder()
                                        .addHttpListener(8080, "0.0.0.0")
                                        .setServerOption(UndertowOptions.ENABLE_HTTP2, true)
                                        .setHandler(path)
                                        .build();
      undertowServer.start();

      undertowOptional = Optional.of(undertowServer);

      undertowServer.getListenerInfo().forEach(System.out::println);

    } catch (ServletException e) {
      e.printStackTrace();
      undertowOptional = Optional.empty();
    }
  }
}
