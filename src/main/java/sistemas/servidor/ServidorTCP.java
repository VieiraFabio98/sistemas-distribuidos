package sistemas.servidor;

import sistemas.servidor.handler.HandleRequisitions;
import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;

public class ServidorTCP {

    public static void main(String[] args) throws Exception {
        HandleRequisitions handle = new HandleRequisitions();
        int serverPort = 22222;

        ServerSocket welcomeSocket = new ServerSocket(serverPort);
        System.out.println("Servidor TCP esperando por conexões...");

        while (true) {
            Socket connectionSocket = welcomeSocket.accept();

            PrintWriter out = new PrintWriter(connectionSocket.getOutputStream(), true);
            BufferedReader in = new BufferedReader(new InputStreamReader(connectionSocket.getInputStream()));
            DataOutputStream outClient = new DataOutputStream(connectionSocket.getOutputStream());

            String jsonCliente = in.readLine();
            System.out.println(jsonCliente + "jsonCliente");

            String response = null;

            if(jsonCliente.contains("loginCandidato")) {
                response = handle.login(jsonCliente);
            }

            if(jsonCliente.contains("logout")) {
                response = handle.logout(jsonCliente);
            }

            if(jsonCliente.contains("cadastrarCandidato")) {
                response = handle.cadastrarCandidato(jsonCliente);
            }

            if(jsonCliente.contains("atualizarCandidato")){
                response = handle.atualizarCandidato(jsonCliente);
            }

            if(jsonCliente.contains("apagarCandidato")){
                response = handle.excluirCandidato(jsonCliente);
            }

            if(jsonCliente.contains("visualizarCandidato")){
                response = handle.visualizarCandidato(jsonCliente);
            }

            System.out.println(response);

            outClient.writeBytes(response);

            connectionSocket.close();
        }
    }


}
