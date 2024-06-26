package model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * Внутренняя модель изменений для их вычисления.
 * Передаётся `Write`-рам в конце процесса обработки изменений
 * Флаг isEmpty() указывает, что значительных изменений в данный момент нет
 * @version 1.0
 * @author Fedor Gusev
 */

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ChangesModel {

    private List<TableEntity> newTables;

    private List<ColumnEntity> newColumns;

    private List<TableEntity> deletedTables;

    private List<ColumnEntity> deletedColumns;

    private List<ColumnEntity> changedColumns;

    public boolean isEmpty() {
        return (newTables == null || newTables.isEmpty()) &&
                (newColumns == null || newColumns.isEmpty()) &&
                (deletedColumns == null || deletedColumns.isEmpty()) &&
                (deletedTables == null || deletedTables.isEmpty()) &&
                (changedColumns == null || changedColumns.isEmpty());
    }

}
