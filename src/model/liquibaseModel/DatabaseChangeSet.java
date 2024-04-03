package model.liquibaseModel;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlElementWrapper;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import lombok.*;

import java.util.List;

/**
 * Представление регулярного ChangeSet файла Liquibase.
 * Содержит набор изменений, каждое из которых может содержать
 * много различных записей изменений
 * @version 1.0
 * @author Fedor Gusev
 */

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

    /**
     * Запись создания таблицы - содержит имя таблицы
     * и список колонок, которые будет иметь таблица
     * @version 1.0
     * @author Fedor Gusev
     */
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

    /**
     * Представление колонки для использования в других конструкциях xml.
     * Содержит имя, тип значения и список ограничений
     * @version 1.0
     * @author Fedor Gusev
     */
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

    /**
     * Представление ограничения для использования внутри xml колонки.
     * В данной версии поддерживаются только ограничения первичного ключа и пустоты
     * @version 1.0
     * @author Fedor Gusev
     */
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

    /**
     * Запись создания колонки - содержит имя целевой таблицы
     * и список всех создаваемых колонок
     * @version 1.0
     * @author Fedor Gusev
     */
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

    /**
     * Запись удаления колонки - содержит имя целевой таблицы
     * и список всех удаляемых колонок.
     * Если список удаляемых колонок пуст, то будет удалена колонка с именем columnName
     * @version 1.0
     * @author Fedor Gusev
     */
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

        @JacksonXmlElementWrapper(useWrapping = false)
        private List<Column> column;
    }

    /**
     * Запись удаления таблицы - содержит имя целевой таблицы,
     * и указания взаимодействия с каскадным удалением ограничений
     * @version 1.0
     * @author Fedor Gusev
     */
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

    /**
     * Запись изменения типа колонки - содержит имя целевой таблицы,
     * имя колонки columnName и новый тип newDataType
     * @version 1.0
     * @author Fedor Gusev
     */
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
