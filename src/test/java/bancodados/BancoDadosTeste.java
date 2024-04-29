package bancodados;

import sistemas.servidor.db.BancoDados;

import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;

public class BancoDadosTeste {
    public static void main(String args []) {
        try {
            Connection conn = BancoDados.conectar();
            System.out.println("conexão estabelecida");

            BancoDados.desconectar();
            System.out.println("conexão finalizada");
        } catch (SQLException | IOException exception) {
            System.out.println(exception.getMessage());
        }
    }
}
