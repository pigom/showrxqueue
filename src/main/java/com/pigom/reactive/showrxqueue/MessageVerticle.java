package com.pigom.reactive.showrxqueue;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.eventbus.MessageConsumer;
import io.vertx.core.http.HttpServer;
import io.vertx.core.http.HttpServerOptions;
import io.vertx.ext.web.Router;

/**
 * MessageVerticle starts an HTTP Server to be able to gather the current value on a channel
 */
public class MessageVerticle extends AbstractVerticle {

    String currentValue;

    @Override
    public void start() throws Exception {
        Router router = Router.router(vertx);
        router.get("/channel1").handler(req -> req.response()
                .putHeader("content-type", "application/json")
                .end("{\"value\": \""+ currentValue + "\"}"));
        HttpServerOptions serverOptions = new HttpServerOptions().setPort(9999);
        HttpServer server = vertx.createHttpServer(serverOptions).requestHandler(router::accept);
        server.listen(res -> {
            if (res.succeeded()) {
                System.out.println("HTTP Server started!");
            } else {
                System.out.println("Fail to load Http server");
            }
        });

        MessageConsumer<String> consumer = vertx.eventBus().consumer("com.pigom.ch1");
        consumer.handler(message -> {
                    System.out.println("received from event bus: " + message.body());
                    currentValue = message.body();
                }
        );

        consumer.completionHandler(res -> {
            if (res.succeeded()) {
                System.out.println("The handler registration has reached all nodes");
            } else {
                System.out.println("Registration failed!");
            }
        });
    }
}
