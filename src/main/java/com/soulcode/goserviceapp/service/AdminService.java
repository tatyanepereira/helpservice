package com.soulcode.goserviceapp.service;

import com.soulcode.goserviceapp.domain.Administrador;
import com.soulcode.goserviceapp.repository.AdminRepository;
import com.soulcode.goserviceapp.service.exceptions.UsuarioNaoAutenticadoException;
import com.soulcode.goserviceapp.service.exceptions.UsuarioNaoEncontradoException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class AdminService {

    @Autowired
    private AdminRepository adminRepository;

    public Administrador findAuthenticated(Authentication authentication){
        if (authentication != null && authentication.isAuthenticated()){
            Optional<Administrador> administrador = adminRepository.findByEmail(authentication.getName());
            if(administrador.isPresent()){
                return administrador.get();
            } else {
                throw new UsuarioNaoEncontradoException();
            }
        } else {
            throw new UsuarioNaoAutenticadoException();
        }
    }

    public Administrador findById(Long id){
        Optional<Administrador> administrador = adminRepository.findById(id);
        if(administrador.isPresent()){
            return administrador.get();
        } else {
            throw  new UsuarioNaoEncontradoException();
        }
    }

    public Administrador update(Administrador administrador) {
        Administrador updatedAdministrador = this.findById(administrador.getId());
        updatedAdministrador.setNome(administrador.getNome());
        updatedAdministrador.setEmail(administrador.getEmail());
        updatedAdministrador.setEndereco(administrador.getEndereco());
        return adminRepository.save(updatedAdministrador);
    }
}
