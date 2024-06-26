package sistemas.servidor.dao;

import com.google.gson.Gson;
import sistemas.servidor.db.BancoDados;
import sistemas.servidor.entities.Candidato;
import sistemas.servidor.entities.Response;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.UUID;

public class CandidatoDAO {
    private Connection conn;

    public CandidatoDAO(Connection conn) {
        this.conn = conn;
    }

    public String cadastrar(Candidato candidato) throws SQLException {
        int candidatoId = getCandidato(candidato);
        PreparedStatement st = null;
        Response response = new Response();
        Gson gson = new Gson();
        UUID uuid = UUID.randomUUID();

        String token = uuid.toString();

        if(candidatoId != -1){
            response.setOperacao("cadastrarCandidato");
            response.setStatus(422);
            response.setMensagem("E-mail já cadastrado");
            return gson.toJson(response);
        }
//
//        if(candidato.getSenha().length() < 3 || candidato.getSenha().length() > 8) {
//            response.setOperacao("cadastrarCandidato");
//            response.setStatus(404);
//            response.setMensagem("Senha não permitida");
//            return gson.toJson(response);
//        }
//
//        if(candidato.getEmail().length() < 7 || candidato.getEmail().length() > 50) {
//            response.setOperacao("cadastrarCandidato");
//            response.setStatus(404);
//            response.setMensagem("Mínimo ou Máximo de caracteres do email não atingidos.");
//            return gson.toJson(response);
//        }
//
//        if((!candidato.getEmail().contains("@"))){
//            response.setOperacao("cadastrarCandidato");
//            response.setStatus(404);
//            response.setMensagem("E-mail inválido");
//            return gson.toJson(response);
//        }
//
//        if(!candidato.getNome().matches("^[a-zA-ZÀ-ÿ\\s]+$")) {
//            response.setOperacao("cadastrarCandidato");
//            response.setStatus(404);
//            response.setMensagem("Nome invalido");
//            return gson.toJson(response);
//        }
//
//        if (!candidato.getSenha().matches("^[0-9]+$")) {
//            response.setOperacao("cadastrarCandidato");
//            response.setStatus(404);
//            response.setMensagem("Senha inválida. Deve conter apenas números.");
//            return new Gson().toJson(response);
//        }

        try{
            st = conn.prepareStatement("insert into candidato(nome, email, senha, logado,token) values(?,?,?,?,?)");
            st.setString(1, candidato.getNome());
            st.setString(2, candidato.getEmail());
            st.setString(3, candidato.getSenha());
            st.setInt(4, 1);
            st.setString(5, token);
            st.executeUpdate();

            response.setOperacao("cadastrarCandidato");
            response.setStatus(201);
            response.setToken(token);

        } catch (SQLException e) {
            response.setOperacao("cadastrarCandidato");
            response.setStatus(500);
            response.setMensagem("Erro do lado do servidor");
        }
            finally {
            BancoDados.finalizarStatement(st);
            BancoDados.desconectar();
        }

        return gson.toJson(response);
    }

    public String visualizar(Candidato candidato) throws SQLException {
        int candidatoId = getCandidato(candidato);
        int isLogado = isCandidatoLogado(candidato);
        Response response = new Response();
        Gson gson = new Gson();

        if(candidatoId == -1){
            response.setOperacao("visualizarCandidato");
            response.setStatus(404);
            response.setMensagem("E-mail não encontrado");
            return gson.toJson(response);
        }

        if(isLogado == 0){
            response.setOperacao("visualizarCandidato");
            response.setStatus(401);
            response.setMensagem("Usuário não autenticado");
            return gson.toJson(response);
        }

        PreparedStatement st = null;
        ResultSet rs = null;

        try {
            st = conn.prepareStatement("select * from candidato where id = ?");
            st.setInt(1, candidatoId);

            rs = st.executeQuery();

            if (rs.next()) {
                response.setOperacao("visualizarCandidato");
                response.setStatus(201);
                response.setNome(rs.getString("nome"));
                response.setSenha(rs.getString("senha"));
            } else {
                response.setOperacao("visualizarCandidato");
                response.setStatus(404);
                response.setMensagem("Nenhum candidato encontrado");
            }
        } finally {
            BancoDados.finalizarStatement(st);
            BancoDados.finalizarResultSet(rs);
            BancoDados.desconectar();
        }

        return gson.toJson(response);
    }


