package node;

import io.vertx.core.DeploymentOptions;
import io.vertx.core.Vertx;
import io.vertx.core.logging.Logger;
import io.vertx.core.logging.LoggerFactory;

import java.util.Properties;

public class StartupInstance {

  private static final Logger log = LoggerFactory.getLogger(StartupInstance.class);

  public static void main(String[] args) {
    /*
    * =================================================
    * FileSystemException or FileAccessException occurs
    * when the the WebServer gets deployed twice or more
    * AND when file caching is disabled AND when the
    * program gets executed in IntelliJ or Eclipse IDE
    * (running the fat-jar works without issues!)
    * =================================================
    */
    Properties properties = System.getProperties();
    properties.setProperty("vertx.disableFileCaching", "true"); //<-- issue related

    // Start Non-clustered
    Vertx vertx = Vertx.vertx();
    startup(vertx);
  }


  private static void startup(Vertx vertx) {
    DeploymentOptions options = new DeploymentOptions();
    options.setInstances(2); //<-- issue related
    vertx.deployVerticle(WebServer.class.getName(), options, res -> {
      if (res.failed()) {
        log.error("Uuups: " + res.cause());
        vertx.close();
      }
    });
  }
}
