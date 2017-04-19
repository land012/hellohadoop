package com.umbrella.hellohadoop;

import com.umbrella.hellohadoop.mr.HelloFS;
import com.umbrella.hellohadoop.mr.HelloMr;
import com.umbrella.hellohadoop.mr.WordCount;
import org.apache.hadoop.util.ProgramDriver;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Created by xudazhou on 2017/4/11.
 */
public class MainDriver {

    private static Logger log = LoggerFactory.getLogger(MainDriver.class);

    public static void main(String[] args) {
        ProgramDriver pd = new ProgramDriver();

        int exitCode = -1;
        try {
            pd.addClass("hellomr", HelloMr.class, "hellomr");
            pd.addClass("hellofs", HelloFS.class, "hellofs");
            pd.addClass("wordcount", WordCount.class, "wordcount");
            exitCode = pd.run(args);
        } catch (Throwable throwable) {
            log.error("main exception", throwable);
        }

        System.exit(exitCode);
    }
}
