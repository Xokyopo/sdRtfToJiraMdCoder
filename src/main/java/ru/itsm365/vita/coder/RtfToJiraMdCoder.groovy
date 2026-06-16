package ru.itsm365.vita.coder

import ru.itsm365.vita.coder.block.AllListCoder
import ru.itsm365.vita.coder.block.TableCoder
import ru.itsm365.vita.coder.inline.*
import ru.itsm365.vita.coder.line.CodeCoder
import ru.itsm365.vita.coder.line.HeaderCoder
import ru.itsm365.vita.coder.line.QuoteCoder
import ru.itsm365.vita.coder.special.*

import java.util.regex.Matcher
import java.util.regex.Pattern

class RtfToJiraMdCoder implements Coder {
    private final String LINE_SEPARATOR = System.lineSeparator();
    //Подстрочные кодеры
    private final Coder ALL_TEST_STYLE_CODER = new AllTextStyleCoder();
    private final Coder IMAGE_CODER = new ImageCoder();
    private final Coder LINK_CODER = new LinkCoder();

    //Строчные кодеры
    private final Coder HEADER_CODER = new HeaderCoder();
    private final Coder QUOTE_CODER = new QuoteCoder();
    private final Coder CODE_CODER = new CodeCoder();
    private final Coder SPAN_CODER = new SpanCoder();

    //Блочные кодеры
    private final Coder ALL_LIST_CODER = new AllListCoder();
    private final Coder TABLE_CODER = new TableCoder();

    //Специальные кодеры
    private final Coder LINE_BREAK_CODER = new LineBreakCoder();
    private final Coder XML_PRETTIFY_CODER = new XmlPrettifyCoder();
    private final Coder ROW_CODER = new RowCoder();
    private final Coder RTF_ESCAPE_CODER = new RtfEscapeCoder();
    private final Coder MD_ESCAPE_CODER = new MdEscapeCoder();
    private final Coder RTF_NBSP_CODER = new NBSPCoder();

    //Настроенные цепочки дял преобразования.
    private final List<Coder> TO_RTF;
    private final List<Coder> TO_MD;

    //Игнорируемые теги
    private final Pattern RTF_OPEN_CODE_TAG_PATTERN = Pattern.compile("<pre[^>]*>", Pattern.CASE_INSENSITIVE)
    private final Pattern RTF_CLOSE_CODE_TAG_PATTERN = Pattern.compile("</pre>", Pattern.CASE_INSENSITIVE)

    private final Pattern MD_OPEN_CODE_TAG_PATTERN = Pattern.compile("\\{code[^}]*}", Pattern.CASE_INSENSITIVE)
    private final Pattern MD_CLOSE_CODE_TAG_PATTERN = Pattern.compile("\\{code}", Pattern.CASE_INSENSITIVE)

    RtfToJiraMdCoder() {
        TO_RTF = [
                //Сначала специальные обработчики
                RTF_ESCAPE_CODER,

                //Затем обходим все подстрочные элементы
                ALL_TEST_STYLE_CODER,
                IMAGE_CODER,
                LINK_CODER,
                SPAN_CODER,
                LINE_BREAK_CODER,

                //Затем обходим все строчные элементы
                HEADER_CODER,
                QUOTE_CODER,

                //Затем обходим все блочные элементы
                TABLE_CODER,
                ALL_LIST_CODER,

                //В завершение опять специальные обработчики.
                ROW_CODER,
                MD_ESCAPE_CODER,

                RTF_NBSP_CODER,
                XML_PRETTIFY_CODER,
        ]

        TO_MD = [
                //Сначала специальные обработчики
                XML_PRETTIFY_CODER,
                RTF_NBSP_CODER,

                MD_ESCAPE_CODER,
                ROW_CODER,

                //Затем обходим все подстрочные элементы
                ALL_TEST_STYLE_CODER,
                IMAGE_CODER,
                LINK_CODER,
                SPAN_CODER,
                LINE_BREAK_CODER,

                //Затем обходим все строчные элементы
                HEADER_CODER,
                QUOTE_CODER,

                //Затем обходим все блочные элементы
                TABLE_CODER,
                ALL_LIST_CODER,

                //В завершение опять специальные обработчики.
                RTF_ESCAPE_CODER
        ]

        //TODO отключено так как все друге кодеры и так трогают все что внутри.
        //Нужно, сделать так что бы такие блоки обрабатывались отдельно от остального.
        QUOTE_CODER.setInlineTexCoder(ROW_CODER);
    }

    @Override
    String toRtf(String s) {
        StringBuilder result = new StringBuilder();
        Integer cursor = 0;

        Matcher openTagMather = MD_OPEN_CODE_TAG_PATTERN.matcher(s);
        Matcher closeTagMather = MD_CLOSE_CODE_TAG_PATTERN.matcher(s);
        while (openTagMather.find(cursor)) {
            if (!closeTagMather.find(openTagMather.end())) {
                break;
            }
            String beforeTagText = s.substring(cursor, openTagMather.start());
            TO_RTF.forEach { beforeTagText = it.toRtf(beforeTagText) }
            result.append(beforeTagText.trim());
            if (result.size() > 0) {
                result.append(LINE_SEPARATOR);
            }
            String codeBlock = s.substring(openTagMather.start(), closeTagMather.end());
            result.append(CODE_CODER.toRtf(codeBlock.trim()));
            if (cursor < s.length()) {
                result.append(LINE_SEPARATOR);
            }
            cursor = closeTagMather.end();
        }

        String afterTagText = s.substring(cursor);
        TO_RTF.forEach { afterTagText = it.toRtf(afterTagText) }
        result.append(afterTagText.trim());

        return result.toString();
    }

    @Override
    String toMd(String s) {
        StringBuilder result = new StringBuilder();
        Integer cursor = 0;

        Matcher openTagMather = RTF_OPEN_CODE_TAG_PATTERN.matcher(s);
        Matcher closeTagMather = RTF_CLOSE_CODE_TAG_PATTERN.matcher(s);
        while (openTagMather.find(cursor)) {
            if (!closeTagMather.find(openTagMather.end())) {
                break;
            }
            String beforeTagText = s.substring(cursor, openTagMather.start());
            TO_MD.forEach { beforeTagText = it.toMd(beforeTagText) }

            result.append(beforeTagText.trim());
            if (result.size() > 0) {
                result.append(LINE_SEPARATOR);
            }
            String codeBlock = s.substring(openTagMather.start(), closeTagMather.end());
            result.append(CODE_CODER.toMd(codeBlock.trim()));
            if (cursor < s.length()) {
                result.append(LINE_SEPARATOR);
            }
            cursor = closeTagMather.end();
        }

        String afterTagText = s.substring(cursor);
        TO_MD.forEach { afterTagText = it.toMd(afterTagText) }
        result.append(afterTagText.trim());

        return result.toString();
    }
}
