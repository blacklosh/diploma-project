package service;

import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.ast.body.ClassOrInterfaceDeclaration;
import com.github.javaparser.ast.body.FieldDeclaration;
import model.ColumnEntity;
import model.TableEntity;
import com.github.javaparser.JavaParser;
import util.ExceptionHandler;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.HashSet;
import java.util.Set;

/**
 * Модуль считывания метаданных моделей из .java классов
 * @version 1.0
 * @author Fedor Gusev
 */

public class JavaModelsDefinitionReader {

    /**
     * Считывание метаданных моделей
     * @param filesWithModels список всех файлов, которые необходимо пройти
     * @return множество таблиц со всеми полями и другими сущностями
     */
    public Set<TableEntity> readJavaModels(File[] filesWithModels) {
        try {
            Set<TableEntity> resultSet = new HashSet<>();
            for(File classFile : filesWithModels) {
                FileInputStream in = new FileInputStream(classFile);
                CompilationUnit compilationUnit = new JavaParser().parse(in).getResult().get();
                TableEntity table = new TableEntity();
                compilationUnit.findAll(ClassOrInterfaceDeclaration.class).forEach(classOrInterface -> {
                    if (!classOrInterface.isInterface()) {
                        table.setName(classOrInterface.getName().asString().toLowerCase());
                    }
                });
                table.setColumns(new HashSet<>());
                compilationUnit.findAll(FieldDeclaration.class).forEach(field -> {
                    ColumnEntity column = new ColumnEntity(
                            field.getVariables().get(0).getName().asString().toLowerCase(),
                            typeFormat(field.getElementType().asString().toLowerCase()),
                            table.getName()
                    );
                    table.getColumns().add(column);
                });
                resultSet.add(table);
            }
            return resultSet;
        } catch (IOException e) {
            ExceptionHandler.handleException(e);
            throw new RuntimeException("Got exception while decoding java models configuration: " + e.getMessage());
        }
    }

    /**
     * Конвертер типов
     * @param javaType тип в классе .java
     * @return тип для языка sql
     */
    private String typeFormat(String javaType) {
        if("string".equals(javaType)) {
            return "varchar(255)";
        }
        return javaType;
    }

}
