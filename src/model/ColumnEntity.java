package model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * Внутренняя модель для репрезентации колонки в базе данных
 * @version 1.0
 * @author Fedor Gusev
 */
@Data
@AllArgsConstructor
@EqualsAndHashCode(exclude = "definition")
public class ColumnEntity {

    private String name;

    private String definition;

    private String tableName;

}
