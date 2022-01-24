package com.caiomoreno.cursomc.resources;

import com.caiomoreno.cursomc.dto.EmailDTO;
import com.caiomoreno.cursomc.security.JwtUtil;
import com.caiomoreno.cursomc.security.UserSS;
import com.caiomoreno.cursomc.services.AuthService;
import com.caiomoreno.cursomc.services.UserService;
import com.caiomoreno.cursomc.services.exceptions.AuthorizationException;
import javassist.tools.rmi.ObjectNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

@RestController
@RequestMapping("/auth")
public class AuthResource {

    @Autowired
    private JwtUtil jwtUtil;

    @Autowired
    private AuthService authService;

    @RequestMapping(value = "/refresh_token", method = RequestMethod.POST)
    public ResponseEntity<Void> refreshToken(HttpServletResponse response) {
        UserSS user = UserService.authenticated();

        if (user == null) {
            throw new AuthorizationException("Acesso negado!");
        }

        String token = jwtUtil.generateToken(user.getUsername());
        response.addHeader("Authorization", "Bearer " + token);
        response.addHeader("access-control-expose-headers", "Authorization");
        return ResponseEntity.noContent().build();
    }

    @RequestMapping(value = "/forgot", method = RequestMethod.POST)
    public ResponseEntity<Void> forgot(@Valid @RequestBody EmailDTO objDto) throws ObjectNotFoundException {
        authService.sendNewPassword(objDto.getEmail());
        return ResponseEntity.noContent().build();
    }

}
