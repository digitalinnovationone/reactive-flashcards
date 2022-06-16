package br.com.dio.reactiveflashcards.core.mongo.converter;

import org.springframework.core.convert.converter.Converter;

import java.time.OffsetDateTime;
import java.time.ZoneId;
import java.util.Date;

public class OffsetDateTimeToDateConverter implements Converter<Date, OffsetDateTime> {

    @Override
    public OffsetDateTime convert(final Date source) {
        return OffsetDateTime.ofInstant(source.toInstant(), ZoneId.systemDefault());
    }

}
