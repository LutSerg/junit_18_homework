package com.asteroster.paramtests;

import com.codeborne.selenide.CollectionCondition;
import com.codeborne.selenide.Configuration;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.MethodSource;
import org.junit.jupiter.params.provider.ValueSource;

import java.util.List;
import java.util.stream.Stream;

import static com.codeborne.selenide.Condition.*;
import static com.codeborne.selenide.Selectors.byText;
import static com.codeborne.selenide.Selectors.withText;
import static com.codeborne.selenide.Selenide.*;

public class FumikoParamsTests {
    @BeforeAll
    static void browserSetUp() {
        Configuration.browserSize = "1920x1080";
    }

    @BeforeEach
    public void setUp() {
        open("https://fumiko.ru/");
    }
    @ValueSource(
            strings = {"Аудиотехника", "Аксессуары для смартфонов", "Автотовары"}
    )
    @ParameterizedTest(name = "Если в меню выбрать {0}, то на раскрытой странице будет текст Купить {0}")
    public void categoriesForBuying(String testData) {
        $(".menu_top_block.catalog_block").$(byText(testData)).click();
        $("#pagetitle").shouldHave(text("Купить" + " " + testData));
    }

    @CsvSource(
            {
                    "Магазины, Магазины",
                    "Как купить, Помощь",
            }
    )
    @ParameterizedTest(name = "При нажатии в верхнем меню на раздел {0}, то на странице будет надпись {1}")
    public void titleAfterClickingTopMenuItems(String menuItem, String titleText) {
        $(".catalog_menu").$(byText(menuItem)).click();
        $("#pagetitle").shouldHave(text(titleText));
    }

    static Stream<Arguments> submenuOfItems() {
        return Stream.of(
                Arguments.of("Аудиотехника", List.of("Наушники", "Портативные колонки", "Компьютерные колонки", "Bluetooth гарнитуры", "Микрофоны караоке", "Радиоприемники")),
                Arguments.of("Память", List.of("USB флешки", "Карты памяти MicroSD", "Внешние жесткие диски", "SSD накопители", "СD/DVD диски"))
        );
    }
    @MethodSource()
    @ParameterizedTest(name = "Если кликнуть на раздел {0}, то в нем будут видны позиции {1}")
    public void submenuOfItems(String menuItem, List<String> submenu) {
        $(".menu_top_block.catalog_block").$(byText(menuItem)).click();
        $$(".section_item").shouldHave(CollectionCondition.texts(submenu));

    }
}
