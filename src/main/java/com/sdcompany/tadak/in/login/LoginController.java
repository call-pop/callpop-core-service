package com.sdcompany.tadak.in.login;

import com.sdcompany.tadak.login.dto.LoginRequest;
import com.sdcompany.tadak.login.service.LoginService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "Login", description = "로그인 API")
@RequestMapping("/api/login")
@RestController
@RequiredArgsConstructor
public class LoginController {

    private final LoginService loginService;

    @Operation(
            summary = "로그인",
            description = "사용자 로그인"
    )
    @PostMapping
    public void login(@Valid @RequestBody LoginRequest request) {
        loginService.login(request);
    }
}
