package erisnilton.com.br.testecrud.model;

import java.io.Serializable;

public class Cliente implements Serializable {
    private  int id;
    private String nome;
    private String sexo;
    private String uf;
    private Boolean vip;


    public Cliente(int id, String nome, String sexo, String uf, Boolean vip) {
        this.id = id;
        this.nome = nome;
        this.sexo = sexo;
        this.uf = uf;
        this.vip = vip;
    }

    public int getId() {
        return id;
    }

    public String getNome() {
        return nome;
    }

    public String getSexo() {
        return sexo;
    }

    public String getUf() {
        return uf;
    }

    public Boolean getVip() {
        return vip;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Cliente cliente = (Cliente) o;
        return id == cliente.id;
    }

    @Override
    public int hashCode() {

        return this.id;
    }
}
