package esgi.services;
import esgi.models.Role;
import esgi.repositories.BookRepository;
import esgi.repositories.LoanRepository;
import esgi.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import esgi.dtos.LoanDTO;
import esgi.models.Book;
import esgi.models.Loan;
import esgi.models.User;
import org.springframework.stereotype.Service;
import java.util.stream.Collectors;
import java.time.LocalDateTime;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

@Service
public class LoanService {


    private LoanRepository loanRepository;


    private BookRepository bookRepository;


    private NotificationService notificationService;


    private UserRepository userRepository;

    public LoanService(LoanRepository loanRepository, BookRepository bookRepository, NotificationService notificationService, UserRepository userRepository) {
        this.loanRepository = loanRepository;
        this.bookRepository = bookRepository;
        this.notificationService = notificationService;
        this.userRepository = userRepository;
    }

    // Liste des emprunts pour un utilisateur
    public List<Loan> getLoansByUser(Integer userId) {
        return loanRepository.findByUserId(userId);
    }

    // Liste des emprunts actifs
    public List<Loan> getActiveLoans() {
        return loanRepository.findActiveLoans();
    }

    // Emprunter un livre
    public Loan borrowBook(Integer userId, Long bookId) {
        User user = userRepository.findById(userId).orElseThrow(() -> new RuntimeException("User not found"));
        Book book = bookRepository.findById(bookId).orElseThrow(() -> new RuntimeException("Book not found"));

        if (!book.isAvailability()) {
            throw new RuntimeException("Book is not available for loan");
        }
        if (loanRepository.existsByBookIdAndReturnDateIsNull(bookId)) {
            throw new RuntimeException("Book is already borrowed");
        }
        Loan loan = new Loan();
        loan.setUser(user);
        loan.setBook(book);
        loan.setLoanDate(LocalDateTime.now());
        loan.setStatus(Loan.LoanStatus.PENDING);
        loan.setPickupDate(LocalDateTime.now().plusDays(1));

        book.setAvailability(false);
        bookRepository.save(book);

        Loan savedLoan = loanRepository.save(loan);
        // Notifier les bibliothécaires
        notificationService.notifyLibrariansAboutNewLoan(savedLoan);

        return savedLoan;
    }

    public void checkForDueDateReminders() {
        notificationService.sendDueDateReminders();
    }

    // Retourner un livre
    public Loan returnBook(Long loanId) {
        Loan loan = loanRepository.findById(loanId).orElseThrow(() -> new RuntimeException("Loan not found"));

        if (loan.getReturnDate() != null) {
            throw new RuntimeException("Book has already been returned");
        }

        // Mettre à jour la date de retour
        loan.setReturnDate(LocalDateTime.now());

        // Marquer le livre comme disponible
        Book book = loan.getBook();
        book.setAvailability(true);
        bookRepository.save(book);

        return loanRepository.save(loan);
    }

    // Supprimer un emprunt
    public void deleteLoan(Long loanId) {
        Loan loan = loanRepository.findById(loanId).orElseThrow(() -> new RuntimeException("Loan not found"));

        // Marquer le livre comme disponible si l'emprunt est supprimé
        Book book = loan.getBook();
        book.setAvailability(true);
        bookRepository.save(book);

        loanRepository.deleteById(loanId);
    }

    public Loan approveLoanRequest(Long loanId, User librarian) {
        if (librarian.getRole() != Role.LIBRARIAN) {
            throw new RuntimeException("Only librarians can approve loan requests");
        }
        Loan loan = loanRepository.findById(loanId)
                .orElseThrow(() -> new RuntimeException("Loan request not found"));
        // Approve the loan (add your business logic here)
        return loanRepository.save(loan);
    }

    public List<LoanDTO> getPendingLoans() {
        // Exemple : Filtre les prêts avec un statut "En attente"
        return loanRepository.findAll().stream()
                .filter(loan -> loan.getStatus() == (Loan.LoanStatus.PENDING))
                .map(loan -> new LoanDTO(
                        loan.getId(),
                        loan.getBook().getTitle(),
                        loan.getUser().getName(),
                        loan.getLoanDate(),
                        loan.getStatus().name()
                ))
                .collect(Collectors.toList());
    }

}
