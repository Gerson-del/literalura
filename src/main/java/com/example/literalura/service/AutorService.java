package com.example.literalura.service;

import com.example.literalura.model.Autor;
import com.example.literalura.repository.AutorRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
public class AutorService {
    private final AutorRepository autorRepository;

    @Autowired
    public AutorService(AutorRepository autorRepository) {
        this.autorRepository = autorRepository;
    }

    @Transactional
    public Autor guardarAutor(Autor autor) {
        return autorRepository.save(autor);
    }

    @Transactional(readOnly = true)
    public List<Autor> listarTodosLosAutores() {
        return autorRepository.findAll();
    }

    @Transactional(readOnly = true)
    public List<Autor> listarAutoresVivosEnAnio(Integer anio) {
        return autorRepository.findAutoresVivosEnAnio(anio);
    }

    @Transactional(readOnly = true)
    public Optional<Autor> buscarPorNombre(String nombre) {
        return autorRepository.findByNombreContainingIgnoreCase(nombre);
    }
}