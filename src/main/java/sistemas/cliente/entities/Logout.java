package sistemas.cliente.entities;

public class Logout {
    private String operacao;
    private String token;

    public Logout() {

    }

    public String getOperacao() {
        return operacao;
    }

    public void setOperacao(String operacao) {
        this.operacao = operacao;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }
}
