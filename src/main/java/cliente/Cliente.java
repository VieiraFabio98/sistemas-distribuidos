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

            Requisicao requisicao = new Requisicao();
            String token = "";
            String  nome = "", email = "", senha = "", emailCandidato = "";
            //implementar logica para saber se o cliente está logado
            //turma precisa decidir oq fazer com o token
            boolean logado = false;
            while(true) {
                BufferedReader in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
                PrintWriter out = new PrintWriter(clientSocket.getOutputStream(), true);
                System.out.println("1.Login - 2.Logout - 3.Cadastrar - 4.Visualizar - 5.Atualizar - 6.Deletar");
                int opcao = scanner.nextInt();
                switch (opcao){
                    case 1:
                        Login login = new Login();
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
                        Logout logout = new Logout();
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
//                        System.out.println("Email: ");
//                        scanner.nextLine();
//                        email = scanner.nextLine();
                        requisicao.setEmail(emailCandidato);
                        requisicao.setOperacao("apagarCandidato");
                        out.println(gson.toJson(requisicao));
                        break;

                    case 7:
                        out.println("");
                        clientSocket.close();
                        return;

                    default:
                        System.out.println("Opção inválida!");
                        break;
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
                System.out.println("Mensagem do servidor: " + mensagemRecebida);
//                out.close();
//                in.close();
            }
        } catch(IOException e) {
            e.printStackTrace();
        }
    }
}