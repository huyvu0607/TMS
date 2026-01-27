package com.example.backend.service.impl;

public interface IUserLockService {
    void handleFailedLogin(Long userID);
    void resetFailedAttempts(Long userId);
}
