package com.example.SpotifyAdapter.domain;


import lombok.Data;

@Data
public class Request {

    private String token;
    private String songTitle;
    private String artistName;
}
