package com.betsanddice.craps.helper;

import com.betsanddice.craps.document.CrapsGameDocument;
import com.betsanddice.craps.dto.CrapsGameDto;
import org.modelmapper.AbstractConverter;
import org.modelmapper.Converter;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Flux;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Component
public class CrapsGameDtoToDocumentConverter {

    //static final DateTimeFormatter CUSTOM_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd hh:mm:ss");

    public CrapsGameDocument fromDtoToDocument(CrapsGameDto dto) {

        ModelMapper mapper = new ModelMapper();
/*      Converter<LocalDateTime, String> fromLocalDateTimeToString = new AbstractConverter<>() {
            @Override
            protected String convert(LocalDateTime localDateTimeFromDocument) {
                return localDateTimeFromDocument.format(CUSTOM_FORMATTER);
            }
        };*/

        mapper.createTypeMap(CrapsGameDto.class, CrapsGameDocument.class)
                .addMapping(CrapsGameDto::getUuid, CrapsGameDocument::setUuid);
        //mapper.addConverter(fromLocalDateTimeToString);

        return mapper.map(dto, CrapsGameDocument.class);
    }

    public Flux<CrapsGameDocument> fromDtoFluxToDocumentFlux(Flux<CrapsGameDto> dtoFlux) {
        return dtoFlux.map(this::fromDtoToDocument);
    }

}
