package com.umbrella.hellohadoop.mr;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.conf.Configured;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.util.Tool;
import org.apache.hadoop.util.ToolRunner;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Created by xudazhou on 2017/4/11.
 */
public class HelloFS extends Configured implements Tool {

    private static Logger log = LoggerFactory.getLogger(HelloFS.class);

    public static void main(String[] args) {
        Configuration cfg = new Configuration();
        int res = -1;
        try {
            res = ToolRunner.run(cfg, new HelloFS(), args);
        } catch (Exception e) {
            log.error("hellofs main exception", e);
        }
        System.exit(res);
    }

    @Override
    public int run(String[] args) throws Exception {
        Configuration cfg = this.getConf();

        FileSystem fs = FileSystem.get(cfg);
        fs.close();

        return 0;
    }
}
