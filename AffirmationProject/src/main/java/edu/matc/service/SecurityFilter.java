package edu.matc.service;

import org.glassfish.jersey.internal.util.Base64;
import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.container.ContainerRequestFilter;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.Provider;
import java.io.IOException;
import java.util.List;
import java.util.StringTokenizer;

/**
 * Created by toddkinsman on 11/9/16.
 */
@Provider
public class SecurityFilter implements ContainerRequestFilter {

    private static final String AUTHORIZATION_HEADER_KEY = "Authorization";
    private static final String AUTHORIZATION_HEADER_PREFIX = "Basic ";
    private static final String SECURED_URL_PREFIX = "affirmationservice";



    @Override
    public void filter(ContainerRequestContext requestContext) throws IOException {


        if (requestContext.getUriInfo().getPath().contains(SECURED_URL_PREFIX)) {

            List<String> authHeader = requestContext.getHeaders().get(AUTHORIZATION_HEADER_KEY);

            if (authHeader.size() > 0 && !authHeader.isEmpty() &&  authHeader != null) {

                String authToken = authHeader.get(0);
                authToken = authToken.replaceFirst(AUTHORIZATION_HEADER_PREFIX, "");
                String decodedString = Base64.decodeAsString(authToken);
                StringTokenizer tokenizer = new StringTokenizer(decodedString, ":");

                if(tokenizer.countTokens() > 0) {
                    String username = tokenizer.nextToken();
                    String password = tokenizer.nextToken();

                    if ("user".equals(username) && "password".equals(password)) {
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
