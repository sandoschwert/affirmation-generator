package edu.matc.service;

import edu.matc.entity.Affirmation;
import edu.matc.persistence.AffirmationDao;
import org.apache.log4j.Logger;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;

import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.*;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.servlet.*;


/**
 * The affirmation web service class that defines the endpoints
 * for the affirmation api webservice
 *
 * @author Todd Kinsman
 * @since 10/27/16
 */
@Path("/affirmationservice")
public class AffirmationService  {

    private final Logger log = Logger.getLogger(this.getClass());
    AffirmationDao affirmationDao = new AffirmationDao();
    private static final String SUCCESS_RESULT="<result>success</result>";
    private static final String FAILURE_RESULT="<result>failure</result>";

    /**
     * Function for endpoint to retrieve all non-nsfw affirmations
     * @return affirmationList List<Affirmation>
     */
    @GET
    @Path("/affirmations")
    @Produces(MediaType.APPLICATION_JSON)
    public List<Affirmation> getAffirmations() {

        List<Affirmation> affirmationList = affirmationDao.getAllGoodAffirmations();
        return affirmationList;
    }

    /**
     * Function for endpoint to retrieve all affirmations, including nsfw
     * @return affirmationList List<Affirmation>
     */
    @GET
    @Path("/nsfw/affirmations")
    @Produces(MediaType.APPLICATION_JSON)
    public List<Affirmation> getBadAndGoodAffirmations() {

        List<Affirmation> affirmationList = affirmationDao.getAllAffirmations();
        return affirmationList;
    }

    /**
     * Function for endpoint to retrieve all affirmations with a limit
     * @param limit int defining limit to affirmations to retrieve
     * @return affirmationList List<Affirmation>
     */
    @GET
    @Path("/nsfw/affirmations/limit/{limit}")
    @Produces(MediaType.APPLICATION_JSON)
    public List<Affirmation> getAffirmationsWithLimit(@PathParam("limit") int limit) {

        List<Affirmation> affirmationList = affirmationDao.getAllAffirmations();

        List<Affirmation> resultLimit = getLimitedList(affirmationList, limit);

        return resultLimit;

    }

    /**
     * Function for endpoint to retrieve all non-nsfw affirmations with a limit
     * @param limit int defining limit to affirmations to retrieve
     * @return affirmationList List<Affirmation>
     */
    @GET
    @Path("/affirmations/limit/{limit}")
    @Produces(MediaType.APPLICATION_JSON)
    public List<Affirmation> getGoodAndBadAffirmationsWithLimit(@PathParam("limit") int limit) {

        List<Affirmation> affirmationList = affirmationDao.getAllGoodAffirmations();

        List<Affirmation> resultLimit = getLimitedList(affirmationList, limit);

        return resultLimit;

    }

    /**
     * Function for endpoint to retrieve an affirmation with a id
     * @param affirmationId int of affirmation to retrieve
     * @return affirmation Affirmation
     */
    @GET
    @Path("/affirmations/{affirmationId}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getOneAffirmation(@PathParam("affirmationId") int affirmationId) {

        Affirmation affirmation = affirmationDao.getAffirmationWithId(affirmationId);
        return Response.status(200).entity(affirmation).build();
    }


