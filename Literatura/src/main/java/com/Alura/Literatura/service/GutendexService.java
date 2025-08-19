package com.Alura.Literatura.service;

import com.Alura.Literatura.model.DatosBusqueda;
import com.Alura.Literatura.model.DatosLibro;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.Collections;

public class GutendexService {

    private static final String BASE = "https://gutendex.com/books/";
    private final ConsumoApi http;
    private final IConvierteDatos conv;

    public GutendexService(ConsumoApi http, IConvierteDatos conv) {
        this.http = http;
        this.conv = conv;
    }

    private DatosBusqueda get(String url) {
        try {
            String json = http.obtenerDatos(url);
            return conv.obtenerDatos(json, DatosBusqueda.class);
        } catch (Exception e) {
            System.out.println("Error consultando Gutendex: " + e.getClass().getSimpleName() + " -> " + e.getMessage());
            return new DatosBusqueda(0, null, null, Collections.emptyList());
        }
    }

    public DatosBusqueda listarPaginaInicial() {
        return get(BASE);
    }

    public DatosBusqueda buscarLibrosPorTitulo(String titulo) {
        String t = titulo == null ? "" : titulo.trim();
        if (t.isEmpty()) return listarPaginaInicial();
        return get(BASE + "?search=" + URLEncoder.encode(t, StandardCharsets.UTF_8));
    }

    public DatosBusqueda buscarLibrosPorIdioma(String idioma) {
        String lang = URLEncoder.encode(idioma.trim(), StandardCharsets.UTF_8);
        return get(BASE + "?languages=" + lang);
    }

    public DatosLibro buscarLibroPorId(int id) {
        var d = get(BASE + "?ids=" + id);
        return (d.results() != null && !d.results().isEmpty()) ? d.results().get(0) : null;
    }
}