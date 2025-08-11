package com.example.literalura.model;

import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
public record Datos(
        @JsonAlias("count") Integer count,
        @JsonAlias("next") String siguiente,
        @JsonAlias("previous") String previo,
        @JsonAlias("results") List<DatosLibro> resultado
) {}