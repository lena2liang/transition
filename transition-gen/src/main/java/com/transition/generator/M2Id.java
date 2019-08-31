package com.transition.generator;

import com.transition.generator.utils.ShortMd5;
import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.lang3.StringUtils;

public class M2Id {

    public static void main(String[] args) {
        String m = "13000000033";

        System.out.println(~Long.parseLong(m));

        String reverse = StringUtils.reverse(m);
        System.out.println(reverse);
        String sum = Long.parseLong(m) + Long.parseLong(reverse) + "";
        System.out.println(Long.parseLong(m) + Long.parseLong(reverse));


        long s = 13000000033L;
        long k = 5828307069L;

        long e = SimpleCrypt.encrypt(s, k);
        long d = SimpleCrypt.decrypt(e, k);

        System.out.println(e);
        System.out.println(d);

        System.out.println(DigestUtils.md5Hex(e+""));

    }

}
