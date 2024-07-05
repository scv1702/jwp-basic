package core;

import core.web.DispatcherServlet;
import lombok.extern.slf4j.Slf4j;
import org.apache.catalina.Wrapper;
import org.apache.catalina.startup.Tomcat;

import java.io.File;

@Slf4j
public class ApplicationLauncher {

    public static void launch(Class<?> clazz) throws Exception {
        String webappDirLocation = "webapp";

        Tomcat tomcat = new Tomcat();
        tomcat.setPort(58080);

        tomcat.addWebapp("", new File(webappDirLocation).getAbsolutePath());
        log.info("Configuring application with basedir: {}", new File(webappDirLocation).getAbsolutePath());

        ApplicationContext ac = new ApplicationContext(clazz.getPackageName());

        Wrapper dispatcher = tomcat.addServlet("", "dispatcher", new DispatcherServlet(ac));
        dispatcher.addMapping("/");
        dispatcher.setLoadOnStartup(1);

        tomcat.start();
        tomcat.getServer().await();
    }
}
