package esgi.models;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

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

    @Column(name = "loan_date", nullable = false)
    @Temporal(TemporalType.TIMESTAMP)
    private LocalDateTime loanDate;

    @Column(name = "return_date")
    @Temporal(TemporalType.TIMESTAMP)
    private LocalDateTime returnDate;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private LoanStatus status;

    @Column(name = "pickup_date")
    @Temporal(TemporalType.TIMESTAMP)
    private LocalDateTime pickupDate;


    @ManyToOne
    @JoinColumn(name = "id_user", nullable = false)
    @JsonBackReference(value = "loans-user")
    private User user;

    @ManyToOne
    @JoinColumn(name = "id_book", nullable = false)
    @JsonIgnoreProperties(value = "loans-book")
    private Book book;

    public enum LoanStatus {
        PENDING, APPROVED, REJECTED
    }
}
