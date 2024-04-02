package model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@AllArgsConstructor
@EqualsAndHashCode(exclude = "definition")
public class ColumnEntity {

    private String name;

    private String definition;

    private String tableName;

}
