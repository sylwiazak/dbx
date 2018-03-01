package folder;

import database.FileTransferHistoryDao;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

public class Folder {
    private List<File> files= new ArrayList<>();
    private FileTransferHistoryDao fileTransferHistoryDao;

    public Folder(FileTransferHistoryDao fileTransferHistoryDao) {
        this.fileTransferHistoryDao = fileTransferHistoryDao;
    }

    public void checkNewFiles(String source){
        listFilesForFolder(source);
        for (File file : files) {
            if(!fileTransferHistoryDao.getListFile().contains(file.getName())){
                fileTransferHistoryDao.addFile(file.getName());
            }
        }
    }

    private void listFilesForFolder(String source) {
        try (Stream<Path> paths = Files.walk(Paths.get(source))) {
            paths.filter(Files::isRegularFile)
                    .map(i->new File(i.toString()))
                    .forEach(i->files.add(i));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
