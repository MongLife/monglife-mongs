package com.monglife.mongs.app.activity.battle.dto.request;

import com.monglife.mongs.app.activity.battle.vo.CreateBattleVo;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Set;

@Getter
@Setter
@NoArgsConstructor
public class CreateBattleRequestDto {

    private Set<CreateBattleVo> createBattleVoSet;

    @Builder
    public CreateBattleRequestDto(Set<CreateBattleVo> createBattleVoSet) {
        this.createBattleVoSet = createBattleVoSet;
    }
}
