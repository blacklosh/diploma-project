package model.liquibaseModel;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlElementWrapper;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import lombok.*;

import java.util.List;

@Data
@Builder
@ToString
@NoArgsConstructor
@AllArgsConstructor
@JsonIgnoreProperties(value = "schemaLocation")
public class DatabaseChangeLog {

    @JacksonXmlElementWrapper(useWrapping = false)
    private List<MasterChangeLogIncludeFile> include;

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    @JsonIgnoreProperties("relativeToChangelogFile")
    public static class MasterChangeLogIncludeFile {

        @JacksonXmlProperty(isAttribute = true)
        private String file;

    }

}
