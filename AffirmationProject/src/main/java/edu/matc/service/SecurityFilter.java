package edu.matc.service;

import org.glassfish.jersey.internal.util.Base64;
import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.container.ContainerRequestFilter;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.Provider;
import java.io.IOException;
import java.util.List;
import java.util.Properties;
import java.util.ResourceBundle;
import java.util.StringTokenizer;

/**
 *
 * The Security filter used to add basic authentication for the webservice.
 * It creates the filter necessary to limit access to the webservice to those
 * who know the user name and password.
 * It protects all URIs of the affirmationservice.
 *
 * @author Todd Kinsman
 * @since 11/9/16.
 */
@Provider
public class SecurityFilter implements ContainerRequestFilter {

    private static final String AUTHORIZATION_HEADER_KEY = "Authorization";
    private static final String AUTHORIZATION_HEADER_PREFIX = "Basic ";
    private static final String SECURED_URL_PREFIX = "affirmationservice";

    private Properties properties;
    private String propertiesFilePath = "/api_config.properties";


    /**
     * Loads the properties file based on the path argument passed to it.
     * @param propertiesFilePath String path where property file is located
     */
    public void loadProperties(String propertiesFilePath) {

        properties = new Properties();
        try {
            properties.load(this.getClass().getResourceAsStream(propertiesFilePath));
        } catch (IOException ioException) {
            ioException.printStackTrace();
        } catch (Exception exception) {
            exception.printStackTrace();
        }
    }

    @Override
    public void filter(ContainerRequestContext requestContext) throws IOException {

        loadProperties(propertiesFilePath);

        String usernameProp = properties.getProperty("user.name");
        String passwordProp = properties.getProperty("password");

        if (requestContext.getUriInfo().getPath().contains(SECURED_URL_PREFIX)) {

            List<String> authHeader = requestContext.getHeaders().get(AUTHORIZATION_HEADER_KEY);

            if (authHeader != null && authHeader.size() > 0 && !authHeader.isEmpty()) {

                String authToken = authHeader.get(0);
                authToken = authToken.replaceFirst(AUTHORIZATION_HEADER_PREFIX, "");
                String decodedString = Base64.decodeAsString(authToken);
                StringTokenizer tokenizer = new StringTokenizer(decodedString, ":");

                if(tokenizer.countTokens() > 0) {
                    String username = tokenizer.nextToken();
                    String password = tokenizer.nextToken();

                    // This sets the user name and password
                    if (usernameProp.equals(username) && passwordProp.equals(password)) {
                        return;
                    }
                }
            }

                Response unauthenticatedResponse = Response
                        .status(Response.Status.UNAUTHORIZED)
                        .entity("User cannot access the resource")
                        .build();

                requestContext.abortWith(unauthenticatedResponse);
        }
    }
}
