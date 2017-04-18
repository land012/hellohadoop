package com.umbrella.hellohadoop.mr;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.conf.Configured;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.util.Tool;
import org.apache.hadoop.util.ToolRunner;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Created by xudazhou on 2017/4/18.
 */
public class WordCount extends Configured implements Tool {

    private static Logger log = LoggerFactory.getLogger(WordCount.class);

    public static void main(String[] args) throws Exception {
        ToolRunner.run(new Configuration(), new WordCount(), args);
    }

    @Override
    public int run(String[] args) throws Exception {
        Configuration cfg = this.getConf();

        Job job = Job.getInstance(cfg, "wordcount");
        job.setJarByClass(WordCount.class);

        return 0;
    }
}
