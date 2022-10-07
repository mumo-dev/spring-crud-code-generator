package io.mumo.recarnated.util;


import org.junit.jupiter.api.Test;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

class StringUtilTest {


    @Test
    void camelCaseTest() {
        assertThat(StringUtil.camelCase("samuel_mumo")).isEqualTo("samuelMumo");
        assertThat(StringUtil.camelCase("Samuel_Mumo")).isEqualTo("samuelMumo");
        assertThat(StringUtil.camelCase("SamuelMumo")).isEqualTo("samuelMumo");
        assertThat(StringUtil.camelCase("samuelMumo")).isEqualTo("samuelMumo");
    }

    @Test
    void titleCaseTest() {
        assertThat(StringUtil.titleCase("samuel_mumo")).isEqualTo("SamuelMumo");
        assertThat(StringUtil.titleCase("Samuel_Mumo")).isEqualTo("SamuelMumo");
        assertThat(StringUtil.titleCase("samuelMumo")).isEqualTo("SamuelMumo");
    }

    @Test
    void snakeCaseTest() {
        assertThat(StringUtil.snakeCase("samuel_mumo")).isEqualTo("samuel_mumo");
        assertThat(StringUtil.snakeCase("Samuel_Mumo")).isEqualTo("samuel_mumo");
        assertThat(StringUtil.snakeCase("samuelMumo")).isEqualTo("samuel_mumo");
        assertThat(StringUtil.snakeCase("SamuelMumo")).isEqualTo("samuel_mumo");
    }

    @Test
    void pluralTest() {
        assertThat(StringUtil.pluralize("categories")).isEqualTo("categories");
        assertThat(StringUtil.pluralize("category")).isEqualTo("categories");
        assertThat(StringUtil.pluralize("product")).isEqualTo("products");
        assertThat(StringUtil.pluralize("products")).isEqualTo("products");
        assertThat(StringUtil.pluralize("estate")).isEqualTo("estates");
        assertThat(StringUtil.pluralize("olt")).isEqualTo("olts");
    }

    @Test
    void singularTest() {
        assertThat(StringUtil.singular("categories")).isEqualTo("category");
        assertThat(StringUtil.singular("category")).isEqualTo("category");
        assertThat(StringUtil.singular("product")).isEqualTo("product");
        assertThat(StringUtil.singular("product")).isEqualTo("product");
        assertThat(StringUtil.singular("estates")).isEqualTo("estate");
        assertThat(StringUtil.singular("olts")).isEqualTo("olt");
        assertThat(StringUtil.singular("olt")).isEqualTo("olt");
    }


}
