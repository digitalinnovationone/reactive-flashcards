package br.com.dio.reactiveflashcards.core.factorybot;

import com.github.javafaker.Faker;

import java.util.List;
import java.util.Locale;
import java.util.Random;

public class RandomData {

    private static final Faker faker = new Faker(new Locale("pt", "BR"));

    public static Faker getFaker(){
        return faker;
    }

    public static <T extends Enum<?>>T randomEnum(final Class<T> clazz){
        var x = new Random().nextInt(clazz.getEnumConstants().length);
        return clazz.getEnumConstants()[x];
    }

    public static <T extends Enum<?>>T randomEnum(final Class<T> clazz, final List<Integer> blackList){
        var x = 0;
        do {
           x = new Random().nextInt(clazz.getEnumConstants().length);
        }while (blackList.contains(x));
        return clazz.getEnumConstants()[x];
    }

}
