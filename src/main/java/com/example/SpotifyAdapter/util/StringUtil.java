package com.example.SpotifyAdapter.util;

import com.example.SpotifyAdapter.constant.YoutubeStrings;

import java.util.ArrayList;
import java.util.List;

public class StringUtil {

    public static String formatString(final String input){
        final String noSpaces = input.replaceAll("\\s", "");
        final String noSpecialChars = noSpaces.replaceAll("[^a-zA-Z0-9]", "");
        return noSpecialChars.toUpperCase();
    }

    public static String removeKeyWords(final String input){
         //capitalize fist letter and all all letter after spaces and "-"
        final StringBuilder result = new StringBuilder();
        boolean capitalizeNext = true;

        for (char ch : input.toCharArray()) {
            if (Character.isWhitespace(ch) || ch == '-') {
                capitalizeNext = true;
            } else if (capitalizeNext) {
                ch = Character.toTitleCase(ch);
                capitalizeNext = false;
            }

            result.append(ch);
        }
        String formattedString = result.toString();
        formattedString = formattedString.replaceAll("\\s", "");
        formattedString = formattedString.replaceAll("[^a-zA-Z0-9]", "");


        for (String key : YoutubeStrings.YOUTUBEKEYS) {
            formattedString = formattedString.replaceAll("(?i)" + key, "");
        }

        return formattedString;
    }

    public static List<String> generateSearchKeys(final String songTitle, final String artistName){
        List<String> searchKeys = new ArrayList<>();

        String secondKey = "";
        String thirdKey;
        String fourthKey;


        String key = songTitle.toUpperCase();
        final String formattedName = removeKeyWords(artistName);
        String formattedTitle = removeKeyWords(songTitle);
        if (formattedTitle.contains(formattedName)) {
            thirdKey = formattedTitle.replace(formattedName, "")+formattedName;
            fourthKey = formattedName+formattedTitle.replace(formattedName, "");
        }
        else{
            thirdKey = formattedTitle+formattedName;
            fourthKey =formattedName+formattedTitle;
        }

        if (!key.contains(artistName.toUpperCase())) {
            key = key + artistName;  // Add a space between songTitle and artistName

        }
        else {
            // Remove artistName from the middle and put it at the end
            int index = key.indexOf(artistName.toUpperCase());
            String firstPart = key.substring(0, index);
            String secondPart = key.substring(index + artistName.length());
            key = firstPart + secondPart + artistName;
            secondKey = artistName+firstPart + secondPart;

        }

        key = removeKeyWords(key);
        secondKey = removeKeyWords(secondKey);


        searchKeys.add(key);
        searchKeys.add(secondKey);
        searchKeys.add(thirdKey);
        searchKeys.add(fourthKey);


        return searchKeys;
    }


    public  static String removeSpotifyKeys(final String input){

        String formattedString = null;
        for (String key : YoutubeStrings.SPOTIFYKEYS) {
            formattedString = input.replaceAll("(?i)" + key, "");
        }

        return formattedString;
    }
}
