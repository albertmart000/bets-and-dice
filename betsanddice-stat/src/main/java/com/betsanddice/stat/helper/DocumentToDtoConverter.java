package com.betsanddice.stat.helper;

import com.betsanddice.stat.document.UserGameStatDocument;
import com.betsanddice.stat.dto.UserGameStatDto;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Flux;

@Component
public class DocumentToDtoConverter<S, D> {

    public D fromDocumentToDto(S document, Class<D> dtoClass) {
        ModelMapper mapper = new ModelMapper();
        if (dtoClass.isAssignableFrom(UserGameStatDto.class)) {
            mapper.createTypeMap(UserGameStatDocument.class, UserGameStatDto.class)
                    .addMapping(UserGameStatDocument::getUuid, UserGameStatDto::setUuid);
        }
        return mapper.map(document, dtoClass);
    }

    public Flux<D> fromDocumentFluxToDtoFlux(Flux<S> documentFlux, Class<D> dtoClass) {
        return documentFlux.map(document -> fromDocumentToDto(document, dtoClass));
    }

}
