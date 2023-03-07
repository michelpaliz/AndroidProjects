package com.example.myProyect.service;

import com.example.myProyect.repo.UsuarioRepositorio;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UsuarioServicio {

    @Autowired
    UsuarioRepositorio usuarioRepositorio;


    public int registrarNuevoUsuario(String nombre, String apellido, String email, String password){
        return usuarioRepositorio.registrarUsuario(nombre,apellido, email,password);
    }

}
