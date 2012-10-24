package com.liveramp.cascading_ext;

import com.liveramp.cascading_ext.bloom.BloomConstants;
import junit.framework.TestCase;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;
import org.apache.log4j.Level;
import org.apache.log4j.Logger;

/**
 * @author eddie
 */
public abstract class BaseTestCase extends TestCase {
  private static final String TEST_ROOT = "/tmp/cascading_ext_tests";
  protected static final FileSystem fs = FileSystemHelper.getFS();

  static {
    Logger.getRootLogger().setLevel(Level.ALL);
  }

  public BaseTestCase() {
    // set the default job polling interval to 10ms. this makes the tests run *much* faster.
    CascadingUtil.get().setDefaultProperty("cascading.flow.job.pollinginterval", 10);
    CascadingUtil.get().setDefaultProperty("io.sort.mb", 1);
    CascadingUtil.get().setDefaultProperty("io.sort.record.percent", 0.10);
  }

  @Override
  protected void setUp() throws Exception {
    super.setUp();
    BloomConstants.DEFAULT_BLOOM_FILTER_BITS = 10;
    fs.delete(new Path(TEST_ROOT), true);
    System.err.println("------ test start ------");
    System.out.println("------ test start ------");
  }

  @Override
  public void tearDown() throws Exception {
    super.tearDown();
  }

  protected String getTestRoot() {
    return TEST_ROOT;
  }
}
