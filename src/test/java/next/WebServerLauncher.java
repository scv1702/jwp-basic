package next;

import lombok.extern.slf4j.Slf4j;
import org.apache.catalina.startup.Tomcat;

import java.io.File;

@Slf4j
public class WebServerLauncher {

    public static void main(String[] args) throws Exception {
        String webappDirLocation = "webapp/";
        Tomcat tomcat = new Tomcat();
        tomcat.setPort(58080);

        tomcat.addWebapp("/", new File(webappDirLocation).getAbsolutePath());
        log.info("configuring app with basedir: {}", new File("./" + webappDirLocation).getAbsolutePath());

        tomcat.start();
        tomcat.getServer().await();
    }
}
