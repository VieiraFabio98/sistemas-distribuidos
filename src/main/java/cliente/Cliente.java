package cliente;

import com.google.gson.Gson;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import cliente.entities.Login;
import cliente.entities.Logout;
import cliente.entities.Requisicao;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Scanner;


public class Cliente {
    public static void main(String[] args) throws Exception {
        try {
            Scanner scanner = new Scanner(System.in);
            System.out.println("Informe o ip do servidor: ");
            String serverAddress = scanner.nextLine();
            int serverPort = 22222;
            Socket clientSocket = new Socket(serverAddress, serverPort);
            JSONParser parser = new JSONParser();
            Gson gson = new Gson();


            Logout logout = new Logout();
            Login login = new Login();
            String token = "";
            String  nome = "", email = "", senha = "", emailCandidato = "";
            //implementar logica para saber se o cliente está logado
            //turma precisa decidir oq fazer com o token
            boolean logado = false;
            while(true) {
                Requisicao requisicao = new Requisicao();
                BufferedReader in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
                PrintWriter out = new PrintWriter(clientSocket.getOutputStream(), true);
                System.out.println("1.Login - 2.Logout - 3.Cadastrar - 4.Visualizar - 5.Atualizar - 6.Deletar");
                int opcao = scanner.nextInt();
                switch (opcao){
                    case 1:

                        System.out.println("Email: ");
                        scanner.nextLine();
                        email = scanner.nextLine();
                        System.out.println("Senha: ");
                        senha = scanner.nextLine();
                        login.setEmail(email);
                        login.setSenha(senha);
                        login.setOperacao("loginCandidato");
                        out.println(gson.toJson(login));
                        break;

                    case 2:

                        logout.setOperacao("logout");
                        logout.setToken(token);
                        out.println(gson.toJson(logout));
                        break;

                    case 3:
                        System.out.println("Nome: ");
                        scanner.nextLine();
                        nome = scanner.nextLine();
                        System.out.println("Email: ");
                        email = scanner.nextLine();
                        System.out.println("Senha: ");
                        senha = scanner.nextLine();
                        requisicao.setNome(nome);
                        requisicao.setEmail(email);
                        requisicao.setSenha(senha);
                        requisicao.setOperacao("cadastrarCandidato");
                        out.println(gson.toJson(requisicao));
                        break;


                    case 4:
                        requisicao.setOperacao("visualizarCandidato");
                        requisicao.setEmail(emailCandidato);
                        out.println(gson.toJson(requisicao));
                        break;

                    case 5:
                        System.out.println("Nome: ");
                        scanner.nextLine();
                        nome = scanner.nextLine();
                        System.out.println("Senha: ");
                        senha = scanner.nextLine();
                        requisicao.setNome(nome);
                        requisicao.setEmail(emailCandidato);
                        requisicao.setSenha(senha);
                        requisicao.setOperacao("atualizarCandidato");
                        out.println(gson.toJson(requisicao));
                        break;

                    case 6:
                        requisicao.setEmail(emailCandidato);
                        requisicao.setOperacao("apagarCandidato");
                        out.println(gson.toJson(requisicao));
                        break;

                    case 7:
                        clientSocket.close();
                        return;

                    default:
                        System.out.println("Opção inválida!");
                        break;
                }

                if(opcao == 2){
                    System.out.println("Mensagem enviada para o servidor: " + gson.toJson(logout));
                }
                if(opcao == 1) {
                    System.out.println("Mensagem enviada para o servidor: " + gson.toJson(login));
                }
                if(opcao > 2){
                    System.out.println("Mensagem enviada para o servidor: " + gson.toJson(requisicao));
                }

                String mensagemRecebida = in.readLine();
                JSONObject jsonObject = (JSONObject) parser.parse(mensagemRecebida);

                if (mensagemRecebida.contains("token")){
                    token = (String) jsonObject.get("token");
                    logado = true;
                }

                if(mensagemRecebida.contains("loginCandidato")) {
                    Long status = (Long) jsonObject.get("status");
                    if(status == 200) {
                        emailCandidato = email;
                        nome = "";
                        logado = true;
                    }
                }

                if(mensagemRecebida.contains("cadastrarCandidato")) {
                    Long status = (Long) jsonObject.get("status");
                    if(status == 201) {
                        emailCandidato = email;
                        logado = true;
                    }
                }

                System.out.println("Mensagem recebida do servidor: " + mensagemRecebida);
            }
        } catch(Exception e) {
            e.printStackTrace();
        }
    }
}