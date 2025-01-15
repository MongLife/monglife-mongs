package com.monglife.mongs.client.fcm.service;

import com.google.firebase.messaging.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class FcmService {

    private final FirebaseMessaging firebaseMessaging;

    public void sendPush(String token, String title, String body) {

        try {
            Notification notification = Notification.builder()
                    .setTitle(title)
                    .setBody(body)
                    .build();

            firebaseMessaging.send(Message.builder()
                    .setNotification(notification)
                    .setToken(token)
                    .build());

        } catch (FirebaseMessagingException e) {
            log.error("[FCM] {}", e.getMessage());
        }
    }

    public void sendPush(List<String> tokens, String title, String body) {

        try {
            Notification notification = Notification.builder()
                    .setTitle(title)
                    .setBody(body)
                    .build();

            firebaseMessaging.sendEachForMulticast(MulticastMessage.builder()
                    .setNotification(notification)
                    .addAllTokens(tokens)
                    .build());

        } catch (FirebaseMessagingException e) {
            log.error("[FCM] {}", e.getMessage());
        }
    }
}
