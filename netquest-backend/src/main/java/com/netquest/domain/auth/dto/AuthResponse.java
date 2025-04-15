package com.netquest.domain.auth.dto;

import java.util.UUID;

public record AuthResponse(UUID id, String name, String role) {
}