    public int getCandidato(Candidato candidato) throws SQLException{
        PreparedStatement st = null;
        ResultSet rs = null;

        int candidatoId = -1;

        try {
            st = conn.prepareStatement("select * from candidato where email = ?");
            st.setString(1, candidato.getEmail());

            rs = st.executeQuery();

            if (rs.next()) {
                candidatoId = rs.getInt("id");
            }

            if(candidatoId != -1) {
                System.out.println("Nenhum candidato encontrado com o email '" + candidato.getEmail() + "'.");
            }

        } finally {
            BancoDados.finalizarStatement(st);
            BancoDados.finalizarResultSet(rs);
        }

        return candidatoId;
    }

    public int getCandidatoByToken(Candidato candidato) throws SQLException {
        PreparedStatement st = null;
        ResultSet rs = null;

        int candidatoId = -1;

        try {
            st = conn.prepareStatement("select * from candidato where token = ?");
            st.setString(1, candidato.getToken());

            rs = st.executeQuery();

            if (rs.next()) {
                candidatoId = rs.getInt("id");
            }
        } finally {
            BancoDados.finalizarStatement(st);
            BancoDados.finalizarResultSet(rs);
        }
        return candidatoId;
    }

    public int isCandidatoLogado(Candidato candidato) throws SQLException {
        PreparedStatement st = null;
        ResultSet rs = null;

        int logado = 0;

        try {
            st = conn.prepareStatement("select logado from candidato where email = ?");
            st.setString(1, candidato.getEmail());

            rs = st.executeQuery();

            if(rs.next()){
                logado = rs.getInt("logado");
            } else {
                System.out.println("Candidato não está logado");
            }
        } finally {
            BancoDados.finalizarStatement(st);
            BancoDados.finalizarResultSet(rs);
        }
        return logado;
    }

    public String atualizar(Candidato candidato) throws SQLException {
        int candidatoId = getCandidato(candidato);
        int isLogado = isCandidatoLogado(candidato);
        Response response = new Response();
        Gson gson = new Gson();

        if(candidatoId == -1){
            System.out.print("E-mail não encontrado.");
            response.setOperacao("atualizarCandidato");
            response.setStatus(404);
            response.setMensagem("E-mail não encontrado");
            return gson.toJson(response);
        }

//        if(candidato.getSenha().length() < 3 || candidato.getSenha().length() > 8) {
//            response.setOperacao("cadastrarCandidato");
//            response.setStatus(404);
//            response.setMensagem("senha nao permitida");
//            return gson.toJson(response);
//        }
//
//        if((!candidato.getEmail().contains("@"))){
//            response.setOperacao("cadastrarCandidato");
//            response.setStatus(404);
//            response.setMensagem("E-mail inválido");
//            return gson.toJson(response);
//        }
//
//        if(!candidato.getNome().matches("^[a-zA-ZÀ-ÿ\\s]+$")) {
//            response.setOperacao("cadastrarCandidato");
//            response.setStatus(404);
//            response.setMensagem("Nome invalido");
//            return gson.toJson(response);
//        }
//
//        if (!candidato.getSenha().matches("^[0-9]+$")) {
//            response.setOperacao("cadastrarCandidato");
//            response.setStatus(404);
//            response.setMensagem("Senha inválida. Deve conter apenas números.");
//            return new Gson().toJson(response);
//        }

        if(isLogado == 0){
            response.setOperacao("visualizarCandidato");
            response.setStatus(401);
            response.setMensagem("Usuário não autenticado");
            return gson.toJson(response);
        }

        PreparedStatement st = null;
        ResultSet rs = null;

        try {
            st = conn.prepareStatement("update candidato set nome = ?, senha = ? where id = ?");
            st.setString(1, candidato.getNome());
            st.setString(2, candidato.getSenha());
            st.setInt(3, candidatoId);
            st.executeUpdate();

            response.setOperacao("atualizarCandidato");
            response.setStatus(201);
        } finally {
            BancoDados.finalizarStatement(st);
            BancoDados.desconectar();
        }

        return gson.toJson(response);
    }

