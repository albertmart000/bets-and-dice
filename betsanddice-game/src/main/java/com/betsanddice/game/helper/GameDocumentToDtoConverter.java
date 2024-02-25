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
public class GameDocumentToDtoConverter {

    static final DateTimeFormatter CUSTOM_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd hh:mm:ss");

    public GameDto fromDocumentToDto(GameDocument document) {

        ModelMapper mapper = new ModelMapper();

        mapper.createTypeMap(GameDocument.class, GameDto.class)
                .addMapping(GameDocument::getUuid, GameDto::setUuid)
                .addMapping(GameDocument::getTutorialId, GameDto::setTutorialId)
                .addMapping(GameDocument::getStatId, GameDto::setStatId);

        return mapper.map(document, GameDto.class);
    }

    public Flux<GameDto> fromDocumentFluxToDtoFlux(Flux<GameDocument> documentFlux) {
        return documentFlux.map(this::fromDocumentToDto);
    }

}