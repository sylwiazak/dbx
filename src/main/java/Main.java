import com.dropbox.core.*;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.time.LocalDate;
import java.util.Locale;


public class Main {
    public static void main(String[] args) throws IOException {

    final String APP_KEY = "Insert app key";
    final String APP_SECRET = "Insert app secret";

        DbxAppInfo appInfo= new DbxAppInfo(APP_KEY, APP_SECRET);

        DbxRequestConfig config= new DbxRequestConfig("First Dbx", Locale.getDefault().toString());
        DbxWebAuthNoRedirect webAuth= new DbxWebAuthNoRedirect(config, appInfo);

        String authorizeUrl= webAuth.start();
        System.out.println("1. Go to: " + authorizeUrl);
        System.out.println("2. Click \" Allow\" ");
        System.out.println("3. Copy the authorization code.");
        String code= new BufferedReader(new InputStreamReader(System.in)).readLine().trim();
        String accessToken= null;
        try {
            DbxAuthFinish authFinish= webAuth.finish(code);
            accessToken= authFinish.getAccessToken();
        } catch (DbxException e) {
            System.out.println("invalid authorization code");
            e.printStackTrace();
        }


    }
}