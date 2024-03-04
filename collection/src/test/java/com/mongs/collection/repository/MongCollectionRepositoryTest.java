//package com.mongs.collection.repository;
//
//import com.mongs.collection.code.TestMapCode;
//import com.mongs.collection.code.TestMongCode;
//import com.mongs.collection.entity.MapCollection;
//import com.mongs.collection.entity.MongCollection;
//import com.mongs.core.code.GroupCode;
//import org.junit.jupiter.api.DisplayName;
//import org.junit.jupiter.api.Test;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
//import org.springframework.test.context.ActiveProfiles;
//
//import java.time.LocalDateTime;
//import java.util.List;
//import java.util.Optional;
//import java.util.concurrent.atomic.AtomicInteger;
//
//import static org.assertj.core.api.Assertions.assertThat;
//
//@DataJpaTest
//@ActiveProfiles("test")
//public class MongCollectionRepositoryTest {
//    @Autowired
//    private MongCollectionRepository mongCollectionRepository;
//
//    @Test
//    @DisplayName("몽 컬렉션 id를 입력하지 않아도 id가 등록된다.")
//    void autoMemberId() {
//        // given
//        Long memberId = 1L;
//        String groupCode = GroupCode.MONG.getGroupCode();
//        String code = TestMongCode.CH000.getCode();
//
//        MongCollection mongCollection = MongCollection.builder()
//                .memberId(memberId)
//                .groupCode(groupCode)
//                .code(code)
//                .build();
//
//        // when
//        MongCollection saveMongCollection = mongCollectionRepository.save(mongCollection);
//
//        // then
//        Long mongCollectionId = saveMongCollection.getId();
//        assertThat(mongCollectionId).isNotNull();
//    }
//
//    @Test
//    @DisplayName("몽 컬렉션 등록 일자가 자동으로 등록된다.")
//    void autoCreatedAt() {
//        // given
//        Long memberId = 1L;
//        String groupCode = GroupCode.MONG.getGroupCode();
//        String code = TestMongCode.CH000.getCode();
//
//        MongCollection mongCollection = MongCollection.builder()
//                .memberId(memberId)
//                .groupCode(groupCode)
//                .code(code)
//                .build();
//
//        LocalDateTime expected = LocalDateTime.now().plusSeconds(1);
//
//        // when
//        MongCollection saveMongCollection = mongCollectionRepository.save(mongCollection);
//
//        // then
//        LocalDateTime createdAt = saveMongCollection.getCreatedAt();
//        assertThat(createdAt).isNotNull();
//        assertThat(createdAt).isBeforeOrEqualTo(expected);
//    }
//
//    @Test
//    @DisplayName("회원 id 를 기준으로 조회하면 회원의 몽 컬렉션 목록을 반환한다.")
//    void findByMemberId() {
//        // given
//        Long memberId = 1L;
//        String groupCode = GroupCode.MONG.getGroupCode();
//
//        for (long codeNumber = 0; codeNumber < 10; codeNumber++) {
//            mongCollectionRepository.save(MongCollection.builder()
//                    .memberId(memberId)
//                    .groupCode(groupCode)
//                    .code(String.format("CH%03d", codeNumber))
//                    .build());
//        }
//
//        // when
//        List<MongCollection> mapCollectionList = mongCollectionRepository.findByMemberId(memberId);
//
//        // then
//        assertThat(mapCollectionList).isNotEmpty();
//
//        AtomicInteger codeNumber = new AtomicInteger();
//        mapCollectionList.forEach(mapCollection -> {
//            assertThat(mapCollection).isNotNull();
//            assertThat(mapCollection.getMemberId()).isEqualTo(memberId);
//            assertThat(mapCollection.getGroupCode()).isEqualTo(groupCode);
//            assertThat(mapCollection.getCode()).isEqualTo(String.format("CH%03d", codeNumber.getAndIncrement()));
//        });
//    }
//
//    @Test
//    @DisplayName("회원 id, 몽 코드를 기준으로 몽 컬렉션을 삭제한다.")
//    void deleteByMemberIdAndMongCode() {
//        // given
//        Long memberId = 1L;
//        String groupCode = GroupCode.MONG.getGroupCode();
//        String code = TestMongCode.CH000.getCode();
//
//        MongCollection mongCollection = MongCollection.builder()
//                .memberId(memberId)
//                .groupCode(groupCode)
//                .code(code)
//                .build();
//        MongCollection saveMongCollection = mongCollectionRepository.save(mongCollection);
//
//        // when
//        mongCollectionRepository.deleteByMemberIdAndCode(memberId, code);
//
//        Optional<MongCollection> expected  = mongCollectionRepository.findById(saveMongCollection.getId());
//
//        // then
//        assertThat(expected.isPresent()).isFalse();
//    }
//}
