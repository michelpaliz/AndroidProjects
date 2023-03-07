package com.example.myProyect.repo;

import com.example.myProyect.models.Usuario;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
public interface UsuarioRepositorio extends CrudRepository<Usuario, Integer> {

    @Transactional
    @Modifying
    @Query(
            value = "insert into usuario(nombre,apellido,email,password) values (:nombre,:apellido, :email, :password)",
            nativeQuery = true)
    int registrarUsuario(@Param("nombre") String nombre,
                         @Param("nombre") String apellido,
                         @Param("email") String email,
                         @Param("password") String password
    );

}
