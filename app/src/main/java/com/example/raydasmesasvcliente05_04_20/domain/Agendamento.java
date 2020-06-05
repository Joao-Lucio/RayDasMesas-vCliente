package com.example.raydasmesasvcliente05_04_20.domain;

import java.io.Serializable;
import java.util.Date;

public class Agendamento implements Serializable {

    private int qtdMesas;
    private int qtdCadeiras;
    private Date data;
    private String dataString;
    private String horario;
    private int desconto;
    private String Status;
    private float valor;
    private String rua;
    private String anoString;
    private int numero;
    private String bairro;
    private String referencia;
    private String nome_responsavel;
    private String telefone_responsavel;
    private String uid;
    private String editado;
    private String tipoAluguel;
    private String admfoinot;
    private String clientefoinot;
    private String ultmodificou;
    private Double lat;
    private Double lon;

    public Agendamento(){}

    public int getQtdMesas() {
        return qtdMesas;
    }

    public void setQtdMesas(int qtdMesas) {
        this.qtdMesas = qtdMesas;
    }

    public int getQtdCadeiras() {
        return qtdCadeiras;
    }

    public void setQtdCadeiras(int qtdCadeiras) {
        this.qtdCadeiras = qtdCadeiras;
    }

    public Date getData() {
        return data;
    }

    public void setData(Date data) {
        this.data = data;
    }

    public String getDataString() {
        return dataString;
    }

    public void setDataString(String dataString) {
        this.dataString = dataString;
    }

    public String getHorario() {
        return horario;
    }

    public void setHorario(String horario) {
        this.horario = horario;
    }

    public int getDesconto() {
        return desconto;
    }

    public void setDesconto(int desconto) {
        this.desconto = desconto;
    }

    public String getStatus() {
        return Status;
    }

    public void setStatus(String status) {
        Status = status;
    }

    public float getValor() {
        return valor;
    }

    public void setValor(float valor) {
        this.valor = valor;
    }

    public String getRua() {
        return rua;
    }

    public void setRua(String rua) {
        this.rua = rua;
    }

    public String getAnoString() {
        return anoString;
    }

    public void setAnoString(String anoString) {
        this.anoString = anoString;
    }

    public int getNumero() {
        return numero;
    }

    public void setNumero(int numero) {
        this.numero = numero;
    }

    public String getBairro() {
        return bairro;
    }

    public void setBairro(String bairro) {
        this.bairro = bairro;
    }

    public String getReferencia() {
        return referencia;
    }

    public void setReferencia(String referencia) {
        this.referencia = referencia;
    }

    public String getNome_responsavel() {
        return nome_responsavel;
    }

    public void setNome_responsavel(String nome_responsavel) {
        this.nome_responsavel = nome_responsavel;
    }

    public String getTelefone_responsavel() {
        return telefone_responsavel;
    }

    public void setTelefone_responsavel(String telefone_responsavel) {
        this.telefone_responsavel = telefone_responsavel;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getEditado() {
        return editado;
    }

    public void setEditado(String editado) {
        this.editado = editado;
    }

    public String getTipoAluguel() {
        return tipoAluguel;
    }

    public void setTipoAluguel(String tipoAluguel) {
        this.tipoAluguel = tipoAluguel;
    }

    public String getAdmfoinot() {
        return admfoinot;
    }

    public void setAdmfoinot(String admfoinot) {
        this.admfoinot = admfoinot;
    }

    public String getClientefoinot() {
        return clientefoinot;
    }

    public void setClientefoinot(String clientefoinot) {
        this.clientefoinot = clientefoinot;
    }

    public String getUltmodificou() {
        return ultmodificou;
    }

    public void setUltmodificou(String ultmodificou) {
        this.ultmodificou = ultmodificou;
    }

    public Double getLat() {
        return lat;
    }

    public void setLat(Double lat) {
        this.lat = lat;
    }

    public Double getLon() {
        return lon;
    }

    public void setLon(Double lon) {
        this.lon = lon;
    }
}
