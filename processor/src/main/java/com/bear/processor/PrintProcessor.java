package com.bear.processor;

import com.google.auto.service.AutoService;
import com.squareup.javapoet.JavaFile;
import com.squareup.javapoet.MethodSpec;
import com.squareup.javapoet.TypeSpec;

import java.io.IOException;
import java.util.HashSet;
import java.util.Set;

import javax.annotation.processing.AbstractProcessor;
import javax.annotation.processing.ProcessingEnvironment;
import javax.annotation.processing.Processor;
import javax.annotation.processing.RoundEnvironment;
import javax.annotation.processing.SupportedAnnotationTypes;
import javax.annotation.processing.SupportedSourceVersion;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.Element;
import javax.lang.model.element.Modifier;
import javax.lang.model.element.Name;
import javax.lang.model.element.TypeElement;
import javax.tools.Diagnostic;

@AutoService(Processor.class)
@SupportedSourceVersion(SourceVersion.RELEASE_7)
@SupportedAnnotationTypes("com.bear.processor.PrintFiled")
public class PrintProcessor extends AbstractProcessor {

    @Override
    public synchronized void init(ProcessingEnvironment processingEnv) {
        super.init(processingEnv);
        print("Hello APT");
        processingEnv.getMessager().printMessage(Diagnostic.Kind.NOTE, "Hello APT!");

    }

    @Override
    public SourceVersion getSupportedSourceVersion() {
        return processingEnv.getSourceVersion();
    }

    @Override
    public Set<String> getSupportedAnnotationTypes() {
        final HashSet<String> set = new HashSet<>();
        set.add(PrintFiled.class.getCanonicalName());
        return set;
    }

    private void print(String s) {
        System.out.println(s);
    }

    @Override
    public boolean process(Set<? extends TypeElement> set, RoundEnvironment roundEnvironment) {
        print("------------->start");
        Set<? extends Element> elements = roundEnvironment.getElementsAnnotatedWith(PrintFiled.class);

        TypeSpec.Builder classBuilder = TypeSpec
                .classBuilder("PrintUtils")
                .addModifiers(Modifier.PUBLIC, Modifier.FINAL);
        for (Element element : elements) {
            Name simpleName = element.getSimpleName();
            MethodSpec method = MethodSpec.methodBuilder("print$$" + simpleName)
                    .addModifiers(Modifier.PUBLIC, Modifier.STATIC)
                    .returns(void.class)
                    .addStatement("$T.out.println($S)", System.class, "Hello, JavaPoet!")
                    .build();
            classBuilder.addMethod(method);
        }

        JavaFile javaFile = JavaFile
                .builder("com.bear.arcdemo", classBuilder.build())
                .build();

        try {
            javaFile.writeTo(processingEnv.getFiler());
        } catch (IOException e) {
            e.printStackTrace();
        }

        return false;
    }
}
