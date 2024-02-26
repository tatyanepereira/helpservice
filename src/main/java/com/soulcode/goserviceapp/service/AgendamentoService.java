package com.soulcode.goserviceapp.service;

import com.soulcode.goserviceapp.domain.*;
import com.soulcode.goserviceapp.domain.enums.StatusAgendamento;
import com.soulcode.goserviceapp.repository.AgendamentoRepository;
import com.soulcode.goserviceapp.service.exceptions.AgendamentoNaoEncontradoException;
import com.soulcode.goserviceapp.service.exceptions.StatusAgendamentoImutavelException;
import com.soulcode.goserviceapp.service.exceptions.ConflitoHorarioException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;


import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
public class AgendamentoService {
    @Autowired
    private AgendamentoRepository agendamentoRepository;

    @Autowired
    private  ServicoService servicoService;

    @Autowired
    private ClienteService clienteService;

    @Autowired
    private PrestadorService prestadorService;

    @Autowired
    private AgendamentoLogService agendamentoLogService;

    public List<Agendamento> totalAgendamentoStatus(){
        return agendamentoRepository.totalAgendamentoStatus();
    }

    public List<Agendamento> findByData(String dataInicio, String dataFim) {
        return agendamentoRepository.findByData(dataInicio, dataFim);
    }

    public Agendamento findById(Long id){
        Optional<Agendamento> agendamento = agendamentoRepository.findById(id);
        if(agendamento.isPresent()) {
            return agendamento.get();
        }
        throw new AgendamentoNaoEncontradoException();
    }

    public Agendamento create(Authentication authentication, Long servicoId, Long prestadorId, LocalDate data, LocalTime hora){
        Cliente cliente = clienteService.findAuthenticated(authentication);
        Prestador prestador = prestadorService.findById(prestadorId);
        Servico servico = servicoService.findById(servicoId);
// horario
        if (isHorarioDisponivel(prestador, data, hora)) {
            Agendamento agendamento = new Agendamento();
            agendamento.setCliente(cliente);
            agendamento.setPrestador(prestador);
            agendamento.setServico(servico);
            agendamento.setData(data);
            agendamento.setHora(hora);
            return agendamentoRepository.save(agendamento);
        } else {
            throw new ConflitoHorarioException("Indisponível: O prestador já possui um agendamento nesse horário.");
        }
    }

    public boolean isHorarioDisponivel(Prestador prestador, LocalDate data, LocalTime hora) {
        Long prestadorId = prestador.getId();
        List<Agendamento> agendamentos = agendamentoRepository.findByPrestadorAndData(prestadorId, data);

        for(Agendamento agendamento : agendamentos) {
            if(agendamento.getHora().equals(hora)) {
                return false;
            }
        }
        return true;
    }


    @Cacheable(cacheNames = "redisCache")
    public List<Agendamento> findByCliente(Authentication authentication){
        System.err.println("BUSCANDO AGENDAMENTOS CLIENTE NO BANCO...");
        Cliente cliente = clienteService.findAuthenticated(authentication);
        return agendamentoRepository.findByClienteEmail(cliente.getEmail());
    }

    @Cacheable(cacheNames = "redisCache")
    public List<Agendamento> findByPrestador(Authentication authentication){
        System.err.println("BUSCANDO AGENDAMENTOS PRESTADOR NO BANCO...");
        Prestador prestador = prestadorService.findAuthenticated(authentication);
        return  agendamentoRepository.findByPrestadorEmail(prestador.getEmail());
    }

    public void agendamentoLog(Agendamento agendamento) {
        try {
            AgendamentoLog agendamentoLog = new AgendamentoLog(agendamento);
            agendamentoLogService.create(agendamentoLog);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public void cancelAgendaPrestador(Authentication authentication, Long id){
        Prestador prestador = prestadorService.findAuthenticated(authentication);
        Agendamento agendamento = findById(id);
        if(agendamento.getStatusAgendamento().equals(StatusAgendamento.AGUARDANDO_CONFIRMACAO)){
            agendamento.setStatusAgendamento(StatusAgendamento.CANCELADO_PELO_PRESTADOR);
            Agendamento agendamento1 = agendamentoRepository.save(agendamento);
            agendamentoLog(agendamento1);
            return;
        }
        throw new StatusAgendamentoImutavelException();
    }

    public void confirmAgenda(Authentication authentication, Long id){
        Prestador prestador = prestadorService.findAuthenticated(authentication);
        Agendamento agendamento = findById(id);
        if(agendamento.getStatusAgendamento().equals(StatusAgendamento.AGUARDANDO_CONFIRMACAO)){
            agendamento.setStatusAgendamento(StatusAgendamento.CONFIRMADO);
            Agendamento agendamento1 = agendamentoRepository.save(agendamento);
            agendamentoLog(agendamento1);
            return;
        }
        throw new StatusAgendamentoImutavelException();
    }

    public void cancelAgendaCliente(Authentication authentication, Long id){
        Cliente cliente = clienteService.findAuthenticated(authentication);
        Agendamento agendamento = findById(id);
        if (agendamento.getStatusAgendamento().equals(StatusAgendamento.AGUARDANDO_CONFIRMACAO)){
            agendamento.setStatusAgendamento(StatusAgendamento.CANCELADO_PELO_CLIENTE);
            Agendamento agendamento1 = agendamentoRepository.save(agendamento);
            agendamentoLog(agendamento1);
            return;
        }
        throw new StatusAgendamentoImutavelException();
    }

    public void completeAgenda(Authentication authentication, Long id){
        Cliente cliente = clienteService.findAuthenticated(authentication);
        Agendamento agendamento = findById(id);
        if (agendamento.getStatusAgendamento().equals(StatusAgendamento.CONFIRMADO)){
            agendamento.setStatusAgendamento(StatusAgendamento.CONCLUIDO);
            Agendamento agendamento1 = agendamentoRepository.save(agendamento);
            agendamentoLog(agendamento1);
            return;
        }
        throw new StatusAgendamentoImutavelException();
    }



}