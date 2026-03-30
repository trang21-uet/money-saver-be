package com.trangnx.saver.service;

import com.google.api.client.googleapis.auth.oauth2.GoogleIdToken;
import com.google.api.client.googleapis.auth.oauth2.GoogleIdTokenVerifier;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.gson.GsonFactory;
import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.security.GeneralSecurityException;
import java.util.Collections;

@Service
public class GoogleTokenVerificationService {

    @Value("${google.client-id}")
    private String googleClientId;

    public GoogleIdToken.Payload verifyIdToken(String idToken) throws GeneralSecurityException, IOException {
        GoogleIdTokenVerifier verifier = new GoogleIdTokenVerifier.Builder(
                new NetHttpTransport(), GsonFactory.getDefaultInstance())
                .setAudience(Collections.singletonList(googleClientId))
                .build();
        GoogleIdToken token = verifier.verify(idToken);
        if (token == null) throw new GeneralSecurityException("Invalid ID token");
        return token.getPayload();
    }

    public GoogleUserInfo extractUserInfo(GoogleIdToken.Payload payload) {
        GoogleUserInfo info = new GoogleUserInfo();
        info.setGoogleId(payload.getSubject());
        info.setEmail(payload.getEmail());
        info.setFullName((String) payload.get("name"));
        info.setAvatarUrl((String) payload.get("picture"));
        return info;
    }

    @Data
    public static class GoogleUserInfo {
        private String googleId;
        private String email;
        private String fullName;
        private String avatarUrl;
    }
}
