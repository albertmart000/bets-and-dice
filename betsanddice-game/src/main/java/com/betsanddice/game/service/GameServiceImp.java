package com.betsanddice.game.service;

import com.betsanddice.game.document.GameDocument;
import com.betsanddice.game.dto.GameDto;
import com.betsanddice.game.helper.GameDocumentToDtoConverter;
import com.betsanddice.game.repository.GameRepository;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;

@Service
public class GameServiceImp implements IGameService {

    private GameRepository gameRepository;

    private GameDocumentToDtoConverter documentToDtoConverter;

    public GameServiceImp(GameRepository gameRepository, GameDocumentToDtoConverter documentToDtoConverter) {
        this.gameRepository = gameRepository;
        this.documentToDtoConverter = documentToDtoConverter;
    }

    @Override
    public Flux<GameDto> getAllGames() {
        Flux<GameDocument> gameList = gameRepository.findAll();
        return documentToDtoConverter.fromDocumentFluxToDtoFlux(gameList);
    }
}
