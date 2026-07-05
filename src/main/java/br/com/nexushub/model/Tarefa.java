package br.com.nexushub.model;

import java.time.LocalDate;

public class Tarefa {

    private int idTarefa;
    private String descricao;
    private final LocalDate dataCriacao;
    private boolean concluida;
    private String prioridade;
    private final int idUsuario;
    private final int idCategoria;

    public Tarefa(String descricao, LocalDate dataCriacao, String prioridade, int idUsuario, int idCategoria) {
        this.descricao = descricao;
        this.dataCriacao = dataCriacao;
        this.prioridade = prioridade;
        this.concluida = false;
        this.idUsuario = idUsuario;
        this.idCategoria = idCategoria;
    }

    public int getIdTarefa() {
        return idTarefa;
    }

    public void setIdTarefa(int idTarefa) {
        this.idTarefa = idTarefa;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public LocalDate getDataCriacao() {
        return dataCriacao;
    }

    public boolean isConcluida() {
        return concluida;
    }

    public void setConcluida(boolean concluida) {
        this.concluida = concluida;
    }

    public String getPrioridade() {
        return prioridade;
    }

    public void setPrioridade(String prioridade) {
        this.prioridade = prioridade;
    }

    public int getIdUsuario() {
        return idUsuario;
    }

    public int getIdCategoria() {
        return idCategoria;
    }

}