package com.soulcode.goserviceapp.repository;
import com.soulcode.goserviceapp.domain.Agendamento;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;


import java.time.LocalDate;
import java.util.Date;
import java.util.List;
import java.util.Optional;

public interface AgendamentoRepository extends JpaRepository<Agendamento, Long> {
    @Query(value = "SELECT * FROM agendamentos a WHERE data BETWEEN ? AND ?", nativeQuery = true)
    List<Agendamento> findByData(String dataInicio, String dataFim);

    @Query(value="SELECT a.* FROM agendamentos a JOIN usuarios u ON a.cliente_id = u.id WHERE u.email = ? ORDER BY data", nativeQuery = true)
    List<Agendamento> findByClienteEmail(String email);

    @Query(value = "SELECT a.* FROM agendamentos a JOIN usuarios u ON a.prestador_id = u.id WHERE u.email = ? ORDER BY data", nativeQuery = true)
    List<Agendamento> findByPrestadorEmail(String email);
//
    @Query(value = "SELECT a.* FROM agendamentos a WHERE a.prestador_id = ? AND a.data = ?", nativeQuery = true)
    List<Agendamento> findByPrestadorAndData(Long prestadorId, LocalDate data);

    @Query(value = "SELECT status_agendamento, COUNT(*) AS quantidade_servicos FROM agendamentos GROUP BY status_agendamento;", nativeQuery = true)
    List<Agendamento> totalAgendamentoStatus();
}