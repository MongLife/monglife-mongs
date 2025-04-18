package com.monglife.mongs.adapter.out.member.persistence.config;

import com.querydsl.jpa.impl.JPAQueryFactory;
import jakarta.persistence.EntityManager;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class MemberQueryDSLConfig {

    @Bean(name = "memberJpaQueryFactory")
    public JPAQueryFactory jpaQueryFactory(@Qualifier("memberEntityManager") EntityManager entityManager){
        return new JPAQueryFactory(entityManager);
    }
}