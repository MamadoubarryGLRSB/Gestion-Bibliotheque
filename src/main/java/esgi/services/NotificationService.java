package esgi.services;

import esgi.Observer.LibrarianObserver;
import esgi.Observer.UserObserver;
import esgi.models.Loan;
import esgi.models.Role;
import esgi.models.User;
import esgi.repositories.LoanRepository;
import esgi.repositories.NotificationRepository;
import esgi.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.time.ZoneId;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.Date;
import java.util.List;

@Service
public class NotificationService {

    private LoanRepository loanRepository;

    public NotificationService(LoanRepository loanRepository, UserRepository userRepository, NotificationRepository notificationRepository) {
        this.loanRepository = loanRepository;
        this.userRepository = userRepository;
        this.notificationRepository = notificationRepository;
    }

    private UserRepository userRepository;


    private NotificationRepository notificationRepository;

    public void sendDueDateReminders() {
        List<Loan> activeLoans = loanRepository.findActiveLoans();
        LocalDateTime now = LocalDateTime.now();

        for (Loan loan : activeLoans) {
            if (loan.getReturnDate() == null && isDueSoon(loan.getLoanDate(), convertToDate(now))) {
                UserObserver userObserver = new UserObserver(loan.getUser(), notificationRepository);
                userObserver.update("Reminder: Your loan for the book '" + loan.getBook().getTitle() +
                        "' is due soon.");
            }
        }
    }

    private Date convertToDate(LocalDateTime localDateTime) {
        return Date.from(localDateTime.atZone(ZoneId.systemDefault()).toInstant());
    }

    public void notifyLibrariansAboutNewLoan(Loan loan) {
        List<User> librarians = userRepository.findByRole(Role.LIBRARIAN);

        for (User librarian : librarians) {
            LibrarianObserver librarianObserver = new LibrarianObserver(librarian, notificationRepository);
            librarianObserver.update("New loan request for the book '" + loan.getBook().getTitle() + "'.");
        }
    }



    private boolean isDueSoon(LocalDateTime loanDate, Date now) {
        // Conversion de Date en LocalDateTime pour assurer la cohérence
        LocalDateTime nowLocalDateTime = now.toInstant().atZone(java.time.ZoneId.systemDefault()).toLocalDateTime();

        // Calcul de la différence en jours
        long days = ChronoUnit.DAYS.between(loanDate, nowLocalDateTime);
        System.out.println("Affiche moi la différence : " + days + " jours");

        return days >= 13;
    }

    public void notifyUserAboutLoanStatus(Loan loan) {
        String message = loan.getStatus() == Loan.LoanStatus.APPROVED
                ? "Votre demande d'emprunt pour le livre '" + loan.getBook().getTitle() +
                "' a été approuvée. Vous pouvez récupérer le livre à partir du " + loan.getPickupDate()
                : "Votre demande d'emprunt pour le livre '" + loan.getBook().getTitle() + "' a été rejetée.";

        UserObserver userObserver = new UserObserver(loan.getUser(), notificationRepository);
        userObserver.update(message);
    }
}
