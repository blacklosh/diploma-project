package service;

import model.ChangesModel;
import model.ColumnEntity;
import model.TableEntity;

import java.util.*;

/**
 * Модуль сравнения метаданных.
 * Позволяет сравнить метаданные, полученные из классов моделей
 * с метаданными, полученными из конфигураций миграций.
 * @version 1.0
 * @author Fedor Gusev
 */

public class MigrationComparator {

    /**
     * Получить разницу
     * @param migration исходник миграции
     * @param entities состояние сущностей
     * @return все изменения, которые нужно внести в миграцию
     */
    public ChangesModel getDifference(Set<TableEntity> migration, Set<TableEntity> entities) {
        ChangesModel result = new ChangesModel();
        result.setNewColumns(new ArrayList<>());
        result.setNewTables(new ArrayList<>());
        result.setDeletedColumns(new ArrayList<>());
        result.setDeletedTables(new ArrayList<>());
        result.setChangedColumns(new ArrayList<>());

        Map<String, TableEntity> migrationTables = new HashMap<>();
        Map<String, TableEntity> modelTables = new HashMap<>();

        for (TableEntity table : migration) {
            migrationTables.put(table.getName(), table);
        }

        for (TableEntity table : entities) {
            modelTables.put(table.getName(), table);
        }

        for (TableEntity table : entities) {
            if(!migrationTables.containsKey(table.getName())) {
                result.getNewTables().add(table);
            } else {
                TableEntity migrationTable = migrationTables.get(table.getName());
                for(ColumnEntity column : table.getColumns()) {
                    if(!migrationTable.getColumns().contains(column)) {
                        result.getNewColumns().add(column);
                    } else {
                        ColumnEntity migrationColumn = migrationTable.getColumns().stream()
                                .filter(c -> c.getName().equals(column.getName()))
                                .findFirst().get();
                        if(!migrationColumn.getDefinition().equals(column.getDefinition())) {
                            result.getChangedColumns().add(column);
                        }
                        // TODO check constraints
                    }
                }
                for(ColumnEntity column : migrationTable.getColumns()) {
                    if(!table.getColumns().contains(column)) {
                        result.getDeletedColumns().add(column);
                    }
                }
            }
        }

        for(TableEntity table : migration) {
            if(!modelTables.containsKey(table.getName())) {
                result.getDeletedTables().add(table);
            }
        }

        return result;
    }

}
