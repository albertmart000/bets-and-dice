package com.betsanddice.craps.helper;

import com.betsanddice.craps.document.CrapsGameDocument;
import com.betsanddice.craps.dto.CrapsGameDto;
import com.betsanddice.craps.dto.DiceRollDto;
import org.modelmapper.AbstractConverter;
import org.modelmapper.Converter;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Flux;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

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
            Converter<List<DiceRollDto>, Integer> diceRollsListToAttempts = c -> c.getSource().size();

            mapper.createTypeMap(CrapsGameDocument.class, CrapsGameDto.class)
                    .addMapping(CrapsGameDocument::getUuid, CrapsGameDto::setUuid)
                    .addMappings(mapperListToSize -> mapperListToSize.using(diceRollsListToAttempts)
                            .map(CrapsGameDocument::getDiceRollsList, CrapsGameDto::setAttempts));
            mapper.addConverter(fromLocalDateTimeToString);
        }

        return mapper.map(document, dtoClass);
    }

    public Flux<D> fromDocumentFluxToDtoFlux(Flux<S> documentFlux, Class<D> dtoClass) {
        return documentFlux.map(document -> fromDocumentToDto(document, dtoClass));
    }

}
