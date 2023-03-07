package com.hc.wx.mp.utils;

import java.io.File;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Test {
    public static void main(String[] args) {
        File file = new File("/Users/liuhaicheng/Downloads/第5季.全12集/");
        File[] files = file.listFiles();
        for (File f : files) {
            if (f.getName().length() > 10) {
                String substring = f.getName().substring(0, f.getName().substring(0, f.getName().lastIndexOf(".")).lastIndexOf(".")+1);
               // System.out.println(f.getName().replace(substring, ""));
                System.out.println();
                f.renameTo(new File(f.getParentFile().getAbsolutePath()+"/"+f.getName().replace(substring,"")));
            }
        }
    }
}
