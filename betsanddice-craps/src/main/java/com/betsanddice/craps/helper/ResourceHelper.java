package com.betsanddice.craps.helper;

import jakarta.validation.constraints.NotNull;
import org.apache.commons.io.FileUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Objects;
import java.util.Optional;

public class ResourceHelper {

    private Resource resource;
    private String resourcePath;
    private static final Logger log = LoggerFactory.getLogger(ResourceHelper.class);

    public ResourceHelper(@NotNull String resourcePath) {
        this.resourcePath = resourcePath;
        resource = new ClassPathResource(this.resourcePath);
    }

    public Optional<String> readResourceAsString (){

        Optional<String> result = Optional.empty();
        try {
            result = Optional.of(FileUtils.readFileToString(resource.getFile(), StandardCharsets.UTF_8));
        } catch (IOException ex) {
            log.error(getResourceErrorMessage("loading/reading").concat(ex.getMessage()));
        }
        return result;
    }

    private String getResourceErrorMessage(String action){
        String resourceIdentifier = Objects.requireNonNullElseGet(resourcePath, () -> resource.getDescription());
        return "Exception when " + action + " " + resourceIdentifier + " resource: \n";
    }
}