    /**
     * Function for endpoint to add affiraiton to affirmation table, not yet available
     * to endusers
     * @param affirmationId int of affirmation to add, to be deprecated.
     * @param phrase text phrase to add.
     * @param categoryid int the id of the category to add to. Will change to human readable in next version.
     * @return String success or failure
     */
    @POST
    @Path("/affirmations")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_FORM_URLENCODED)
    public String createAffirmation(@FormParam("affirmationId") int affirmationId, @FormParam("phrase") String phrase,
                                    @FormParam("categoryId") int categoryid,
                                    @Context HttpServletResponse servletResponse) throws IOException {

        int ratingDefault = 0;
        Affirmation affirmation = new Affirmation(phrase, ratingDefault, categoryid);
        int result = affirmationDao.addAffirmation(affirmation);

        if(result > 0){
            return SUCCESS_RESULT;
        }
        return FAILURE_RESULT;
    }

    /**
     * Function for endpoint to update an affirmation rating given id. Rating
     * will be incremented by 1.
     * @param affirmationId int of affirmation to update
     * @return affirmation Affirmation incremented
     */
    @PUT
    @Path("/affirmations/upvote/{affirmationId}")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_FORM_URLENCODED)
    public Response upvoteAffirmation(@PathParam("affirmationId") int affirmationId,
                                    @Context HttpServletResponse servletResponse) throws IOException {

        int totalVotes = affirmationDao.upVoteAffirmation(affirmationId);
        Affirmation affirmation = affirmationDao.getAffirmationWithId(affirmationId);
        return Response.status(200).entity(affirmation).build();
    }

    /**
     * Function for endpoint to update an affirmation rating given id. Rating
     * will be decremented by 1.
     * @param affirmationId int of affirmation to downvote
     * @return affirmation Affirmation decremented
     */
    @PUT
    @Path("/affirmations/downvote/{affirmationId}")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_FORM_URLENCODED)
    public Response downvoteAffirmation(@PathParam("affirmationId") int affirmationId) throws IOException {

        int totalVotes = affirmationDao.downVoteAffirmation(affirmationId);
        Affirmation affirmation = affirmationDao.getAffirmationWithId(affirmationId);
        return Response.status(200).entity(affirmation).build();

    }

    /**
     * Function for endpoint to retrieve all affirmations given a category name
     * @param categoryName String of affirmation category
     * @return affirmationList List<Affirmation>
     */
    @GET
    @Path("/affirmations/categories/{categoryName}")
    @Produces(MediaType.APPLICATION_JSON)
    public List<Affirmation> getAllAffirmationsFromCategory(@PathParam("categoryName") String categoryName) {

        AffirmationDao affirmationDao = new AffirmationDao();

        List<Affirmation> affirmationList = affirmationDao.getAllAffirmationsFromCategory(categoryName);

        return affirmationList;

    }

    /**
     * Function for endpoint to retrieve all popular affirmations given a category name
     * @param categoryName String of affirmation category
     * @param limit int limit of affirmations returned
     * @return affirmationList List<Affirmation>
     */
    @GET
    @Path("/affirmations/popular/{categoryName}/{limit}")
    @Produces(MediaType.APPLICATION_JSON)
    public List<Affirmation> getAllPopularAffirmationsFromCategory(@PathParam("categoryName") String categoryName, @PathParam("limit") int limit) {

        AffirmationDao affirmationDao = new AffirmationDao();
        List<Affirmation> affirmationList = affirmationDao.getMostPopularAffirmations(categoryName, limit);
        return affirmationList;
    }


    /**
     * Function for endpoint to retrieve all affirmations given a category name with limit
     * @param categoryName String of affirmation category
     * @param limit int to limit number of affirmations returned
     * @return affirmationList List<Affirmation>
     */
    @GET
    @Path("/affirmations/categories/{categoryName}/{limit}")
    @Produces(MediaType.APPLICATION_JSON)
    public List<Affirmation> getMostPopularAffirmationByCategory(@PathParam("categoryName") String categoryName, @PathParam("limit") int limit) {

        AffirmationDao affirmationDao = new AffirmationDao();

        List<Affirmation> affirmationList = affirmationDao.getAllAffirmationsFromCategory(categoryName);
        List<Affirmation> resultLimit = new ArrayList<Affirmation>();
        return getLimitedList(affirmationList, limit);


    }

    /**
     * The function used to create a list of a specific size
     * @param affirmations List<Affirmation> to limit
     * @param limit int for list limit
     * @return affirmations List<Affirmation> with of specified limit
     */
    public List<Affirmation> getLimitedList(List<Affirmation> affirmations, int limit) {

        List<Affirmation> resultLimit = new ArrayList<Affirmation>();
        log.info("The affirmaiton limited list function with limit: " + limit);

        if (limit > 0) {

            for(int i = 0; i < limit; i++) {

                Affirmation affirmation = new Affirmation();
                affirmation = affirmations.get(i);
                resultLimit.add(affirmation);
            }
        } else {

            resultLimit = affirmations;

        }

        log.info("Result limit of affs: " + resultLimit);

        return resultLimit;

    }



}
