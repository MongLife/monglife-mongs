package com.monglife.mongs.application.device.port.out;

import com.monglife.mongs.application.device.port.out.dto.RestoreWalkingCountDto;

public interface DevicePublishPort {

    void publishRestoreWalkingCountPort(RestoreWalkingCountDto restoreWalkingCountDto);
}
