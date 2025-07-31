package org.pqkkkkk.ticsys.identity_service.service.impl;

import org.pqkkkkk.ticsys.identity_service.dto.Request.UsernamePasswordAuthRequest;
import org.pqkkkkk.ticsys.identity_service.entity.User;
import org.pqkkkkk.ticsys.identity_service.exception.UserNotFoundException;
import org.pqkkkkk.ticsys.identity_service.exception.WrongPasswordException;
import org.pqkkkkk.ticsys.identity_service.service.AbstractAuthService;
import org.pqkkkkk.ticsys.identity_service.service.UserService;
import org.pqkkkkk.ticsys.identity_service.utils.JwtUtils;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service("usernamePasswordAuthService")
public class UsernamePasswordAuthServiceImpl extends AbstractAuthService<UsernamePasswordAuthRequest> {
    private final UserService userService;
    private final PasswordEncoder passwordEncoder;

    public UsernamePasswordAuthServiceImpl(UserService userService, PasswordEncoder passwordEncoder, JwtUtils jwtUtils) {
        super(userService, jwtUtils);
        this.userService = userService;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    protected User authenticate(UsernamePasswordAuthRequest request) {
        User user = userService.getUserByUserName(request.username());

        if (user == null) {
            throw new UserNotFoundException("User not found with username: " + request.username());
        }

        if (!passwordEncoder.matches(request.password(), user.getPassWord())) {
            throw new WrongPasswordException("Invalid password");
        }

        return user;
    }

}
