package com.example.SpotifyAdapter.service.impl;

import com.example.SpotifyAdapter.domain.ResponseData;
import com.example.SpotifyAdapter.service.SpotifyApiService;
import com.example.SpotifyAdapter.util.StringUtil;
import com.fasterxml.jackson.core.JsonProcessingException;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;


@Service
public class SpotifyServiceImpl implements SpotifyApiService {




    private final RestTemplate restTemplate;


    private final String BASE_URL = "https://api.spotify.com/v1/";

    public SpotifyServiceImpl(final RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }


    @Override
    public ResponseEntity<?> searchSongByKey(final String token, final String songTitle, final String artistName) throws JsonProcessingException {


        System.out.println(token);
        System.out.println();
        final List<String> searchKeys = StringUtil.generateSearchKeys(songTitle,artistName);

        ResponseData resData = new ResponseData();

        List<Object> items = new ArrayList<>();
        for (String key:searchKeys) {
            System.out.println("********************************************");

            if (key == null || key.isEmpty()) {
                key = songTitle+artistName;
            }

            System.out.println("Searching for key: " +key);
            items = callSearchApi(key,token);
            resData = loopItems(items,artistName,songTitle,key);
            if(resData!=null){
                return  ResponseEntity.ok(resData);
            }
            System.out.println("********************************************");

        }

        return ResponseEntity.badRequest().build();
    }


    private ResponseData loopItems(final List<Object> items, final String artistName, final String songTitle, final String key){
        String songName;
        String songArtist;
        ResponseData  resData;

        for (int i = 0; i < items.size(); i++) {
            resData = processJson(items.get(i));

            songName = StringUtil.formatString(resData.getSongName());;
            songArtist  = StringUtil.formatString(resData.getArtistName());
            final String artistNameString = StringUtil.formatString(artistName);
            final String songTitleString = StringUtil.formatString(songTitle);

            songName = StringUtil.removeSpotifyKeys(songName);
            songArtist = StringUtil.removeSpotifyKeys( songArtist);

            System.out.println("");
            System.out.println("============================================");
            System.out.println("Song: "+songName);
            System.out.println("Artist: "+songArtist);
            System.out.println("============================================");
            System.out.println("");


            final boolean requestContains  = artistNameString.contains(songArtist) && songTitleString.contains(songName);
            final boolean responseContains = songArtist.contains(artistNameString) && songName.contains(songTitleString);


            if(requestContains || responseContains){
                System.out.println("Resutl:>>>>>>>>>>>>>>>>>>>>>>>>");
                System.out.println("Found" +key);
                System.out.println("Resutl:>>>>>>>>>>>>>>>>>>>>>>>>");
                return  resData;
            }

        }
        System.out.println("Resutl:>>>>>>>>>>>>>>>>>>>>>>>>");
        System.out.println("Nothing found" +key );
        System.out.println("Resutl:>>>>>>>>>>>>>>>>>>>>>>>>");

        return  null;

    }






    private List<Object>  callSearchApi(final String key, String token){


        final String url = BASE_URL + "search";
        final UriComponentsBuilder builder = UriComponentsBuilder.fromUriString(url)
                .queryParam("q", key)
                .queryParam("type", "track");

        final String finalUrl = builder.toUriString();


        System.out.println("Searching Music");
        System.out.println("URL "+finalUrl);


        final HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "Bearer " + token);
        final HttpEntity<Object> http = new HttpEntity<>(headers);
        // Making the request using RestTemplate
        final ResponseEntity<?> response =  restTemplate.exchange(
                finalUrl,
                HttpMethod.GET,
                http,
                Object.class  // Replace Object with the expected response type
        );

        final LinkedHashMap<String,Object> responseJson = (LinkedHashMap<String, Object>) response.getBody();
        final  LinkedHashMap<String,Object> tracks = (LinkedHashMap<String, Object>) responseJson.get("tracks");
        return  (List<Object>) tracks.get("items");

    }












    public ResponseData processJson(Object firstItem){

        LinkedHashMap<String,Object> songJson = (LinkedHashMap<String, Object>) firstItem;
        final   LinkedHashMap<String,Object> album = ( LinkedHashMap<String,Object>) songJson.get("album");
        final String releaseDate  = (String) album.get("release_date");
        final String albumName =(String) album.get("name");

        final List<Object> images = (List<Object>) album.get("images");
        final  LinkedHashMap<String,Object> firstImage = (LinkedHashMap<String, Object>) images.get(0);
        final String albumImage = (String) firstImage.get("url");

        final List<Object>artists = (List<Object>) songJson.get("artists");
        final LinkedHashMap<String,Object> firstArtist = (LinkedHashMap<String,Object>) artists.get(0);
        final String artistName = (String) firstArtist.get("name");
        final String songName = (String) songJson.get("name");
        final String id = (String) songJson.get("id");


        final ResponseData responseData = new ResponseData();
        responseData.setSongName(songName);
        responseData.setAlbumName(albumName);
        responseData.setAlbumCoverUrl(albumImage);
        responseData.setReleaseDate(releaseDate);
        responseData.setArtistName(artistName);
        responseData.setId(id);


        return responseData;
    }










}

