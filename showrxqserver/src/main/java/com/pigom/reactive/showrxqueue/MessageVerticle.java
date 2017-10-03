package com.pigom.reactive.showrxqueue;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.pigom.reactive.showrxqueue.model.Channel;
import com.pigom.reactive.showrxqueue.model.Message;

import java.util.HashMap;
import java.util.Map;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.eventbus.MessageConsumer;
import io.vertx.core.http.HttpMethod;
import io.vertx.core.http.HttpServer;
import io.vertx.core.http.HttpServerOptions;
import io.vertx.ext.web.Router;
import io.vertx.ext.web.handler.CorsHandler;

/**
 * MessageVerticle starts an HTTP Server to be able to gather the current value on a channel
 */
public class MessageVerticle extends AbstractVerticle {

    Map<String, Channel> channels = new HashMap<>();

    @Override
    public void start() throws Exception {
        Router router = Router.router(vertx);
        router.route().handler(CorsHandler.create("*").allowedMethod(HttpMethod.GET));
        router.get("/channel").handler(req -> req.response()
                .putHeader("content-type", "application/json")
                .end(getChannelsJson()));
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

                    String channel = message.address().substring("com.pigom.".length());
                    System.out.println("received from event bus '" + channel + "': " + message.body());

                    Channel ch;
                    if (channels.containsKey(channel)) {
                        ch = channels.get(channel);
                    } else {
                        ch = new Channel();
                        ch.setName(channel);
                    }
                    Message m = new Message();
                    m.setValue(message.body());
                    ch.setCurrentMessage(m);
                    channels.put(channel, ch);
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

    public String getChannelsJson() {
        ObjectMapper mapper = new ObjectMapper();

        try {
            return mapper.writeValueAsString(channels.values());
        } catch (JsonProcessingException e) {
            return null;
        }
    }
}
