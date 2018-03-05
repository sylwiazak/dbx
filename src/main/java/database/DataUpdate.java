package database;

import dropBox.SenderWorker;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

public class DataUpdate {
    private FileTransferHistoryDao fileTransferHistoryDao;
    private ExecutorService executorService = Executors.newFixedThreadPool(4);

    public DataUpdate(FileTransferHistoryDao fileTransferHistoryDao) {
        this.fileTransferHistoryDao = fileTransferHistoryDao;
    }

    public void addOrUpdate(String fileName, SenderWorker senderWorker) throws ExecutionException, InterruptedException {
        if (!fileTransferHistoryDao.getNewFile(fileName).isPresent()) {
            Future<Long> future = executorService.submit(senderWorker);
            fileTransferHistoryDao.addFile(fileName, future.get());
        } else {
            Future<Long> future = executorService.submit(senderWorker);
            fileTransferHistoryDao.updateFile(fileName, future.get());
        }
    }
}
