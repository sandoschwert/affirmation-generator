package edu.matc.persistence;

import edu.matc.entity.Affirmation;
import edu.matc.entity.Category;
import org.apache.log4j.Logger;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;


/**
 * Test class for category dao class. Tests all functions work using junit.
 *
 * @author Todd Kinsman
 * @since 10/24/16.
 */
public class CategoryDaoTest {

    private final Logger log = Logger.getLogger(this.getClass());

    CategoryDao categoryDao;
    Category categoryAdd;
    Category categoryUpdate;
    Category categoryDel;

    int catAddId;
    int catUpId;
    int catDelId;

    DatabaseSetupDao databaseSetupDao;

    /**
     * The setup for the tests. Clears test database, adds need test data.
     */
    @Before
    public void setup() {

        databaseSetupDao = new DatabaseSetupDao();
        databaseSetupDao.clearAllDataFromCategoryTable();

        categoryDao = new CategoryDao();

        categoryAdd = new Category("Success");
        categoryUpdate = new Category("Happiness");
        categoryDel = new Category("Sadness");

        catAddId = categoryDao.addCategory(categoryAdd);
        catUpId = categoryDao.addCategory(categoryUpdate);
        catDelId = categoryDao.addCategory(categoryDel);

    }

    /**
     * Test to get all categories.
     * @throws Exception
     */
    @Test
    public void getAllCategories() throws Exception {

        List<Category> categoryList = new ArrayList<Category>();
        categoryList = categoryDao.getAllCategories();
        log.info("The category list is: " + categoryList);

        assertNotNull(categoryList);

    }

    /**
     * Test to get a category by Id
     * @throws Exception
     */
    @Test
    public void getACategoryWithId() throws Exception {

        Category category = new Category();
        category = categoryDao.getACategoryWithId(catAddId);
        log.info("The category is: " + category);

        assertNotNull(category);


    }

    /**
     * Test to add a category.
     * @throws Exception
     */
    @Test
    public void addCategory() throws Exception {

        int addAffId = categoryDao.addCategory(categoryAdd);
        log.info("The id of the added category: " + addAffId);
        assertNotNull(addAffId);

    }

    /**
     * Test to update a category.
     *
     * @throws Exception
     */
    @Test
    public void updateCategory() throws Exception {

        Category category = new Category();
        category = categoryDao.getACategoryWithId(catUpId);

        String categoryNameUp = "Love";
        category.setCategoryName(categoryNameUp);

        categoryDao.updateCategory(category);

        String updatedCatName = categoryDao.getACategoryWithId(catUpId).getCategoryName();


        log.info("This is the update test: " + category);
        assertEquals("Category update not successful", categoryNameUp, updatedCatName);
    }

    /**
     * Test to delete a category
     *
     * @throws Exception
     */
    @Test
    public void deleteCategory() throws Exception {
        Category category = new Category();
        category = categoryDao.getACategoryWithId(catDelId);
        int delResponse = categoryDao.deleteCategory(category);

        log.info("The delete id is: " + catDelId);

        assertEquals("Didn't delete category", 1, delResponse);
    }

    /**
     * Test to get category id given category name.
     */
    @Test
    public void getCategoryIdByName() {

        String categoryName = "Success";
        int categoryId = categoryDao.getCategoryIdOfCategory(categoryName);
        log.info("The category id for success: " + categoryId);
        int catInt = categoryDao.getACategoryWithId(categoryId).getCatgoryId();

        assertEquals("Didn't get category ID", categoryId, catInt);
    }

}