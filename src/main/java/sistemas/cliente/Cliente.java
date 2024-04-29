package sistemas.cliente;

import com.google.gson.Gson;
import sistemas.cliente.entities.Login;
import sistemas.cliente.entities.Logout;
import sistemas.cliente.entities.Requisicao;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Scanner;

public class Cliente {
    public static void main(String[] args) throws Exception {
        String serverAddress = "192.168.1.9";
        int serverPort = 22222;
        Socket clientSocket = new Socket(serverAddress, serverPort);
        Scanner scanner = new Scanner(System.in);
        BufferedReader in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
        PrintWriter out = new PrintWriter(clientSocket.getOutputStream(), true);

        System.out.println("1.Login - 2.Logout - 3.Cadastrar - 4.Visualizar - 5.Atualizar - 6.Deletar");

        Gson gson = new Gson();
        String token;
        int opcao = scanner.nextInt();
        Requisicao requisicao = new Requisicao();

        switch (opcao){
            case 1:
                Login login = new Login();
                login.setEmail("vieirafabio5271@gmail.com");
                login.setSenha("123456");
                login.setOperacao("loginCandidato");
                out.println(gson.toJson(login));
                break;

            case 2:
                Logout logout = new Logout();
                logout.setOperacao("logout");
                logout.setToken("bb690f48-47cf-47c2-8c0b-4d9d98eacf24");
                out.println(gson.toJson(logout));
                break;

            case 3:
                requisicao.setNome("Marcelly");
                requisicao.setEmail("marcellyalemar@gmail.com");
                requisicao.setSenha("123");
                requisicao.setOperacao("cadastrarCandidato");
                out.println(gson.toJson(requisicao));
                break;

            case 4:
                requisicao.setEmail("marcellyalemar@gmail.com");
                requisicao.setOperacao("visualizarCandidato");
                out.println(gson.toJson(requisicao));
                break;

            case 5:
                requisicao.setOperacao("atualizarCandidato");
                requisicao.setEmail("marcellyalemar@gmail.comm");
                requisicao.setNome("Marcelly Alemar");
                requisicao.setSenha("789");
                out.println(gson.toJson(requisicao));
                break;

            case 6:
                requisicao.setOperacao("apagarCandidato");
                requisicao.setEmail("marcellyalemar@gmail.com");
                out.println(gson.toJson(requisicao));
                break;

            default:
                System.out.println("Opção inválida!");
                break;
        }

        String mensagemRecebida = in.readLine();
        System.out.println("Resposta do servidor: " + mensagemRecebida);

//        do {
//           opcao = scanner.nextInt();
//           System.out.println(opcao + "opcao");
//
//            switch (opcao){
//                case 1:
//                    Login login = new Login();
//                    login.setEmail("vieirafabio5271@gmail.com");
//                    login.setSenha("123456");
//                    login.setOperacao("loginCandidato");
//                    out.println(gson.toJson(login));
//                    break;
//
//                case 2:
//                    Logout logout = new Logout();
//                    logout.setOperacao("logout");
//                    logout.setToken("bb690f48-47cf-47c2-8c0b-4d9d98eacf24");
//                    out.println(gson.toJson(logout));
//                    break;
//
//                case 3:
//                    Requisicao requisicao = new Requisicao();
//                    requisicao.setNome("Marcelly");
//                    requisicao.setEmail("marcellyalemar@gmail.com");
//                    requisicao.setSenha("123");
//                    requisicao.setOperacao("cadastrarCandidato");
//                    out.println(gson.toJson(requisicao));
//                    break;
//
//                default:
//                    System.out.println("Opção inválida!");
//                    break;
//            }
//
//            String mensagemRecebida = in.readLine();
//            System.out.println("Resposta do servidor: " + mensagemRecebida);
//
//        }while(opcao != 2);

        out.close();
        in.close();
        clientSocket.close();
    }
}