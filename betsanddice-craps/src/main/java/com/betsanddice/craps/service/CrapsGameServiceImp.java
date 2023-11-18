package com.betsanddice.craps.service;

import com.betsanddice.craps.document.CrapsGameDocument;
import com.betsanddice.craps.dto.CrapsGameDto;
import com.betsanddice.craps.dto.DiceRollDto;
import com.betsanddice.craps.exception.BadUuidException;
import com.betsanddice.craps.helper.CrapsGameDocumentToDtoConverter;
import com.betsanddice.craps.repository.CrapsGameRepository;
import io.micrometer.common.util.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.UUID;
import java.util.regex.Pattern;

import static java.util.UUID.fromString;

@Service
public class CrapsGameServiceImp implements ICrapsGameService {

    private static final Logger log = LoggerFactory.getLogger(CrapsGameServiceImp.class);
    private static final Pattern UUID_PATTERN= Pattern.compile("^[0-9a-fA-F]{8}-[0-9a-fA-F]{4}-[0-9a-fA-F]{4}-[0-9a-fA-F]{4}-[0-9a-fA-F]{12}$", Pattern.CASE_INSENSITIVE);

    @Autowired
    private CrapsGameRepository crapsGameRepository;

    @Autowired
    private final CrapsGameDocumentToDtoConverter documentToDtoConverter = new CrapsGameDocumentToDtoConverter();

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

    public List<DiceRollDto> getDiceRollsList() {
        Random diceValue = new Random();
        List<DiceRollDto> diceRollList = new ArrayList<>();
        int result = 0;
        while (result != 7) {
            int dice1 = diceValue.nextInt(5) + 1;
            int dice2 = diceValue.nextInt(5) + 1;
            result = dice1 + dice2;
            diceRollList.add(new DiceRollDto(dice1, dice2, result));
        }
        return diceRollList;
    }

    private Mono<UUID> validateUuid(String id) {
        boolean validUuid = !StringUtils.isEmpty(id) && UUID_PATTERN.matcher(id).matches();
        if (!validUuid) {
            log.warn("Invalid ID format: {}", id);
            return Mono.error(new BadUuidException("Invalid ID format. Please indicate the correct format."));
        }
        return Mono.just(fromString(id));
    }

}
