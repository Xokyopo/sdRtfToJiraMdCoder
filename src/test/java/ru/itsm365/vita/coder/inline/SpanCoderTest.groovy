package ru.itsm365.vita.coder.inline

import org.junit.Assert
import org.junit.Before
import org.junit.Test

class SpanCoderTest {

    private SpanCoder coder;

    @Before
    void setUp() throws Exception {
        this.coder = new SpanCoder();
    }

    @Test
    void toRtf() {
        String mdText = "test";
        String expected = "test";
        String actual = this.coder.toRtf(mdText);
        Assert.assertEquals(expected, actual);
    }

    @Test
    void toMd() {
        String rtfText = "<span>test</span>";
        String expected = "test";
        String actual = this.coder.toMd(rtfText);

        Assert.assertEquals(expected, actual);
    }

    @Test
    void toMd_ShouldReturnOnlyText_WhenTagHaveAttributes() {
        String rtfText = '<span color="">test</span>';
        String expected = "test";
        String actual = this.coder.toMd(rtfText);

        Assert.assertEquals(expected, actual);
    }

    @Test
    void toMd_ShouldReturnOriginalText_WhenHaveTagNameWithPrefixSpan() {
        String rtfText = '<spand color="">test</spand>';
        String expected = "<spand color=\"\">test</spand>";
        String actual = this.coder.toMd(rtfText);

        Assert.assertEquals(expected, actual);
    }

    /**
     * 1. Проверка базовой конвертации HEX в RGB.
     * Проверяет правильность математических вычислений для крайних значений (черный/белый).
     */
    @Test
    void toMd_testBasicColorConversion() {
        String rtfText = '<span style="color: rgb(0, 0, 0);">Black</span> and <span style="color: rgb(255, 255, 255);">White</span>'
        String expected = "{color:#000000}Black{color} and {color:#ffffff}White{color}"
        String actual = this.coder.toMd(rtfText)

        Assert.assertEquals(expected, actual)
    }

    /**
     * 1. Проверка базовой конвертации HEX в RGB.
     * Проверяет правильность математических вычислений для крайних значений (черный/белый).
     */
    @Test
    void toRtf_testBasicColorConversion() {
        String mdText = "{color:#000000}Black{color} and {color:#FFFFFF}White{color}"
        String expected = '<span style="color: rgb(0, 0, 0);">Black</span> and <span style="color: rgb(255, 255, 255);">White</span>'
        String actual = this.coder.toRtf(mdText)

        Assert.assertEquals(expected, actual)
    }

    /**
     * 2. Проверка сохранения структуры Jira.
     * Гарантирует, что символы '#' для списков не преобразуются в цвета.
     */
    @Test
    void toMd_testJiraMarkupIntegrity() {
        String rtfText = '# Этап 1\n## Подпункт\n<span style="color: rgb(87, 217, 163);">Текст</span>'
        String expected = "# Этап 1\n## Подпункт\n{color:#57d9a3}Текст{color}"
        String actual = this.coder.toMd(rtfText)

        Assert.assertEquals(expected, actual)
    }

    /**
     * 2. Проверка сохранения структуры Jira.
     * Гарантирует, что символы '#' для списков не преобразуются в цвета.
     */
    @Test
    void toRtf_testJiraMarkupIntegrity() {
        String mdText = "# Этап 1\n## Подпункт\n{color:#57d9a3}Текст{color}"
        String expected = '# Этап 1\n## Подпункт\n<span style="color: rgb(87, 217, 163);">Текст</span>'
        String actual = this.coder.toRtf(mdText)

        Assert.assertEquals(expected, actual)
    }

    /**
     * 3. Проверка многострочного текста.
     * Тестирует работу флага DOTALL: тег должен закрыться, даже если внутри есть переносы строк.
     */
    @Test
    void toMd_testMultilineColorBlock() {
        String rtfText = '<span style="color: rgb(61, 142, 185);">Строка 1\nСтрока 2\nСтрока 3</span>'
        String expected = "{color:#3d8eb9}Строка 1\nСтрока 2\nСтрока 3{color}"
        String actual = this.coder.toMd(rtfText)

        Assert.assertEquals(expected, actual)
    }

    /**
     * 3. Проверка многострочного текста.
     * Тестирует работу флага DOTALL: тег должен закрыться, даже если внутри есть переносы строк.
     */
    @Test
    void toRtf_testMultilineColorBlock() {
        String rtfText = "{color:#3d8eb9}Строка 1\nСтрока 2\nСтрока 3{color}"
        String expected = '<span style="color: rgb(61, 142, 185);">Строка 1\nСтрока 2\nСтрока 3</span>'
        String actual = this.coder.toRtf(rtfText)

        Assert.assertEquals(expected, actual)
    }

