package br.edu.ifg.estoque.model;

public class Fornecedor {

    private int id;
    private String nomeFantasia;
    private String razaoSocial;
    private String cnpj;

    public Fornecedor() {}

    public Fornecedor(String nomeFantasia, String razaoSocial, String cnpj) {// construtor para criar novos fornecedores
    // o id é gerado automaticamente pelo banco de dados, por isso não esta sendo passado como parâmetro
        this.nomeFantasia = nomeFantasia;
        this.razaoSocial  = razaoSocial;
        this.cnpj         = cnpj;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNomeFantasia() {
        return nomeFantasia;
    }

    public void setNomeFantasia(String nomeFantasia) {
        this.nomeFantasia = nomeFantasia;
    }

    public String getRazaoSocial() {
        return razaoSocial;
    }
    
    public void setRazaoSocial(String razaoSocial) {
        this.razaoSocial = razaoSocial;
    }

    public String getCnpj() {
        return cnpj;
    }

    public void setCnpj(String cnpj) {
        this.cnpj = cnpj;
    }

    @Override
    public String toString() {
        return "Fornecedor{id=" + id + ", nomeFantasia='" + nomeFantasia + "', cnpj='" + cnpj + "'}";
    }
}
