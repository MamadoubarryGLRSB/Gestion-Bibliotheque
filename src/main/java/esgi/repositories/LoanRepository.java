package esgi.repositories;

import esgi.models.Loan;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import java.util.List;

public interface LoanRepository extends JpaRepository<Loan, Long> {
    // Trouver les emprunts d'un utilisateur
    List<Loan> findByUserId(Integer userId);

    // Trouver les emprunts actifs (livres non retournés)
    @Query("SELECT l FROM Loan l WHERE l.returnDate IS NULL")
    List<Loan> findActiveLoans();

    // Trouver les emprunts pour un livre spécifique
    List<Loan> findByBookId(Long bookId);
    Loan getLoanById(Long loanId);

    @Query("SELECT COUNT(l) > 0 FROM Loan l WHERE l.book.id = :bookId AND l.returnDate IS NULL")
    boolean existsByBookIdAndReturnDateIsNull(@Param("bookId") Long bookId);

}
