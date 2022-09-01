package com.example.coingame;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.BufferedReader;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;

public abstract class AbstractDataController<T> {

    protected static String readFileFromResources(Path path) throws IOException {

        StringBuilder sb = new StringBuilder();

        try {
            final BufferedReader r = Files.newBufferedReader(path, StandardCharsets.UTF_8);
            String str;
            while ((str = r.readLine()) != null) {
                sb.append(str);
            }
        } catch (IOException e) {
            throw new IOException("Can not read file with Buff reader " + path + "!");
        }
        return sb.toString();
    }

    protected <T> T[] parseJsonData (String result, Class<T> clazz) throws IOException {

        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        T[] t;


        if(clazz.isAssignableFrom(Coin.class)){
            t = (T[]) objectMapper.readValue(result, Coin[].class);
        } else if(clazz.isAssignableFrom(Credentials.class)) {
            t = (T[]) objectMapper.readValue(result, Credentials[].class);
        } else {
            t = null;
        }


//        t = (T[]) objectMapper.readValue(result, clazz.class);
        return t;
    }
}
