package br.com.dio.reactiveflashcards.utils;

import lombok.NoArgsConstructor;
import org.apache.commons.io.FileUtils;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.util.ResourceUtils;

import java.io.File;
import java.io.FileNotFoundException;
import java.nio.charset.StandardCharsets;

import static lombok.AccessLevel.PRIVATE;

@NoArgsConstructor(access = PRIVATE)
public class MockWebServerUtils {

    public static String getSimpleJson(final String fileName){
        try{
            return new JSONObject(FileUtils.readFileToString(getJsonFile(fileName), StandardCharsets.UTF_8)).toString();
        }catch (final Exception e){
            throw new RuntimeException(e);
        }
    }

    public static String getListJson(final String fileName){
        try{
            return new JSONArray(FileUtils.readFileToString(getJsonFile(fileName), StandardCharsets.UTF_8)).toString();
        }catch (final Exception e){
            throw new RuntimeException(e);
        }
    }

    private static File getJsonFile(final String fileName) throws FileNotFoundException {
        return ResourceUtils.getFile(String.format("%s%s/%s.json", ResourceUtils.CLASSPATH_URL_PREFIX, "json", fileName));
    }

}
