package com.monglife.mongs.adapter.out.member.persistence.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.SmartInitializingSingleton;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.init.DatabasePopulatorUtils;
import org.springframework.jdbc.datasource.init.ResourceDatabasePopulator;
import org.springframework.util.StringUtils;

import javax.sql.DataSource;

/**
 * member 데이터소스 초기 데이터 적재.
 *
 * 스크립트는 configs 서브모듈에서 온다:
 *   configs/properties/adapter-out/member-persistence-adapter-out/*.sql
 *   → copyPrivate → src/main/resources/*.sql → classpath:*.sql
 *
 * <p><b>스크립트가 지켜야 할 것.</b> {@code ScriptUtils} 의 파서가 걸리는 지점들이다.
 * <ul>
 *   <li>파일은 반드시 {@code ;} 로 끝난다. 문장 구분자가 하나도 없으면 파서가 개행을
 *       구분자로 대체해 여러 줄짜리 문자열 리터럴이 산산조각 난다.</li>
 *   <li>줄 맨 앞의 {@code --} 는 리터럴 안이든 밖이든 그 줄 전체가 주석으로 지워진다.
 *       데이터 값이 대시로 시작하면 안 된다.</li>
 *   <li>백슬래시를 쓰지 않는다. 이스케이프 분기가 뒤 한 글자의 따옴표 토글을 무시한다.</li>
 * </ul>
 *
 * <p>yml 만으로 되는 길이 없어서 이 빈이 있다.
 * <ul>
 *   <li>부트의 {@code spring.sql.init.*} 은 부트가 <b>자동 구성한 단일</b> 데이터소스에만 걸린다.
 *       이 프로젝트는 battle / mong / task / member 를 직접 만들어 써서 백오프한다.</li>
 *   <li>하이버네이트의 {@code hbm2ddl.import_files} 는 스키마를 <b>생성</b>할 때만 돈다.
 *       dev 의 {@code update}, stg/prd 의 {@code none} 에서는 실행되지 않는다.</li>
 * </ul>
 *
 * <p><b>discovery 의 같은 이름 설정과 한 가지가 다르다.</b> discovery 는
 * {@code DataSourceInitializer} 빈에 {@code @DependsOn(EntityManager)} 를 걸지만,
 * 여기서는 {@link SmartInitializingSingleton} 으로 <b>모든 싱글턴이 만들어진 뒤</b>에 돈다.
 * user-service 는 EntityManager 가 member 하나뿐이라 여기만 놓고 보면 {@code @DependsOn} 으로도
 * 충분하다. 그럼에도 맞춰 두는 것은 character-service 쪽 사정 때문이다 — 거기는
 * battle · mong · task 세 EntityManager 가 모두 {@code com.monglife.module.common.jpa.entity} 를
 * 스캔해 같은 DB 의 {@code monglife_comn_code} · {@code monglife_group_code} 를 각자
 * {@code create-drop} 하므로, EMF 하나에만 순서를 걸면 뒤에 생성되는 EMF 가 시드를 날린다.
 * 두 서비스가 같은 파일을 공유하는 이상 적재 시점도 같은 규칙으로 두는 편이 낫다.
 */
@Slf4j
@Configuration(SqlInitConfig.CONFIG_NAME)
public class SqlInitConfig implements SmartInitializingSingleton {

    /**
     * const values for config bean name
     */
    static final String CONFIG_NAME            = "memberSqlInitConfig";
    private static final String DOMAIN_NAME    = "member";
    private static final String DATASOURCE_NAME = DOMAIN_NAME + "DataSource";
    private static final String SQL_SUFFIX     = ".sql";
    private static final String TABLE_PATTERN  = "[A-Za-z0-9_]+";
    private static final String COUNT_QUERY    = "SELECT COUNT(*) FROM ";
    private static final String SCRIPT_ENCODING = "UTF-8";

    // @Value 는 YAML 리스트를 List<String> 으로 받지 못한다. 여러 개면 콤마로 구분한다.
    // test 프로파일은 빈 문자열이다 — 단위 테스트는 자기 픽스처로 살고, 실데이터가 섞이면 깨진다.
    @Value("${spring.datasource." + DOMAIN_NAME + ".init.data-locations}")
    private String dataLocations;

