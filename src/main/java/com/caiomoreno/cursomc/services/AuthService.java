package com.caiomoreno.cursomc.services;

import com.caiomoreno.cursomc.domain.Cliente;
import com.caiomoreno.cursomc.repositories.ClienteRepository;
import javassist.tools.rmi.ObjectNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Random;

@Service
public class AuthService {

    @Autowired
    private ClienteRepository clienteRepository;

    @Autowired
    private BCryptPasswordEncoder crypt;

    @Autowired
    private EmailService emailService;

    private Random random = new Random();

    public void sendNewPassword(String email) throws ObjectNotFoundException {

            Cliente cliente = clienteRepository.findByEmail(email);
            if (cliente == null) {
                throw new ObjectNotFoundException("Email não encontrado!");
            }

            String newPass = newPassword();
            cliente.setSenha(crypt.encode(newPass));

            clienteRepository.save(cliente);

            emailService.sendNewPasswordEmail(cliente, newPass);

    }

    private String newPassword() {
        char[] vet = new char[10];

        for (int i = 0; i < 10; i++) {
            vet[i] = ramdomChar();
        }
        return new String(vet);
    }

    private char ramdomChar() {
        int opt = random.nextInt(3);
        if (opt == 0) {//gera numero
            return (char) (random.nextInt(10) + 48);

        } else if (opt == 1) {// gera letra maiuscula
            return (char) (random.nextInt(26) + 65);
        } else {// gera letra minuscula
            return (char) (random.nextInt(26) + 97);
        }
    }
}
