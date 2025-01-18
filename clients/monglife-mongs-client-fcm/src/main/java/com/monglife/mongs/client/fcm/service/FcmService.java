package com.monglife.mongs.client.fcm.service;

import com.google.firebase.messaging.FirebaseMessaging;
import com.google.firebase.messaging.FirebaseMessagingException;
import com.google.firebase.messaging.MulticastMessage;
import com.google.firebase.messaging.Notification;
import com.monglife.mongs.client.fcm.client.AuthClient;
import com.monglife.mongs.client.fcm.exception.InvalidGetDevicesException;
import com.monglife.mongs.client.fcm.vo.DeviceVo;
import feign.RetryableException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class FcmService {

    private final AuthClient authClient;

    private final FirebaseMessaging firebaseMessaging;

    /**
     * FCM 전송
     * @param accountId 계정 ID
     * @param title 제목
     * @param body 본문
     */
    public void sendPush(Long accountId, String title, String body) {

        try {
            List<DeviceVo> deviceVos = this.getDevices(accountId);

            List<String> tokens = deviceVos.stream()
                    .map(DeviceVo::getFcmToken)
                    .toList();

            if (!tokens.isEmpty()) {

                Notification notification = Notification.builder()
                        .setTitle(title)
                        .setBody(body)
                        .build();

                firebaseMessaging.sendEachForMulticast(MulticastMessage.builder()
                        .setNotification(notification)
                        .addAllTokens(tokens)
                        .build());
            }

        } catch (FirebaseMessagingException e) {
            log.error("[FCM] {}", e.getMessage());
        }
    }

    /**
     * 계정 ID 기준 연결된 기기 목록 조회
     * @param accountId 계정 ID
     * @return 기기 목록 Vo
     */
    private List<DeviceVo> getDevices(Long accountId) {

        List<DeviceVo> deviceVos = Collections.emptyList();

        try {
            var response = authClient.getDevices(accountId);

            if (response.getStatusCode().is2xxSuccessful() && response.getBody() != null) {
                deviceVos = response.getBody().getResult().stream()
                        .map(getDeviceResponseDto -> DeviceVo.builder()
                                .deviceId(getDeviceResponseDto.getDeviceId())
                                .deviceName(getDeviceResponseDto.getDeviceName())
                                .fcmToken(getDeviceResponseDto.getFcmToken())
                                .build())
                        .toList();
            }
        } catch (RetryableException e) {
            throw new InvalidGetDevicesException();
        }

        return deviceVos;
    }
}
