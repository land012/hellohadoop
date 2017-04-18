package com.umbrella.hellohadoop.mr;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.conf.Configured;
import org.apache.hadoop.filecache.DistributedCache;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.Mapper;
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;
import org.apache.hadoop.mapreduce.lib.input.TextInputFormat;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;
import org.apache.hadoop.mapreduce.lib.output.MultipleOutputs;
import org.apache.hadoop.mapreduce.lib.output.TextOutputFormat;
import org.apache.hadoop.util.Tool;
import org.apache.hadoop.util.ToolRunner;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.net.URI;
import java.util.StringTokenizer;

/**
 *
 * Created by xudazhou on 2017/3/20.
 * DistributedCache
 * 多目录输出
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
        Configuration cfg = this.getConf();

        DistributedCache.addCacheFile(new URI("/user/hector/file1.txt"), cfg);

        Path inpath = new Path(args[0]);
        Path outputpath = new Path(args[1]);

        FileSystem fs = FileSystem.get(cfg);

        if (fs.exists(outputpath)) {
            fs.delete(outputpath, true);
        }
        fs.close();

        Job job = Job.getInstance(cfg, "hellomr");
        job.setJarByClass(HelloMr.class);
        job.setNumReduceTasks(0);

        job.setMapperClass(HelloMapper.class);

        job.setInputFormatClass(TextInputFormat.class);
        job.setOutputFormatClass(TextOutputFormat.class);

        FileInputFormat.addInputPath(job, inpath);
//        FileOutputFormat.setOutputPath(job, outputpath);

        job.waitForCompletion(true);
        return 0;
    }

    static class HelloMapper extends Mapper<Object, Text, Text, IntWritable> {

        MultipleOutputs<Text, IntWritable> mos;

        @Override
        protected void setup(Context context) throws IOException, InterruptedException {
            // 在 hadoop 集群节点上的路径
            Path[] paths = DistributedCache.getLocalCacheFiles(context.getConfiguration());
            for (Path p : paths) {
                log.info("p={}", p);
            }

            // 在 hdfs 上的路径
            URI[] uris = DistributedCache.getCacheFiles(context.getConfiguration());
            for (URI u : uris) {
                log.info("uri={}", u);
            }

            mos = new MultipleOutputs<Text, IntWritable>(context);
        }

        @Override
        protected void map(Object key, Text value, Context context) throws IOException, InterruptedException {
            StringTokenizer st = new StringTokenizer(value.toString());
            while (st.hasMoreTokens()) {
                Text key1 = new Text(st.nextToken());
//                context.write(key1, new IntWritable(1));
                mos.write(key1, new IntWritable(1), "hello/");
                mos.write(key1, new IntWritable(2), "world/");
            }
        }

        @Override
        protected void cleanup(Context context) throws IOException, InterruptedException {
            if (mos != null) {
                mos.close();
            }
        }
    }
}
