package com.monglife.mongs.app.management.service.business;

import com.monglife.mongs.app.management.vo.FeedLogVo;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;


@Service
@RequiredArgsConstructor
public class InteractionService {

    private final Random random = new Random();

    @Transactional
    public void evolutionMongReady(Long mongId) {

    }

    @Transactional
    public void evolutionMong(Long accountId, Long mongId) {

    }


    //    @ValidationEvolution
    @Transactional
    public void strokeMong(Long accountId, Long mongId) {

    }

    @Transactional
    public void sleepingMong(Long accountId, Long mongId) {

    }

//    @ValidationEvolution
    @Transactional
    public void poopClean(Long accountId, Long mongId) {

    }

//    @ValidationEvolution
//    @ValidationDead
    @Transactional
    public void trainingMong(Long accountId, Long mongId, String trainingCode, Integer score) {

    }

    @Transactional
    public void graduateMong(Long accountId, Long mongId) {

    }
//    @ValidationEvolution
//    @ValidationDead
    @Transactional
    public void feedMong(Long accountId, Long mongId, String foodCode) {

    }

    @Transactional
    public List<FeedLogVo> getFeedLog(Long accountId, Long mongId) {
        return new ArrayList<>();
    }
}
