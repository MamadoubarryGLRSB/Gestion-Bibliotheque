package esgi.models;
import esgi.models.Library;

import jakarta.persistence.*;
import lombok.*;

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
    private String isbn;

    @Column(nullable = false)
    private String title;

    @Column(nullable = false)
    private String author;

    @Column(name = "image", nullable = true)
    private String image;

    private String yearOfPublication;

    private String publisher;

    private String genre;


    @Column(name = "image_url_s")
    private String imageUrlS;

    @Column(name = "image_url_m")
    private String imageUrlM;

    @Column(name = "image_url_l")
    private String imageUrlL;

    @Column(nullable = false)
    private boolean availability = true;

    @ManyToOne // Relation avec la bibliothèque
    @JoinColumn(name = "id_library", nullable = true)
    private Library library;
}
