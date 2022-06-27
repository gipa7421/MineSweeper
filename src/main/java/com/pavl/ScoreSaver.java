package com.pavl;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;

public class ScoreSaver {

    public static void save(String time, String name) throws IOException {
        FileOutputStream fileOutputStream = new FileOutputStream("./src/main/resources/records.txt", true);
        String fullString = name + " " + time;
        fileOutputStream.write(fullString.getBytes());
        fileOutputStream.write(10);
    }

    public static List<String> get() throws IOException {
        FileInputStream fileInputStream = new FileInputStream("./src/main/resources/records.txt");
        List<String> result = new ArrayList<>();

        String completeString = "";
        while(true) {
            int output = fileInputStream.read();
            if(output == -1) {
                break;
            }
            completeString += (char) output;
        }

        StringTokenizer stringTokenizer = new StringTokenizer(completeString, "\n");
        while(stringTokenizer.hasMoreTokens()) {
            result.add(stringTokenizer.nextToken());
        }

        if(result.size() > 10) {
            return result.subList(0, 10);
        }else {
            return result;
        }
    }
}
