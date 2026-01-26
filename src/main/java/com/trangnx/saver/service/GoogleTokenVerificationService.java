package com.trangnx.saver.service;

import com.google.api.client.googleapis.auth.oauth2.GoogleIdToken;
import com.google.api.client.googleapis.auth.oauth2.GoogleIdTokenVerifier;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.gson.GsonFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.security.GeneralSecurityException;
import java.util.Collections;

@Service
@RequiredArgsConstructor
public class GoogleTokenVerificationService {

    @Value("${google.client-id}")
    private String googleClientId;

    /**
     * Verify Google ID token and extract payload
     */
    public GoogleIdToken.Payload verifyIdToken(String idTokenString)
            throws GeneralSecurityException, IOException {

        GoogleIdTokenVerifier verifier = new GoogleIdTokenVerifier.Builder(
                new NetHttpTransport(),
                new GsonFactory()
        )
                .setAudience(Collections.singletonList(googleClientId))
                .build();

        GoogleIdToken idToken = verifier.verify(idTokenString);

        if (idToken == null) {
            throw new IllegalArgumentException("Invalid ID token");
        }

        return idToken.getPayload();
    }

    /**
     * Extract user info from Google ID token payload
     */
    public GoogleUserInfo extractUserInfo(GoogleIdToken.Payload payload) {
        String email = payload.getEmail();
        boolean emailVerified = payload.getEmailVerified();
        String name = (String) payload.get("name");
        String pictureUrl = (String) payload.get("picture");
        String googleId = payload.getSubject();

        if (!emailVerified) {
            throw new IllegalArgumentException("Email not verified");
        }

        return GoogleUserInfo.builder()
                .email(email)
                .name(name)
                .pictureUrl(pictureUrl)
                .googleId(googleId)
                .build();
    }

    @lombok.Data
    @lombok.Builder
    public static class GoogleUserInfo {
        private String email;
        private String name;
        private String pictureUrl;
        private String googleId;
    }
}