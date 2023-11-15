package com.betsanddice.craps.helper;

import com.betsanddice.craps.document.CrapsGameDocument;
import com.betsanddice.craps.dto.CrapsGameDto;
import org.modelmapper.ModelMapper;
import reactor.core.publisher.Flux;

public class CrapsGameDtoToDocumentConverter {

    public CrapsGameDocument fromDtoToDocument(CrapsGameDto dto) {
        ModelMapper mapper = new ModelMapper();
        mapper.createTypeMap(CrapsGameDto.class, CrapsGameDocument.class)
                .addMapping(CrapsGameDto::getUuid, CrapsGameDocument::setUuid);
        return mapper.map(dto, CrapsGameDocument.class);
    }

    public Flux<CrapsGameDocument> fromDtoFluxToDocumentFlux(Flux<CrapsGameDto> dtoFlux) {
        return dtoFlux.map(this::fromDtoToDocument);
    }

}
