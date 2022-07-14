package poc.Entity;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;

import javax.persistence.*;
import java.util.Set;

@Entity
@JsonIdentityInfo(
        generator = ObjectIdGenerators.PropertyGenerator.class,
        property = "id")
public class Book {

    @Id
    @GeneratedValue
    private Long id;

    @Column(nullable=false)
    private String title;

    @ManyToMany(fetch = FetchType.LAZY,
            cascade = {
                    CascadeType.PERSIST,
                    CascadeType.MERGE
            })
    private Set<Author> authors;

    private String isbn;

    private int year;

    private double price;

    private String genre;

    public Book() {
    }

    public Book(String title, String isbn, int year, double price, String genre) {
        this.title = title;
        this.isbn = isbn;
        this.year = year;
        this.price = price;
        this.genre = genre;
    }

    public Book(Long id, String title, String isbn, int year, double price, String genre) {
        this.id = id;
        this.title = title;
        this.isbn = isbn;
        this.year = year;
        this.price = price;
        this.genre = genre;
    }

    public Book(Long id, String title, Set<Author> authors, String isbn, int year, double price, String genre) {
        this.id = id;
        this.title = title;
        this.authors = authors;
        this.isbn = isbn;
        this.year = year;
        this.price = price;
        this.genre = genre;
    }

    public Long getId() {
        return id;
    }
    public void setId(Long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Set<Author> getAuthors() {
        return authors;
    }

    public void setAuthors(Set<Author> authors) {
        this.authors = authors;
    }

    public void addAuthor(Author author) {
        this.authors.add(author);
        author.getBooks().add(this);
    }

    public void removeAuthor(long authorId) {
        Author author = this.authors.stream().filter(a -> a.getId() == authorId).findFirst().orElse(null);
        if (author != null) {
            this.authors.remove(author);
            author.getBooks().remove(this);
        }
    }

    public String getIsbn() {
        return isbn;
    }

    public void setIsbn(String isbn) {
        this.isbn = isbn;
    }

    public int getYear() {
        return year;
    }

    public void setYear(int year) {
        this.year = year;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public String getGenre() {
        return genre;
    }

    public void setGenre(String genre) {
        this.genre = genre;
    }

    @Override
    public String toString() {
        return "Book{" +
                "id=" + id +
                ", title='" + title + '\'' +
                ", author='" + authors.toString() + '\'' +
                '}';
    }
}
