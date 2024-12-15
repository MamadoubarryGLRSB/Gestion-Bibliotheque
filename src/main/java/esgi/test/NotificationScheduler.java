package esgi.test;

import esgi.services.LoanService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
public class NotificationScheduler {

    @Autowired
    private LoanService loanService;

    @Scheduled(cron = "0 0 8 * * ?") // Chaque jour à 8h
    public void sendDueDateReminders() {
        loanService.checkForDueDateReminders();
    }
}
