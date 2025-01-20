package ru.itsm365.vita.coder.block


import ru.itsm365.vita.coder.Coder

import java.util.regex.Matcher
import java.util.regex.Pattern

class TableCoder implements Coder {
//    toRtf
    final String TAG_TABLE_START = "<table style=\"border-collapse: collapse;\">"
    final String TAG_TABLE_END = "</table>"
    final String TAG_THEAD_START = "<thead>"
    final String TAG_THEAD_END = "</thead>"
    final String TAG_TBODY_START = "<tbody>"
    final String TAG_TBODY_END = "</tbody>"
    final String TAG_TR_START = "<tr >"
    final String TAG_TR_END = "</tr>"
    final String TAG_TH_START = "<th style=\"background: rgb(230, 230, 230);border: 1px solid rgb(221, 221, 221);\">"
    final String TAG_TH_END = "</th>"
    final String TAG_TD_START = "<td style=\"border: 1px solid rgb(221, 221, 221);\">"
    final String TAG_TD_END = "</td>"
    final String ROW_REGEX = "(?msi)^ *(?=\\|).+?(?<=\\|)\$\\n?|"
    final String LINK_REGEX = "\\[.+\\|http(s)*:\\/\\/.+\\]";
    final String FILE_REGEX = "!.+?(\\|)thumbnail!";
    final Pattern LINK_PATTERN = Pattern.compile(LINK_REGEX);
    final Pattern FILE_PATTERN = Pattern.compile(FILE_REGEX);
    final Pattern ROW_PATTERN = Pattern.compile(ROW_REGEX);

//    toMd
    final String tr = "(?s)<tr[^<]*?>.+?</tr[^<]*?>";
    final String th = "(?s)<th[^<]*?>.+?</th[^<]*?>";
    final String td = "(?s)<td[^<]*?>.+?</td[^<]*?>";
    final String theadP = "(?s)<thead[^<]*?>.+</thead[^<]*?>";
    final String tbodyP = "(?s)<tbody[^<]*?>.+</tbody[^<]*?>";
    final String tableP = "(?s)<table[^<]*?>.+</table[^<]*?>";
    final Pattern patternTable = Pattern.compile(tableP, Pattern.CASE_INSENSITIVE);
    final Pattern patternTHead = Pattern.compile(theadP, Pattern.CASE_INSENSITIVE);
    final Pattern patternTBody = Pattern.compile(tbodyP, Pattern.CASE_INSENSITIVE);
    final Pattern patternTr = Pattern.compile(tr, Pattern.CASE_INSENSITIVE);
    final Pattern patternTh = Pattern.compile(th, Pattern.CASE_INSENSITIVE);
    final Pattern patternTd = Pattern.compile(td, Pattern.CASE_INSENSITIVE);
    final String TABLE_REGEX = "(?msi)<table.+?</table>";
    final String TR_REGEX = "(?msi) *<tr.*?>(.+?)</tr>";
    final String TH_REGEX = "(?msi)<th.*?>(.+?)</th>";
    final String TD_REGEX = "(?msi)<td.*?>(.+?)</td>"
    final Pattern TABLE_PATTERN = Pattern.compile(TABLE_REGEX);
    final Pattern TR_PATTERN = Pattern.compile(TR_REGEX);
    final Pattern TH_PATTERN = Pattern.compile(TH_REGEX);
    final Pattern TD_PATTERN = Pattern.compile(TD_REGEX);

    StringBuilder resultRtf;
    int shift = 0;
    Boolean hasNewLine = false;

    @Override
    String toRtf(String s) {

        s = s.replaceAll("(\\\\\\|)", "Џ");
        s = LINK_PATTERN.matcher(s).replaceAll({ it -> it.group(0).replaceAll("[|]", "˦") });
        s = FILE_PATTERN.matcher(s).replaceAll({ it -> it.group(0).replaceAll("[|]", "˦") });
        resultRtf = new StringBuilder(s);
        List<String> text = new ArrayList<>();
        List<List<String>> data = new ArrayList<>();

        int start = Integer.MAX_VALUE;
        int end = 0;
        int cells = 0
        Boolean isBroken = false;

        boolean hasHeader = false;
        Matcher matcher = ROW_PATTERN.matcher(s);
        List<String> whitespace = new ArrayList<>()
        while (matcher.find()) {
            String check = matcher.group();
            if (check) {
                whitespace.add(check.find("^ *(?=\\|)"))
                hasNewLine = check.endsWith("\n")
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
                        text.add(items[i]);
                    }
                }
                if (!cells) {
                    cells = text.size()
                }
                if (cells == 1 || cells != text.size()) {
                    isBroken = true;
                }
                data.add(text);
                text = new ArrayList<>();
            } else {
                if (!data.isEmpty() && !isBroken) {
                    toRtf000(start, end, hasHeader, data, whitespace);
                    start = Integer.MAX_VALUE;
                    data = new ArrayList<>();
                    hasHeader = false;
                    whitespace = new ArrayList<>()
                }
            }
        }
        return resultRtf.toString().replaceAll("[Џ˦]", "|")
    }

    private void toRtf000(Integer start, Integer end, Boolean isHasHeader, List<List<String>> data, List<String> space) {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(TAG_TABLE_START);
        int counterBodyRows = 0;
        int counter = 0;
        for (List<String> row : data) {
            if (isHasHeader) {
                stringBuilder.append(TAG_THEAD_START);
            } else if (counterBodyRows == 0) {
                stringBuilder.append(TAG_TBODY_START);
                counterBodyRows++;
            }
            stringBuilder.append(space[counter])
            stringBuilder.append(TAG_TR_START);
            for (String cell : row) {
                isHasHeader ? stringBuilder.append(TAG_TH_START) : stringBuilder.append(TAG_TD_START);
                stringBuilder.append(cell);
                isHasHeader ? stringBuilder.append(TAG_TH_END) : stringBuilder.append(TAG_TD_END);
            }
            stringBuilder.append(TAG_TR_END);
            isHasHeader ? stringBuilder.append(TAG_THEAD_END) : "";
            isHasHeader = false;
            counter++
        }
        stringBuilder.append(TAG_TBODY_END)
        stringBuilder.append(TAG_TABLE_END);
        if (hasNewLine) {
            stringBuilder.append("\n")
        }
        resultRtf.replace(start + shift, end + shift, stringBuilder.toString());
        shift -= -stringBuilder.length() + (end - start);
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
                result.replace(start - shift, end - shift, tableContent.toString().substring(0, tableContent.length() - 1))
                shift += end - start - tableContent.length() + 1
            }
        }
        return result
    }

    private static StringBuilder createCell(StringBuilder tableContent, String space, Matcher matcher, String ch) {
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
            tableContent.append(ch).append("\n");
        }
        return tableContent;
    }


    Integer cells = 0;
    Integer rows = 0;
    Boolean isFirst = true;

    Boolean checkTable(String rtf) {
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

    static String getBlock(Pattern pattern, String str, String openingTag, String closingTag) {
        Matcher matcher = pattern.matcher(str);
        if (matcher.find()) {
            return replaceTags(matcher.group(), openingTag, closingTag);
        }
        return ""
    }

    void checkTags(String block, Pattern patternTr, Pattern pattern) {
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

    static String replaceTags(String text, String element1, String element2) {
        text = text.replaceAll(element1 + "[^<]*?>", "");
        text = text.replaceAll(element2 + "[^<]*?>", "");
        return text;
    }
}
