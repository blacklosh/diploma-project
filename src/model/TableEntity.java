package model;

import lombok.Data;

import java.util.Set;

@Data
public class TableEntity {

    private String name;

    private Set<ColumnEntity> columns;

}
