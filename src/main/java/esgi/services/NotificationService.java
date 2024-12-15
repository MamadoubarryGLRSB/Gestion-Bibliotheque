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
        Date now = new Date();

        for (Loan loan : activeLoans) {
            if (loan.getReturnDate() == null && isDueSoon(loan.getLoanDate(), now)) {
                UserObserver userObserver = new UserObserver(loan.getUser(), notificationRepository);
                userObserver.update("Reminder: Your loan for the book '" + loan.getBook().getTitle() +
                        "' is due soon.");
            }
        }
    }

    public void notifyLibrariansAboutNewLoan(Loan loan) {
        List<User> librarians = userRepository.findByRole(Role.LIBRARIAN);

        for (User librarian : librarians) {
            LibrarianObserver librarianObserver = new LibrarianObserver(librarian, notificationRepository);
            librarianObserver.update("New loan request for the book '" + loan.getBook().getTitle() + "'.");
        }
    }

    private boolean isDueSoon(Date loanDate, Date now) {
        long difference = now.getTime() - loanDate.getTime();
        System.out.println("Affiche moi la difference +++++++++++++++++++++++"+ difference);
        long days = difference / (1000 * 60 * 60 * 24);
        return days >= 2; // Loan is due soon if it’s been 13 days
    }
}
