package com.betsanddice.craps.service;

import com.betsanddice.craps.document.CrapsGameDocument;
import com.betsanddice.craps.dto.CrapsGameDto;
import com.betsanddice.craps.dto.DiceRollDto;
import com.betsanddice.craps.helper.CrapsGameDocumentToDtoConverter;
import com.betsanddice.craps.repository.CrapsGameRepository;
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

    private CrapsGameRepository crapsGameRepository;

    private CrapsGameDocumentToDtoConverter documentToDtoConverter;

    public CrapsGameServiceImp(CrapsGameRepository crapsGameRepository, CrapsGameDocumentToDtoConverter documentToDtoConverter) {
        this.crapsGameRepository = crapsGameRepository;
        this.documentToDtoConverter = documentToDtoConverter;
    }

    @Override
    public Mono<CrapsGameDto> addCrapsGameToUser(String userId) {
        List<DiceRollDto> diceRollsList = getDiceRollsList();
        CrapsGameDocument crapsGameDocument = new CrapsGameDocument(UUID.randomUUID(),
                UUID.fromString(userId), LocalDateTime.now(), diceRollsList.size(),
                diceRollsList);
        crapsGameRepository.save(crapsGameDocument).block();
        CrapsGameDto crapsGameDto = documentToDtoConverter.fromDocumentToDto(crapsGameDocument);
        return Mono.just(crapsGameDto);
    }

    @Override
    public Flux<CrapsGameDto> getAllCrapsGame() {
        Flux<CrapsGameDocument> crapsGameList = crapsGameRepository.findAll();
        return documentToDtoConverter.fromDocumentFluxToDtoFlux(crapsGameList);
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
