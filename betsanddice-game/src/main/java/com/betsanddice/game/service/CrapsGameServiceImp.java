package com.betsanddice.game.service;

import com.betsanddice.game.document.CrapsGameDocument;
import com.betsanddice.game.dto.CrapsGameDto;
import com.betsanddice.game.dto.DiceRollDto;
import com.betsanddice.game.helper.DocumentToDtoConverter;
import com.betsanddice.game.repository.CrapsGameRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.security.SecureRandom;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
public class CrapsGameServiceImp implements ICrapsGameService {

    @Autowired
    private CrapsGameRepository crapsGameRepository;

    @Autowired
    private DocumentToDtoConverter<CrapsGameDocument, CrapsGameDto> converter = new DocumentToDtoConverter<>();

    @Override
    public Mono<CrapsGameDto> addCrapsGameToUser(String userId) {
        List<DiceRollDto> diceRollsList = getDiceRollsList();
        CrapsGameDocument crapsGameDocument = new CrapsGameDocument(UUID.randomUUID(),
                UUID.fromString(userId), LocalDateTime.now(), diceRollsList.size(),
                diceRollsList);
        crapsGameRepository.save(crapsGameDocument).block();
        CrapsGameDto crapsGameDto = converter.fromDocumentToDto(crapsGameDocument, CrapsGameDto.class);
        return Mono.just(crapsGameDto);
    }

    @Override
    public Flux<CrapsGameDto> getAllCrapsGame() {
        Flux<CrapsGameDocument> crapsGameList = crapsGameRepository.findAll();
        return converter.fromDocumentFluxToDtoFlux(crapsGameList, CrapsGameDto.class);
    }

    private List<DiceRollDto> getDiceRollsList() {
        SecureRandom secureRandom = new SecureRandom();
        List<DiceRollDto> diceRollList = new ArrayList<>();
        int result = 0;
        while (result != 7) {
            int dice1 = secureRandom.nextInt(6) + 1;
            int dice2 = secureRandom.nextInt(6) + 1;
            result = dice1 + dice2;
            diceRollList.add(new DiceRollDto(dice1, dice2, result));
        }
        return diceRollList;
    }

}
