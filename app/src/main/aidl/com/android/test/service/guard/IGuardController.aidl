package com.android.test.service.guard;

interface IGuardController {
    void pauseGuard();
    void continueGuard();
    boolean isGuarding();
}