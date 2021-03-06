package de.mexchange.packagingdb.domain;

import de.mexchange.packagingdb.domain.lcp.Language;
import de.mexchange.packagingdb.entity.CoatingInfoEntity;
import org.hibernate.validator.constraints.NotEmpty;

import javax.validation.constraints.NotNull;

/**
 * Data Transfer Object representing {@link CoatingInfoEntity}
 */
public class CoatingInfo {

    private Long id;

    @NotNull(message = "{err.field.coating.description.language.not.specified}")
    private Language language;

    @NotEmpty(message = "{err.field.coating.description.required}")
    private String description;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Language getLanguage() {
        return language;
    }

    public void setLanguage(Language language) {
        this.language = language;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
