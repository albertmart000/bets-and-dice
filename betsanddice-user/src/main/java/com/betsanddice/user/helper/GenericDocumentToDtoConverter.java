package com.betsanddice.user.helper;

import com.betsanddice.user.document.UserDocument;
import com.betsanddice.user.dto.UserDto;
import org.modelmapper.AbstractConverter;
import org.modelmapper.Converter;
import org.modelmapper.ModelMapper;
import org.modelmapper.convention.MatchingStrategies;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class GenericDocumentToDtoConverter<D, S> {


    static final DateTimeFormatter CUSTOM_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd");

    public D convertDocumentToDto(S document, Class<D> dtoClass) {

        ModelMapper mapper = new ModelMapper();
        Converter<LocalDateTime, String> fromLocalDateTimeToString = new AbstractConverter<>() {
            @Override
            protected String convert(LocalDateTime creationDateFromDocument) {
                return creationDateFromDocument.format(CUSTOM_FORMATTER);
            }
        };
        if (dtoClass.isAssignableFrom(UserDto.class)) {
            mapper.createTypeMap(UserDocument.class, UserDto.class);
            mapper.addConverter(fromLocalDateTimeToString);
            mapper.getConfiguration().setMatchingStrategy(MatchingStrategies.LOOSE);
        }
        return mapper.map(document, dtoClass);
    }

}
