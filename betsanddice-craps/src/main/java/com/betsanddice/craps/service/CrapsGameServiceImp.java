package com.betsanddice.craps.service;

import com.betsanddice.craps.document.CrapsGameDocument;
import com.betsanddice.craps.dto.CrapsGameDto;
import com.betsanddice.craps.helper.CrapsGameDocumentToDtoConverter;
import com.betsanddice.craps.repository.CrapsGameRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;

@Service
public class CrapsGameServiceImp implements ICrapsGameService{

    private static final Logger log = LoggerFactory.getLogger(CrapsGameServiceImp.class);

    @Autowired
    private CrapsGameRepository crapsGameRepository;

    @Autowired
    private CrapsGameDocumentToDtoConverter crapsGameConverter = new CrapsGameDocumentToDtoConverter();

    @Override
    public Flux<CrapsGameDto> getAllCrapsGame() {
        Flux<CrapsGameDocument> crapsGameList = crapsGameRepository.findAll();
        return crapsGameConverter.fromDocumentFluxToDtoFlux(crapsGameList);
    }

}
