package br.edu.ifg.estoque.model;

public class Cliente {

    private int id;
    private String nome;
    private String cpf;
    private String rg;
    private String endereco;
    private String telefone;

    public Cliente() {}

    public Cliente(String nome, String cpf, String rg, String endereco, String telefone) {// construtor sem id para criação de novos clientes
        this.nome = nome;
        this.cpf = cpf;
        this.rg = rg;
        this.endereco = endereco;
        this.telefone = telefone;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) { this.id = id; }
        this.id = id;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getCpf() {
        return cpf;
    }

    public void setCpf(String cpf) {
        this.cpf = cpf;
    }

    public String getRg() {
        return rg;
    }

    public void setRg(String rg) {
        this.rg = rg;
    }

    public String getEndereco() {
        return endereco;
    }

    public void setEndereco(String end) {
        this.endereco = end;
    }

    public String getTelefone() {
        return telefone;
    }
    
    public void setTelefone(String tel) {
        this.telefone = tel;
    }

    @Override
    public String toString() {
        return "Cliente{id=" + id + ", nome='" + nome + "', cpf='" + cpf + "'}";
    }
}
