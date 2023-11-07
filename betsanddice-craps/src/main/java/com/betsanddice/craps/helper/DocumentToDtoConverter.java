package com.betsanddice.craps.helper;

import com.betsanddice.craps.document.CrapsGameDocument;
import com.betsanddice.craps.dto.CrapsGameDto;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Flux;

@Component
public class DocumentToDtoConverter<S, D> {

    public D fromDocumentToDto(S document, Class<D> dtoClass) {
        ModelMapper mapper = new ModelMapper();
        if (dtoClass.isAssignableFrom(CrapsGameDto.class)) {
            mapper.createTypeMap(CrapsGameDocument.class, CrapsGameDto.class)
                    .addMapping(CrapsGameDocument::getUuid, CrapsGameDto::setUuid);
        }
        return mapper.map(document, dtoClass);
    }

    public Flux<D> fromDocumentFluxToDtoFlux(Flux<S> documentFlux, Class<D> dtoClass) {
        return documentFlux.map(document -> fromDocumentToDto(document, dtoClass));
    }
}
