package test.ui;

import dataaccess.Repository;
import model.File;
import org.apache.log4j.BasicConfigurator;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Listeners;
import org.testng.annotations.Test;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Listeners(config.ListenerTest.class)
public class UserStory03_db {

    private static final Logger logger = LogManager.getLogger(UserStory03_db.class);

    private String filePath;

    @BeforeClass
    public void setUp() {

        BasicConfigurator.configure();
    }

    /**
     * User Story 03/AC4
     */
    @Test(priority = 1, description = "Verify data in the DB")
    public void fetchFileFromDB() {
        logger.info("invoked fetchHeroFromDB test");
        Repository file = new Repository();
        File dbFile = file.getFile();
        this.filePath = dbFile.getFilePath();
        Assert.assertEquals(dbFile.getFileType(), "TAX_RELIEF", "File type not found");
        Assert.assertTrue(dbFile.getFileStatus().equals("PROCESSING")
                || dbFile.getFileStatus().equals("COMPLETED")
                || dbFile.getFileStatus().equals("ERROR"), "Invalid file type");
    }

    /**
     * User Story 03/AC2
     */
    @Test(priority = 2)
    public void validateData() throws IOException {
        System.out.println(this.filePath);
        FileReader fileReader = new FileReader(this.filePath);
        BufferedReader bufferedReader = new BufferedReader(fileReader);
        String line;
        List<String> fileContent = new ArrayList<>();
        while ((line = bufferedReader.readLine()) != null) {
            fileContent.add(line);
        }
        for (int i = 0; i < fileContent.size() - 1; i++) {
            Assert.assertTrue(fileContent.get(i).contains("natid"));
        }
        Assert.assertEquals(Integer.parseInt(fileContent.get(fileContent.size() - 1)), (fileContent.size() - 1));
        bufferedReader.close();
        fileReader.close();
    }

}
