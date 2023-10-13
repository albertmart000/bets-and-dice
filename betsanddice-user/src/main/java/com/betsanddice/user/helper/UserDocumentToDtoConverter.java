package com.betsanddice.user.helper;

import com.betsanddice.user.document.UserDocument;
import com.betsanddice.user.dto.UserDto;
import com.betsanddice.user.exception.ConverterException;
import org.modelmapper.AbstractConverter;
import org.modelmapper.Converter;
import org.modelmapper.ModelMapper;
import org.modelmapper.convention.MatchingStrategies;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Flux;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Component
public class UserDocumentToDtoConverter {

    static final DateTimeFormatter CUSTOM_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd");

    public UserDto convertDocumentToDto(UserDocument document) throws ConverterException {

        ModelMapper mapper = new ModelMapper();
        Converter<LocalDateTime, String> fromLocalDateTimeToString = new AbstractConverter<>() {
            @Override
            protected String convert(LocalDateTime localDateTimeFromDocument) {
                return localDateTimeFromDocument.format(CUSTOM_FORMATTER);
            }
        };
        Converter<LocalDate, String> fromLocalDateToString = new AbstractConverter<>() {
            @Override
            protected String convert(LocalDate localDateFromDocument) {
                return localDateFromDocument.format(CUSTOM_FORMATTER);
            }
        };

        mapper.createTypeMap(UserDocument.class, UserDto.class);
        mapper.addConverter(fromLocalDateTimeToString);
        mapper.addConverter(fromLocalDateToString);
        mapper.getConfiguration().setMatchingStrategy(MatchingStrategies.LOOSE);
        return mapper.map(document, UserDto.class);
    }

    public Flux<UserDto> convertDocumentFluxToDtoFlux(Flux<UserDocument> documentFlux) {
        return documentFlux.map(this::convertDocumentToDto);
    }

}