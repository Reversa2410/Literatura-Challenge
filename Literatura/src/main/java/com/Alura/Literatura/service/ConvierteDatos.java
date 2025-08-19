package com.Alura.Literatura.service;

import com.fasterxml.jackson.databind.ObjectMapper;

public class ConvierteDatos implements IConvierteDatos {
    private final ObjectMapper mapper;

    public ConvierteDatos() {
        this.mapper = new ObjectMapper();
        this.mapper.findAndRegisterModules(); // soporte extra si es necesario
    }

    @Override
    public <T> T obtenerDatos(String json, Class<T> clase) {
        try {
            return mapper.readValue(json, clase);
        } catch (Exception e) {
            throw new RuntimeException("Error convirtiendo JSON a " + clase.getSimpleName(), e);
        }
    }
}



