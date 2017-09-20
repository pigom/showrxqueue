package com.pigom.reactive.showrxqueue;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import io.vertx.core.Vertx;
import io.vertx.ext.unit.Async;
import io.vertx.ext.unit.TestContext;
import io.vertx.ext.unit.junit.VertxUnitRunner;

@RunWith(VertxUnitRunner.class)
public class MessageVerticleTest {

    private final static int PORT = 9999;
    private final static String HOST = "localhost";
    private final static String PATH_CHANNEL1 = "/channel1";
    Vertx vertx;

    @Before
    public void before(final TestContext context) {
        vertx = Vertx.vertx();
        vertx.deployVerticle(MessageVerticle.class.getName(), context.asyncAssertSuccess());
    }

    @After
    public void after(final TestContext context) {
        vertx.close(context.asyncAssertSuccess());
    }

    @Test
    public void testChannel1(final TestContext context) {
        final Async async = context.async();

        vertx.createHttpClient()
                .getNow(PORT, HOST, PATH_CHANNEL1, response -> {
                    response.handler(body -> {
                        context.assertTrue(body.toString().contains("1.25"));
                        async.complete();
                    });
                });
    }
}
