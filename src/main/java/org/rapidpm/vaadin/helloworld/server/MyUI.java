package org.rapidpm.vaadin.helloworld.server;

import org.vaadin.leif.headertags.Link;
import org.vaadin.leif.headertags.Meta;
import org.vaadin.leif.headertags.MetaTags;
import com.vaadin.annotations.JavaScript;
import com.vaadin.annotations.Title;
import com.vaadin.server.VaadinRequest;
import com.vaadin.ui.Label;
import com.vaadin.ui.UI;

/**
 *
 */
@JavaScript("vaadin://js/app.js")
@Link(rel = "manifest", href = "VAADIN/manifest.json")
@MetaTags({
    @Meta(name = "viewport", content = "width=device-width, initial-scale=1") ,
    @Meta(name = "theme-color", content = "#404549") ,
})
@Title("Vaadin PWA Jumpstart")
public class MyUI extends UI {

  @Override
  protected void init(VaadinRequest request) {
    setContent(new Label("Hello World"));
  }
}
