package com.soulcode.goserviceapp.repository;

import com.soulcode.goserviceapp.controller.AdministradorController;
import com.soulcode.goserviceapp.domain.Administrador;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface AdminRepository extends JpaRepository<Administrador, Long> {
    Optional<Administrador> findByEmail(String email);
    @Modifying
    @Query(value = "UPDATE usuarios u SET u.nome = ?, u.email = ? WHERE u.id = ?", nativeQuery = true)
    void updateNomeEmail(String nome, String email, Long id);
}


