package edu.matc.persistence;

import edu.matc.entity.Affirmation;
import org.apache.log4j.Logger;
import org.hibernate.Session;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import static org.junit.Assert.*;

/**
 * Created by toddkinsman on 10/24/16.
 */
public class AffirmationDaoTest {

    private final Logger log = Logger.getLogger(this.getClass());

    AffirmationDao affirmationDao;
    Affirmation affirmationAdd;
    Affirmation affirmationUpdate;
    Affirmation affirmationDelete;
    Affirmation badAffirmation;
    Affirmation popAffirmation;
    Affirmation upVoteAffirmation;
    Affirmation downVoteAffirmation;

    int affDelId;
    int affUpdId;
    int affGetId;
    int badAff;
    int popAff;
    int upVoteAff;
    int downVoteAff;

    DatabaseSetupDao databaseSetupDao;

//    in the test/resources dir looks to the hibernate file there first
//    can do it for the lo4j logging files as well

    @Before
    public void setup() {
        //properties path
        propertiesFilePath = "/api_config.properties";

        // load the properites file and writer
        loadProperties(propertiesFilePath);

        databaseSetupDao = new DatabaseSetupDao();
        databaseSetupDao.clearAllDataFromAffirmationTable();

        affirmationDao = new AffirmationDao();

        affirmationAdd = new Affirmation("Meow meow meow meow", 2, 17);
        affirmationUpdate = new Affirmation("Ruf ruf ruf", 5, 17);
        affirmationDelete = new Affirmation("Meow Delete Meow", 3, 18);
        badAffirmation = new Affirmation("Fuck this", 4, 10);
        popAffirmation = new Affirmation("Yay this", 8, 3);
        upVoteAffirmation = new Affirmation("Vote me up", 6, 3);
        downVoteAffirmation = new Affirmation("vote me down", 8, 3);

        affGetId = affirmationDao.addAffirmation(affirmationAdd);
        affUpdId = affirmationDao.addAffirmation(affirmationUpdate);
        affDelId = affirmationDao.addAffirmation(affirmationDelete);
        badAff = affirmationDao.addAffirmation(badAffirmation);
        popAff = affirmationDao.addAffirmation(popAffirmation);
        upVoteAff = affirmationDao.addAffirmation(upVoteAffirmation);
        downVoteAff = affirmationDao.addAffirmation(downVoteAffirmation);

    }

    @Test
    public void getAllAffirmationsWithBad() throws Exception {

        List<Affirmation> affirmationList = new ArrayList<Affirmation>();
        affirmationList = affirmationDao.getAllAffirmations();
        log.info("The affirmation list is: " + affirmationList);
        int affirmationCount = affirmationList.size();
        log.info("Afflist size: " + affirmationCount);

        assertEquals("List size suggests bad aff not included", 7, affirmationCount);
        assertNotNull(affirmationList);
    }

    @Test
    public void getAllAffirmationsGood() throws Exception {

        List<Affirmation> affirmationList = new ArrayList<Affirmation>();
        affirmationList = affirmationDao.getAllGoodAffirmations();
        log.info("The affirmation list is: " + affirmationList);

        int affirmationCount = affirmationList.size();
        log.info("Afflist size good: " + affirmationCount);

        assertEquals("List size suggests bad aff included", 6, affirmationCount);
    }

    @Test
    public void getAffirmationWithId() throws Exception {

        Affirmation affirmation = new Affirmation();
        affirmation = affirmationDao.getAffirmationWithId(affGetId);
        log.info("The affirmation is: " + affirmation);

        assertNotNull(affirmation);
    }

    @Test
    public void addAffirmation() throws Exception {

        int addAffId = affirmationDao.addAffirmation(affirmationAdd);
        log.info("The id of the added Affirmation: " + addAffId);
        assertNotNull(addAffId);

    }

    @Test
    public void updateAffirmation() throws Exception {

        Affirmation affirmation = new Affirmation();
        affirmation = affirmationDao.getAffirmationWithId(affUpdId);

        String updatePhrase = "Ruf updated ruf";
        affirmation.setPhrase(updatePhrase);
        affirmationDao.updateAffirmation(affirmation);

        String retrievedUpdatePhrase = affirmationDao.getAffirmationWithId(affUpdId).getPhrase();

        log.info("This is the update test: " + affirmation);
        assertEquals("The update did not work", updatePhrase, retrievedUpdatePhrase);
    }


    @Test
    public void deleteAffirmation() throws Exception {

        Affirmation affirmation = new Affirmation();
        affirmation = affirmationDao.getAffirmationWithId(affDelId);
        int delResponse = affirmationDao.deleteAffirmation(affirmation);

        log.info("The delete id is: " + affDelId);

        assertEquals("Didn't delete affirmation", 1, delResponse);


    }

    @Test
    public void getAffFromCategoryTest() {

        List<Affirmation> affirmationList = new ArrayList<Affirmation>();
        String categoryName = "Success";
        affirmationList = affirmationDao.getAllAffirmationsFromCategory(categoryName);

        log.info("The list of aff by category: " + affirmationList);

        assertEquals("The category name didn't get results", 2, affirmationList.size());
    }

    @Test
    public void testMostPopularByCategoryWithLimit() {

        List<Affirmation> affirmationList = new ArrayList<Affirmation>();
        affirmationList = affirmationDao.getMostPopularAffirmations("Success", 1);

        for (int i = 0; i < affirmationList.size(); i++) {
            Affirmation affirmation = new Affirmation();
            affirmation = affirmationList.get(i);
            log.info("The most popular from success category: " + affirmation.getRating() + "The Rating: " + affirmation.getRating());
        }

        assertEquals("List size suggets that retunred limit not working", 1, affirmationList.size());

    }

    @Test
    public void testMostPopulaWithLimit() {

        List<Affirmation> affirmationList = new ArrayList<Affirmation>();
        affirmationList = affirmationDao.getAllAffirmations();
        Affirmation affirmation = new Affirmation();
        affirmation = affirmationList.get(0);

        int affRating = affirmation.getRating();
        log.info("Popular affirmation rating" + affRating);

        assertEquals("Rating not the most popular", 8, affRating);

    }

    @Test
    public void testUpvote() {

        Affirmation affirmation = new Affirmation();

        // should return updated rating/vote
        int voteCount = affirmationDao.upVoteAffirmation(upVoteAff);

        assertEquals("The upvote count is off", 7, voteCount);
    }

    @Test
    public void testDownvote() {

        Affirmation affirmation = new Affirmation();

        // should return updated rating/vote
        int voteCount = affirmationDao.downVoteAffirmation(downVoteAff);

        assertEquals("The downvote count is off", 7, voteCount);

    }

    private Properties properties;
    private String propertiesFilePath;


    /**
     * Loads the properties file based on the path argument passed to it.
     *
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

    @Test
    public void testPropertiesfile() {

        String name = properties.getProperty("author.test");
        log.info("The property name: " + name);

        assertEquals("The properties file isn't broken", "teststring", name);
    }

}