    public String excluirCandidato(Candidato candidato) throws SQLException {
        int candidatoId = getCandidato(candidato);
        int isLogado = isCandidatoLogado(candidato);
        Response response = new Response();
        Gson gson = new Gson();

        if(candidatoId == -1){
            response.setOperacao("apagarCandidato");
            response.setStatus(404);
            response.setMensagem("E-mail não encontrado");
            return gson.toJson(response);
        }

        if(isLogado == 0){
            response.setOperacao("visualizarCandidato");
            response.setStatus(401);
            response.setMensagem("Usuário não autenticado");
            return gson.toJson(response);
        }

        PreparedStatement st = null;

        try {
            st = conn.prepareStatement("delete from candidato where id = ?");
            st.setInt(1, candidatoId);
            st.executeUpdate();
            response.setOperacao("apagarCandidato");
            response.setStatus(201);
        } finally {
            BancoDados.finalizarStatement(st);
            BancoDados.desconectar();
        }

        return gson.toJson(response);
    }

    public String login(Candidato candidato) throws SQLException {
        int candidatoId = checkUser(candidato);
        int isLogado = isCandidatoLogado(candidato);
        UUID uuid = UUID.randomUUID();
        Response response = new Response();
        Gson gson = new Gson();

        if(candidatoId == -1){
            response.setOperacao("loginCandidato");
            response.setStatus(401);
            response.setMensagem("Login ou senha incorretos");
            return gson.toJson(response);
        }

        if(isLogado == 1){
            response.setOperacao("visualizarCandidato");
            response.setStatus(401);
            response.setMensagem("Usuário já autenticado");
            return gson.toJson(response);
        }

        PreparedStatement st = null;

        try {
            st = conn.prepareStatement("update candidato set logado = 1, token = ? WHERE id = ?");
            st.setString(1, uuid.toString());
            st.setInt(2, candidatoId);
            st.executeUpdate();
        } finally {
            BancoDados.finalizarStatement(st);
            BancoDados.desconectar();
        }


        response.setOperacao("loginCandidato");
        response.setStatus(200);
        response.setToken(uuid.toString());
        System.out.println(gson.toJson(response));
        return gson.toJson(response);
    }

    public String logout(Candidato candidato) throws SQLException {
        int candidatoId = getCandidatoByToken(candidato);

        Gson gson = new Gson();
        Response response = new Response();

        if(candidatoId == -1 ){
            response.setOperacao("logout");
            response.setStatus(404);
            return gson.toJson(response);
        }

        PreparedStatement st = null;

        try {
            st = conn.prepareStatement("update candidato set logado = 0, token = null WHERE id = ?");
            st.setInt(1, candidatoId);
            st.executeUpdate();
        } finally {
            BancoDados.finalizarStatement(st);
            BancoDados.desconectar();
        }

        response.setOperacao("logout");
        response.setStatus(204);
        return gson.toJson(response);
    }

    public int checkUser(Candidato candidato) throws SQLException{
        PreparedStatement st = null;
        ResultSet rs = null;

        int candidatoId = -1;

        try {
            st = conn.prepareStatement("select * from candidato where email = ? and senha = ?");
            st.setString(1, candidato.getEmail());
            st.setString(2, candidato.getSenha());

            rs = st.executeQuery();

            if (rs.next()) {
                candidatoId = rs.getInt("id");
            }

        } finally {
            BancoDados.finalizarStatement(st);
            BancoDados.finalizarResultSet(rs);
        }

        return candidatoId;
    }
}
