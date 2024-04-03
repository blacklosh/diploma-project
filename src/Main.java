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
        File master = new File(GlobalParamsUtil.getProperty("MASTER_CHANGELOG_FILE"));
        File changeSetDir = new File(GlobalParamsUtil.getProperty("CHANGELOG_DIR"));

        LiquibaseDefinitionReader reader = new LiquibaseDefinitionReader();

        Set<TableEntity> result = reader.readXmlConfiguration(master, null);
        System.out.println(result);

        System.out.println();
        System.out.println();
        System.out.println();

        JavaModelsDefinitionReader reader2 = new JavaModelsDefinitionReader();
        File[] files = new File(GlobalParamsUtil.getProperty("MODELS_DIR")).listFiles();
        Set<TableEntity> result2 = reader2.readJavaModels(files);
        System.out.println(result2);

        System.out.println();
        System.out.println();
        System.out.println();

        MigrationComparator migrationComparator = new MigrationComparator();
        ChangesModel changes = migrationComparator.getDifference(result, result2);
        System.out.println(changes.getNewTables());
        System.out.println(changes.getNewColumns());
        System.out.println(changes.getDeletedTables());
        System.out.println(changes.getDeletedColumns());

        if(!changes.isEmpty()) {
            String format = GlobalParamsUtil.getProperty("DATETIME_FORMAT");
            String timeStamp = new SimpleDateFormat(format).format(Calendar.getInstance().getTime());
            File newChangeLogFile = new File(changeSetDir.getAbsolutePath() + File.separator + timeStamp + "_" + UUID.randomUUID() + ".xml");
            LiquibaseDefinitionWriter.appendMasterChangelogFile(master, newChangeLogFile);
            LiquibaseDefinitionWriter.createNewChangeLogFile(newChangeLogFile, changes);
        }
    }
}