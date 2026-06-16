package ru.itsm365.vita.coder.block

import ru.itsm365.vita.coder.Coder

import java.util.regex.Matcher
import java.util.regex.Pattern

class TableCoder implements Coder {
    private final String LINE_SEPARATOR = "${System.lineSeparator()}"
//    toRtf
    private final String TAG_TABLE_START = "<table style=\"border-collapse: collapse;\">"
    private final String TAG_TABLE_END = "</table>"
    private final String TAG_THEAD_START = "<thead>"
    private final String TAG_THEAD_END = "</thead>"
    private final String TAG_TBODY_START = "<tbody>"
    private final String TAG_TBODY_END = "</tbody>"
    private final String TAG_TR_START = "<tr>"
    private final String TAG_TR_END = "</tr>"
    private final String TAG_TH_START = "<th style=\"background: rgb(230, 230, 230);border: 1px solid rgb(0, 0, 0);\">"
    private final String TAG_TH_END = "</th>"
    private final String TAG_TD_START = "<td style=\"border: 1px solid rgb(0, 0, 0);\">"
    private final String TAG_TD_END = "</td>"
    private final String ROW_REGEX = "(?msi)^ *(?=\\|).+?(?<=\\|)\$(${LINE_SEPARATOR})?| *"
    private final String LINK_REGEX = "\\[.+\\|http(s)*:\\/\\/.+\\]";
    private final String FILE_REGEX = "!.+?(\\|)thumbnail!";
    private final Pattern LINK_PATTERN = Pattern.compile(LINK_REGEX);
    private final Pattern FILE_PATTERN = Pattern.compile(FILE_REGEX);
    private final Pattern ROW_PATTERN = Pattern.compile(ROW_REGEX);

//    toMd
    private final String tr = "(?s)<tr[^<]*?>.+?</tr[^<]*?>";
    private final String th = "(?s)<th[^<]*?>.+?</th[^<]*?>";
    private final String td = "(?s)<td[^<]*?>.+?</td[^<]*?>";
    private final String theadP = "(?s)<thead[^<]*?>.+</thead[^<]*?>";
    private final String tbodyP = "(?s)<tbody[^<]*?>.+</tbody[^<]*?>";
    private final String tableP = "(?s)<table[^<]*?>.+</table[^<]*?>";
    private final Pattern patternTable = Pattern.compile(tableP, Pattern.CASE_INSENSITIVE);
    private final Pattern patternTHead = Pattern.compile(theadP, Pattern.CASE_INSENSITIVE);
    private final Pattern patternTBody = Pattern.compile(tbodyP, Pattern.CASE_INSENSITIVE);
    private final Pattern patternTr = Pattern.compile(tr, Pattern.CASE_INSENSITIVE);
    private final Pattern patternTh = Pattern.compile(th, Pattern.CASE_INSENSITIVE);
    private final Pattern patternTd = Pattern.compile(td, Pattern.CASE_INSENSITIVE);
    private final String TABLE_REGEX = "(?msi)<table.+?</table>";
    private final String TR_REGEX = "(?msi) *<tr.*?>(.+?)</tr>";
    private final String TH_REGEX = "(?msi)<th.*?>(.+?)</th>";
    private final String TD_REGEX = "(?msi)<td.*?>(.+?)</td>"
    private final Pattern TABLE_PATTERN = Pattern.compile(TABLE_REGEX);
    private final Pattern TR_PATTERN = Pattern.compile(TR_REGEX);
    private final Pattern TH_PATTERN = Pattern.compile(TH_REGEX);
    private final Pattern TD_PATTERN = Pattern.compile(TD_REGEX);

    @Override
    String toRtf(String s) {
        int cursor = 0;
        boolean hasNewLine = false;
        s = s.replaceAll("(\\\\\\|)", "Џ");
        s = LINK_PATTERN.matcher(s).replaceAll({ it -> it.group(0).replaceAll("[|]", "˦") });
        s = FILE_PATTERN.matcher(s).replaceAll({ it -> it.group(0).replaceAll("[|]", "˦") });
        List<String> rowCells = new ArrayList<>();
        List<List<String>> tableRows = new ArrayList<>();

        StringBuilder resultRtf = new StringBuilder();

        int start = Integer.MAX_VALUE;
        int end = 0;
        int cells = 0
        Boolean isBroken = false;

        boolean hasHeader = false;
        Matcher matcher = ROW_PATTERN.matcher(s);
        while (matcher.find()) {
            String check = matcher.group(0);
            if (!check.isBlank()) {
                hasNewLine = check.endsWith("${LINE_SEPARATOR}")
                start = Math.min(matcher.start(), start);
                end = matcher.end();
                String[] items = null;
                if (check.find("^ *\\|\\|")) {
                    hasHeader = true;
                    items = check.split("\\|\\|");
                } else {
                    items = check.split("[|]");
                }
                for (int i = 1; i < items.length; i++) {
                    if (items[i].trim()) {
                        rowCells.add(items[i]);
                    }
                }
                if (!cells) {
                    cells = rowCells.size()
                }
                if (cells == 1 || cells != rowCells.size()) {
                    isBroken = true;
                }
                tableRows.add(rowCells);
                rowCells = new ArrayList<>();
            } else {
                if (!tableRows.isEmpty() && !isBroken) {
                    resultRtf.append(s.substring(cursor, start))

                    resultRtf.append(TAG_TABLE_START);
                    resultRtf.append(createRtfTable(tableRows, hasHeader))
                    resultRtf.append(TAG_TBODY_END)
                    resultRtf.append(TAG_TABLE_END);
                    if (hasNewLine) {
                        resultRtf.append("${LINE_SEPARATOR}")
                    }

                    start = Integer.MAX_VALUE;
                    tableRows.clear();
                    hasHeader = false;
                } else {
                    resultRtf.append(s.substring(cursor, end));
                }
                cursor = end;
            }
        }

        if (cursor < s.length()) {
            resultRtf.append(s.substring(cursor));
        }

        return resultRtf.toString().replaceAll("[Џ˦]", "|")
    }

