package sistemas.servidor.handler;

import com.google.gson.Gson;
import sistemas.servidor.dao.CandidatoDAO;
import sistemas.servidor.entities.Candidato;
import sistemas.servidor.entities.Response;

import java.io.IOException;
import java.sql.SQLException;

public class HandleErros {

    public HandleErros() {}

    private static Response response = new Response();
    private static Gson gson = new Gson();

    public String handleErrors(Candidato candidato) throws SQLException, IOException {

        response.setOperacao("cadastrarCandidato");

        if(candidato.getSenha().length() < 3 || candidato.getSenha().length() > 8) {
            response.setStatus(404);
            response.setMensagem("Senha não permitida");
            return gson.toJson(response);
        }

        if(candidato.getEmail().length() < 7 || candidato.getEmail().length() > 50) {
            response.setStatus(404);
            response.setMensagem("Mínimo ou Máximo de caracteres do email não atingidos.");
            return gson.toJson(response);
        }

        if((!candidato.getEmail().contains("@"))){
            response.setStatus(404);
            response.setMensagem("E-mail inválido");
            return gson.toJson(response);
        }

        if(!candidato.getNome().matches("^[a-zA-ZÀ-ÿ\\s]+$")) {
            response.setStatus(404);
            response.setMensagem("Nome invalido");
            return gson.toJson(response);
        }

        if (!candidato.getSenha().matches("^[0-9]+$")) {
            response.setStatus(404);
            response.setMensagem("Senha inválida. Deve conter apenas números.");
            return new Gson().toJson(response);
        }

        return "";
    }
}
