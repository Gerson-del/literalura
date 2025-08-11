package com.example.literalura.service;

import com.example.literalura.model.Libro;
import com.example.literalura.repository.LibroRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
public class LibroService {
    private final LibroRepository libroRepository;

    @Autowired
    public LibroService(LibroRepository libroRepository) {
        this.libroRepository = libroRepository;
    }

    @Transactional
    public Libro guardarLibro(Libro libro) {
        return libroRepository.save(libro);
    }

    @Transactional(readOnly = true)
    public List<Libro> listarTodosLosLibros() {
        return libroRepository.findAll();
    }

    @Transactional(readOnly = true)
    public Optional<Libro> buscarPorTitulo(String titulo) {
        return libroRepository.findByTituloContainingIgnoreCase(titulo);
    }

    @Transactional(readOnly = true)
    public List<Libro> buscarPorIdioma(String idioma) {
        return libroRepository.findByContieneIdioma(idioma.toLowerCase());
    }
}