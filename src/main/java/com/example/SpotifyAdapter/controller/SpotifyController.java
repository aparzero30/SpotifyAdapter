package com.example.SpotifyAdapter.controller;


import com.example.SpotifyAdapter.domain.Request;
import com.example.SpotifyAdapter.service.SpotifyApiService;
import com.fasterxml.jackson.core.JsonProcessingException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;

@RestController
public class SpotifyController {


    private final SpotifyApiService service;

    public SpotifyController(SpotifyApiService service) {
        this.service = service;
    }

    @PostMapping("/searchSongByKey")
    public ResponseEntity<?> searchByKey(@RequestBody Request request) throws IOException {

        return service.searchSongByKey(request.getToken(), request.getSongTitle(), request.getArtistName());
    }
}
