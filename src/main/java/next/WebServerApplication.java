package next;

import core.ApplicationLauncher;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class WebServerApplication {

    public static void main(String[] args) throws Exception {
        ApplicationLauncher.launch(WebServerApplication.class);
    }
}
