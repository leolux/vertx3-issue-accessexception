package node;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.Future;
import io.vertx.core.Vertx;
import io.vertx.core.buffer.Buffer;
import io.vertx.core.http.HttpMethod;
import io.vertx.core.http.HttpServer;
import io.vertx.core.http.HttpServerOptions;
import io.vertx.core.logging.Logger;
import io.vertx.core.logging.LoggerFactory;
import io.vertx.core.net.JksOptions;
import io.vertx.ext.web.Router;

public class WebServer extends AbstractVerticle {

  private static final Logger log = LoggerFactory.getLogger(StartupInstance.class);
  private static final int PORT = 8888;

  @Override
  public void start(Future<Void> startFuture) {

    // Create Router
    Router router = Router.router(vertx);

    // Add handler for index.html
    addRootHandler(router);

    // Add catchAll handler
    addCatchAllHandler(router);

    // Start server
    HttpServerOptions httpServerOptions = new HttpServerOptions();
    enableSSL(httpServerOptions, vertx);
    HttpServer server = vertx.createHttpServer(httpServerOptions);
    server.requestHandler(router::accept).listen(PORT, ar -> {
      if (ar.failed()) {
        if (ar.cause() != null) {
          ar.cause().printStackTrace();
        }
        startFuture.fail(ar.cause());
      } else {
        log.info("Webserver started successfully!");
        startFuture.complete();
      }
    });
  }

  private static void enableSSL(HttpServerOptions options, Vertx vertx) {
    options.setSsl(true);

    // self-signed certificate for the subdomains *.vertx.io (can be any file, irrelevant for this issue)
    Buffer jksBuffer = vertx.fileSystem().readFileBlocking("cert/wild.vertx.io.jks");
    JksOptions jksOptions = new JksOptions().setValue(jksBuffer).setPassword("reactive");
    options.setKeyStoreOptions(jksOptions);

    options.addEnabledCipherSuite("TLS_ECDHE_ECDSA_WITH_AES_128_CBC_SHA256").addEnabledCipherSuite("TLS_ECDHE_RSA_WITH_AES_128_CBC_SHA256")
      .addEnabledCipherSuite("TLS_RSA_WITH_AES_128_CBC_SHA256").addEnabledCipherSuite("TLS_ECDH_ECDSA_WITH_AES_128_CBC_SHA256")
      .addEnabledCipherSuite("TLS_ECDH_RSA_WITH_AES_128_CBC_SHA256").addEnabledCipherSuite("TLS_DHE_DSS_WITH_AES_128_CBC_SHA256")
      .addEnabledCipherSuite("TLS_ECDHE_ECDSA_WITH_AES_128_CBC_SHA").addEnabledCipherSuite("TLS_ECDHE_RSA_WITH_AES_128_CBC_SHA")
      .addEnabledCipherSuite("TLS_RSA_WITH_AES_128_CBC_SHA").addEnabledCipherSuite("TLS_ECDH_ECDSA_WITH_AES_128_CBC_SHA")
      .addEnabledCipherSuite("TLS_ECDH_RSA_WITH_AES_128_CBC_SHA").addEnabledCipherSuite("TLS_DHE_DSS_WITH_AES_128_CBC_SHA")
      .addEnabledCipherSuite("TLS_ECDHE_ECDSA_WITH_AES_128_GCM_SHA256").addEnabledCipherSuite("TLS_ECDHE_RSA_WITH_AES_128_GCM_SHA256")
      .addEnabledCipherSuite("TLS_RSA_WITH_AES_128_GCM_SHA256").addEnabledCipherSuite("TLS_ECDH_ECDSA_WITH_AES_128_GCM_SHA256")
      .addEnabledCipherSuite("TLS_ECDH_RSA_WITH_AES_128_GCM_SHA256").addEnabledCipherSuite("TLS_DHE_DSS_WITH_AES_128_GCM_SHA256")
      .addEnabledCipherSuite("TLS_ECDHE_ECDSA_WITH_3DES_EDE_CBC_SHA").addEnabledCipherSuite("SSL_RSA_WITH_3DES_EDE_CBC_SHA")
      .addEnabledCipherSuite("TLS_ECDH_ECDSA_WITH_3DES_EDE_CBC_SHA").addEnabledCipherSuite("TLS_ECDH_RSA_WITH_3DES_EDE_CBC_SHA")
      .addEnabledCipherSuite("SSL_DHE_DSS_WITH_3DES_EDE_CBC_SHA").addEnabledCipherSuite("TLS_ECDHE_ECDSA_WITH_RC4_128_SHA")
      .addEnabledCipherSuite("SSL_RSA_WITH_RC4_128_SHA").addEnabledCipherSuite("TLS_ECDH_ECDSA_WITH_RC4_128_SHA")
      .addEnabledCipherSuite("TLS_ECDH_RSA_WITH_RC4_128_SHA").addEnabledCipherSuite("SSL_RSA_WITH_RC4_128_MD5")
      .addEnabledCipherSuite("TLS_EMPTY_RENEGOTIATION_INFO_SCSV").addEnabledCipherSuite("TLS_ECDH_anon_WITH_RC4_128_SHA")
      .addEnabledCipherSuite("SSL_DH_anon_WITH_RC4_128_MD5").addEnabledCipherSuite("SSL_RSA_WITH_DES_CBC_SHA")
      .addEnabledCipherSuite("SSL_DHE_RSA_WITH_DES_CBC_SHA").addEnabledCipherSuite("SSL_DHE_DSS_WITH_DES_CBC_SHA")
      .addEnabledCipherSuite("SSL_DH_anon_WITH_DES_CBC_SHA").addEnabledCipherSuite("SSL_RSA_EXPORT_WITH_DES40_CBC_SHA")
      .addEnabledCipherSuite("SSL_DHE_RSA_EXPORT_WITH_DES40_CBC_SHA").addEnabledCipherSuite("SSL_DHE_DSS_EXPORT_WITH_DES40_CBC_SHA")
      .addEnabledCipherSuite("SSL_DH_anon_EXPORT_WITH_DES40_CBC_SHA").addEnabledCipherSuite("SSL_RSA_EXPORT_WITH_RC4_40_MD5")
      .addEnabledCipherSuite("SSL_DH_anon_EXPORT_WITH_RC4_40_MD5").addEnabledCipherSuite("TLS_KRB5_WITH_3DES_EDE_CBC_SHA")
      .addEnabledCipherSuite("TLS_KRB5_WITH_3DES_EDE_CBC_MD5").addEnabledCipherSuite("TLS_KRB5_WITH_RC4_128_SHA")
      .addEnabledCipherSuite("TLS_KRB5_WITH_RC4_128_MD5").addEnabledCipherSuite("TLS_KRB5_WITH_DES_CBC_SHA")
      .addEnabledCipherSuite("TLS_KRB5_WITH_DES_CBC_MD5").addEnabledCipherSuite("TLS_KRB5_EXPORT_WITH_DES_CBC_40_SHA")
      .addEnabledCipherSuite("TLS_KRB5_EXPORT_WITH_DES_CBC_40_MD5").addEnabledCipherSuite("TLS_KRB5_EXPORT_WITH_RC4_40_SHA")
      .addEnabledCipherSuite("TLS_KRB5_EXPORT_WITH_RC4_40_MD5");
  }


  private void addCatchAllHandler(Router router) {
    router.routeWithRegex(".*").handler(routingContext -> {
      routingContext.response().sendFile("404.html");
    });
  }

  private void addRootHandler(Router router) {
    router.route().method(HttpMethod.GET).path("/").handler(routingContext -> {
      routingContext.response().sendFile("index.html");
    });
  }

}
