package io.github.linyimin0812.agent;

import java.io.File;
import java.io.IOException;
import java.lang.instrument.ClassFileTransformer;
import java.lang.instrument.Instrumentation;
import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.net.URL;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;
import java.util.jar.JarFile;

/**
 * @author linyimin
 * @date 2023/04/18 16:20
 **/
public class ProfilerAgentBoostrap {

    private static final String BRIDGE_JAR = "java-profiler-bridge.jar";

    private static final String AGENT_HOME = System.getProperty("user.home") + File.separator + "java-profiler-boost" + File.separator;
    private static final String LIB_HOME = AGENT_HOME + "lib" + File.separator;
    private static final String EXTENSION_HOME = LIB_HOME + "extension" + File.separator;

    public static void premain(String args, Instrumentation instrumentation) {

        // bridge.jar
        File spyJarFile = new File(LIB_HOME + BRIDGE_JAR);
        if (!spyJarFile.exists()) {
            System.out.println("Spy jar file does not exist: " + spyJarFile);
            return;
        }
        // load agent-spy.jar
        try {
            instrumentation.appendToBootstrapClassLoaderSearch(new JarFile(spyJarFile));
        } catch (IOException ignore) {

        }

        // load agent-core.jar
        final ClassLoader agentLoader;
        try {
            agentLoader = createAgentClassLoader();
            Class<?> transFormer = agentLoader.loadClass("io.github.linyimin0812.profiler.core.enhance.ProfilerClassFileTransformer");
            Constructor<?> constructor = transFormer.getConstructor(Instrumentation.class, String.class, List.class);
            Method retransform = transFormer.getDeclaredMethod("retransformLoadedClass");
            Object instance = constructor.newInstance(instrumentation, args, getManifests());

            instrumentation.addTransformer((ClassFileTransformer) instance, true);

            retransform.invoke(instance);

        } catch (Throwable e) {
            System.out.println("throwable: " + e.getMessage());
            throw new RuntimeException(e);
        }
    }

    private static ClassLoader createAgentClassLoader() throws Throwable {

        List<URL> urlList = new ArrayList<>();

        // 加载lib下的jar包
        urlList.addAll(getJars(LIB_HOME));
        urlList.addAll(getJars(EXTENSION_HOME));

        System.out.println(urlList);

        return new ProfilerAgentClassLoader(urlList.toArray(new URL[0]));
    }

    private static List<URL> getJars(String path) throws Throwable {

        List<URL> urlList = new ArrayList<>();

        // 加载lib下的jar包
        File folder = new File(path);

        if (!folder.exists()) {
            throw new IllegalStateException(path + " is not exit.");
        }

        File[] files = folder.listFiles(file -> !file.getName().contains(BRIDGE_JAR) && file.isFile() && file.getName().endsWith("jar"));

        if (files == null) {
            throw new IllegalStateException(path + " does not contain any jar files.");
        }

        for (File file : files) {
            urlList.add(file.toURI().toURL());
        }

        return urlList;

    }

    private static List<URL> getManifests() {

        List<URL> packages = new ArrayList<>();

        try {
            Enumeration<URL> urls = ProfilerAgentBoostrap.class.getClassLoader().getResources("META-INF/MANIFEST.MF");
            while (urls.hasMoreElements()) {
                packages.add(urls.nextElement());
            }
        } catch (IOException e) {
            System.out.println("getManifests error. error: " + e.getMessage());
        }

        return packages;
    }

}