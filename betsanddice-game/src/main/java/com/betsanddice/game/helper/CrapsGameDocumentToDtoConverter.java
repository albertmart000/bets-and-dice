package com.betsanddice.game.helper;

import com.betsanddice.game.document.CrapsGameDocument;
import com.betsanddice.game.dto.CrapsGameDto;
import org.modelmapper.AbstractConverter;
import org.modelmapper.Converter;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Flux;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Component
public class CrapsGameDocumentToDtoConverter{

    static final DateTimeFormatter CUSTOM_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd hh:mm:ss");

    public CrapsGameDto fromDocumentToDto(CrapsGameDocument document) {

        ModelMapper mapper = new ModelMapper();
        Converter<LocalDateTime, String> fromLocalDateTimeToString = new AbstractConverter<>() {
            @Override
            protected String convert(LocalDateTime localDateTimeFromDocument) {
                return localDateTimeFromDocument.format(CUSTOM_FORMATTER);
            }
        };

        mapper.createTypeMap(CrapsGameDocument.class, CrapsGameDto.class)
                .addMapping(CrapsGameDocument::getUuid, CrapsGameDto::setUuid);
        mapper.addConverter(fromLocalDateTimeToString);
        return mapper.map(document, CrapsGameDto.class);
    }

    public Flux<CrapsGameDto> fromDocumentFluxToDtoFlux(Flux<CrapsGameDocument> documentFlux) {
        return documentFlux.map(this::fromDocumentToDto);
    }

}