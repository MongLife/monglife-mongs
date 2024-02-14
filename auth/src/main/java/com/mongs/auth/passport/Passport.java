package com.mongs.auth.passport;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Builder;

@Builder(toBuilder = true)
public record Passport(
        PassportMember member,
        @JsonIgnore
        String passportIntegrity
) {
}