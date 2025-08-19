package com.Alura.Literatura.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown = true)
public record DatosAutor(
        @JsonProperty("name") String name,
        @JsonProperty("birth_year") Integer nacimiento,
        @JsonProperty("death_year") Integer muerte
) {}
