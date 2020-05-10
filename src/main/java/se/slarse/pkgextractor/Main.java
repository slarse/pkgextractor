package se.slarse.pkgextractor;

import org.objectweb.asm.ClassReader;
import spoon.Launcher;
import spoon.reflect.CtModel;
import spoon.reflect.declaration.CtPackageDeclaration;
import spoon.support.compiler.FileSystemFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;

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

    private static String parseSourceFilePackage(Path sourceFile) {
        Launcher launcher = new Launcher();
        launcher.getEnvironment().setLevel("OFF");
        launcher.getEnvironment().setNoClasspath(true);
        launcher.addInputResource(new FileSystemFile(sourceFile.toFile()));
        CtModel model = launcher.buildModel();

        CtPackageDeclaration pkgDecl = model.getRootPackage().getFactory()
                .CompilationUnit().getMap()
                .values().iterator().next()
                .getPackageDeclaration();
        return pkgDecl.getReference().getQualifiedName();
    }
}
