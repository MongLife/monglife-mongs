package com.monglife.mongs.app.common.business.service;

import com.monglife.mongs.app.common.business.vo.FindCodeVersionVo;
import com.monglife.mongs.app.common.business.vo.FindCodeVo;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class CommonService {

    @Transactional(readOnly = true)
    public FindCodeVersionVo findCodeVersion(String buildVersion, String codeIntegrity) {
        return FindCodeVersionVo.builder().build();
    }

    @Transactional(readOnly = true)
    public FindCodeVo findCode(String buildVersion) {
        return FindCodeVo.builder().build();
    }
}
