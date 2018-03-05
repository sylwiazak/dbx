package folder;

import database.DataUpdate;
import dropBox.DropBoxService;
import dropBox.SenderWorker;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.stream.Stream;

import static java.lang.String.format;

public class Folder {
    private List<File> files = new ArrayList<>();
    private DataUpdate dataUpdate;
    private DropBoxService dropBoxService;

    public Folder(DataUpdate dataUpdate, DropBoxService dropBoxService) {
        this.dropBoxService = dropBoxService;
        this.dataUpdate = dataUpdate;
    }

    public void checkNewFiles(String source) {
        listFilesForFolder(source);
        for (File file : files) {
            try {
                dataUpdate.addOrUpdate(file.getName(), new SenderWorker(toFile(source, file.toPath()), file.getName(), dropBoxService));
            } catch (ExecutionException | InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    private void listFilesForFolder(String source) {
        try (Stream<Path> paths = Files.walk(Paths.get(source))) {
            paths.filter(Files::isRegularFile)
                    .map(i -> new File(i.toString()))
                    .forEach(i -> files.add(i));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private File toFile(String source, Path ev) {
        return new File(format("%s/%s", source, ev.getFileName()));
    }
}