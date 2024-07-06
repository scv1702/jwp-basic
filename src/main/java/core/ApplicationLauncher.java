package core;

import core.web.DispatcherServlet;
import lombok.extern.slf4j.Slf4j;
import org.apache.catalina.Wrapper;
import org.apache.catalina.startup.Tomcat;
import org.springframework.core.io.ClassPathResource;
import org.yaml.snakeyaml.Yaml;

import java.io.File;

@Slf4j
public class ApplicationLauncher {

    public static void launch(Class<?> clazz) throws Exception {
        final Yaml yaml = new Yaml();

        final ApplicationProperties properties = yaml.loadAs(
            new ClassPathResource("application.yml").getInputStream(),
            ApplicationProperties.class
        );
        final String webappDirLocation = properties.getWebapp();
        final String contextPath = properties.getContextPath();
        final int port = properties.getPort();

        final Tomcat tomcat = new Tomcat();
        tomcat.setPort(port);

        tomcat.addWebapp(contextPath, new File(webappDirLocation).getAbsolutePath());
        log.info("Configuring application with basedir: {}", new File(webappDirLocation).getAbsolutePath());

        final ApplicationContext ac = new ApplicationContext(clazz.getPackageName());

        final Wrapper dispatcher = tomcat.addServlet(contextPath, "dispatcher", new DispatcherServlet(ac));
        dispatcher.addMapping("/");
        dispatcher.setLoadOnStartup(1);

        tomcat.start();
        tomcat.getServer().await();
    }
}
