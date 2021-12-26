package unittest;

import org.junit.Test;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static org.junit.jupiter.api.Assumptions.assumeTrue;

public class StupidPojoTestCase {

    private StupidPojo stupidPojo;
    private static Logger LOG = LoggerFactory.getLogger(StupidPojoTestCase.class);

    @BeforeAll
    static void suiteStartup(){
        LOG.info("Beginning test harnesses.");
    }

    @BeforeEach
    public void init(){
        stupidPojo = new StupidPojo();
    }

    @DisplayName("Testing int field of StupidPojo class.")
    @Test
    public void testIntegerField(){
        stupidPojo.setIntegerField(5);
        assumeTrue(stupidPojo.getIntegerField() != null, "Assumption of non-nullity for int field failed.");
    }
    @AfterAll
    static void suiteTeardown(){
        LOG.info("Test suite done.");
    }
}