package com.hc.wx.mp.utils;


import javassist.ClassPool;
import javassist.CtClass;
import javassist.CtMethod;
import javassist.NotFoundException;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.Scanner;
import java.util.zip.GZIPInputStream;

public class Test {
    public static void main(String[] args) throws IOException {

        System.out.println(new String(new byte[]{-31, -66, -69, -32, -86, -75},
                StandardCharsets.UTF_8));

    }

}
