package com.caiomoreno.cursomc.resources;

import com.caiomoreno.cursomc.security.JwtUtil;
import com.caiomoreno.cursomc.security.UserSS;
import com.caiomoreno.cursomc.services.UserService;
import com.caiomoreno.cursomc.services.exceptions.AuthorizationException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletResponse;

@RestController
@RequestMapping("/auth")
public class AuthResource {

    @Autowired
    private JwtUtil jwtUtil;

    @RequestMapping(value="/refresh_token", method= RequestMethod.POST)
    public ResponseEntity<Void> refreshToken(HttpServletResponse response) {
        UserSS user = UserService.authenticated();

        if(user == null){
            throw new AuthorizationException("Acesso negado!");
        }

        String token = jwtUtil.generateToken(user.getUsername());
        response.addHeader("Authorization", "Bearer " + token);
        return ResponseEntity.noContent().build();
    }

}
