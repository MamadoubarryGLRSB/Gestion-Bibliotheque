package esgi.models;

import java.util.List;

public class OpenLibraryBookDTO {
    private String title;
    private List<String> author_name;
    private List<String> isbn;
    private Integer cover_i;
    private List<Integer> publish_year;

    // Getters et setters
    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }
    public List<String> getAuthor_name() { return author_name; }
    public void setAuthor_name(List<String> author_name) { this.author_name = author_name; }
    public List<String> getIsbn() { return isbn; }
    public void setIsbn(List<String> isbn) { this.isbn = isbn; }
    public Integer getCover_i() { return cover_i; }
    public void setCover_i(Integer cover_i) { this.cover_i = cover_i; }
    public List<Integer> getPublish_year() { return publish_year; }
    public void setPublish_year(List<Integer> publish_year) { this.publish_year = publish_year; }
}
