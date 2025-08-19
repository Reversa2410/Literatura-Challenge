package com.Alura.Literatura.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
public record DatosLibro(
        @JsonProperty("id") Integer id,
        @JsonProperty("title") String titulo,
        @JsonProperty("authors") List<DatosAutor> autores,
        @JsonProperty("languages") List<String> lenguajes,
        @JsonProperty("download_count") Integer cantidadDescargas
) {}