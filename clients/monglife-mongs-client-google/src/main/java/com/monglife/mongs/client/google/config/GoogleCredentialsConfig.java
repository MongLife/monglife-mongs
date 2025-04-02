package com.monglife.mongs.client.google.config;

import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
import com.google.api.client.json.gson.GsonFactory;
import com.google.api.services.androidpublisher.AndroidPublisher;
import com.google.api.services.androidpublisher.AndroidPublisherScopes;
import com.google.auth.http.HttpCredentialsAdapter;
import com.google.auth.oauth2.GoogleCredentials;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;

import java.io.IOException;
import java.io.InputStream;
import java.security.GeneralSecurityException;

@Configuration
public class GoogleCredentialsConfig {

    @Value("${google.account.filepath}")
    private String GOOGLE_ACCOUNT_FILE_PATH;

    @Value("${google.package-name}")
    private String APP_PACKAGE_NAME;

    @Bean
    public AndroidPublisher androidPublisher() throws IOException, GeneralSecurityException {

        InputStream inputStream = new ClassPathResource(GOOGLE_ACCOUNT_FILE_PATH).getInputStream();
        GoogleCredentials credentials = GoogleCredentials.fromStream(inputStream).createScoped(AndroidPublisherScopes.ANDROIDPUBLISHER);

        return new AndroidPublisher.Builder(
                GoogleNetHttpTransport.newTrustedTransport(),
                GsonFactory.getDefaultInstance(),
                new HttpCredentialsAdapter(credentials)
        ).setApplicationName(APP_PACKAGE_NAME).build();
    }
}