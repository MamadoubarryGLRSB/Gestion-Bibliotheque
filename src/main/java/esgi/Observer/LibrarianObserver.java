package esgi.Observer;

import esgi.models.Notification;
import esgi.models.User;
import esgi.repositories.NotificationRepository;
import org.springframework.beans.factory.annotation.Autowired;

public class LibrarianObserver implements Observer {

    private User librarian;


    private NotificationRepository notificationRepository;

    public LibrarianObserver(User librarian, NotificationRepository notificationRepository) {
        this.librarian = librarian;
        this.notificationRepository = notificationRepository;
    }

    @Override
    public void update(String message) {
        Notification notification = new Notification();
        notification.setMessage(message);
        notification.setUser(librarian);
        notificationRepository.save(notification);
    }
}
