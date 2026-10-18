package subequipe03.ecommerce;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Cliente {
    private final int id;
    private String nome;
    private String email;
    private String telefone;
    
    private final List<Endereco> enderecos = new ArrayList<>();
    private Carrinho carrinho;

    public Cliente(int id, String nome, String email, String telefone) {
        this.id = id;
        this.nome = nome;
        this.email = email;
        this.telefone = telefone;
    }

    public String getDados() {
        return "Cliente #" + id + " | " + nome + " | " + email + " | " + telefone;
    }

    public void cadastrarEndereco(Endereco endereco) {
        if (endereco == null) {
            throw new IllegalArgumentException("Endereço não pode ser nulo.");
        }
        enderecos.add(endereco);
    }

    public Carrinho montarCarrinho() {
        if (carrinho == null) {
            carrinho = new Carrinho();
        }
        return carrinho;
    }

    public int getId() { return id; }
    public String getNome() { return nome; }
    public void setNome(String nome) { this.nome = nome; }
    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }
    public String getTelefone() { return telefone; }
    public void setTelefone(String telefone) { this.telefone = telefone; }
    public List<Endereco> getEnderecos() { return Collections.unmodifiableList(enderecos); }
    public Carrinho getCarrinho() { return carrinho; }
}
