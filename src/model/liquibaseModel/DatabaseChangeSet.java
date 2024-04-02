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
@JsonIgnoreProperties(value = {"schemaLocation", ""})
public class DatabaseChangeSet {

    @JacksonXmlElementWrapper(useWrapping = false)
    private List<ChangeSet> changeSet;

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class ChangeSet {

        @JacksonXmlProperty(isAttribute = true)
        private String id;

        @JacksonXmlProperty(isAttribute = true)
        private String author;

        @JacksonXmlElementWrapper(useWrapping = false)
        private List<CreateTable> createTable;

        @JacksonXmlElementWrapper(useWrapping = false)
        private List<AddColumn> addColumn;

        @JacksonXmlElementWrapper(useWrapping = false)
        private List<DropTable> dropTable;

        @JacksonXmlElementWrapper(useWrapping = false)
        private List<DropColumn> dropColumn;

        @JacksonXmlElementWrapper(useWrapping = false)
        private List<ModifyDataType> modifyDataType;

    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class CreateTable {

        @JacksonXmlProperty(isAttribute = true)
        private String tableName;

        @JacksonXmlElementWrapper(useWrapping = false)
        private List<Column> column;

    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    @JsonIgnoreProperties(value = "autoIncrement")
    public static class Column {

        @JacksonXmlProperty(isAttribute = true)
        private String name;

        @JacksonXmlProperty(isAttribute = true)
        private String type;

        @JacksonXmlElementWrapper(useWrapping = false)
        private List<Constraints> constraints;

    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    @JsonIgnoreProperties(value = {"catalogName", "schemaName"})
    public static class Constraints {

        @JacksonXmlProperty(isAttribute = true)
        private String nullable;

        @JacksonXmlProperty(isAttribute = true)
        private String primaryKey;

    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    @JsonIgnoreProperties(value = {"catalogName", "schemaName"})
    public static class AddColumn {

        @JacksonXmlProperty(isAttribute = true)
        private String tableName;

        @JacksonXmlElementWrapper(useWrapping = false)
        private List<Column> column;
    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    @JsonIgnoreProperties(value = {"catalogName", "schemaName"})
    public static class DropColumn {

        @JacksonXmlProperty(isAttribute = true)
        private String tableName;

        @JacksonXmlProperty(isAttribute = true)
        private String columnName;
    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    @JsonIgnoreProperties(value = {"catalogName", "schemaName"})
    public static class DropTable {

        @JacksonXmlProperty(isAttribute = true)
        private String tableName;

        @JacksonXmlProperty(isAttribute = true)
        private String cascadeConstraints;
    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    @JsonIgnoreProperties(value = {"catalogName", "schemaName"})
    public static class ModifyDataType {

        @JacksonXmlProperty(isAttribute = true)
        private String tableName;

        @JacksonXmlProperty(isAttribute = true)
        private String columnName;

        @JacksonXmlProperty(isAttribute = true)
        private String newDataType;
    }

}
