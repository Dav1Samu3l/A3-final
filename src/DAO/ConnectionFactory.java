package DAO;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.SQLTimeoutException;
import java.sql.SQLNonTransientConnectionException;
import java.sql.SQLTransientConnectionException;

public class ConnectionFactory {
    // Configurações do banco de dados
    private static final String DRIVER = "com.mysql.cj.jdbc.Driver";
    private static final String URL = "jdbc:mysql://localhost:3306/db_estoque?useTimezone=true&serverTimezone=UTC";
    private static final String USER = "root";
    private static final String PASSWORD = "aaaa"; 

    // Método para obter uma conexão
    public static Connection getConnection() {
        try {
            Class.forName(DRIVER);
            return DriverManager.getConnection(URL, USER, PASSWORD);
        } catch (ClassNotFoundException e) {
            throw new RuntimeException("Driver do MySQL não encontrado!", e);
        } catch (SQLTimeoutException e) {
            throw new RuntimeException("Timeout ao conectar ao banco de dados", e);
        } catch (SQLNonTransientConnectionException e) {
            throw new RuntimeException("Erro permanente na conexão (ex: credenciais inválidas)", e);
        } catch (SQLTransientConnectionException e) {
            throw new RuntimeException("Erro temporário na conexão (ex: servidor inalcançável)", e);
        } catch (SQLException e) {
            throw new RuntimeException("Erro geral de SQL: " + e.getMessage(), e);
        }
    }

    // Método para testar a conexão 
    public static void testarConexao() {
        try {
            System.out.println("Conexão com o banco de dados estabelecida com sucesso!");
        } catch (RuntimeException e) {
            Throwable causa = e.getCause();
            if (causa instanceof SQLException) {
                SQLException sqlEx = (SQLException) causa;
                // Verifica códigos de erro específicos do MySQL
                switch (sqlEx.getErrorCode()) {
                    case 1045: // Access Denied
                        System.err.println("ERRO: Credenciais inválidas (usuário/senha)");
                        break;
                    case 1049: // Unknown Database
                        System.err.println("ERRO: Banco de dados não encontrado");
                        break;
                    case 0: // Timeout/Comunicação
                        System.err.println("ERRO: Servidor MySQL inalcançável");
                        break;
                    default:
                        System.err.println("ERRO MySQL [" + sqlEx.getErrorCode() + "]: " + sqlEx.getMessage());
                }
            } else {
                System.err.println("ERRO: " + e.getMessage());
            }
        } catch (Exception e) {
            System.err.println("Erro inesperado: " + e.getMessage());
        }
    }
}