package com.sdcompany.tadak.login.service;

import com.sdcompany.tadak.config.security.TokenProvider;
import com.sdcompany.tadak.entity.Users;
import com.sdcompany.tadak.login.dto.LoginRequest;
import com.sdcompany.tadak.login.dto.LoginResult;
import com.sdcompany.tadak.message.repository.UsersRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
public class LoginService {

    private final UsersRepository usersRepository;
    private final PasswordEncoder passwordEncoder;
    private final TokenProvider tokenProvider;

    @Transactional
    public LoginResult login(LoginRequest request) {
        Users user = findUserByLoginId(request.loginId());
        validPassword(request.password(), user.getPassword());
        String accessToken = createAccessToken(user.getId(), user.getLoginId());
        return new LoginResult(accessToken);
    }

    private String createAccessToken(Long id, String loginId) {
        return tokenProvider.createToken(id, loginId);
    }

    private void validPassword(String rawPassword, String encodedPassword) {
        if (!passwordEncoder.matches(rawPassword, encodedPassword)) {
            throw new IllegalArgumentException("Invalid password");
        }
    }

    private Users findUserByLoginId(String loginId) {
        return usersRepository.findByLoginId(loginId)
                .orElseThrow(() -> new IllegalArgumentException("Invalid loginId: " + loginId));
    }

}
