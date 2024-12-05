package esgi.config;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DatabaseConnection {

    // Instance unique de la connexion
    private static DatabaseConnection instance;
    private Connection connection;

    // Paramètres de la base de données
    private static final String URL = "jdbc:mysql://localhost:8889/bd-bibliotheque?useSSL=false&allowPublicKeyRetrieval=true&serverTimezone=UTC";
    private static final String USER = "root";
    private static final String PASSWORD = "root";

    // Constructeur privé pour empêcher l'instanciation directe
    private DatabaseConnection() throws SQLException {
        try {
            // Charger le driver JDBC
            Class.forName("com.mysql.cj.jdbc.Driver");
            // Initialiser la connexion
            this.connection = DriverManager.getConnection(URL, USER, PASSWORD);
        } catch (ClassNotFoundException e) {
            throw new SQLException("Erreur lors du chargement du driver JDBC", e);
        }
    }

    // Méthode pour obtenir l'instance unique
    public static DatabaseConnection getInstance() {
        if (instance == null) {
            synchronized (DatabaseConnection.class) {
                if (instance == null) {
                    try {
                        instance = new DatabaseConnection();
                    } catch (SQLException e) {
                        throw new RuntimeException("Erreur lors de la création de la connexion à la base de données.", e);
                    }
                }
            }
        }
        return instance;
    }

    public Connection getConnection() throws SQLException {
        if (connection == null || connection.isClosed()) {
            connection = DriverManager.getConnection(URL, USER, PASSWORD);
        }
        return connection;
    }

}
