package edu.matc.persistence;

import edu.matc.entity.Affirmation;
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
    AffirmationDao affirmationDao;
    Affirmation affirmationAdd;
    Affirmation affirmationUpdate;
    Affirmation affirmationDelete;

//    in the test/resources dir looks to the hibernate file there first
//    can do it for the lo4j logging files as well

    @Before
    public void setup() {
        affirmationDao = new AffirmationDao();

        affirmationAdd = new Affirmation("Meow meow meow meow", 2, 1);
        affirmationUpdate = new Affirmation("Ruf ruf ruf", 5, 1);
        affirmationDelete = new Affirmation("Meow Delete Meow", 3, 1);

        affirmationDao.addAffirmation(affirmationUpdate);
        affirmationDao.addAffirmation(affirmationDelete);
    }

    @Test
    public void getAllAffirmations() throws Exception {

        List<Affirmation> affirmationList = new ArrayList<Affirmation>();
        affirmationList = affirmationDao.getAllAffirmations();
        System.out.println("The affirmation list is: " + affirmationList);

        assertNotNull(affirmationList);
    }

    @Test
    public void getAffirmationWithId() throws Exception {
        Affirmation affirmation = new Affirmation();
        affirmation = affirmationDao.getAffirmationWithId(1);
        System.out.println("The affirmation list is: " + affirmation);

        assertNotNull(affirmation);
    }

    @Test
    public void addAffirmation() throws Exception {

        int addAffId = affirmationDao.addAffirmation(affirmationAdd);
        System.out.println("The id of the added Affirmation: " + addAffId);
        assertNotNull(addAffId);

    }

    @Test
    public void updateAffirmation() throws Exception {

        Affirmation affirmation = new Affirmation();
        affirmation = affirmationDao.getAffirmationWithId(2);
        affirmation.setPhrase("Ruf updated ruf");

        affirmationDao.updateAffirmation(affirmation);
        System.out.println("This is the update test: " + affirmation);
    }

    @Test
    public void deleteAffirmation() throws Exception {
        Affirmation affirmation = new Affirmation();
        int deleteSize = affirmationDao.getAllAffirmations().size() - 1;
        affirmation = affirmationDao.getAffirmationWithId(deleteSize);
        int delResponse = affirmationDao.deleteAffirmation(affirmation);

        assertEquals("Didn't delete affirmation", 1, delResponse);


    }

}