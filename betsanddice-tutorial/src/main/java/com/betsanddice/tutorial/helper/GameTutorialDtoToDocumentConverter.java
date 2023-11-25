package com.betsanddice.tutorial.helper;

import com.betsanddice.tutorial.document.GameTutorialDocument;
import com.betsanddice.tutorial.dto.in.GameTutorialDtoByName;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Flux;

@Component
public class GameTutorialDtoToDocumentConverter {
    public GameTutorialDocument fromDtoToDocument(GameTutorialDtoByName gameTutorialDtoByName) {
        ModelMapper mapper = new ModelMapper();
        mapper.createTypeMap(GameTutorialDtoByName.class, GameTutorialDocument.class);
        return mapper.map(gameTutorialDtoByName, GameTutorialDocument.class);
    }

    public Flux<GameTutorialDocument> fromDtoFluxToDocumentFlux(Flux<GameTutorialDtoByName> dtoFlux) {
        return dtoFlux.map(this::fromDtoToDocument);
    }

}
