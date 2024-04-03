package service;

import com.fasterxml.jackson.dataformat.xml.XmlMapper;
import lombok.experimental.UtilityClass;
import model.ChangesModel;
import model.ColumnEntity;
import model.TableEntity;
import model.liquibaseModel.DatabaseChangeLog;
import util.ExceptionHandler;
import util.GlobalParamsUtil;

import javax.xml.stream.*;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.UUID;

@UtilityClass
public class LiquibaseDefinitionWriter {

    public void appendMasterChangelogFile(File master, File newFile) {
        try {
            XmlMapper xmlMapper = new XmlMapper();
            XMLInputFactory factory = XMLInputFactory.newInstance();
            XMLStreamReader xmlr = factory.createXMLStreamReader("master.xml", new FileInputStream(master));
            DatabaseChangeLog result = xmlMapper.readValue(xmlr, DatabaseChangeLog.class);
            System.out.println(result);
            result.getInclude().add(new DatabaseChangeLog.MasterChangeLogIncludeFile(newFile.getAbsolutePath()));
            XMLOutputFactory factory1 = XMLOutputFactory.newFactory();
            XMLStreamWriter xmlw = factory1.createXMLStreamWriter(new FileOutputStream(master));

            xmlw.writeStartElement("databaseChangeLog");
            xmlw.writeAttribute("xmlns", "http://www.liquibase.org/xml/ns/dbchangelog");
            xmlw.writeAttribute("xmlns:xsi", "http://www.w3.org/2001/XMLSchema-instance");
            xmlw.writeAttribute("xsi:schemaLocation", "http://www.liquibase.org/xml/ns/dbchangelog http://www.liquibase.org/xml/ns/dbchangelog/dbchangelog-3.4.xsd");
            xmlw.writeCharacters("\n\n");

            for (DatabaseChangeLog.MasterChangeLogIncludeFile i : result.getInclude()) {
                xmlw.writeStartElement("include");
                xmlw.writeAttribute("file", i.getFile());
                xmlw.writeAttribute("relativeToChangelogFile", "false");
                xmlw.writeEndElement();
                xmlw.writeCharacters("\n");
            }

            xmlw.writeEndElement();
            xmlw.writeEndDocument();

            xmlw.close();
        } catch (IOException | XMLStreamException e) {
            ExceptionHandler.handleException(e);
        }

    }

