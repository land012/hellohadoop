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

        /*
          * -rw-r--r--   1 land supergroup          0 2017-04-12 15:44 /user/hector/file1.txt
          * 如果文件已经存在，仍然能创建，且会覆盖原文件，更新时间戳
          * -rw-r--r--   1 land supergroup          0 2017-04-12 15:45 /user/hector/file1.txt
          */

//        FSDataOutputStream fdos = fs.create(new Path("/user/hector/file1.txt"));
//        fdos.close();

        /*
         * -rw-r--r--   1 land supergroup          0 2017-04-12 15:44 /user/hector/file2.txt
         * 如果文件已经存在，则返回失败
         */
//        log.info("createNewFile={}", fs.createNewFile(new Path("/user/hector/file2.txt")));

        /*
         * drwxr-xr-x   - land supergroup          0 2017-04-12 15:44 /user/hector/dir1
         * 如果目录已经存在，仍然返回成功，应该没有任何变化
         */
//        log.info("mkdir={}", fs.mkdirs(new Path("/user/hector/dir1")));

        fs.close();

        return 0;
    }
}
