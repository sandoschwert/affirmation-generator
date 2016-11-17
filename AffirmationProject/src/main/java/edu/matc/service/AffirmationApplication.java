package edu.matc.service;

import javax.ws.rs.ApplicationPath;
import javax.ws.rs.core.Application;
import java.util.HashSet;
import java.util.Set;

/**
 * A class that defines the service wrapper for related
 * web service classes
 *
 * @author Todd Kinsman
 * @since 10/18/16
 *
 */

//Defines the base URI for all resource URIs.
@ApplicationPath("/")
public class AffirmationApplication extends Application {

    //The method returns a non-empty collection with classes, that must be included in the published JAX-RS application
    @Override
    public Set<Class<?>> getClasses() {
        HashSet h = new HashSet<Class<?>>();
        h.add(AffirmationService.class );
        return h;
    }

}
