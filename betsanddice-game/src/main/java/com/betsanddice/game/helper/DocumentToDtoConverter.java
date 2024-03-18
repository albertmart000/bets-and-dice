package com.betsanddice.game.helper;

import com.betsanddice.game.document.CrapsGameDocument;
import com.betsanddice.game.document.GameDocument;
import com.betsanddice.game.dto.CrapsGameDto;
import com.betsanddice.game.dto.GameDto;
import org.modelmapper.AbstractConverter;
import org.modelmapper.Converter;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Flux;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Component
public class DocumentToDtoConverter<S,D> {

    static final DateTimeFormatter CUSTOM_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd hh:mm:ss");

    public D fromDocumentToDto(S document, Class<D> dtoClass){

        ModelMapper mapper = new ModelMapper();

        if(dtoClass.isAssignableFrom(CrapsGameDto.class)) {
            Converter<LocalDateTime, String> fromLocalDateTimeToString = new AbstractConverter<>() {
                @Override
                protected String convert(LocalDateTime creationDateFromDocument) {
                    return creationDateFromDocument.format(CUSTOM_FORMATTER);
                }
            };
            mapper.createTypeMap(CrapsGameDocument.class, CrapsGameDto.class)
                    .addMapping(CrapsGameDocument::getUuid, CrapsGameDto::setUuid);
            mapper.addConverter(fromLocalDateTimeToString);
        }

        if(dtoClass.isAssignableFrom(GameDto.class)) {
            mapper.createTypeMap(GameDocument.class, GameDto.class)
                    .addMapping(GameDocument::getUuid, GameDto::setUuid)
                    .addMapping(GameDocument::getTutorialId, GameDto::setTutorialId)
                    .addMapping(GameDocument::getStatId, GameDto::setStatId);
        }
        return mapper.map(document, dtoClass);
    }

    public Flux<D> fromDocumentFluxToDtoFlux(Flux<S> documentFlux, Class<D> dtoClass) {
        return documentFlux.map(document -> fromDocumentToDto(document, dtoClass));
    }

}
