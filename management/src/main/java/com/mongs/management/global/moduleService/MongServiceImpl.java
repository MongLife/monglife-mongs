package com.mongs.management.global.moduleService;

import com.mongs.management.global.entity.Mong;
import com.mongs.management.global.repository.MongRepository;
import com.mongs.management.exception.ManagementErrorCode;
import com.mongs.management.exception.ManagementException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class MongServiceImpl implements MongService {
    private final MongRepository mongRepository;

    @Override
    public Mong getMong(Long mongId, Long accountId) {
        return mongRepository.findByIdAndAccountIdAndIsActiveTrue(mongId, accountId)
                .orElseThrow(() -> new ManagementException(ManagementErrorCode.NOT_FOUND_MONG));
    }

    @Override
    public List<Mong> getAllMong(Long accountId) {
        return mongRepository.findByAccountIdAndIsActiveTrue(accountId);
    }

    @Override
    public Mong saveMong(Mong mong) {
        return mongRepository.save(mong);
    }
}
