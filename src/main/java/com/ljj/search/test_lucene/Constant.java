package com.ljj.search.test_lucene;

import java.io.File;

import org.apache.lucene.util.Version;

public class Constant {
    public static final String USERDIR = System.getProperty("user.dir");
    public static final String INDEXPATH = USERDIR + File.separator + "index";
    public static final Version VERSION = Version.LUCENE_48;
}
