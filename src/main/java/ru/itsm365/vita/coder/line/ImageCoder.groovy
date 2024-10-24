package ru.itsm365.vita.coder.line

import ru.itsm365.vita.coder.Coder

import java.util.regex.Matcher
import java.util.regex.Pattern

class ImageCoder implements Coder {
    private static final String MD_REGEXP_PATTERN = "![^\\|]*\\|thumbnail!";
    private static final String MD_IMAGE_DIRECTIVE = "thumbnail";
    private static final String MD_NEXT_LINE = "\n";
    private static final String MD_TAG = "!";
    private static final String MD_PART_DELIMITER_TAG = "|";
    private static final String RTF_TAG_NAME = "img";
    private static final String RTF_IMG_TAG_REGEXP_PATTERN = "<img\\s[^><]*>";
    private static final String RTF_SRC_REGEXP_TOKEN = '(?<=src=\\")[^\\"]+';

    @Override
    String toRtf(String s) {
        return convertImageMdToRtf(s)
    }

    @Override
    String toMd(String s) {
        return convertImageRtfToMd(s);
    }

    private static String convertImageMdToRtf(String mdImage) {
        Integer mdImageTailSize = MD_TAG.size() + MD_PART_DELIMITER_TAG.size() + MD_IMAGE_DIRECTIVE.size();
        if (mdImage.length() > mdImageTailSize) {
            return createRtfImage(mdImage.substring(MD_TAG.size(), mdImage.length() - mdImageTailSize));
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
