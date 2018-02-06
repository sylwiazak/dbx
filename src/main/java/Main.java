import java.io.IOException;
import java.nio.file.WatchService;


public class Main {
    public static void main(String[] args) {


        DropBoxService dropBoxService = new DropBoxService("accessToken");
        WatcherService watcherService = new WatcherService(dropBoxService);

        try {
            watcherService.watch("src\\main\\resources\\source");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}