    public void createNewChangeLogFile(File newChangelog, ChangesModel changes) {
        try {
            newChangelog.createNewFile();

            XMLOutputFactory factory1 = XMLOutputFactory.newFactory();
            XMLStreamWriter xmlw = factory1.createXMLStreamWriter(new FileOutputStream(newChangelog));

            xmlw.writeStartElement("databaseChangeLog");
            xmlw.writeAttribute("xmlns", "http://www.liquibase.org/xml/ns/dbchangelog");
            xmlw.writeAttribute("xmlns:xsi", "http://www.w3.org/2001/XMLSchema-instance");
            xmlw.writeAttribute("xmlns:ext", "http://www.liquibase.org/xml/ns/dbchangelog-ext");
            xmlw.writeAttribute("xsi:schemaLocation", "http://www.liquibase.org/xml/ns/dbchangelog " +
                    "http://www.liquibase.org/xml/ns/dbchangelog/dbchangelog-3.4.xsd " +
                    "http://www.liquibase.org/xml/ns/dbchangelog-ext " +
                    "http://www.liquibase.org/xml/ns/dbchangelog/dbchangelog-ext.xsd");
            xmlw.writeCharacters("\n\n");

            String author = GlobalParamsUtil.getProperty("USERNAME");

            /**
             * ADDING NEW TABLES
             */

            if(changes.getNewTables() != null && changes.getNewTables().size() > 0) {
                xmlw.writeStartElement("changeSet");
                xmlw.writeAttribute("id", UUID.randomUUID().toString());
                xmlw.writeAttribute("author", author);
                xmlw.writeCharacters("\n");
                for (TableEntity table : changes.getNewTables()) {
                    xmlw.writeStartElement("createTable");
                    xmlw.writeAttribute("tableName", table.getName());
                    xmlw.writeCharacters("\n");
                    for (ColumnEntity column : table.getColumns()) {
                        xmlw.writeStartElement("column");
                        xmlw.writeAttribute("name", column.getName());
                        xmlw.writeAttribute("type", column.getDefinition());
                        xmlw.writeEndElement();
                        xmlw.writeCharacters("\n");
                    }

                    xmlw.writeEndElement();
                    xmlw.writeCharacters("\n");
                }
                xmlw.writeEndElement();
                xmlw.writeCharacters("\n\n");
            }

            /**
             * ADDING NEW COLUMNS
             */

            if(changes.getNewColumns() != null && changes.getNewColumns().size() > 0) {
                xmlw.writeStartElement("changeSet");
                xmlw.writeAttribute("id", UUID.randomUUID().toString());
                xmlw.writeAttribute("author", author);
                xmlw.writeCharacters("\n");
                for(ColumnEntity column : changes.getNewColumns()) {
                    xmlw.writeStartElement("addColumn");
                    xmlw.writeAttribute("tableName", column.getTableName());
                    xmlw.writeCharacters("\n");

                    xmlw.writeStartElement("column");
                    xmlw.writeAttribute("name", column.getName());
                    xmlw.writeAttribute("type", column.getDefinition());
                    xmlw.writeEndElement();
                    xmlw.writeCharacters("\n");

                    xmlw.writeEndElement();
                    xmlw.writeCharacters("\n");
                }
                xmlw.writeEndElement();
                xmlw.writeCharacters("\n\n");
            }

            /**
             * CHANGING COLUMNS
             */

            if(changes.getChangedColumns() != null && changes.getChangedColumns().size() > 0) {
                xmlw.writeStartElement("changeSet");
                xmlw.writeAttribute("id", UUID.randomUUID().toString());
                xmlw.writeAttribute("author", author);
                xmlw.writeCharacters("\n");
                for (ColumnEntity column : changes.getChangedColumns()) {
                    xmlw.writeStartElement("modifyDataType");
                    xmlw.writeAttribute("tableName", column.getTableName());
                    xmlw.writeAttribute("columnName", column.getName());
                    xmlw.writeAttribute("newDataType", column.getDefinition());
                    xmlw.writeEndElement();
                    xmlw.writeCharacters("\n");
                }
                xmlw.writeEndElement();
                xmlw.writeCharacters("\n\n");
            }


            /**
             * DELETING COLUMNS
             */

            if(changes.getDeletedColumns() != null && changes.getDeletedColumns().size() > 0) {
                xmlw.writeStartElement("changeSet");
                xmlw.writeAttribute("id", UUID.randomUUID().toString());
                xmlw.writeAttribute("author", author);
                xmlw.writeCharacters("\n");
                for (ColumnEntity column : changes.getDeletedColumns()) {
                    xmlw.writeStartElement("dropColumn");
                    xmlw.writeAttribute("tableName", column.getTableName());
                    xmlw.writeAttribute("columnName", column.getName());
                    xmlw.writeEndElement();
                    xmlw.writeCharacters("\n");
                }
                xmlw.writeEndElement();
                xmlw.writeCharacters("\n\n");
            }

            /**
             * DELETING TABLES
             */

            if(changes.getDeletedTables() != null && changes.getDeletedTables().size() > 0) {
                xmlw.writeStartElement("changeSet");
                xmlw.writeAttribute("id", UUID.randomUUID().toString());
                xmlw.writeAttribute("author", author);
                xmlw.writeCharacters("\n");
                for (TableEntity table : changes.getDeletedTables()) {
                    xmlw.writeStartElement("dropTable");
                    xmlw.writeAttribute("tableName", table.getName());
                    xmlw.writeAttribute("cascadeConstraints", "true");
                    xmlw.writeEndElement();
                    xmlw.writeCharacters("\n");
                }
                xmlw.writeEndElement();
                xmlw.writeCharacters("\n\n");
            }

            xmlw.writeEndElement();
            xmlw.writeEndDocument();
            xmlw.close();
        } catch (IOException | XMLStreamException e) {
            ExceptionHandler.handleException(e);
        }
    }

}
