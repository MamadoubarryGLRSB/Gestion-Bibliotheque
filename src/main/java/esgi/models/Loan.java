package esgi.models;

import jakarta.persistence.*;
import lombok.*;
import esgi.models.User;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "loan")
public class Loan {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String borrower;

    @Column(nullable = false)
    private String date;

    @ManyToOne
    @JoinColumn(name = "id_book", nullable = false)
    private Book book;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;
}
