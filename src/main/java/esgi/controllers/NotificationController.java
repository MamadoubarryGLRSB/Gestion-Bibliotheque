package esgi.controllers;

import esgi.models.Loan;
import esgi.models.Notification;
import esgi.repositories.LoanRepository;
import esgi.repositories.NotificationRepository;
import esgi.services.LoanService;
import esgi.services.NotificationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/notifications")
public class NotificationController {


    private NotificationRepository notificationRepository;
    private NotificationService notificationService;
    private LoanRepository loanRepository;



    public NotificationController(NotificationRepository notificationRepository, NotificationService notificationService, LoanRepository loanRepository) {
        this.notificationRepository = notificationRepository;
        this.notificationService = notificationService;
        this.loanRepository = loanRepository;
    }

    @GetMapping("/user/{userId}")
    public List<Notification> getNotificationsByUser(@PathVariable Integer userId) {
        return notificationRepository.findByUserId(userId);
    }

    @PostMapping("/remind")
    public String sendDueDateReminders() {
        notificationService.sendDueDateReminders();
        return "Deadline reminders sent successfully.";
    }

    @PostMapping("/new-loan/{loanId}")
    public String notifyLibrariansAboutNewLoan(@PathVariable Long loanId) {
        Loan loan = loanRepository.getLoanById(loanId);
        if (loan != null) {
            notificationService.notifyLibrariansAboutNewLoan(loan);
            return "Librarians have been notified of the new loan.";
        } else {
            return "The specified loan does not exist.";
        }
    }
}
