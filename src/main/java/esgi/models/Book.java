package esgi.models;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@Entity
@Getter
@Setter
@Data
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "book")
public class Book {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String title;
    private String author;
    private String genre;
    private String image;
    private boolean availability;

    @ManyToOne
    @JoinColumn(name = "id_library", nullable = false)
    private Library library;

    @OneToMany(mappedBy = "book")
    @JsonBackReference // Pour éviter la sérialisation infinie
    private List<Loan> loans;
}
