package org.imgcnv.rest;

import java.util.HashSet;
import java.util.Set;

import javax.ws.rs.ApplicationPath;
import javax.ws.rs.core.Application;

import com.wordnik.swagger.jaxrs.config.BeanConfig;;

/**
 * Custom Application subclass, it would need to add swagger-core's
 * providers to the set up process.
 * @author Dmitry_Slepchenkov
 *
 */
@ApplicationPath("api")
public class RestApp extends Application {

    /**
     * Constructor to set up Swagger for this class.
     */
    public RestApp() {
        BeanConfig beanConfig = new BeanConfig();
        beanConfig.setVersion("1.0");
        beanConfig.setBasePath("api");
        beanConfig.setResourcePackage("org.imgcnv.rest");
        beanConfig.setScan(true);
    }

    /**
     * Mapping of either servlet or filter depends on your own needs.
     * @return Set<Class<?>>
     */
    @Override
    public final Set<Class<?>> getClasses() {
        Set<Class<?>> resources = new HashSet<Class<?>>();
        resources.add(RestImageResource.class);
        resources.add(
               com.wordnik.swagger.jaxrs.listing.ApiListingResourceJSON.class);
        resources.add(
               com.wordnik.swagger.jaxrs.listing.ApiDeclarationProvider.class);
        resources.add(
               com.wordnik.swagger.jaxrs.listing.ResourceListingProvider.class);
        //http://localhost:8080/imgcnv/api-docs/
        // http://localhost:8080/imgcnv/api-docs/rest
        //http://localhost:8080/imgcnvs/#!/rest/getJobsInfo
        return resources;
    }
}
