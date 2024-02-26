package com.soulcode.goserviceapp.service;

import com.soulcode.goserviceapp.domain.AvaliacaoLog;
import com.soulcode.goserviceapp.repository.AvaliacaoLogRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.Date;

@Service
public class AvaliacaoLogService {
    private final AvaliacaoLogRepository avaliacaoLogRepository;

    @Autowired
    public AvaliacaoLogService(AvaliacaoLogRepository avaliacaoLogRepository) {
        this.avaliacaoLogRepository = avaliacaoLogRepository;
    }

    public AvaliacaoLog registroAvaliacao(int nota, String identificacaoPrestador) {
        AvaliacaoLog avaliacaoLog = new AvaliacaoLog();
        avaliacaoLog.setNota(nota);
        avaliacaoLog.setIdentificacaoPrestador(identificacaoPrestador);
        avaliacaoLog.setDataRegistro(new Date());
        return avaliacaoLogRepository.save(avaliacaoLog);
    }
}
