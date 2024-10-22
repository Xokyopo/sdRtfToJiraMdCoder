package ru.itsm365.vita.coder.line


import org.xml.sax.Attributes
import org.xml.sax.InputSource
import org.xml.sax.SAXException
import org.xml.sax.helpers.DefaultHandler
import ru.itsm365.vita.coder.Coder

import javax.xml.parsers.SAXParser
import javax.xml.parsers.SAXParserFactory
import java.util.regex.Matcher
import java.util.regex.Pattern

class LinkCoder implements Coder {
    private static final String MD_REGEXP_PATTERN = "\\[[^\\|]+\\|[^\\[\\|]*\\]";
    private static final String MD_NEXT_LINE = "\n";
    private static final String MD_OPEN_TAG = "[";
    private static final String MD_CLOSE_TAG = "]";
    private static final String MD_PART_DELIMITER_TAG = "|";
    private static final String RTF_TAG_NAME = "a";
    private static final String RTF_START_LINK_REGEXP_PATTERN = "<a\\s[^><]*>";
    private static final String RTF_LINK_REGEXP_PATTERN = "<a\\s[^><]*>.*<\\/a>";

    @Override
    String toRtf(String s) {
        StringBuilder result = new StringBuilder();
        List<String> lines = s.split(MD_NEXT_LINE);

        Pattern mdToken = Pattern.compile(MD_REGEXP_PATTERN);

        for (String line in lines) {
            Integer cursor = 0;

            Matcher matcher = mdToken.matcher(line);

            while (matcher.find(cursor)) {
                if (cursor < matcher.start()) {
                    result.append(line.substring(cursor, matcher.start()));
                    cursor = matcher.start();
                }

                result.append(convertLinkMdToRtf(line.substring(matcher.start(), matcher.end())));

                cursor = matcher.end();
            }

            if (cursor < line.length()) {
                result.append(line.substring(cursor));
            }

            result.append(MD_NEXT_LINE);
        }

        //Исправляем перевод строки в последней.
        if (result.size() > 0) {
            result.setLength(result.size() - MD_NEXT_LINE.length());
        }

        return result.toString();
    }

    @Override
    String toMd(String s) {
        StringBuilder result = new StringBuilder();

        Pattern pattern = Pattern.compile(RTF_LINK_REGEXP_PATTERN, Pattern.CASE_INSENSITIVE);
        Matcher matcher = pattern.matcher(s);

        Integer cursor = 0;
        while (matcher.find()) {
            if (cursor < matcher.start()) {
                result.append(s.substring(cursor, matcher.start()));
            }

            result.append(convertLinkRtfToMd(s.substring(matcher.start(), matcher.end())));
            cursor = matcher.end();
        }

        if (cursor < s.length()) {
            result.append(s.substring(cursor));
        }

        return result.toString();
    }

    private static String convertLinkMdToRtf(String link) {
        Integer delimiterIndex = link.indexOf(MD_PART_DELIMITER_TAG);
        String text = link.substring(1, delimiterIndex);
        String href = link.substring(delimiterIndex + 1, link.length() - 1);
        return createRtfLink(href, text);
    }

    private static String convertLinkRtfToMd(String link) {
        def parser = new XmlLinkParser();
        parser.parse(link);

        return link;
    }

    private static String createRtfLink(String href, String text) {
        return "<${RTF_TAG_NAME} href=\"${href}\">${text}</${RTF_TAG_NAME}>"
    }

    private static String createMdLink(String href, String text) {
        return "${MD_OPEN_TAG}${text}${MD_PART_DELIMITER_TAG}${href}${MD_CLOSE_TAG}"
    }

    static class XmlLinkParser {
        public String value;
        public String href;

        void parse(String text) {
            SAXParserFactory factory = SAXParserFactory.newInstance();
            SAXParser parser = factory.newSAXParser();
            parser.parse(new InputSource(new StringReader(text)), new ParserHandler())
        }

        private class ParserHandler extends DefaultHandler {
            private int opened = 0;
            private int closed = 0;


            @Override
            void startElement(String uri, String localName, String qName, Attributes attributes) throws SAXException {
                if (RTF_TAG_NAME == qName.toLowerCase()) {
                    if (opened == 0) {
                        href = attributes.getType("href");
                    }
                    opened++;
                }
            }

            @Override
            void endElement(String uri, String localName, String qName) throws SAXException {
                if (RTF_TAG_NAME == qName.toLowerCase()) {
                    closed++;
                }
            }

            @Override
            void characters(char[] ch, int start, int length) throws SAXException {
                println("opened = ${opened}");
                println("closed = ${closed}");
                if (opened == closed && opened > 0) {
                    value = new String(ch, start, length);
                    opened = 0;
                    closed = 0;
                }
            }
        }
    }
}
