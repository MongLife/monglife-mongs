package com.monglife.mongs.app.management.service.module;

import com.monglife.mongs.app.management.repository.FoodCodeRepository;
import com.monglife.mongs.app.management.repository.MapCodeRepository;
import com.monglife.mongs.app.management.repository.MongCodeRepository;
import com.monglife.mongs.app.management.repository.MongRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class MongModuleService {

    private final MongRepository mongRepository;

    private final MongCodeRepository mongCodeRepository;

    private final MapCodeRepository mapCodeRepository;

    private final FoodCodeRepository foodCodeRepository;


}
