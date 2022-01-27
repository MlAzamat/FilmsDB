package ru.testqa.app;

import ru.testqa.resource.FilmsDataBaseResource;
import io.swagger.jaxrs.config.BeanConfig;
import io.swagger.jaxrs.listing.ApiListingResource;
import org.glassfish.jersey.server.ResourceConfig;
import org.springframework.stereotype.Component;

import javax.ws.rs.ApplicationPath;
import javax.ws.rs.core.MediaType;

@Component
@ApplicationPath("/app")
public class AppResourceConfig extends ResourceConfig {

    public AppResourceConfig() {

        packages("in.testqa.resource", "in.testqa.app");
        register(FilmsDataBaseResource.class);

        configureSwagger();
    }

    private void configureSwagger() {
        register(ApiListingResource.class);
        BeanConfig beanConfig = new BeanConfig();
        beanConfig.setVersion("1.0.2");
        beanConfig.setSchemes(new String[] { "http", "https" });
        beanConfig.setBasePath("/app");
        beanConfig.setTitle("Films Data Base");
        beanConfig.setDescription("https://github.com/MlAzamat/FilmsDB");
        beanConfig.getSwagger().addConsumes(MediaType.APPLICATION_JSON);
        beanConfig.getSwagger().addProduces(MediaType.APPLICATION_JSON);
        beanConfig.setContact("Azamat Malikov");
        beanConfig.setResourcePackage("ru.testqa.resource");
        beanConfig.setPrettyPrint(false);
        beanConfig.setScan();
    }

}
