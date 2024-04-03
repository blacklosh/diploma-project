import model.ChangesModel;
import model.TableEntity;
import service.JavaModelsDefinitionReader;
import service.LiquibaseDefinitionReader;
import service.LiquibaseDefinitionWriter;
import service.MigrationComparator;
import util.GlobalParamsUtil;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Set;
import java.util.UUID;

public class Main {
    public static void main(String[] args) {

        // Ниже представлен MVP процесса работы программы

        File master = new File(GlobalParamsUtil.getProperty("MASTER_CHANGELOG_FILE"));
        File changeSetDir = new File(GlobalParamsUtil.getProperty("CHANGELOG_DIR"));

        // Читаем всё, что написано в миграции
        LiquibaseDefinitionReader reader = new LiquibaseDefinitionReader();
        Set<TableEntity> result = reader.readXmlConfiguration(master);
        System.out.println(result);

        System.out.println();
        System.out.println();
        System.out.println();

        // Читаем всё, что написано в классах
        JavaModelsDefinitionReader reader2 = new JavaModelsDefinitionReader();
        File[] files = new File(GlobalParamsUtil.getProperty("MODELS_DIR")).listFiles();
        Set<TableEntity> result2 = reader2.readJavaModels(files);
        System.out.println(result2);

        System.out.println();
        System.out.println();
        System.out.println();

        // Сравниваем эти модели
        MigrationComparator migrationComparator = new MigrationComparator();
        ChangesModel changes = migrationComparator.getDifference(result, result2);
        System.out.println(changes.getNewTables());
        System.out.println(changes.getNewColumns());
        System.out.println(changes.getDeletedTables());
        System.out.println(changes.getDeletedColumns());

        // Если есть разница, то записываем её в новый файл миграции
        if(!changes.isEmpty()) {
            String format = GlobalParamsUtil.getProperty("DATETIME_FORMAT");
            String timeStamp = new SimpleDateFormat(format).format(Calendar.getInstance().getTime());
            File newChangeLogFile = new File(changeSetDir.getAbsolutePath() + File.separator + timeStamp + "_" + UUID.randomUUID() + ".xml");
            LiquibaseDefinitionWriter writer = new LiquibaseDefinitionWriter();
            writer.appendMasterChangelogFile(master, newChangeLogFile);
            writer.createNewChangeLogFile(newChangeLogFile, changes);
        }
    }
}