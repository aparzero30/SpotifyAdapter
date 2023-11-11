package com.example.SpotifyAdapter.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import org.springframework.http.ResponseEntity;

public interface SpotifyApiService {
    ResponseEntity<?> searchSongByKey(final String token, final String songTitle, final String artistName) throws JsonProcessingException;
}
