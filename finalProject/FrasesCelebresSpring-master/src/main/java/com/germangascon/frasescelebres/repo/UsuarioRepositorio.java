package com.germangascon.frasescelebres.repo;

import com.germangascon.frasescelebres.models.Usuario;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UsuarioRepositorio extends CrudRepository<Usuario, Integer> {


}
