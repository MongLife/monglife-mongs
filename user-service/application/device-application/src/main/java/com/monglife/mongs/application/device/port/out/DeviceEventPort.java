package com.monglife.mongs.application.device.port.out;

import com.monglife.mongs.application.device.port.out.dto.ExchangeCurrentWalkingCountDto;

public interface DeviceEventPort {

    void exchangeCurrentWalkingCountEventPort(ExchangeCurrentWalkingCountDto exchangeCurrentWalkingCountDto);
}
