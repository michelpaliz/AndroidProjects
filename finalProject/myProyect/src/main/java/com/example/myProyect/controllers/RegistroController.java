package com.example.myProyect.controllers;

import com.example.myProyect.service.UsuarioServicio;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCrypt;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;



@RestController
@RequestMapping("/api/v1")
public class RegistroController {

    @Autowired
    UsuarioServicio usuarioServicio;

    @PostMapping("/usuario/registrar")
    public ResponseEntity registrarNuevoUsuario(@RequestParam("nombre") String nombre,
                                                @RequestParam("apellido") String apellido,
                                                @RequestParam("email") String email,
                                                @RequestParam("password") String password) {

        if (nombre.isEmpty() || apellido.isEmpty() || email.isEmpty() || password.isEmpty()){
            return new ResponseEntity<>("Por favor rellena los campos", HttpStatus.BAD_REQUEST);
        }
        //no espera un json object
        //Encrypt or Hash password
        String hashed_password = BCrypt.hashpw(password,BCrypt.gensalt());
        //Registrar usuario
        int result = usuarioServicio.registrarNuevoUsuario(nombre,apellido, email,hashed_password);

        if (result != 1 ){
            return new ResponseEntity<>("failed", HttpStatus.BAD_REQUEST);
        }
        return new ResponseEntity<>("success", HttpStatus.OK);
    }


}
