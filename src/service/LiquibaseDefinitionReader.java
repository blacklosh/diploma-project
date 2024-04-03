package service;

import com.fasterxml.jackson.dataformat.xml.XmlMapper;
import model.ColumnEntity;
import model.TableEntity;
import model.liquibaseModel.DatabaseChangeLog;
import model.liquibaseModel.DatabaseChangeSet;
import util.ExceptionHandler;

import javax.naming.OperationNotSupportedException;
import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 * Модуль считывания метаданных конфигурации Liquibase
 * @version 1.0
 * @author Fedor Gusev
 */

public class LiquibaseDefinitionReader {

    /**
     * Считать метаданные
     * @param mainChangesetFile основной файл конфигурации миграций, в нём должны быть указаны все остальные
     * @return множество таблиц со всеми полями и другими сущностями
     */
    public Set<TableEntity> readXmlConfiguration(File mainChangesetFile) {
        try {
            XmlMapper xmlMapper = new XmlMapper();
            XMLInputFactory factory = XMLInputFactory.newInstance();
            XMLStreamReader xmlr = factory.createXMLStreamReader("master.xml", new FileInputStream(mainChangesetFile));
            DatabaseChangeLog result = xmlMapper.readValue(xmlr, DatabaseChangeLog.class);
            System.out.println(result);

            Map<String, TableEntity> resultMap = new HashMap<>();

            for (DatabaseChangeLog.MasterChangeLogIncludeFile file : result.getInclude()) {
                xmlr = factory.createXMLStreamReader("1", new FileInputStream(file.getFile()));
                DatabaseChangeSet set = xmlMapper.readValue(xmlr, DatabaseChangeSet.class);
                System.out.println(set);

                for(DatabaseChangeSet.ChangeSet changeSet : set.getChangeSet()) {
                    if(changeSet.getCreateTable() != null) {
                        for (DatabaseChangeSet.CreateTable createTable : changeSet.getCreateTable()) {
                            String tableName = createTable.getTableName().toLowerCase();
                            TableEntity previousTable = new TableEntity();
                            previousTable.setName(tableName);
                            if(resultMap.containsKey(tableName)) {
                                previousTable = resultMap.get(tableName);
                            }
                            if(previousTable.getColumns() == null) previousTable.setColumns(new HashSet<>());
                            for(DatabaseChangeSet.Column column : createTable.getColumn()) {
                                ColumnEntity newColumn = new ColumnEntity(
                                        column.getName().toLowerCase(),
                                        column.getType().toLowerCase(),
                                        tableName
                                );
                                //TODO newColumn.setConstraints(column.getConstraints());
                                previousTable.getColumns().add(newColumn);
                            }
                            resultMap.put(tableName, previousTable);
                        }
                    }

                    if(changeSet.getAddColumn() != null) {
                        for (DatabaseChangeSet.AddColumn addColumn : changeSet.getAddColumn()) {
                            String tableName = addColumn.getTableName().toLowerCase();
                            TableEntity previousTable = resultMap.get(tableName);
                            if(previousTable == null) {
                                throw new OperationNotSupportedException("Adding column to non-existing table " + tableName);
                            }
                            if(previousTable.getColumns() == null) previousTable.setColumns(new HashSet<>());
                            for(DatabaseChangeSet.Column column : addColumn.getColumn()) {
                                ColumnEntity newColumn = new ColumnEntity(
                                        column.getName().toLowerCase(),
                                        column.getType().toLowerCase(),
                                        tableName
                                );
                                //TODO newColumn.setConstraints(column.getConstraints());
                                previousTable.getColumns().add(newColumn);
                            }
                            resultMap.put(tableName, previousTable);
                        }
                    }

                    if(changeSet.getDropColumn() != null) {
                        for (DatabaseChangeSet.DropColumn column : changeSet.getDropColumn()) {
                            String tableName = column.getTableName().toLowerCase();
                            TableEntity previousTable = resultMap.get(tableName);
                            if(previousTable.getColumns() == null) previousTable.setColumns(new HashSet<>());
                            if(column.getColumn() == null || column.getColumn().size() == 0) {
                                ColumnEntity newColumn = new ColumnEntity(
                                        column.getColumnName().toLowerCase(),
                                        "",
                                        tableName
                                );
                                previousTable.getColumns().remove(newColumn);
                            } else {
                                for(DatabaseChangeSet.Column c : column.getColumn()) {
                                    ColumnEntity newColumn = new ColumnEntity(
                                            c.getName().toLowerCase(),
                                            c.getType(),
                                            tableName
                                    );
                                    previousTable.getColumns().remove(newColumn);
                                }
                            }

                        }
                    }

                    if(changeSet.getDropTable() != null) {
                        for(DatabaseChangeSet.DropTable table : changeSet.getDropTable()) {
                            resultMap.remove(table.getTableName());
                        }
                    }

                    if(changeSet.getModifyDataType() != null) {
                        for(DatabaseChangeSet.ModifyDataType modify : changeSet.getModifyDataType()) {
                            String tableName = modify.getTableName().toLowerCase();
                            TableEntity previousTable = resultMap.get(tableName);
                            if(previousTable.getColumns() == null) previousTable.setColumns(new HashSet<>());
                            ColumnEntity newColumn = new ColumnEntity(
                                    modify.getColumnName().toLowerCase(),
                                    modify.getNewDataType(),
                                    tableName
                            );
                            previousTable.getColumns().remove(newColumn);
                            previousTable.getColumns().add(newColumn);
                        }
                    }
                }
            }

            return new HashSet<>(resultMap.values());
        } catch (IOException | XMLStreamException | OperationNotSupportedException e) {
            ExceptionHandler.handleException(e);
            throw new RuntimeException("Got exception while decoding xml configuration: " + e.getMessage());
        }
    }

}
