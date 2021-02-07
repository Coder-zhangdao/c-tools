package com.bixuebihui.tablegen.generator;

import org.junit.jupiter.api.Test;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.*;

class PojoGeneratorTest {

    @Test
    void getExtendsClasses() throws IOException {
        PojoGenerator gen = new PojoGenerator();
        gen.init(this.getClass().getResource("/tablegen.properties").getPath());
        String res = gen.generate("test");

        System.out.println(res);
    }

    @Test
    void getAnnotationForClass() {
    }

    @Test
    void getTargetFileName() {
    }

    @Test
    void getTemplateFileName() {
    }

    @Test
    void getClassName() {
    }
}
