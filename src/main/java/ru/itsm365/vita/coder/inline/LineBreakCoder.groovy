package ru.itsm365.vita.coder.inline

import ru.itsm365.vita.coder.Coder

class LineBreakCoder implements Coder {
    private static final String MD_LINE_SEPARATOR = System.lineSeparator();
    private static final String MD_NBSP = " "
    private static final String RTF_LINE_SEPARATOR = "<br>";
    private static final String MD_TABLE_CELL_DELIMITER = "|";
    private static final String RTF_FIRST_LINE_BREAKER_TAG_TOKEN = "(?i)(?<!<br/?>)<br *\\/?>";
    private static final String RTF_LINE_BREAKER_TAG_TOKEN = "(?i)<br *\\/?>";
    private static final String MD_SEPARATED_TABLE_LINE_TOKEN = "${MD_NBSP}${MD_LINE_SEPARATOR}(?!${MD_LINE_SEPARATOR})";


    @Override
    String toRtf(String s) {
        String mergedTableText = s.replaceAll(MD_SEPARATED_TABLE_LINE_TOKEN, "${RTF_LINE_SEPARATOR}")
        String mergedTableBR = mergedTableText.replaceAll("(?<!${MD_LINE_SEPARATOR})${MD_LINE_SEPARATOR}(?=<br/?>)", "${RTF_LINE_SEPARATOR}")

        //Преобразует у таблицы переносы строк.
        StringBuilder joinedTableBreakLine = new StringBuilder();
        String[] lines = mergedTableBR.split(MD_LINE_SEPARATOR)
        boolean isSplitTableRow = false;
        for (int i = 0; i < lines.length; i++) {
            joinedTableBreakLine.append(lines[i])

            String trimmedLine = lines[i].trim();

            if (!isSplitTableRow) {

                isSplitTableRow = trimmedLine.startsWith(MD_TABLE_CELL_DELIMITER) && !trimmedLine.endsWith(MD_TABLE_CELL_DELIMITER)
            }

            if (isSplitTableRow) {
                if (trimmedLine.endsWith(MD_TABLE_CELL_DELIMITER)) {
                    isSplitTableRow = false;
                } else {
                    joinedTableBreakLine.append(RTF_LINE_SEPARATOR)
                }
                continue;
            }

            if (i < lines.length - 1) {
                joinedTableBreakLine.append(MD_LINE_SEPARATOR)
            }
        }

        String withEmptyLines = joinedTableBreakLine.replaceAll("(?<=${MD_LINE_SEPARATOR})${MD_NBSP}(?=${MD_LINE_SEPARATOR})", RTF_LINE_SEPARATOR);
        String removedBefore = withEmptyLines.replaceAll("(\\r?\\n)+(?=${RTF_LINE_SEPARATOR})", MD_LINE_SEPARATOR);
        return removedBefore.replaceAll("(?<=${RTF_LINE_SEPARATOR})(\\r?\\n)+", MD_LINE_SEPARATOR);
    }

    @Override
    String toMd(String s) {
        String replacedEmptyLine = s.replaceAll("(?<=${MD_LINE_SEPARATOR})${RTF_LINE_SEPARATOR}(?=${MD_LINE_SEPARATOR})", "${MD_LINE_SEPARATOR}${MD_NBSP}${MD_LINE_SEPARATOR}");
        String fixedDoubleSeparetion = replacedEmptyLine.replaceAll("(?<=${MD_NBSP})(\\r?\\n)+(?=${MD_NBSP})", "${MD_LINE_SEPARATOR}${MD_LINE_SEPARATOR}");
        String tableLineBreaker = fixedDoubleSeparetion.replaceAll(RTF_FIRST_LINE_BREAKER_TAG_TOKEN, MD_LINE_SEPARATOR)
        return tableLineBreaker.replaceAll(RTF_LINE_BREAKER_TAG_TOKEN, "${MD_NBSP}${MD_LINE_SEPARATOR}")
    }
}
