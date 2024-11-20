package com.betsanddice.user.helper;

import com.betsanddice.user.document.UserDocument;
import com.betsanddice.user.dto.UserDto;
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
public class DocumentToDtoConverter <S,D>{

    static final DateTimeFormatter CUSTOM_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd");

    public D fromDocumentToDto(S document, Class<D> dtoClass) {

        ModelMapper mapper = new ModelMapper();

               if(dtoClass.isAssignableFrom(UserDto.class)) {
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

                   mapper.createTypeMap(UserDocument.class, UserDto.class)
                           .addMapping(UserDocument::getUuid, UserDto::setUuid);
                   mapper.addConverter(fromLocalDateTimeToString);
                   mapper.addConverter(fromLocalDateToString);
                   mapper.getConfiguration().setMatchingStrategy(MatchingStrategies.LOOSE);
               }
        return mapper.map(document, dtoClass);
    }

    public Flux<D> fromDocumentFluxToDtoFlux(Flux<S> documentFlux, Class<D> dtoClass) {
        return documentFlux.map(document -> fromDocumentToDto(document, dtoClass));
    }

}