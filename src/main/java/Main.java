import java.io.IOException;
import java.nio.file.WatchService;


public class Main {
    public static void main(String[] args) {

        WatcherService watcherService= new WatcherService();

        try {
            watcherService.watch("src\\main\\resources\\source");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}