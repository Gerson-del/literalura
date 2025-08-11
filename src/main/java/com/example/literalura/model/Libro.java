package com.example.literalura.model;

import jakarta.persistence.*;
import java.util.List;

@Entity
@Table(name = "libros")
public class Libro {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String titulo;
    private Integer numeroDeDescargas;

    @ElementCollection(fetch = FetchType.EAGER)
    @CollectionTable(name = "libro_idiomas", joinColumns = @JoinColumn(name = "libro_id"))
    @Column(name = "idioma")
    private List<String> idiomas;

    @ManyToOne(fetch = FetchType.EAGER)
    private Autor autor;

    // Constructores
    public Libro() {}

    public Libro(String titulo, List<String> idiomas, Integer numeroDeDescargas, Autor autor) {
        this.titulo = titulo;
        this.idiomas = idiomas;
        this.numeroDeDescargas = numeroDeDescargas;
        this.autor = autor;
    }

    // Getters y Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getTitulo() { return titulo; }
    public void setTitulo(String titulo) { this.titulo = titulo; }
    public List<String> getIdiomas() { return idiomas; }
    public void setIdiomas(List<String> idiomas) { this.idiomas = idiomas; }
    public Integer getNumeroDeDescargas() { return numeroDeDescargas; }
    public void setNumeroDeDescargas(Integer numeroDeDescargas) { this.numeroDeDescargas = numeroDeDescargas; }
    public Autor getAutor() { return autor; }
    public void setAutor(Autor autor) { this.autor = autor; }

    @Override
    public String toString() {
        return "Libro: " + titulo +
                "\nAutor: " + (autor != null ? autor.getNombre() : "Desconocido") +
                "\nIdiomas: " + String.join(", ", idiomas) +
                "\nDescargas: " + numeroDeDescargas + "\n";
    }
}