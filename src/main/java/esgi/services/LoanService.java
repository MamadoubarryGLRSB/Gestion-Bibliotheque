package esgi.services;

import esgi.enums.LoanStatus;
import esgi.models.Book;
import esgi.models.Loan;
import esgi.models.User;
import esgi.repositories.BookRepository;
import esgi.repositories.LoanRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;

@Service
public class LoanService {

    @Autowired
    private LoanRepository loanRepository;

    @Autowired
    private BookRepository bookRepository;

    public Loan createLoanRequest(int userId, Long bookId) {
        Book book = bookRepository.findById(bookId)
                .orElseThrow(() -> new RuntimeException("Livre non trouvé"));

        if (!book.isAvailability()) {
            throw new RuntimeException("Le livre n'est pas disponible");
        }

        Loan loan = new Loan();
        loan.setUser(new User(userId));
        loan.setBook(book);
        loan.setBorrowDate(LocalDate.now().toString());
        loan.setStatus(LoanStatus.PENDING);

        return loanRepository.save(loan);
    }

    public Loan approveLoan(Long loanId) {
        Loan loan = loanRepository.findById(loanId)
                .orElseThrow(() -> new RuntimeException("Emprunt non trouvé"));

        loan.setStatus(LoanStatus.APPROVED);
        loan.getBook().setAvailability(false); // Livre non disponible
        return loanRepository.save(loan);
    }

    public Loan returnLoan(Long loanId) {
        Loan loan = loanRepository.findById(loanId)
                .orElseThrow(() -> new RuntimeException("Emprunt non trouvé"));

        loan.setStatus(LoanStatus.RETURNED);
        loan.setReturnDate(LocalDate.now().toString());
        loan.getBook().setAvailability(true); // Livre de nouveau disponible
        return loanRepository.save(loan);
    }
}
