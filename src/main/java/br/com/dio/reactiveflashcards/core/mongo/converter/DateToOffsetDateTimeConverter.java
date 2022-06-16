package br.com.dio.reactiveflashcards.core.mongo.converter;

import org.springframework.core.convert.converter.Converter;

import java.time.OffsetDateTime;
import java.util.Date;

public class DateToOffsetDateTimeConverter implements Converter<OffsetDateTime, Date> {

    @Override
    public Date convert(final OffsetDateTime source) {
        return Date.from(source.toInstant());
    }

}
