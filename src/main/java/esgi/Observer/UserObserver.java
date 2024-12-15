package esgi.Observer;

import esgi.models.Notification;
import esgi.models.User;
import esgi.repositories.NotificationRepository;
import org.springframework.beans.factory.annotation.Autowired;

public class UserObserver implements Observer{

    private User user;

    private NotificationRepository notificationRepository;

    @Autowired
    public UserObserver(User user, NotificationRepository notificationRepository) {
        this.user = user;
        this.notificationRepository = notificationRepository;
    }

    @Override
    public void update(String message) {
        Notification notification = new Notification();
        notification.setMessage(message);
        notification.setUser(user);
        notificationRepository.save(notification);
    }
}
