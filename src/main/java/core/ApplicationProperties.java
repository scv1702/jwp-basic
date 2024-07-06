package core;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ApplicationProperties {
    private String webapp;
    private String contextPath;
    private Integer port;
}