    private StringBuilder createRtfTable(ArrayList<List<String>> data, boolean hasHeader) {
        StringBuilder stringBuilder = new StringBuilder();

        int counterBodyRows = 0;
        int counter = 0;
        for (List<String> row : data) {
            if (hasHeader) {
                stringBuilder.append(TAG_THEAD_START);
            } else if (counterBodyRows == 0) {
                stringBuilder.append(TAG_TBODY_START);
                counterBodyRows++;
            }

            stringBuilder.append(TAG_TR_START);
            for (String cell : row) {
                hasHeader ? stringBuilder.append(TAG_TH_START) : stringBuilder.append(TAG_TD_START);
                stringBuilder.append(cell);
                hasHeader ? stringBuilder.append(TAG_TH_END) : stringBuilder.append(TAG_TD_END);
            }
            stringBuilder.append(TAG_TR_END);
            hasHeader ? stringBuilder.append(TAG_THEAD_END) : "";
            hasHeader = false;
            counter++
        }

        return stringBuilder
    }


    @Override
    String toMd(String s) {
        StringBuilder result = new StringBuilder(s);
        Matcher tableMatcher = TABLE_PATTERN.matcher(s);
        int shift = 0;

        while (tableMatcher.find()) {
            int start = tableMatcher.start()
            int end = tableMatcher.end()
            String table = tableMatcher.group(0)
            if (checkTable(table)) {
                Matcher tr_matcher = TR_PATTERN.matcher(table.trim());
                StringBuilder tableContent = new StringBuilder()
                while (tr_matcher.find()) {
                    String row = tr_matcher.group()
                    String whitespace = row.find("(?msi) *(?=<tr.*?>)")
                    createCell(tableContent, whitespace, TH_PATTERN.matcher(row), "||");
                    createCell(tableContent, whitespace, TD_PATTERN.matcher(row), "|");
                }
                tableContent.setLength(tableContent.length() - 1)
                result.replace(start - shift, end - shift, tableContent.toString().substring(0, tableContent.length() - 1))
                shift += end - start - tableContent.length() + 1
            }
        }
        return result
    }

    private StringBuilder createCell(StringBuilder tableContent, String space, Matcher matcher, String ch) {
        boolean hasCell = false;
        Boolean isFirstSpace = true;
        while (matcher.find()) {
            if (isFirstSpace) {
                tableContent.append(space);
                isFirstSpace = false;
            }
            tableContent.append(ch)
            tableContent.append(matcher.group(1))
            hasCell = true;
        }
        if (hasCell) {
            tableContent.append(ch).append("${LINE_SEPARATOR}");
        }
        return tableContent;
    }


    private Boolean checkTable(String rtf) {
        Matcher matcher = patternTable.matcher(rtf);
        StringBuilder builder = new StringBuilder();
        try {
            String result = ""
            if (matcher.find()) {
                result = matcher.group()
            } else {
                throw new Exception("Не нашёл таблицу");
            }
            result = replaceTags(result, "<table", "</table");
            String thead = getBlock(patternTHead, result, "<thead", "</thead");
            String tbody = getBlock(patternTBody, result, "<tbody", "</tbody");
            if (!thead && !tbody) {
                throw new Exception("Что-то пошло не так...222")
            }
            if (thead) {
                result = result.replaceAll(theadP, "");
            }
            if (tbody) {
                result = result.replaceAll(tbodyP, "");
            }
            builder.append(result.trim());

            checkTags(thead, patternTr, patternTh);
            checkTags(tbody, patternTr, patternTd);
            String thRow = thead.replaceAll(tr, "");
            String tdRow = tbody.replaceAll(tr, "");
            builder.append(thRow.trim());
            builder.append(tdRow.trim());
            thead = replaceTags(thead, "<tr", "</tr");
            tbody = replaceTags(tbody, "<tr", "</tr");
            String thCell = thead.replaceAll(th, "");
            String tdCell = tbody.replaceAll(td, "");
            builder.append(thCell.trim());
            builder.append(tdCell.trim())
            if (builder.toString() != "") {
                throw new Exception("Что-то пошло не так...000")
            }
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    private String getBlock(Pattern pattern, String str, String openingTag, String closingTag) {
        Matcher matcher = pattern.matcher(str);
        if (matcher.find()) {
            return replaceTags(matcher.group(), openingTag, closingTag);
        }
        return ""
    }

    private void checkTags(String block, Pattern patternTr, Pattern pattern) {

        Integer cells = 0;
        Integer rows = 0;
        Boolean isFirst = true;

        Matcher matcherTr = patternTr.matcher(block);
        while (matcherTr.find()) {
            Integer counter = 0;
            rows++;
            String row = matcherTr.group();
            Matcher matcher = pattern.matcher(row);
            while (matcher.find()) {
                counter++;
            }
            if (isFirst) {
                cells = counter;
            }
            if (cells != counter) {
                throw new Exception("Количество ячеек не совпадает");
            }
            isFirst = false
        }
    }

    private String replaceTags(String text, String element1, String element2) {
        text = text.replaceAll(element1 + "[^<]*?>", "");
        text = text.replaceAll(element2 + "[^<]*?>", "");
        return text;
    }
}
