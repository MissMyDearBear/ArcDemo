package com.bear.arcdemo;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class Test {


    @org.junit.Test
   public void test() {
        int ret = 4/3;
        int ret3 = 15 / 8;



        List<String> versions = new ArrayList<>();
        versions.add("1.5");
        versions.add("1.45.0");
        versions.add("6");
        versions.add("3.3.3.3.3.3");
        versions.add("1.5.66.0.0");

        List<String> result = sortVersions(versions);
        int size = result.size();

    }

    private List<String> sortVersions(List<String> versions) {
        Collections.sort(versions, (x, y) -> {
            String[] xArray = x.split("\\.");
            String[] yArray = y.split("\\.");
            int m = xArray.length;
            int n = yArray.length;
            for (int i = 0; i < Math.min(m, n); i++) {
                if (!xArray[i].equals(yArray[i])) {
                    return Integer.parseInt(xArray[i]) - Integer.parseInt(yArray[i]);
                }
            }
            return m - n;
        });
        return versions;
    }


}
