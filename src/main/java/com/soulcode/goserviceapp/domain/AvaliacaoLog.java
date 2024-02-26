package com.soulcode.goserviceapp.domain;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import java.util.Date;

@Document(collection = "avaliacao_logs")
public class AvaliacaoLog {

    @Id
    private String id;
    private int nota;
    private String identificacaoPrestador;
    private Date dataRegistro;

    public AvaliacaoLog() {

    }

    public AvaliacaoLog(String id, int nota, String identificacaoPrestador, Date dataRegistro) {
        this.id = id;
        this.nota = nota;
        this.identificacaoPrestador = identificacaoPrestador;
        this.dataRegistro = dataRegistro;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public int getNota() {
        return nota;
    }

    public void setNota(int nota) {
        if (nota >= 1 && nota <= 5){
            this.nota = nota;
        }else {
            throw new IllegalArgumentException("A nota da avaliação deve ser entre 1 e 5.");
        }

    }

    public String getIdentificacaoPrestador() {
        return identificacaoPrestador;
    }

    public void setIdentificacaoPrestador(String identificacaoPrestador) {
        this.identificacaoPrestador = identificacaoPrestador;
    }

    public Date getDataRegistro() {
        return dataRegistro;
    }

    public void setDataRegistro(Date dataRegistro) {
        this.dataRegistro = dataRegistro;
    }
}