    /**
     * 4. Проверка безопасности спецсимволов.
     * Проверяет, что знаки $ или \, которые могут быть в тексте задачи,
     * не вызывают ошибок в Matcher.appendReplacement.
     */
    @Test
    void toMd_testSpecialCharactersInContent() {
        String rtfText = '<span style="color: rgb(17, 34, 51);">Цена \$100 и путь C:\\\\Temp</span>'
        String expected = "{color:#112233}Цена \$100 и путь C:\\\\Temp{color}"
        String actual = this.coder.toMd(rtfText)

        Assert.assertEquals(expected, actual)
    }

    /**
     * 4. Проверка безопасности спецсимволов.
     * Проверяет, что знаки $ или \, которые могут быть в тексте задачи,
     * не вызывают ошибок в Matcher.appendReplacement.
     */
    @Test
    void toRtf_testSpecialCharactersInContent() {
        String mdText = "{color:#112233}Цена \$100 и путь C:\\\\Temp{color}"
        String expected = '<span style="color: rgb(17, 34, 51);">Цена \$100 и путь C:\\\\Temp</span>'
        String actual = this.coder.toRtf(mdText)

        Assert.assertEquals(expected, actual)
    }

    /**
     * 5. Проверка пустых и некорректных данных.
     * Гарантирует, что метод не упадет с ошибкой (NPE) при пустом вводе.
     */
    @Test
    void toMd_testEmptyAndNullInput() {
        Assert.assertEquals(this.coder.toMd(""), "")
        Assert.assertEquals(this.coder.toMd(null), null)
        Assert.assertEquals(this.coder.toMd("Просто текст без тегов"), "Просто текст без тегов")
    }

    /**
     * 5. Проверка пустых и некорректных данных.
     * Гарантирует, что метод не упадет с ошибкой (NPE) при пустом вводе.
     */
    @Test
    void toRtf_testEmptyAndNullInput() {
        Assert.assertEquals(this.coder.toRtf(""), "")
        Assert.assertEquals(this.coder.toRtf(null), null)
        Assert.assertEquals(this.coder.toRtf("Просто текст без тегов"), "Просто текст без тегов")
    }

    /**
     * 6. Проверка нескольких цветовых тегов в одной строке.
     * Проверяет, что текст между окрашенными словами (второе слово) сохраняется,
     * а первый и третий фрагменты получают свои уникальные RGB-значения.
     */
    @Test
    void toMd_testMultipleColorsInSingleLine() {
        String rtfText = '<span style="color: rgb(255, 0, 0);">Первое</span> второе <span style="color: rgb(0, 0, 255);">третье</span>'
        String expected = "{color:#ff0000}Первое{color} второе {color:#0000ff}третье{color}"
        String actual = this.coder.toMd(rtfText)

        Assert.assertEquals(expected, actual)
    }

    /**
     * 6. Проверка нескольких цветовых тегов в одной строке.
     * Проверяет, что текст между окрашенными словами (второе слово) сохраняется,
     * а первый и третий фрагменты получают свои уникальные RGB-значения.
     */
    @Test
    void testMultipleColorsInSingleLine() {
        String mdText = "{color:#FF0000}Первое{color} второе {color:#0000FF}третье{color}"
        String expected = '<span style="color: rgb(255, 0, 0);">Первое</span> второе <span style="color: rgb(0, 0, 255);">третье</span>'
        String actual = this.coder.toRtf(mdText)

        Assert.assertEquals(expected, actual)
    }

    @Test
    void toRtf_testNestedColorsToRtf() {
        // Проверка вложенности: Зеленый внутри Красного
        String mdText = "{color:#FF0000}Красный {color:#00FF00}Зеленый{color} Красный{color}"
        String expected = '<span style="color: rgb(255, 0, 0);">Красный <span style="color: rgb(0, 255, 0);">Зеленый</span> Красный</span>'
        String actual = this.coder.toRtf(mdText)

        Assert.assertEquals(expected, actual)
    }

    @Test
    void toMd_testNestedColorsToMd() {
        // Проверка обратной вложенности: rgb(0, 255, 0) внутри rgb(255, 0, 0)
        String rtfText = '<span style="color: rgb(255, 0, 0);">Красный <span style="color: rgb(0, 255, 0);">Зеленый</span> Красный</span>'
        String expected = "{color:#ff0000}Красный {color:#00ff00}Зеленый{color} Красный{color}"
        String actual = this.coder.toMd(rtfText)

        Assert.assertEquals(expected, actual)
    }
}
