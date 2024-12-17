package esgi.models;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "book")
public class Book {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String title;

    private String author;

    private String genre;

    private String image;

    @Column(nullable = false)
    private boolean availability;

    @ManyToOne
    @JoinColumn(name = "id_library", nullable = false)
    @JsonBackReference(value = "books-library")
    private Library library;

    @OneToMany(mappedBy = "book", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JsonBackReference(value = "book-loans")
    private List<Loan> loans;
}