    private final DataSource dataSource;
    private final JdbcTemplate jdbcTemplate;
    private final ResourceLoader resourceLoader;

    public SqlInitConfig(@Qualifier(DATASOURCE_NAME) DataSource dataSource, ResourceLoader resourceLoader) {
        this.dataSource = dataSource;
        this.jdbcTemplate = new JdbcTemplate(dataSource);
        this.resourceLoader = resourceLoader;
    }

    /**
     * 대상 테이블이 비어 있을 때만 적재한다. 이미 행이 있으면 파일을 통째로 건너뛴다.
     * 즉 <b>파일에 행을 더해도 이미 채워진 DB 에는 반영되지 않는다.</b> 운영에 새 마스터
     * 데이터를 넣는 것은 별도 SQL 의 몫이다.
     *
     * <p>그 위에 스크립트가 {@code INSERT IGNORE} 다. character 와 user 두 서비스가 같은 DB 의
     * 공통 코드 테이블을 보기 때문에, 동시에 떠서 둘 다 "비어 있음"을 읽는 경합이 실제로 가능하다.
     * 존재 여부 확인만으로는 그 창을 닫지 못한다.
     *
     * <p>{@code continueOnError} 는 기본값(false) 그대로 둔다. 스크립트가 깨지면 기동을 세워야
     * 한다 — 데이터가 안 들어간 채 조용히 뜨면 첫 요청에서야 드러난다.
     */
    @Override
    public void afterSingletonsInstantiated() {
        final String[] locations = StringUtils.commaDelimitedListToStringArray(dataLocations);

        if (locations.length == 0) {
            log.info("[{}] 초기 데이터 적재 대상 없음", DOMAIN_NAME);
            return;
        }

        for (String rawLocation : locations) {
            final String location = rawLocation.trim();

            if (location.isEmpty()) {
                continue;
            }

            final String table = tableNameOf(location);

            if (rowCount(table) > 0) {
                log.info("[{}] 초기 데이터 건너뜀 (이미 있음): {}", DOMAIN_NAME, table);
                continue;
            }

            final Resource resource = resourceLoader.getResource(location);
            final ResourceDatabasePopulator populator = new ResourceDatabasePopulator(resource);
            // 인코딩을 지정하지 않으면 플랫폼 기본(file.encoding)으로 읽는다. UTF-8 이 기본인 것은
            // Java 18 부터고 이 프로젝트는 17 이다. 시드가 전부 한글이라 컨테이너 로케일에 따라 깨진다.
            populator.setSqlScriptEncoding(SCRIPT_ENCODING);
            DatabasePopulatorUtils.execute(populator, dataSource);

            log.info("[{}] 초기 데이터 적재: {}", DOMAIN_NAME, table);
        }
    }

    /**
     * 파일명이 곧 테이블명이다 (classpath:mongs_food.sql → mongs_food).
     * 이름이 그대로 SQL 에 들어가므로 형식을 확인한다.
     *
     * <p>{@code classpath:} 접두사에는 슬래시가 없다. 디렉터리 구분자와 프로토콜 구분자를
     * 함께 잘라야 한다 — 슬래시만 보면 "classpath:mongs_food" 가 테이블명이 되어 버린다.
     */
    private String tableNameOf(String location) {
        final int nameStart = Math.max(location.lastIndexOf('/'), location.lastIndexOf(':')) + 1;
        final String fileName = location.substring(nameStart);

        if (!fileName.endsWith(SQL_SUFFIX)) {
            throw new IllegalStateException("초기 데이터 경로가 .sql 이 아니다: " + location);
        }

        final String table = fileName.substring(0, fileName.length() - SQL_SUFFIX.length());

        if (!table.matches(TABLE_PATTERN)) {
            throw new IllegalStateException("파일명에서 테이블명을 얻을 수 없다: " + location);
        }

        return table;
    }

    private int rowCount(String table) {
        final Integer count = jdbcTemplate.queryForObject(COUNT_QUERY + table, Integer.class);
        return count == null ? 0 : count;
    }
}
