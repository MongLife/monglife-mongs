package com.monglife.mongs.adapter.in.battle.web.controller;

import com.monglife.core.dto.response.ResponseDto;
import com.monglife.module.common.logging.annotation.EntryLoggingPoint;
import com.monglife.mongs.adapter.in.battle.web.dto.response.GetMatchOutcomeResponseDto;
import com.monglife.mongs.adapter.in.battle.web.dto.response.GetMatchResponseDto;
import com.monglife.mongs.adapter.in.battle.web.dto.response.GetWinMatchPlayerResponseDto;
import com.monglife.mongs.adapter.in.battle.web.enums.AdapterInBattleWebResponse;
import com.monglife.mongs.adapter.in.battle.web.vo.MatchPlayerVo;
import com.monglife.mongs.application.battle.port.in.MatchUseCase;
import com.monglife.mongs.application.battle.port.in.command.GetMatchCommand;
import com.monglife.mongs.application.battle.port.in.command.GetWinMatchPlayerCommand;
import com.monglife.mongs.application.battle.port.in.vo.MatchOutcomeVo;
import com.monglife.mongs.domain.battle.model.Match;
import com.monglife.mongs.domain.battle.model.MatchPlayer;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Validated
@RestController
@RequestMapping("/battle/match")
@RequiredArgsConstructor
public class MatchController {

    private final MatchUseCase matchUseCase;

    /**
     * 매칭 보상 정보 조회
     */
    @EntryLoggingPoint
    @GetMapping("")
    public ResponseEntity<ResponseDto<GetMatchOutcomeResponseDto>> getMatchOutcome() {

        MatchOutcomeVo matchOutcomeVo = matchUseCase.getMatchOutcomeUseCase();

        GetMatchOutcomeResponseDto getMatchOutcomeResponseDto = GetMatchOutcomeResponseDto.builder()
                .rewardPayPoint(matchOutcomeVo.getRewardPayPoint())
                .battingPayPoint(matchOutcomeVo.getBettingPayPoint())
                .build();

        return ResponseEntity.ok(AdapterInBattleWebResponse.GET_MATCH_OUT_COME.toResponseDto(getMatchOutcomeResponseDto));
    }

    /**
     * 매치 조회
     * @param matchId 매치 ID
     */
    @EntryLoggingPoint
    @GetMapping("/{matchId}")
    public ResponseEntity<ResponseDto<GetMatchResponseDto>> getMatch(@PathVariable("matchId") @NotNull @Min(1) Long matchId) {

        GetMatchCommand command = GetMatchCommand.builder()
                .matchId(matchId)
                .build();

        Match match = matchUseCase.getMatchUseCase(command);

        List<MatchPlayerVo> matchPlayers = match.getMatchPlayers().stream()
                .map(matchPlayer -> MatchPlayerVo.builder()
                        .playerId(matchPlayer.getPlayerId())
                        .deviceId(matchPlayer.getDeviceId())
                        .mongCode(matchPlayer.getMongCode())
                        .mongName(matchPlayer.getMongName())
                        .name(matchPlayer.getName())
                        .hp(matchPlayer.getHp())
                        .roundCode(matchPlayer.getRoundCode())
                        .build())
                .toList();

        GetMatchResponseDto getMatchResponseDto = GetMatchResponseDto.builder()
                .matchId(match.getMatchId())
                .round(match.getRound())
                .isLastRound(match.isLastRound())
                .matchPlayers(matchPlayers)
                .build();

        return ResponseEntity.ok(AdapterInBattleWebResponse.GET_MATCH.toResponseDto(getMatchResponseDto));
    }

    /**
     * 승리한 매치 플레이어 조회
     * @param matchId 배틀 ID
     */
    @EntryLoggingPoint
    @GetMapping("/over/{matchId}")
    public ResponseEntity<ResponseDto<GetWinMatchPlayerResponseDto>> getOverBattle(@PathVariable("matchId") @NotNull @Min(1) Long matchId) {

        GetWinMatchPlayerCommand command = GetWinMatchPlayerCommand.builder()
                .matchId(matchId)
                .build();

        MatchPlayer matchPlayer = matchUseCase.getWinMatchPlayerUseCase(command);

        GetWinMatchPlayerResponseDto getWinMatchPlayerResponseDto = GetWinMatchPlayerResponseDto.builder()
                .playerId(matchPlayer.getPlayerId())
                .mongCode(matchPlayer.getMongCode())
                .mongName(matchPlayer.getMongName())
                .name(matchPlayer.getName())
                .build();

        return ResponseEntity.ok(AdapterInBattleWebResponse.GET_WIN_MATCH_PLAYER.toResponseDto(getWinMatchPlayerResponseDto));
    }
}
