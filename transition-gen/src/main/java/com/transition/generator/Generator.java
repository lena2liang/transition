package com.transition.generator;

import com.transition.generator.utils.ShortMd5;
import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.LineIterator;
import org.apache.commons.lang3.StringUtils;

import java.io.File;
import java.io.FileWriter;
import java.util.Random;

public class Generator {

    static long KEY = 5828307069851651206L;

    public static void gen(String file) throws Exception {

        LineIterator it = FileUtils.lineIterator(new File(file), "UTF-8");
        FileWriter writer = new FileWriter(String.format("./gen-location.txt"), false);
        FileWriter agewriter = new FileWriter(String.format("./gen-age.txt"), false);

        Random random = new Random();
        try {
            while (it.hasNext()) {
                String line = it.nextLine();
                System.out.println(line);
                String[] ret = StringUtils.splitPreserveAllTokens(line, ',');
                if (ret.length != 3) continue;

                Long number = Long.parseLong(ret[0]) * 10000;

                for (int i = 0; i< 10000; i++) {
                    number ++;
                    String id = ShortMd5.shortMd5(DigestUtils.md5Hex(SimpleCrypt.encrypt(number, KEY) + ""));
                    writer.write( id + "\t" + ret[1] + "-" + ret[2] + "\n");
                    agewriter.write(id + "\t" + random.nextInt(60) + "\n");
                }

            }
        } finally {
            LineIterator.closeQuietly(it);
            writer.close();
        }

    }

    public static void main(String[] args) {
        try {
            if (args.length != 1) {
                System.out.println("Pls input file");
                System.exit(1);
            }
            gen(args[0]);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
