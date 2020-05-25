package br.com.tardelli.controller;

import org.eclipse.microprofile.openapi.annotations.OpenAPIDefinition;
import org.eclipse.microprofile.openapi.annotations.info.Contact;
import org.eclipse.microprofile.openapi.annotations.info.Info;
import org.eclipse.microprofile.openapi.annotations.info.License;
import org.eclipse.microprofile.openapi.annotations.tags.Tag;

import javax.ws.rs.core.Application;

@OpenAPIDefinition(
    tags = {
        @Tag(name = "movies", description = "Movies operations."),
    },
    info = @Info(
        title = "Movies API",
        version = "1.0.0",
        contact = @Contact(
            name = "Tardelli",
            email = "tardelli.m@gmail.com"),
        license = @License(
            name = "Apache 2.0",
            url = "http://www.apache.org/licenses/LICENSE-2.0.html"))
)
public class ApiApplication extends Application {
}
