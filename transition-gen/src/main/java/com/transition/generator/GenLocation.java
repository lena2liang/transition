package com.transition.generator;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.LineIterator;
import org.apache.commons.lang3.StringUtils;

import java.io.File;
import java.io.FileWriter;

public class GenLocation {

    public static void gen(String file) throws Exception {

        LineIterator it = FileUtils.lineIterator(new File(file), "UTF-8");
        FileWriter writer = new FileWriter(String.format("./location.txt"), false);
        try {
            while (it.hasNext()) {
                String line = it.nextLine();

                String[] ret = StringUtils.splitPreserveAllTokens(line);
                if (ret.length != 3) continue;

                Long number = Long.parseLong(ret[0]) * 10000;

                for (int i = 0; i< 10000; i++) {
                    number ++;
                    writer.write(number + '\t' + ret[1] + "-" + ret[2] + "\n");
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
