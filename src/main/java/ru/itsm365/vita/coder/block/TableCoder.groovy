package ru.itsm365.vita.coder.block


import ru.itsm365.vita.coder.Coder
import ru.itsm365.vita.coder.model.Table

import java.util.regex.Matcher
import java.util.regex.Pattern

class TableCoder implements Coder {
//    toRtf
    private final String TAG_TABLE_START = "<table style=\"border-collapse: collapse;\">"
    private final String TAG_TABLE_END = "</table>"
    private final String TAG_THEAD_START = "<thead>"
    private final String TAG_THEAD_END = "</thead>"
    private final String TAG_TBODY_START = "<tbody>"
    private final String TAG_TBODY_END = "</tbody>"
    private final String TAG_TR_START = "<tr >"
    private final String TAG_TR_END = "</tr>"
    private final String TAG_TH_START = "<th style=\"background: rgb(230, 230, 230);border: 1px solid rgb(221, 221, 221);\">"
    private final String TAG_TH_END = "</th>"
    private final String TAG_TD_START = "<td style=\"border: 1px solid rgb(221, 221, 221);\">"
    private final String TAG_TD_END = "</td>"
    private final String LINK_REGEX = "\\[.+\\|http(s)*:\\/\\/.+\\]";
    private final String FILE_REGEX = "!.+?(\\|)thumbnail!";
    private final String TABLE_ROW_REGEX = "^\\|.+\\|\$"
    private final Pattern LINK_PATTERN = Pattern.compile(LINK_REGEX);
    private final Pattern FILE_PATTERN = Pattern.compile(FILE_REGEX);
    private final Pattern TABLE_ROW_PATTERN = Pattern.compile(TABLE_ROW_REGEX);

//    toMd
    private final String TABLE_REGEX = "(?s)<table .+?</table>";
    private final String TR_REGEX = "(?s)<tr .+?>(.+?)</tr>";
    private final String TH_REGEX = "(?s)<th .+?>(.+?)</th>";
    private final String TD_REGEX = "(?s)<td .+?>(.+?)</td>"
    private final Pattern TABLE_PATTERN = Pattern.compile(TABLE_REGEX);
    private final Pattern TR_PATTERN = Pattern.compile(TR_REGEX);
    private final Pattern TH_PATTERN = Pattern.compile(TH_REGEX);
    private final Pattern TD_PATTERN = Pattern.compile(TD_REGEX);


    @Override
    String toRtf(String s) {

        StringBuilder result = new StringBuilder(s);
        s = s.replaceAll("(\\\\\\|)", "Џ");
        s = LINK_PATTERN.matcher(s).replaceAll({ it -> it.group(0).replaceAll("[|]", "˦") });
        s = FILE_PATTERN.matcher(s).replaceAll({ it -> it.group(0).replaceAll("[|]", "˦") });

        List<String> list = List.of(s.split("(?<=\\|)\\n|\\n(?=\\|)"));
        List<Table> tables = new ArrayList<>();
        List<String> text = new ArrayList<>();
        List<List<String>> data = new ArrayList<>();

        int start = Integer.MAX_VALUE;
        int end = 0;
        int counterChars = 0;
        int cccc = 0;
        int cursor = 0;

        boolean hasHeader = false;
        for (String str : list) {
            str = str.replaceAll("\\n", "њ")
            Matcher matcher = TABLE_ROW_PATTERN.matcher(str.trim());
            counterChars += (int) Arrays.stream(str.split("")).filter({ c -> Objects.equals(c, "Џ") }).count();
            cursor += str.length() + 1;
            if (matcher.find()) {
                start = Math.min(cursor - str.length() - 1, start);
                end = cursor - 1;
                String check = matcher.group();
                String[] items = null;
                if (check.startsWith("||")) {
                    hasHeader = true;
                    items = check.split("\\|\\|");
                } else {
                    items = check.split("[|]");
                }
                for (int i = 1; i < items.length; i++) {
                    text.add(items[i]);
                }
                data.add(text);
                text = new ArrayList<>();
            } else {
                if (!data.isEmpty()) {
                    tables.add(new Table(start + cccc, end + counterChars + cccc, hasHeader, data));
                    start = Integer.MAX_VALUE;
                    data = new ArrayList<>();
                    hasHeader = false;
                    cccc += counterChars;
                    counterChars = 0;
                }
            }
        }
        if (!data.isEmpty()) {
            tables.add(new Table(start, end + counterChars, hasHeader, data));
        }

        int shift = 0;
        for (Table table : tables) {
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append(TAG_TABLE_START);
            int counterBodyRows = 0;
            for (List<String> row : table.getData()) {
                if (table.isHasHeader()) {
                    stringBuilder.append(TAG_THEAD_START);
                } else if (counterBodyRows == 0) {
                    stringBuilder.append(TAG_TBODY_START);
                    counterBodyRows++;
                }
                stringBuilder.append(TAG_TR_START);
                for (String cell : row) {
                    table.isHasHeader() ? stringBuilder.append(TAG_TH_START) : stringBuilder.append(TAG_TD_START);
                    stringBuilder.append(cell);
                    table.isHasHeader() ? stringBuilder.append(TAG_TH_END) : stringBuilder.append(TAG_TD_END);
                }
                stringBuilder.append(TAG_TR_END);
                table.isHasHeader() ? stringBuilder.append(TAG_THEAD_END) : "";
                table.setHasHeader(false);
            }
            stringBuilder.append(TAG_TBODY_END)
            counterBodyRows = 0
            stringBuilder.append(TAG_TABLE_END);
            result.replace(table.getStart() + shift, table.getEnd() + shift, stringBuilder.toString());
            shift += stringBuilder.length() - table.getEnd() + table.getStart();
        }
        return result.toString().replaceAll("[Џ˦]", "|").replaceAll("њ", "\n");
    }


    @Override
    String toMd(String s) {
        StringBuilder result = new StringBuilder(s);
        Matcher tableMatcher = TABLE_PATTERN.matcher(s.trim());
        int shift = 0;

        while (tableMatcher.find()) {
            int start = tableMatcher.start()
            int end = tableMatcher.end() //+ 1
            String table = tableMatcher.group(0)
            Matcher tr_matcher = TR_PATTERN.matcher(table.trim());
            StringBuilder tableContent = new StringBuilder()
            while (tr_matcher.find()) {
                String row = tr_matcher.group()
                row = row.replaceAll("\n|\t", "");
                createCell(tableContent, TH_PATTERN.matcher(row.trim()), "||");
                createCell(tableContent, TD_PATTERN.matcher(row.trim()), "|");
            }
            result.replace(start - shift, end - shift, tableContent.toString().substring(0, tableContent.length() - 1))
            shift += end - start - tableContent.length()
        }
        return result
    }

    private static StringBuilder createCell(StringBuilder tableContent, Matcher matcher, String ch) {
        boolean hasCell = false;
        while (matcher.find()) {
            tableContent.append(ch)
            tableContent.append(matcher.group(1))
            hasCell = true;
        }
        if (hasCell) {
            tableContent.append(ch).append("\n");
        }
        return tableContent;
    }
}
