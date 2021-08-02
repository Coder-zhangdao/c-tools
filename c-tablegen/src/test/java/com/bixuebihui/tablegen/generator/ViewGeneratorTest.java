package com.bixuebihui.tablegen.generator;

import org.hamcrest.core.StringEndsWith;
import org.junit.jupiter.api.Test;

import java.io.IOException;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.containsString;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class ViewGeneratorTest {

    @Test
    void getTargetFileName() {
        ViewGenerator gen = new ViewGenerator();
        gen.init(this.getClass().getResource("/tablegen.properties").getPath());
        String res = gen.getTargetFileName("Test");
        assertThat(res, StringEndsWith.endsWith("view/Test.java"));


        String v = gen.config.getViewList().get("my_test_view");
        assertTrue(v.length()>0);
        assertEquals(1, gen.config.getViewList().size());
    }

    @Test
    void testGenerate() throws IOException {
        ViewGenerator gen = new ViewGenerator();
        gen.init(this.getClass().getResource("/tablegen.properties").getPath());
        String res = gen.generate("my_test_view");

        System.out.println(res);
    }

    @Test
    void testGenerateViewAsField() throws IOException {
        ViewGenerator gen = new ViewGenerator();
        gen.init(this.getClass().getResource("/tablegen.properties").getPath());
        String res = gen.generate("my_test_view");
        assertThat("contains as field", res, containsString("DegreePlus"));

        System.out.println(res);
    }


}
