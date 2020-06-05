package com.example.raydasmesasvcliente05_04_20.domain;

import java.util.Date;

public class Promocao {

    private String nome;
    private Date data_inic;
    private Date data_fim;
    private String descricao;
    private int desconto;
    private int quantidade;
    private String tipo;
    private String uid;
    private String foinotificado;

    public Promocao(){

    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public Date getData_inic() {
        return data_inic;
    }

    public void setData_inic(Date data_inic) {
        this.data_inic = data_inic;
    }

    public Date getData_fim() {
        return data_fim;
    }

    public void setData_fim(Date data_fim) {
        this.data_fim = data_fim;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public int getDesconto() {
        return desconto;
    }

    public void setDesconto(int desconto) {
        this.desconto = desconto;
    }

    public int getQuantidade() {
        return quantidade;
    }

    public void setQuantidade(int quantidade) {
        this.quantidade = quantidade;
    }

    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getFoinotificado() {
        return foinotificado;
    }

    public void setFoinotificado(String foinotificado) {
        this.foinotificado = foinotificado;
    }
}
