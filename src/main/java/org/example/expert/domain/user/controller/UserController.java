package org.example.expert.domain.user.controller;

import lombok.RequiredArgsConstructor;
import org.example.expert.domain.common.annotation.Auth;
import org.example.expert.domain.common.dto.AuthUser;
import org.example.expert.domain.user.dto.request.UserChangePasswordRequest;
import org.example.expert.domain.user.dto.response.UserResponse;
import org.example.expert.domain.user.service.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @GetMapping("/users/{userId}")
    public ResponseEntity<UserResponse> getUser(@PathVariable long userId) {
        return ResponseEntity.ok(userService.getUser(userId));
    }

    @GetMapping("/users/search/{nickname}")
    public ResponseEntity<UserResponse> getUserWithNickname(@PathVariable String nickname) {
        return ResponseEntity.ok(userService.getUserWithNickname(nickname));
    }

    @PutMapping("/users")
    public void changePassword(@AuthenticationPrincipal AuthUser authUser, @RequestBody UserChangePasswordRequest userChangePasswordRequest) {
        userService.changePassword(authUser.getId(), userChangePasswordRequest);
    }

    @PutMapping("/users/profile")
    public void updateProfile(
            @AuthenticationPrincipal AuthUser authUser,
            @RequestPart(value = "photo") MultipartFile photo
            ){
        userService.updateProfile(authUser.getId(), photo);
    }

    @DeleteMapping("/users/profile")
    public void deleteProfile(@AuthenticationPrincipal AuthUser authUser){
        userService.deleteProfile(authUser.getId());
    }
}
