package sistemas.servidor.handler;

import com.google.gson.Gson;
import sistemas.servidor.dao.CandidatoDAO;
import sistemas.servidor.db.BancoDados;
import sistemas.servidor.entities.Candidato;

import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;

public class HandleRequisitions {


    public HandleRequisitions(){
    }

    public static String login(String jsonCliente) throws SQLException, IOException {
        Gson gson = new Gson();
        Candidato candidato = gson.fromJson(jsonCliente, Candidato.class);

        Connection conn = BancoDados.conectar();
        String response = new CandidatoDAO(conn).login(candidato);

        return response;
    }

    public static String logout(String jsonCliente) throws SQLException, IOException {
        Gson gson = new Gson();
        Candidato candidato = gson.fromJson(jsonCliente, Candidato.class);

        Connection conn = BancoDados.conectar();
        String response = new CandidatoDAO(conn).logout(candidato);

        return response;
    }

    public static String cadastrarCandidato(String jsonCandidato) throws SQLException, IOException {
        Gson gson = new Gson();

        try (Connection conn = BancoDados.conectar()) {
            Candidato candidato = gson.fromJson(jsonCandidato, Candidato.class);
            String errorResponse = new HandleErros().handleErrors(candidato);

            if (!errorResponse.isEmpty()) {
                return errorResponse;
            }

            return new CandidatoDAO(conn).cadastrar(candidato);
        }
    }

    public static String visualizarCandidato(String jsonCandidato) throws SQLException, IOException {
        Gson gson = new Gson();
        Candidato candidato = gson.fromJson(jsonCandidato, Candidato.class);

        Connection conn = BancoDados.conectar();
        String response = new CandidatoDAO(conn).visualizar(candidato);

        return response;
    }

    public static String atualizarCandidato(String jsonCandidato) throws SQLException, IOException {
        Gson gson = new Gson();
        try (Connection conn = BancoDados.conectar()) {
            Candidato candidato = gson.fromJson(jsonCandidato, Candidato.class);
            String errorResponse = new HandleErros().handleErrors(candidato);

            if (!errorResponse.isEmpty()) {
                return errorResponse;
            }

            return new CandidatoDAO(conn).atualizar(candidato);
        }
    }

    public static String excluirCandidato(String jsonCandidato) throws  SQLException, IOException {
        Gson gson = new Gson();
        Candidato candidato = gson.fromJson(jsonCandidato, Candidato.class);

        Connection conn = BancoDados.conectar();
        String response = new CandidatoDAO(conn).excluirCandidato(candidato);

        return response;
    }
}
