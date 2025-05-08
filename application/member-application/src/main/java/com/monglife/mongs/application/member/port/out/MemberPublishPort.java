package com.monglife.mongs.application.member.port.out;

import com.monglife.mongs.domain.model.Player;

public interface MemberPublishPort {

    void publishStarPointPort(Player player);

    void publishSlotCountPort(Player player);
}
