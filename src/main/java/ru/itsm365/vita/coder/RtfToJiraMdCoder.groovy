package ru.itsm365.vita.coder

import ru.itsm365.vita.coder.block.MarkedListCoder
import ru.itsm365.vita.coder.block.NumberedListCoder
import ru.itsm365.vita.coder.block.TableCoder
import ru.itsm365.vita.coder.inline.AllTextStyleCoder
import ru.itsm365.vita.coder.inline.ImageCoder
import ru.itsm365.vita.coder.inline.LinkCoder
import ru.itsm365.vita.coder.line.CodeCoder
import ru.itsm365.vita.coder.line.HeaderCoder
import ru.itsm365.vita.coder.line.QuoteCoder
import ru.itsm365.vita.coder.special.*

class RtfToJiraMdCoder implements Coder {
    //Подстрочные кодеры
    private static final Coder ALL_TEST_STYLE_CODER = new AllTextStyleCoder();
    private static final Coder IMAGE_CODER = new ImageCoder();
    private static final Coder LINK_CODER = new LinkCoder();

    //Строчные кодеры
    private static final Coder HEADER_CODER = new HeaderCoder();
    private static final Coder QUOTE_CODER = new QuoteCoder();
    private static final Coder CODE_CODER = new CodeCoder();

    //Блочные кодеры
    private static final Coder MARKED_LIST_CODER = new MarkedListCoder();
    private static final Coder NUMBERED_LIST_CODER = new NumberedListCoder();
    private static final Coder TABLE_CODER = new TableCoder();

    //Специальные кодеры
    private static final Coder LINE_BREAK_CODER = new LineBreakCoder();
    private static final Coder XML_PRETTIFY_CODER = new XmlPrettifyCoder();
    private static final Coder ROW_CODER = new RowCoder();
    private static final Coder RTF_ESCAPE_CODER = new RtfEscapeCoder();
    private static final Coder MD_ESCAPE_CODER = new MdEscapeCoder();

    //Настроенные цепочки дял преобразования.
    private static final List<Coder> TO_RTF;
    private static final List<Coder> TO_MD;

    static {
        TO_RTF = [
                //Сначала специальные обработчики
                RTF_ESCAPE_CODER,

                //Затем обходим все подстрочные элементы
                ALL_TEST_STYLE_CODER,
                IMAGE_CODER,
                LINK_CODER,

                //Затем обходим все строчные элементы
                HEADER_CODER,
                QUOTE_CODER,
                CODE_CODER,

                //Затем обходим все блочные элементы
                TABLE_CODER,
                MARKED_LIST_CODER,
                NUMBERED_LIST_CODER,

                //В завершение опять специальные обработчики.
                //LINE_BREAK_CODER,
                ROW_CODER,
                XML_PRETTIFY_CODER,
                MD_ESCAPE_CODER
        ]

        TO_MD = [
                //Сначала специальные обработчики
                MD_ESCAPE_CODER,
                XML_PRETTIFY_CODER,
                ROW_CODER,
                //LINE_BREAK_CODER,

                //Затем обходим все подстрочные элементы
                ALL_TEST_STYLE_CODER,
                IMAGE_CODER,
                LINK_CODER,

                //Затем обходим все строчные элементы
                HEADER_CODER,
                QUOTE_CODER,
                CODE_CODER,

                //Затем обходим все блочные элементы
                TABLE_CODER,
                MARKED_LIST_CODER,
                NUMBERED_LIST_CODER,

                //В завершение опять специальные обработчики.
                RTF_ESCAPE_CODER
        ]
    }

    @Override
    String toRtf(String s) {
        String result = s;
        TO_RTF.forEach { result = it.toRtf(result) }
        return result;
    }

    @Override
    String toMd(String s) {
        String result = s;
        TO_MD.forEach { result = it.toMd(result) }
        return result;
    }
}
