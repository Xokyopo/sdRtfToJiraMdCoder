package ru.itsm365.vita.coder.inline

import ru.itsm365.vita.coder.Coder

import java.util.regex.Matcher
import java.util.regex.Pattern

/**
 * thumbnail - в MD это миниатюра.
 * При конвертации в MD всегда будет проставляться миниатюра, а в RTF обычное изображение.
 * При конвертации из MD поддерживаются как обычные изображения так и миниатюры
 */
class ImageCoder implements Coder {
    private static final String MD_REGEXP_PATTERN = "(?<=^|\\s)![^!\\s]+!(?=\\s|\$)";
    private static final String MD_IMAGE_DIRECTIVE = "thumbnail";
    private static final String MD_SRC_REGEXP_TOKEN = '(?<=!)[^!|\\|]+';
    private static final String MD_TAG = "!";
    private static final String MD_PART_DELIMITER_TAG = "|";
    private static final String RTF_TAG_NAME = "img";
    private static final String RTF_REGEXP_PATTERN = "<img\\s[^><]*>";
    private static final String RTF_SRC_REGEXP_TOKEN = '(?<=src=\\")[^\\"]+';
    private static final String SPACE = " ";
    private static final String MD_NEW_LINE = System.lineSeparator();

    @Override
    String toRtf(String s) {
        StringBuilder result = new StringBuilder();
        Integer cursor = 0;

        Pattern pattern = Pattern.compile(MD_REGEXP_PATTERN, Pattern.CASE_INSENSITIVE);
        Matcher matcher = pattern.matcher(s);

        while (matcher.find()) {
            if (cursor < matcher.start()) {
                result.append(s.substring(cursor, matcher.start()));
            }

            result.append(convertImageMdToRtf(matcher.group()))
            cursor = matcher.end();
        }

        if (cursor < s.length()) {
            result.append(s.substring(cursor));
        }

        return result.toString();
    }

    @Override
    String toMd(String s) {
        StringBuilder result = new StringBuilder();
        Integer cursor = 0;

        Pattern pattern = Pattern.compile(RTF_REGEXP_PATTERN, Pattern.CASE_INSENSITIVE);
        Matcher matcher = pattern.matcher(s);

        while (matcher.find()) {
            if (cursor < matcher.start()) {
                String prefix = s.substring(cursor, matcher.start());
                result.append(prefix);
                if (!(prefix.endsWith(SPACE) || prefix.endsWith(MD_NEW_LINE))) {
                    result.append(SPACE);
                }
            }

            result.append(convertImageRtfToMd(matcher.group()));
            cursor = matcher.end()

            if (cursor < s.length()) {
                if (!(s.startsWith(SPACE, cursor) || s.startsWith(MD_NEW_LINE, cursor))) {
                    result.append(SPACE);
                }
            }
        }

        if (cursor < s.length()) {
            result.append(s.substring(cursor));
        }

        return result.toString();
    }

    private static String convertImageMdToRtf(String mdImage) {
        Pattern pattern = Pattern.compile(MD_SRC_REGEXP_TOKEN);
        Matcher matcher = pattern.matcher(mdImage);

        if (matcher.find()) {
            return createRtfImage(matcher.group());
        }

        return mdImage
    }

    private static String convertImageRtfToMd(String rtfImage) {
        Pattern pattern = Pattern.compile(RTF_SRC_REGEXP_TOKEN, Pattern.CASE_INSENSITIVE);
        Matcher matcher = pattern.matcher(rtfImage);

        if (matcher.find()) {
            return createMdImage(matcher.group());
        }

        return rtfImage;
    }

    private static String createRtfImage(String src) {
        return "<${RTF_TAG_NAME} src=\"${src}\">"
    }

    private static String createMdImage(String src) {
        return "${MD_TAG}${src}${MD_PART_DELIMITER_TAG}${MD_IMAGE_DIRECTIVE}${MD_TAG}"
    }
}
