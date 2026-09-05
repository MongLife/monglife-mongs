package com.monglife.mongs.adapter.out.member.publish.utils;

import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.GenericContainer;
import org.testcontainers.images.builder.Transferable;
import org.testcontainers.utility.DockerImageName;

public abstract class MqttTestContainer {

    private static final String IMAGE = "eclipse-mosquitto:2.0";
    private static final int MQTT_PORT = 1883;

    private static final String DOCKER_API_VERSION_KEY = "api.version";
    private static final String DOCKER_API_VERSION = "1.41";

    /**
     * mosquitto 2.0 은 설정이 없으면 localhost 로만 듣고 익명 접속을 막는다.
     * 컨테이너 밖에서 붙어야 하므로 리스너를 열어 준다.
     */
    private static final String CONFIG = "listener " + MQTT_PORT + "\nallow_anonymous true\n";

    private static final GenericContainer<?> MQTT =
            new GenericContainer<>(DockerImageName.parse(IMAGE))
                    .withExposedPorts(MQTT_PORT)
                    .withCopyToContainer(Transferable.of(CONFIG), "/mosquitto/config/mosquitto.conf");

    static {
        if (System.getProperty(DOCKER_API_VERSION_KEY) == null) {
            System.setProperty(DOCKER_API_VERSION_KEY, DOCKER_API_VERSION);
        }
        MQTT.start();
    }

    @DynamicPropertySource
    static void mqttProperties(DynamicPropertyRegistry registry) {
        registry.add("module.mqtt.host", MQTT::getHost);
        registry.add("module.mqtt.port", () -> MQTT.getMappedPort(MQTT_PORT));
    }
}
