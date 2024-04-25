package com.barsha.monopolygame.Constants;

import java.util.Collections;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;

public class CommonFunction {
    public static String GenerateRandomString(int length){
        String upperCase = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
        String lowerCase = "abcdefghijklmnopqrstuvwxyz";
        String totalChars = upperCase + lowerCase;
        Random random = new Random();
        String randomString = "";

        // Include at least one of each character type
        randomString += upperCase.charAt(random.nextInt(upperCase.length()));
        randomString += lowerCase.charAt(random.nextInt(lowerCase.length()));
        

        for (int i = 0; i < length - 4; i++) {
            randomString += totalChars.charAt(random.nextInt(totalChars.length()));
        }

        return shuffleString(randomString);    
    }

    private static String shuffleString(String input) {
        List<Character> characters = input.chars().mapToObj(c -> (char) c).collect(Collectors.toList());
        Collections.shuffle(characters);
        return characters.stream().map(Object::toString).collect(Collectors.joining());
    }

    public static int GenerateRandomNumber(int min, int max) {
        Random  random = new Random();
        int     number = random.nextInt(max - min) + min;
        return number;
    }
}
