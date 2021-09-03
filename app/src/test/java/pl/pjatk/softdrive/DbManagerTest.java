package pl.pjatk.softdrive;

import static org.junit.Assert.assertEquals;

import androidx.test.ext.junit.runners.AndroidJUnit4;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import pl.pjatk.softdrive.database.DbManager;
import pl.pjatk.softdrive.rest.controllers.ActivitySingleton;

@RunWith(AndroidJUnit4.class)
public class DbManagerTest {

    DbManager db;

    @Before
    public void setUp() {
        db = new DbManager(ActivitySingleton.getInstance());
    }

    @Test
    public void whenCommitDatabase_givenDistanceValue_thenReadSameFromDatabase() throws InterruptedException {
        //GIVEN
        db.setDistance(234);
        //WHEN
        db.dbCommit();
        //THEN
        assertEquals(db.getDbDistance(), 234);
    }

}
