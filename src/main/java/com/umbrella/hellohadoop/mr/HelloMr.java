package com.umbrella.hellohadoop.mr;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.conf.Configured;
import org.apache.hadoop.mapred.JobConf;
import org.apache.hadoop.util.Tool;
import org.apache.hadoop.util.ToolRunner;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Created by xudazhou on 2017/3/20.
 */
public class HelloMr extends Configured implements Tool {

    private static Logger log = LoggerFactory.getLogger(HelloMr.class);

    public static void main(String[] args) {
        Configuration cfg = new Configuration();
        int res = 0;
        try {
            res = ToolRunner.run(cfg, new HelloMr(), args);
        } catch (Exception e) {
            log.error("hellomr main exception", e);
        }
        System.exit(res);
    }

    @Override
    public int run(String[] args) throws Exception {
        JobConf jobConf = new JobConf(this.getConf(), HelloMr.class);
        jobConf.setMemoryForMapTask(200);
//        jobConf.setMaxPhysicalMemoryForTask(100);
        return 0;
    }
}
