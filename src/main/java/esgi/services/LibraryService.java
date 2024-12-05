package esgi.services;

import esgi.config.DatabaseConnection;
import esgi.models.Library;
import org.springframework.stereotype.Service;

import java.sql.*;

@Service
public class LibraryService {

    public Library getDefaultLibrary() {
        String query = "SELECT * FROM library WHERE name = 'Default Library'";
        Library library = null;

        try (Connection connection = DatabaseConnection.getInstance().getConnection();
             PreparedStatement statement = connection.prepareStatement(query);
             ResultSet resultSet = statement.executeQuery()) {

            if (resultSet.next()) {
                library = new Library();
                library.setId(resultSet.getLong("id"));
                library.setName(resultSet.getString("name"));
            } else {
                library = createDefaultLibrary(connection);
            }

        } catch (SQLException e) {
            throw new RuntimeException("Erreur lors de la récupération de la bibliothèque par défaut.", e);
        }

        return library;
    }

    private Library createDefaultLibrary(Connection connection) throws SQLException {
        String insertQuery = "INSERT INTO library (name) VALUES ('Default Library')";
        try (PreparedStatement statement = connection.prepareStatement(insertQuery, Statement.RETURN_GENERATED_KEYS)) {
            statement.executeUpdate();
            ResultSet generatedKeys = statement.getGeneratedKeys();
            if (generatedKeys.next()) {
                Library library = new Library();
                library.setId(generatedKeys.getLong(1));
                library.setName("Default Library");
                return library;
            } else {
                throw new RuntimeException("Impossible de créer la bibliothèque par défaut.");
            }
        }
    }
}
