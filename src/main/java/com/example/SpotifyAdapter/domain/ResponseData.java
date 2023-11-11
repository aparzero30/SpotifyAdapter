package com.example.SpotifyAdapter.domain;

import lombok.Builder;
import lombok.Data;

@Data
public class ResponseData {


    private String artistName;
    private String albumName;
    private String albumCoverUrl;
    private String songName;
    private String releaseDate;
    private String id;

}
