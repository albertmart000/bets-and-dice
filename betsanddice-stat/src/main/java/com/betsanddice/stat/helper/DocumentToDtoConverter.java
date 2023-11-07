package com.betsanddice.stat.helper;

import com.betsanddice.stat.document.UserStatDocument;
import com.betsanddice.stat.dto.UserStatDto;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Flux;

@Component
public class DocumentToDtoConverter<S, D> {

    public D fromDocumentToDto(S document, Class<D> dtoClass) {
        ModelMapper mapper = new ModelMapper();
        if (dtoClass.isAssignableFrom(UserStatDto.class)) {
            mapper.createTypeMap(UserStatDocument.class, UserStatDto.class)
                    .addMapping(UserStatDocument::getUuid, UserStatDto::setUuid);
        }
        return mapper.map(document, dtoClass);
    }

    public Flux<D> fromDocumentFluxToDtoFlux(Flux<S> documentFlux, Class<D> dtoClass) {
        return documentFlux.map(document -> fromDocumentToDto(document, dtoClass));
    }

}
