package com.betsanddice.game.helper;

import com.betsanddice.game.document.GameDocument;
import com.betsanddice.game.dto.GameDto;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Flux;

@Component
public class GameDocumentToDtoConverter {

    public GameDto fromDocumentToDto(GameDocument document) {

        ModelMapper mapper = new ModelMapper();

        mapper.createTypeMap(GameDocument.class, GameDto.class)
                .addMapping(GameDocument::getUuid, GameDto::setUuid)
                .addMapping(GameDocument::getTutorialId, GameDto::setGameTutorialDto)
                .addMapping(GameDocument::getStatId, GameDto::setStatId);

        return mapper.map(document, GameDto.class);
    }

    public Flux<GameDto> fromDocumentFluxToDtoFlux(Flux<GameDocument> documentFlux) {
        return documentFlux.map(this::fromDocumentToDto);
    }

}