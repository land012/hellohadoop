package com.umbrella.hellohadoop;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.FSDataOutputStream;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;

import java.io.IOException;

/**
 * create by xudazhou 2019/1/10
 */
public class FileSystemDemo {
    public static void main(String[] args) {
        Configuration conf = new Configuration();
        conf.set("fs.defaultFS", "");

        try {
            FileSystem fs = FileSystem.get(conf);
            Path file = new Path("hdfs://xxx/yy");
            // 写文件
            FSDataOutputStream fdos = fs.create(file, true);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
