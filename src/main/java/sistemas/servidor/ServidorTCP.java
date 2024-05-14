package sistemas.servidor;

import sistemas.servidor.handler.HandleRequisitions;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.sql.SQLException;

public class ServidorTCP {

    private static ServerSocket serverSocket;

    public static void main(String[] args) throws Exception {
        serverSocket = new ServerSocket(22222);
        startServer();
    }

    public static void startServer() throws SQLException {
        String host = "";
        try {
            HandleRequisitions handle = new HandleRequisitions();
//            int serverPort = 22222;

            System.out.println("Servidor TCP esperando por conexões...");
            Socket connectionSocket = serverSocket.accept();
            boolean serverOn = true;
            host = connectionSocket.getInetAddress().getHostAddress();
            System.out.println("Conexão estabelecida com cliente: " + host);
            BufferedReader in = new BufferedReader(new InputStreamReader(connectionSocket.getInputStream()));
            DataOutputStream outClient = new DataOutputStream(connectionSocket.getOutputStream());
            PrintWriter out = new PrintWriter(connectionSocket.getOutputStream(), true);

            while (serverOn) {
//                PrintWriter out = new PrintWriter(connectionSocket.getOutputStream(), true);
                String jsonCliente = in.readLine();
                System.out.println("Mensagem do Cliente: " + jsonCliente);

                String response = jsonCliente;

                if (jsonCliente.contains("loginCandidato")) {
                    response = handle.login(jsonCliente);
                }

                if (jsonCliente.contains("logout")) {
                    response = handle.logout(jsonCliente);
                }

                if (jsonCliente.contains("cadastrarCandidato")) {
                    response = handle.cadastrarCandidato(jsonCliente);
                }

                if (jsonCliente.contains("atualizarCandidato")) {
                    response = handle.atualizarCandidato(jsonCliente);
                }

                if (jsonCliente.contains("apagarCandidato")) {
                    response = handle.excluirCandidato(jsonCliente);
                }

                if (jsonCliente.contains("visualizarCandidato")) {
                    response = handle.visualizarCandidato(jsonCliente);
                }

                if(jsonCliente.isEmpty()) {
                    break;
                }

                System.out.println("Responsta do servidor: " + response);
                out.println(response);
//                outClient.writeBytes(response);
//                in.close();
//                outClient.close();
            }
            in.close();
            outClient.close();
            connectionSocket.close();
        } catch (IOException e) {
            System.out.print("Conexão finalizada - " + host + "\n");
            startServer();
        }
    }

}
