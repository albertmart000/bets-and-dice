package com.betsanddice.user.helper;

import com.betsanddice.user.document.UserDocument;
import com.betsanddice.user.dto.UserDto;
import com.betsanddice.user.exception.ConverterException;
import org.modelmapper.AbstractConverter;
import org.modelmapper.Converter;
import org.modelmapper.ModelMapper;
import org.modelmapper.convention.MatchingStrategies;
import reactor.core.publisher.Flux;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class UserDocumentToDtoConverter{

    static final DateTimeFormatter CUSTOM_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd");

    public UserDto convertDocumentToDto(UserDocument document) throws ConverterException {

        ModelMapper mapper = new ModelMapper();
        Converter<LocalDateTime, String> fromLocalDateTimeToString = new AbstractConverter<>() {
            @Override
            protected String convert(LocalDateTime creationDateFromDocument) {
                return creationDateFromDocument.format(CUSTOM_FORMATTER);
            }
        };

        mapper.createTypeMap(UserDocument.class, UserDto.class);
        mapper.addConverter(fromLocalDateTimeToString);
        mapper.getConfiguration().setMatchingStrategy(MatchingStrategies.LOOSE);
        return mapper.map(document, UserDto.class);
    }

    public Flux<UserDto> convertDocumentFluxToDtoFlux(Flux<UserDocument> documentFlux) {
        return documentFlux.map(this::convertDocumentToDto);
    }

}