package com.betsanddice.tutorial.helper;

import com.betsanddice.tutorial.document.GameTutorialDocument;

import com.betsanddice.tutorial.dto.GameTutorialDto;
import org.modelmapper.ModelMapper;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Flux;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Component
public class GameTutorialDtoToDocumentConverter {

    public GameTutorialDocument fromDtoToDocument(GameTutorialDto gameTutorialDto) {
            ModelMapper mapper = new ModelMapper();
            mapper.createTypeMap(GameTutorialDto.class, GameTutorialDocument.class);
            return mapper.map(gameTutorialDto, GameTutorialDocument.class);
        }
/*    public GameTutorialDocument fromDtoToDocument(GameTutorialDto gameTutorialDto) {
        GameTutorialDocument gameTutorialDocument = new GameTutorialDocument();

        BeanUtils.copyProperties(gameTutorialDto, gameTutorialDocument);
        gameTutorialDocument.setUuid(UUID.randomUUID());
        return gameTutorialDocument;
    }*/

    public Flux<GameTutorialDocument> fromDtoFluxToDocumentFlux(Flux<GameTutorialDto> dtoFlux) {
        return dtoFlux.map(this::fromDtoToDocument);
    }

}
