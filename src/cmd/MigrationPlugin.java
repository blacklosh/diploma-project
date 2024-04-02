package cmd;

import java.io.File;

public interface MigrationPlugin {

    void setChangesetDir(File changesetDir);
    void setMainChangesetFile(File mainChangesetFile);

    void processChanges(File[] classes);

}
