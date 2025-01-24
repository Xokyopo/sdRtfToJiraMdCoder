package ru.itsm365.vita.coder

import ru.itsm365.vita.coder.block.MarkedListCoder
import ru.itsm365.vita.coder.block.NumberedListCoder
import ru.itsm365.vita.coder.block.TableCoder
import ru.itsm365.vita.coder.inline.*
import ru.itsm365.vita.coder.line.HeaderCoder
import ru.itsm365.vita.coder.special.BlockBreakCoder
import ru.itsm365.vita.coder.special.LineBreakCoder
import ru.itsm365.vita.coder.special.XmlPrettifyCoder

class RtfToJiraMdCoder implements Coder {
    //Подстрочные кодеры
    private static final Coder BOLD_TEXT_STYLE_CODER = new BoldTextStyleCoder();
    private static final Coder IMAGE_CODER = new ImageCoder();
    private static final Coder ITALIC_TEXT_STYLE_CODER = new ItalicTextStyleCoder();
    private static final Coder LINK_CODER = new LinkCoder();
    private static final Coder STRIKETHROUGH_TEXT_STYLE_CODER = new StrikethroughTextStyleCoder();
    private static final Coder SUB_SCRIPT_TEXT_STYLE_CODER = new SubScriptTextStyleCoder();
    private static final Coder SUPER_SCRIPT_TEXT_STYLE_CODER = new SuperScriptTextStyleCoder();
    private static final Coder UNDERSCORE_TEXT_STYLE_CODER = new UnderscoreTextStyleCoder();

    //Строчные кодеры
    private static final Coder HEADER_CODER = new HeaderCoder();

    //Блочные кодеры
    private static final Coder MARKED_LIST_CODER = new MarkedListCoder();
    private static final Coder NUMBERED_LIST_CODER = new NumberedListCoder();
    private static final Coder TABLE_CODER = new TableCoder();

    //Блочные Специальные кодеры
    private static final Coder BLOCK_BREAK_CODER = new BlockBreakCoder();
    private static final Coder LINE_BREAK_CODER = new LineBreakCoder();
    private static final Coder XML_PRETTIFY_CODER = new XmlPrettifyCoder();

    //Настроенные цепочки дял преобразования.
    private static final List<Coder> TO_RTF;
    private static final List<Coder> TO_MD;

    static {
        TO_RTF = [
                //Сначала специальные обработчики

                //Затем обходим все подстрочные элементы
                BOLD_TEXT_STYLE_CODER,
                IMAGE_CODER,
                ITALIC_TEXT_STYLE_CODER,
                LINK_CODER,
                STRIKETHROUGH_TEXT_STYLE_CODER,
                SUB_SCRIPT_TEXT_STYLE_CODER,
                SUPER_SCRIPT_TEXT_STYLE_CODER,
                UNDERSCORE_TEXT_STYLE_CODER,

                //Затем обходим все строчные элементы
                HEADER_CODER,

                //Затем обходим все блочные элементы
                TABLE_CODER,
                MARKED_LIST_CODER,
                NUMBERED_LIST_CODER,

                //В завершение опять специальные обработчики.
                BLOCK_BREAK_CODER,
                //LINE_BREAK_CODER,
                XML_PRETTIFY_CODER
        ]

        TO_MD = [
                //Сначала специальные обработчики
                XML_PRETTIFY_CODER,
                BLOCK_BREAK_CODER,
                //LINE_BREAK_CODER,

                //Затем обходим все подстрочные элементы
                BOLD_TEXT_STYLE_CODER,
                IMAGE_CODER,
                ITALIC_TEXT_STYLE_CODER,
                LINK_CODER,
                STRIKETHROUGH_TEXT_STYLE_CODER,
                SUB_SCRIPT_TEXT_STYLE_CODER,
                SUPER_SCRIPT_TEXT_STYLE_CODER,
                UNDERSCORE_TEXT_STYLE_CODER,

                //Затем обходим все строчные элементы
                HEADER_CODER,

                //Затем обходим все блочные элементы
                TABLE_CODER,
                MARKED_LIST_CODER,
                NUMBERED_LIST_CODER,

                //В завершение опять специальные обработчики.
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
