package com.pigom.reactive.showrxqueue;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import io.vertx.core.Vertx;
import io.vertx.ext.unit.Async;
import io.vertx.ext.unit.TestContext;
import io.vertx.ext.unit.junit.VertxUnitRunner;
import io.vertx.redis.RedisClient;
import io.vertx.redis.RedisOptions;

@RunWith(VertxUnitRunner.class)
public class RedisChannelVerticleTest {

    Vertx vertx;
    Async subscribeAsync;

    @Before
    public void before(final TestContext context) {
        subscribeAsync = context.async();
        vertx = Vertx.vertx();
        vertx.deployVerticle(RedisChannelVerticle.class.getName(), res -> {
            if (res.succeeded()) {
                subscribeAsync.complete();
            }
        });
    }

    @After
    public void after(final TestContext context) {
        vertx.close(context.asyncAssertSuccess());
    }

    @Test
    public void testPublish(final TestContext context) {
        Async async = context.async();
        subscribeAsync.awaitSuccess();

        RedisClient redis = RedisClient.create(vertx,
                new RedisOptions().setAddress("LAPQC05987.exfo.com").setPort(6379));

        redis.publish("channel1", "Hello World!", res -> {
            if (res.succeeded()) {
                System.out.println("Test published");
            } else {
                System.out.println("Test error");
            }
        });
        async.complete();
    }
}
