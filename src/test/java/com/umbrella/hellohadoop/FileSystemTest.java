package com.umbrella.hellohadoop;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;
import org.junit.Test;

import java.io.IOException;
import java.net.URI;

/**
 * Created by xudazhou on 2017/4/18.
 */
public class FileSystemTest {

    @Test
    public void test1() throws IOException {
        Configuration conf = new Configuration();
//        conf.set("fs.defaultFS", "hdfs://192.168.186.3:9000");
        FileSystem fs = FileSystem.get(URI.create("hdfs://192.168.186.3:9000"), conf);

        System.out.println(fs.exists(new Path("/user/hector")));
    }
}
