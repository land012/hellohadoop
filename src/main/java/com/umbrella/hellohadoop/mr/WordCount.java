package com.umbrella.hellohadoop.mr;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.conf.Configured;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.io.compress.GzipCodec;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.Mapper;
import org.apache.hadoop.mapreduce.Reducer;
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;
import org.apache.hadoop.mapreduce.lib.output.MultipleOutputs;
import org.apache.hadoop.mapreduce.lib.output.SequenceFileOutputFormat;
import org.apache.hadoop.mapreduce.lib.output.TextOutputFormat;
import org.apache.hadoop.util.Tool;
import org.apache.hadoop.util.ToolRunner;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.Arrays;
import java.util.StringTokenizer;

/**
 * Created by xudazhou on 2017/4/18.
 * SequenceFile
 * GzipCodec
 */
public class WordCount extends Configured implements Tool {

    private static Logger log = LoggerFactory.getLogger(WordCount.class);

    public static void main(String[] args) throws Exception {
        log.info("main args={}", Arrays.toString(args));

        Configuration conf = new Configuration();
//        String[] remainArgs = new GenericOptionsParser(conf, args).getRemainingArgs();
        ToolRunner.run(conf, new WordCount(), args);
    }

    @Override
    public int run(String[] args) throws Exception {
        log.info("run args={}", Arrays.toString(args));

        Configuration conf = this.getConf();

        Path inputpath = new Path(args[0]);
        Path outputpath = new Path(args[1]);
        conf.set("output1", args[2]);
        conf.set("output2", args[3]);

        FileSystem fs = FileSystem.get(conf);
        if (fs.exists(outputpath)) {
            fs.delete(outputpath, true);
        }
        fs.close();

        Job job = Job.getInstance(conf, "wordcount");
        job.setJarByClass(WordCount.class);

        job.setMapperClass(WCMapper.class);
        /*
         * 不配置则会报错
         * Type mismatch in key from map: expected org.apache.hadoop.io.LongWritable, received org.apache.hadoop.io.Text
         */
        job.setMapOutputKeyClass(Text.class);
        job.setMapOutputValueClass(IntWritable.class);

        job.setReducerClass(WCReducer.class);
        job.setOutputKeyClass(Text.class);
        job.setOutputValueClass(IntWritable.class);

//        job.setInputFormatClass(TextInputFormat.class);

        job.setOutputFormatClass(SequenceFileOutputFormat.class);

        FileInputFormat.addInputPath(job, inputpath);
        FileOutputFormat.setOutputPath(job, outputpath);
//        FileOutputFormat.setCompressOutput(job, true);
//        FileOutputFormat.setOutputCompressorClass(job, GzipCodec.class);

        MultipleOutputs.addNamedOutput(job, "wc1", TextOutputFormat.class, Text.class, IntWritable.class);
        MultipleOutputs.addNamedOutput(job, "wc2", TextOutputFormat.class, Text.class, IntWritable.class);

        return job.waitForCompletion(true) ? 0 : 1;
    }

    static class WCMapper extends Mapper<Object, Text, Text, IntWritable> {
        @Override
        protected void map(Object key, Text value, Context context) throws IOException, InterruptedException {
            StringTokenizer st = new StringTokenizer(value.toString());
            while (st.hasMoreTokens()) {
                context.write(new Text(st.nextToken()), new IntWritable(1));
            }
        }
    }

    static class WCReducer extends Reducer<Text, IntWritable, Text, IntWritable> {

        private MultipleOutputs mos;
        String output1;
        String output2;

        @Override
        protected void setup(Context context) throws IOException, InterruptedException {
            mos = new MultipleOutputs(context);
            Configuration cfg = context.getConfiguration();
            output1 = cfg.get("output1");
            output2 = cfg.get("output2");
            log.info("output1={}, output2={}", output1, output2);
        }

        @Override
        protected void reduce(Text key, Iterable<IntWritable> values, Context context) throws IOException, InterruptedException {
            int sum = 0;
            for (IntWritable i : values) {
                sum += i.get();
            }
            context.write(key, new IntWritable(sum));

            mos.write("wc1", key, new IntWritable(sum), output1 + "/");
            mos.write("wc2", key, new IntWritable(sum*2), output2 + "/");

//            String v = String.valueOf(sum);
//            context.write(key, new BytesWritable(v.getBytes("utf-8")));
        }
    }
}
