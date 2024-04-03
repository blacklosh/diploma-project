package util;

import lombok.experimental.UtilityClass;

import java.util.HashMap;
import java.util.Map;

@UtilityClass
public class GlobalParamsUtil {

    private final Map<String, String> params;

    static {
        params = new HashMap<>();
        params.put("USERNAME", "fedor2");
        params.put("MASTER_CHANGELOG_FILE", "c://cfg/Diploma/LiquibaseConfig/master.xml");
        params.put("CHANGELOG_DIR", "c://cfg/Diploma/LiquibaseConfig/changesets");
        params.put("MODELS_DIR", "c://cfg/Diploma/JavaModels/");
        params.put("DATETIME_FORMAT", "yyyyMMdd_HHmmss");
    }

    public String getProperty(String property) {
        if(!params.containsKey(property)) {
            System.err.println("Warning! No property named " + property + " found!");
            return "<<<NONE>>>";
        }
        return params.get(property);
    }

}
