package com.betsanddice.craps.service;

import com.betsanddice.craps.document.CrapsGameDocument;
import com.betsanddice.craps.dto.CrapsGameDto;
import com.betsanddice.craps.dto.DiceRollDto;
import com.betsanddice.craps.exception.BadUuidException;
import com.betsanddice.craps.helper.DocumentToDtoConverter;
import com.betsanddice.craps.repository.CrapsGameRepository;
import io.micrometer.common.util.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.security.SecureRandom;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;
import java.util.regex.Pattern;

@Service
public class CrapsGameServiceImp implements ICrapsGameService {

    private static final Logger log = LoggerFactory.getLogger(CrapsGameServiceImp.class);
    private static final Pattern UUID_FORM = Pattern.compile("^[0-9a-f]{8}-[0-9a-f]{4}-[0-9a-f]{4}-[0-9a-f]{4}-[0-9a-f]{12}$", Pattern.CASE_INSENSITIVE);

    private final SecureRandom secureRandom = new SecureRandom();

    @Autowired
    private CrapsGameRepository crapsGameRepository;

    @Autowired
    private DocumentToDtoConverter<CrapsGameDocument, CrapsGameDto> converter = new DocumentToDtoConverter<>();

    public Mono<CrapsGameDto> playCrapsGameByUser(String uuid) {
        return validateUuid(uuid)
                .flatMap(userUuid -> generateDiceRollsList()
                        .map(diceRollsList -> CrapsGameDocument.builder()
                                .uuid(UUID.randomUUID())
                                .userId(userUuid)
                                .date(LocalDateTime.now())
                                .diceRollsList(diceRollsList)
                                .build())
                        .flatMap(crapsGameDocument -> crapsGameRepository.save(crapsGameDocument))
                        .map(documentToSave -> converter.fromDocumentToDto(documentToSave, CrapsGameDto.class)))
                .doOnSuccess(crapsGameDocument -> log.info("Successfully played CrapsGame by user with ID: {}", uuid))
                .doOnError(error -> log.error("Operation failed with error message: {}", error.getMessage()));
    }

    private Mono<List<DiceRollDto>> generateDiceRollsList() {
        return Flux.<DiceRollDto>generate(flux -> {
                    int dice1 = secureRandom.nextInt(6) + 1;
                    int dice2 = secureRandom.nextInt(6) + 1;
                    int result = dice1 + dice2;
                    flux.next(new DiceRollDto(dice1, dice2, result));
                    if (result == 7) {
                        flux.complete();
                    }
                })
                .collectList();
    }

    private Mono<UUID> validateUuid(String id) {
        boolean validUuid = !StringUtils.isEmpty(id) && UUID_FORM.matcher(id).matches();

        if (!validUuid) {
            log.warn("Invalid ID format: {}", id);
            return Mono.error(new BadUuidException("Invalid ID format. Please indicate the correct format."));
        }

        return Mono.just(UUID.fromString(id));
    }

}