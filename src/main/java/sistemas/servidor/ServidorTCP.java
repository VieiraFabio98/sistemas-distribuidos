package sistemas.servidor;

import sistemas.servidor.handler.HandleRequisitions;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;

public class ServidorTCP {

    public static void main(String[] args) throws Exception {
        try {
            HandleRequisitions handle = new HandleRequisitions();
            int serverPort = 22222;

            ServerSocket serverSocket = new ServerSocket(serverPort);
            System.out.println("Servidor TCP esperando por conexões...");
            Socket connectionSocket = serverSocket.accept();
            boolean serverOn = true;
            System.out.println("Conexão estabelecida com cliente: " + connectionSocket.getInetAddress().getHostAddress());
            BufferedReader in = new BufferedReader(new InputStreamReader(connectionSocket.getInputStream()));
            DataOutputStream outClient = new DataOutputStream(connectionSocket.getOutputStream());
            PrintWriter out = new PrintWriter(connectionSocket.getOutputStream(), true);

            while (serverOn) {
//                PrintWriter out = new PrintWriter(connectionSocket.getOutputStream(), true);
                String jsonCliente = in.readLine();
                System.out.println(jsonCliente + "jsonCliente");

                String response = jsonCliente;

                if (jsonCliente.contains("loginCandidato")) {
                    response = handle.login(jsonCliente);
                }

                if (jsonCliente.contains("logout")) {
                    response = handle.logout(jsonCliente);
                    serverOn = false;
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

                System.out.println(response + "response");
                out.println(response);
//                outClient.writeBytes(response);
//                in.close();
//                outClient.close();
            }
            in.close();
            outClient.close();
            connectionSocket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
