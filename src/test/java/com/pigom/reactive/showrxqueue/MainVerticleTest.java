package com.pigom.reactive.showrxqueue;

import org.junit.After;
import org.junit.Before;
import org.junit.runner.RunWith;

import io.vertx.core.Vertx;
import io.vertx.ext.unit.TestContext;
import io.vertx.ext.unit.junit.VertxUnitRunner;


@RunWith(VertxUnitRunner.class)
public class MainVerticleTest {

    Vertx vertx;

    @Before
    public void before(final TestContext context) {
        vertx = Vertx.vertx();
        vertx.deployVerticle(MainVerticle.class.getName(), context.asyncAssertSuccess());
    }

    @After
    public void after(final TestContext context) {
        vertx.close(context.asyncAssertSuccess());
    }

}