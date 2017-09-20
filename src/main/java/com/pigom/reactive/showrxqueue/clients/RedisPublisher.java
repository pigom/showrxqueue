package com.pigom.reactive.showrxqueue.clients;

import io.lettuce.core.RedisClient;
import io.lettuce.core.RedisURI;
import io.lettuce.core.pubsub.StatefulRedisPubSubConnection;
import io.lettuce.core.pubsub.api.reactive.RedisPubSubReactiveCommands;

public class RedisPublisher {

    public RedisPublisher() {
    }

    public static void main(String args[]) {
        RedisClient client = RedisClient.create(RedisURI.create("lapqc05987", 6379));

        StatefulRedisPubSubConnection<String, String> connection = client.connectPubSub();
        RedisPubSubReactiveCommands reactive = connection.reactive();
        reactive.publish("pigom", "{\"name\":\"toto\"}");

        connection.close();
    }
}
