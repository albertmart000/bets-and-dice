package com.betsanddice.tutorial.helper;

import com.betsanddice.tutorial.document.GameTutorialDocument;
import com.betsanddice.tutorial.dto.GameTutorialDto;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Flux;

@Component
public class GameTutorialDocumentToDtoConverter {
    public GameTutorialDto fromDocumentToDto(GameTutorialDocument document) {
        ModelMapper mapper = new ModelMapper();
        mapper.createTypeMap(GameTutorialDocument.class, GameTutorialDto.class)
                .addMapping(GameTutorialDocument::getUuid, GameTutorialDto::setGameId);
        return mapper.map(document, GameTutorialDto.class);
    }

    public Flux<GameTutorialDto> fromDocumentFluxToDtoFlux(Flux<GameTutorialDocument> documentFlux) {
        return documentFlux.map(this::fromDocumentToDto);
    }

}
