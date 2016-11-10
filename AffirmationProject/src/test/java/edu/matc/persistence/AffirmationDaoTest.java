package edu.matc.persistence;

import edu.matc.entity.Affirmation;
import org.apache.log4j.Logger;
import org.hibernate.Session;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

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

    int affDelId;
    int affUpdId;
    int affGetId;

    DatabaseSetupDao databaseSetupDao;

//    in the test/resources dir looks to the hibernate file there first
//    can do it for the lo4j logging files as well

    @Before
    public void setup() {

        databaseSetupDao = new DatabaseSetupDao();
        databaseSetupDao.clearAllDataFromAffirmationTable();

        affirmationDao = new AffirmationDao();

        affirmationAdd = new Affirmation("Meow meow meow meow", 2, 17);
        affirmationUpdate = new Affirmation("Ruf ruf ruf", 5, 17);
        affirmationDelete = new Affirmation("Meow Delete Meow", 3, 18);

        affGetId = affirmationDao.addAffirmation(affirmationAdd);
        affUpdId = affirmationDao.addAffirmation(affirmationUpdate);
        affDelId = affirmationDao.addAffirmation(affirmationDelete);
    }

    @Test
    public void getAllAffirmations() throws Exception {

        List<Affirmation> affirmationList = new ArrayList<Affirmation>();
        affirmationList = affirmationDao.getAllAffirmations();
        log.info("The affirmation list is: " + affirmationList);

        assertNotNull(affirmationList);
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

}