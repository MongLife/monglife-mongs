package com.monglife.mongs.adapter.out.mong.persistence.utils;

import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.GenericContainer;
import org.testcontainers.utility.DockerImageName;

/**
 * Redis 통합 테스트용 컨테이너.
 *
 * 이 어댑터가 쓰는 Redis 를 테스트 프로세스 안에서 직접 띄운다. 예전에는 test 프로필의
 * 외부 개발 서버를 그대로 봤는데, 그러면 CI 가 그 서버의 가용성에 묶이고 테스트끼리
 * 같은 데이터를 덮어썼다.
 *
 * 실행에는 Docker 가 필요하다. Docker Desktop 이나 CI 러너처럼 /var/run/docker.sock 이
 * 있는 환경은 설정 없이 그대로 돈다. colima 처럼 소켓이 다른 곳에 있으면 셸에 두 줄을 준다.
 *
 *   export DOCKER_HOST="unix://${HOME}/.colima/default/docker.sock"
 *   export TESTCONTAINERS_DOCKER_SOCKET_OVERRIDE=/var/run/docker.sock
 */
public abstract class RedisTestContainer {

    /** 이 프로젝트는 spring.data.<도메인>.redis.* 라는 자체 네임스페이스를 쓴다. */
    private static final String DOMAIN = "mong";

    private static final String IMAGE = "redis:7-alpine";
    private static final String PASSWORD = "test-password";
    private static final int REDIS_PORT = 6379;
    private static final int DATABASE = 3;

    private static final String DOCKER_API_VERSION_KEY = "api.version";
    private static final String DOCKER_API_VERSION = "1.41";

    /**
     * 테스트 JVM 하나에 컨테이너 하나다. 클래스마다 새로 띄우면 모듈 안의 테스트 클래스 수만큼
     * 기동 비용을 낸다. JVM 이 끝나면 Testcontainers 의 Ryuk 이 컨테이너를 지운다.
     */
    private static final GenericContainer<?> REDIS =
            new GenericContainer<>(DockerImageName.parse(IMAGE))
                    .withExposedPorts(REDIS_PORT)
                    // 운영은 비밀번호를 쓴다. 인증 경로까지 같은 조건으로 검증한다.
                    .withCommand("redis-server", "--requirepass", PASSWORD);

    static {
        // docker-java 는 API 버전을 1.32 로 협상하는데, Docker Engine 29 부터는 1.40 미만을
        // 거부한다("client version 1.32 is too old"). 컨테이너를 만들기 전에 올려 둔다.
        // 밖에서 지정한 값이 있으면 그대로 존중한다.
        if (System.getProperty(DOCKER_API_VERSION_KEY) == null) {
            System.setProperty(DOCKER_API_VERSION_KEY, DOCKER_API_VERSION);
        }

        REDIS.start();
    }

    /**
     * 표준 키(spring.data.redis.*)가 아니라 @ServiceConnection 이 붙지 않는다. 네 값을 직접 넣는다.
     *
     * 여기서 등록한 값은 application-test.yml 보다 우선한다. 그래서 yml 의 외부 서버 주소를
     * 그대로 둬도 테스트는 항상 이 컨테이너를 본다. (yml 은 configs 서브모듈이 만들어 내는
     * 파일이라 이 저장소에서 고칠 수도 없다.)
     */
    @DynamicPropertySource
    static void redisProperties(DynamicPropertyRegistry registry) {
        registry.add("spring.data." + DOMAIN + ".redis.host", REDIS::getHost);
        registry.add("spring.data." + DOMAIN + ".redis.port", () -> REDIS.getMappedPort(REDIS_PORT));
        registry.add("spring.data." + DOMAIN + ".redis.password", () -> PASSWORD);
        registry.add("spring.data." + DOMAIN + ".redis.database", () -> DATABASE);
    }
}
