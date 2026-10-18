package subequipe03.ecommerce;

public class Endereco {
    private String logradouro;
    private String numero;
    private String complemento;
    private String cidade;
    private String estado;
    private String cep;
    private String bairro;

    public Endereco(String logradouro, String numero, String complemento,
                    String cidade, String estado, String cep, String bairro) {
        this.logradouro = logradouro;
        this.numero = numero;
        this.complemento = complemento;
        this.cidade = cidade;
        this.estado = estado;
        this.cep = cep;
        this.bairro = bairro;
    }

    
    public String getEndereco() {
        StringBuilder sb = new StringBuilder();
        sb.append(logradouro).append(", ").append(numero);
        if (complemento != null && !complemento.isBlank()) {
            sb.append(" - ").append(complemento);
        }
        sb.append(", ").append(bairro)
          .append(", ").append(cidade).append("/").append(estado)
          .append(", CEP ").append(cep);
        return sb.toString();
    }

    public String getLogradouro() { return logradouro; }
    public void setLogradouro(String logradouro) { this.logradouro = logradouro; }

    public String getNumero() { return numero; }
    public void setNumero(String numero) { this.numero = numero; }

    public String getComplemento() { return complemento; }
    public void setComplemento(String complemento) { this.complemento = complemento; }

    public String getCidade() { return cidade; }
    public void setCidade(String cidade) { this.cidade = cidade; }

    public String getEstado() { return estado; }
    public void setEstado(String estado) { this.estado = estado; }

    public String getCep() { return cep; }
    public void setCep(String cep) { this.cep = cep; }

    public String getBairro() { return bairro; }
    public void setBairro(String bairro) { this.bairro = bairro; }
}
