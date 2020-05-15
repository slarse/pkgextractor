package se.slarse.pkgextractor;

import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.objectweb.asm.ClassReader;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.List;

public class Main {
    public static void main(String[] args) throws IOException {
        if (args.length != 1) {
            System.out.println("usage: java -jar pkgextractor.jar <classfile>");
            System.exit(1);
        }

        Path path = Paths.get(args[0]);

        String packageName;
        if (path.toString().endsWith(".java")) {
            packageName = parseSourceFilePackage(path);
        } else if (path.toString().endsWith(".class")) {
            packageName = parseClassfilePackage(path);
        } else {
            throw new RuntimeException("Expected .class or .java file, got: " + path.toString());
        }

        System.out.println(packageName);
    }

    private static String parseClassfilePackage(Path classfile) throws IOException {
        byte[] content = Files.readAllBytes(classfile);
        ClassReader reader = new ClassReader(content);
        String[] classname = reader.getClassName().split("/");
        return String.join(".", Arrays.copyOf(classname, classname.length - 1));
    }

    private static String parseSourceFilePackage(Path sourceFile) throws IOException {
        List<String> lines = Files.readAllLines(sourceFile);
        Pattern pattern = Pattern.compile("\\s*package\\s+?(\\S+)\\s*;");
        for (String line : lines) {
            Matcher matcher = pattern.matcher(line);
            if (matcher.matches()) {
                return matcher.group(1);
            }
        }
        return "";
    }
}
