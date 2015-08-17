package com.github.javaparser;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;

/**
 * Created by federico on 17/08/15.
 */
public class JDKParser {

    private static int ok = 0;
    private static int ko = 0;

    private static List<String> brokenFiles = Arrays.asList(/*"/home/federico/Downloads/openjdk/hotspot/test/runtime/8007320/ConstMethodTest.java",
            "/home/federico/Downloads/openjdk/jdk/src/macosx/classes/sun/font/CFont.java",*/
            "/home/federico/Downloads/openjdk/jdk/src/share/classes/sun/nio/cs/ext/IBM964.java" /*slow*/,
            "/home/federico/Downloads/openjdk/jdk/src/share/classes/sun/nio/cs/ext/IBM33722.java" /*slow*/ /*,
            "/home/federico/Downloads/openjdk/jdk/test/java/lang/annotation/TypeAnnotationReflection.java",
            "/home/federico/Downloads/openjdk/jdk/test/java/lang/annotation/typeAnnotations/ConstructorReceiverTest.java"*/);

    // BROKE /home/federico/Downloads/openjdk/jdk/test/java/lang/annotation/typeAnnotations/GetAnnotatedReceiverType.java
    // BROKE /home/federico/Downloads/openjdk/jdk/test/java/lang/management/MemoryMXBean/CollectionUsageThreshold.java
    // BROKE /home/federico/Downloads/openjdk/jdk/test/java/util/WeakHashMap/GCDuringIteration.java


    public static void parse(File file) throws IOException, ParseException {
        if (file.isDirectory()) {
            for (File child : file.listFiles()) {
                parse(child);
            }
        } else if (file.getName().endsWith(".java")) {
            if (!brokenFiles.contains(file.getAbsolutePath())) {
                try {
                    JavaParser.parse(file, "UTF-8", false);
                    ok++;
                    if ((ok % 500) == 0) {
                        System.err.println("OK="+ok);
                    }
                }catch (ParseException e) {
                    System.out.println("BROKE "+file);
                    ko++;
                } catch (TokenMgrError e) {
                    System.out.println("BROKE LEXICAL "+file);
                    ko++;
                } catch (Throwable e) {
                    System.out.println("BROKE ELSE "+file);
                    ko++;
                }
            }

        }
    }

    public static void main(String[] args) throws IOException, ParseException {
        System.out.println("OK " + ok);
        String path = "/home/federico/Downloads/openjdk";
        parse(new File(path));
        System.out.println("OK " + ok);
        System.out.println("KO " + ko);
    }

}
