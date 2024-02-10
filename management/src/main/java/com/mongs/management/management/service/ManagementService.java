package com.mongs.management.management.service;

import com.mongs.management.management.repository.ManagementRepository;
import com.mongs.management.management.service.dto.SlotListResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class ManagementService {

    private ManagementRepository managementRepository;

//    public SlotListResponse SlotList () {
//
//        return SlotListResponse.builder()
//                .stateCode()
//                .isDie()
//                .build();
//
//    }
}
