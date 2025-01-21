package ru.itsm365.vita.coder

import org.junit.Assert
import org.junit.Before
import org.junit.Test

class RtfToJiraMdCoderTest {
    private Coder coder;

    private final TestValue simpleText;

    {
        simpleText = new TestValue(
                """
Обычное предложение, ничего интересного.
Предложение на второй строке.
*Жирный текст _Жирный курсив +Жирный подчеркнутый курсив+_*
_Курсив +Подчеркнутый курсив+_
+Подчеркнутый+
 # Первый элемент нумерованного списка.
 # Второй элемент нумерованного списка.

 * Первый элемент маркированного списка.
 * Второй элемент маркированного списка.

обычная таблица
|С/З|С|С/В|
|З|экватор|В|
|Ю/З|Ю|Ю/В|

таблица с стилизованным текстом
|*жирный*|_курсив_|+подчеркнутый+|
|зачеркнуты|~подстрочный~|^надстрочный^|
|красный|георгическим шрифтом|[https://ya.ru/]|

скриншот
!image-2025-01-17-11-23-48-789.png!
h1. Заголовок 1
h2. Заголовок 2
h3. Заголовок 3
h4. Заголовок 4
{code:java}
Код printf('Hellow World'){code}
{quote}Цитата
{quote}
💏-какой то смайл""",
                """
<div>Обычное предложение, ничего интересного.</div>
<div>Предложение на второй строке.</div>
<div><strong fr-original-style="" style="font-weight: 700;">Жирный текст&nbsp;<em>Жирный курсив&nbsp;<u>Жирный подчеркнутый курсив</u></em></strong></div>
<div><em>Курсив&nbsp;<u>Подчеркнутый курсив</u></em></div>
<div><u>Подчеркнутый</u></div>

<ol>
\t<li>Первый элемент нумерованного списка.</li>
\t<li>Второй элемент нумерованного списка.</li>
</ol>

<ul>
\t<li>Первый элемент маркированного списка.</li>
\t<li>Второй элемент маркированного списка.</li>
</ul>
<div>обычная таблица</div>
<div>

\t<table fr-original-class="fr-table-with-borders" fr-original-style="width: 100%;" fr-table-with-borders="fr-table-with-borders" style="width: 100%; border: 0px; border-collapse: collapse; empty-cells: show; max-width: 100%;">
\t\t<tbody>
\t\t\t<tr fr-original-style="" style="user-select: none;">
\t\t\t\t<td fr-original-style="width: 33.3333%;" style="width: 33.3333%; min-width: 5px; user-select: text; border: 1px solid rgb(221, 221, 221);">С/З</td>
\t\t\t\t<td fr-original-style="width: 33.3333%;" style="width: 33.3333%; min-width: 5px; user-select: text; border: 1px solid rgb(221, 221, 221);">С</td>
\t\t\t\t<td fr-original-style="width: 33.3333%;" style="width: 33.3333%; min-width: 5px; user-select: text; border: 1px solid rgb(221, 221, 221);">С/В</td>
\t\t\t</tr>
\t\t\t<tr fr-original-style="" style="user-select: none;">
\t\t\t\t<td fr-original-style="width: 33.3333%;" style="width: 33.3333%; min-width: 5px; user-select: text; border: 1px solid rgb(221, 221, 221);">З</td>
\t\t\t\t<td fr-original-style="width: 33.3333%;" style="width: 33.3333%; min-width: 5px; user-select: text; border: 1px solid rgb(221, 221, 221);">экватор</td>
\t\t\t\t<td fr-original-style="width: 33.3333%;" style="width: 33.3333%; min-width: 5px; user-select: text; border: 1px solid rgb(221, 221, 221);">В</td>
\t\t\t</tr>
\t\t\t<tr fr-original-style="" style="user-select: none;">
\t\t\t\t<td fr-original-style="width: 33.3333%;" style="width: 33.3333%; min-width: 5px; user-select: text; border: 1px solid rgb(221, 221, 221);">Ю/З</td>
\t\t\t\t<td fr-original-style="width: 33.3333%;" style="width: 33.3333%; min-width: 5px; user-select: text; border: 1px solid rgb(221, 221, 221);">Ю</td>
\t\t\t\t<td fr-original-style="width: 33.3333%;" style="width: 33.3333%; min-width: 5px; user-select: text; border: 1px solid rgb(221, 221, 221);">Ю/В</td>
\t\t\t</tr>
\t\t</tbody>
\t</table>
</div>
<div>таблица с стилизованным текстом</div>
<div>

\t<table fr-original-class="fr-table-with-borders" fr-original-style="width: 100%;" fr-table-with-borders="fr-table-with-borders" style="width: 100%; border: 0px; border-collapse: collapse; empty-cells: show; max-width: 100%;">
\t\t<tbody>
\t\t\t<tr fr-original-style="" style="user-select: none;">
\t\t\t\t<td fr-original-style="width: 33.3333%;" style="width: 33.3333%; min-width: 5px; user-select: text; border: 1px solid rgb(221, 221, 221);"><strong fr-original-style="" style="font-weight: 700;">жирный</strong></td>
\t\t\t\t<td fr-original-style="width: 33.3333%;" style="width: 33.3333%; min-width: 5px; user-select: text; border: 1px solid rgb(221, 221, 221);"><em>курсив</em></td>
\t\t\t\t<td fr-original-style="width: 33.3333%;" style="width: 33.3333%; min-width: 5px; user-select: text; border: 1px solid rgb(221, 221, 221);"><u>подчеркнутый</u></td>
\t\t\t</tr>
\t\t\t<tr fr-original-style="" style="user-select: none;">
\t\t\t\t<td fr-original-style="width: 33.3333%;" style="width: 33.3333%; min-width: 5px; user-select: text; border: 1px solid rgb(221, 221, 221);"><s>зачеркнуты</s></td>
\t\t\t\t<td fr-original-style="width: 33.3333%;" style="width: 33.3333%; min-width: 5px; user-select: text; border: 1px solid rgb(221, 221, 221);"><sub>подстрочный</sub></td>
\t\t\t\t<td fr-original-style="width: 33.3333%;" style="width: 33.3333%; min-width: 5px; user-select: text; border: 1px solid rgb(221, 221, 221);"><sup>надстрочный</sup></td>
\t\t\t</tr>
\t\t\t<tr fr-original-style="" style="user-select: none;">
\t\t\t\t<td fr-original-style="width: 33.3333%;" style="width: 33.3333%; min-width: 5px; user-select: text; border: 1px solid rgb(221, 221, 221);"><span style="color: rgb(184, 49, 47);">красный</span></td>
\t\t\t\t<td fr-original-style="width: 33.3333%;" style="width: 33.3333%; min-width: 5px; user-select: text; border: 1px solid rgb(221, 221, 221);"><span style="font-family: Georgia,serif;">георгическим шрифтом</span></td>
\t\t\t\t<td fr-original-style="width: 33.3333%;" style="width: 33.3333%; min-width: 5px; user-select: text; border: 1px solid rgb(221, 221, 221);"><a fr-original-style="" href="https://ya.ru/" style="user-select: auto;" target="_top">https://ya.ru/</a></td>
\t\t\t</tr>
\t\t</tbody>
\t</table>
</div>
<div>скриншот</div>
<div><img src="./download?uuid=file\$1960562362" style="display: inline-block; vertical-align: bottom; margin-right: 5px; margin-left: 5px; max-width: 700px; text-align: center; cursor: pointer; position: relative;" fr-original-style="display: inline-block; vertical-align: bottom; margin-right: 5px; margin-left: 5px; max-width:700px; text-align: center;" fr-original-class="fr-draggable"></div>

<h1>Заголовок 1</h1>

<h2>Заголовок 2</h2>

<h3>Заголовок 3</h3>

<h4>Заголовок 4</h4><pre fr-original-style="" style="white-space: pre-wrap; overflow-wrap: break-word; overflow: visible;">//Код

printf('Hellow World')</pre>

<blockquote fr-original-style="" style="border-color: rgb(153, 153, 153); border-left-width: 2px; border-left-style: solid; margin-left: 0px; padding-left: 5px; color: rgb(153, 153, 153);">
\t<div>Цитата</div>
</blockquote>

<div>💏-какой то смайл</div>
""".replaceAll(">\\s+<", "><").replaceAll("\n", "")
        )
    }

    @Before
    void setUp() {
        this.coder = new RtfToJiraMdCoder();
    }

    @Test
    void reverse_rtf_simpleText() {
        String expectedRtfText = this.simpleText.rtf
        String actualRtfTest = this.coder.toRtf(this.coder.toMd(expectedRtfText));
        Assert.assertEquals(expectedRtfText, actualRtfTest);
    }

    @Test
    void reverse_md_simpleText() {
        String expectedMdText = this.simpleText.md
        String actualMdTest = this.coder.toMd(this.coder.toRtf(expectedMdText));
        Assert.assertEquals(expectedMdText, actualMdTest);
    }

    @Test
    void testToRtf() {
        println(this.coder.toRtf(this.simpleText.md));
    }

    @Test
    void testToMd() {
        println(this.coder.toMd(this.simpleText.rtf));
    }

    static class TestValue {
        public final String md;
        public final String rtf;

        TestValue(String md, String rtf) {
            this.md = md
            this.rtf = rtf
        }
    }
}
