package edu.matc.service;

import edu.matc.entity.Affirmation;
import edu.matc.persistence.AffirmationDao;

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
 * Created by toddkinsman on 10/27/16.
 */
@Path("/affirmationservice")
public class AffirmationService  {

    AffirmationDao affirmationDao = new AffirmationDao();
    private static final String SUCCESS_RESULT="<result>success</result>";
    private static final String FAILURE_RESULT="<result>failure</result>";


    @GET
    @Path("/affirmations")
    @Produces(MediaType.APPLICATION_JSON)
    public List<Affirmation> getAffirmations() {

        List<Affirmation> affirmationList = affirmationDao.getAllAffirmations();
        return affirmationList;
    }

    @GET
    @Path("/affirmations/{affirmationId}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getOneAffirmation(@PathParam("affirmationId") int affirmationId) {

        Affirmation affirmation = affirmationDao.getAffirmationWithId(affirmationId);
        return Response.status(200).entity(affirmation).build();
    }



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

    @PUT
    @Path("/affirmations/downvote/{affirmationId}")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_FORM_URLENCODED)
    public Response downvoteAffirmation(@PathParam("affirmationId") int affirmationId) throws IOException {

        int totalVotes = affirmationDao.downVoteAffirmation(affirmationId);
        Affirmation affirmation = affirmationDao.getAffirmationWithId(affirmationId);
        return Response.status(200).entity(affirmation).build();

    }

    @GET
    @Path("/affirmations/categories/{categoryName}")
    @Produces(MediaType.APPLICATION_JSON)
    public List<Affirmation> getAllAffirmationsFromCategory(@PathParam("categoryName") String categoryName) {

        AffirmationDao affirmationDao = new AffirmationDao();

        List<Affirmation> affirmationList = affirmationDao.getAllAffirmationsFromCategory(categoryName);

        return affirmationList;

    }

    @GET
    @Path("/affirmations/categories/{categoryName}/{limit}")
    @Produces(MediaType.APPLICATION_JSON)
    public List<Affirmation> getAllAffirmationsFromCategory(@PathParam("categoryName") String categoryName, @PathParam("limit") int limit) {

        AffirmationDao affirmationDao = new AffirmationDao();

        List<Affirmation> affirmationList = affirmationDao.getAllAffirmationsFromCategory(categoryName);
        List<Affirmation> resultLimit = new ArrayList<Affirmation>();

        if (limit > 0) {

            for(int i = 0; i < limit; i++) {

                Affirmation affirmation = new Affirmation();
                affirmation = affirmationList.get(i);
                resultLimit.add(affirmation);
            }
        } else {

            resultLimit = affirmationList;
        }

        return resultLimit;
    }






}
