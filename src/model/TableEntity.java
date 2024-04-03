package model;

import lombok.Data;

import java.util.Set;

/**
 * Внутренняя модель для репрезентации таблицы в базе данных
 * @version 1.0
 * @author Fedor Gusev
 */

@Data
public class TableEntity {

    private String name;

    private Set<ColumnEntity> columns;

}
