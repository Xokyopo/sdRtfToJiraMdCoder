package ru.itsm365.vita.coder.block

import ru.itsm365.vita.coder.Coder
import ru.itsm365.vita.coder.model.Table

import java.util.regex.Matcher
import java.util.regex.Pattern

class TableCoder implements Coder {
    final String TABLE_START = "<table style=\"border-collapse: collapse;\">"
    final String TABLE_END = "</table>"
    final String THEAD_START = "<thead>"
    final String THEAD_END = "</thead>"
    final String TBODY_START = "<tbody>"
    final String TBODY_END = "</tbody>"
    final String TR_START = "<tr>"
    final String TR_END = "</tr>"
    final String TH_START = "<th style=\"background: rgb(230, 230, 230);border: 1px solid rgb(221, 221, 221);\">"
    final String TH_END = "</th>"
    final String TD_START = "<td style=\"border: 1px solid rgb(221, 221, 221);\">"
    final String TD_END = "</td>"
    final String LINK_PATTERN = "\\[.+\\|http(s)*:\\/\\/.+\\]";
    final String FILE_PATTERN = "!.+?(\\|)thumbnail!";

    @Override
    String toRtf(String s) {

        StringBuilder result = new StringBuilder(s);
        Pattern link_pattern = Pattern.compile(LINK_PATTERN);
        Pattern file_pattern = Pattern.compile(FILE_PATTERN);
        s = s.replaceAll("(\\\\\\|)", "Џ");
        s = link_pattern.matcher(s).replaceAll({ it -> it.group(0).replaceAll("[|]", "˦") });
        s = file_pattern.matcher(s).replaceAll({ it -> it.group(0).replaceAll("[|]", "˦") });

        List<String> list = List.of(s.split("(?<=\\|)\\n|\\n(?=\\|)"));
        List<Table> tables = new ArrayList<>();
        List<String> text = new ArrayList<>();
        List<List<String>> data = new ArrayList<>();

        int start = Integer.MAX_VALUE;
        int end = 0;
        int counterChars = 0;
        int cccc = 0;
        int cursor = 0;
        Pattern pattern = Pattern.compile('^\\|.+\\|$');

        boolean hasHeader = false;
        for (String str : list) {
            str = str.replaceAll("\\n", "њ")
            Matcher matcher = pattern.matcher(str.trim());
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
            stringBuilder.append(TABLE_START);
            int counterBodyRows = 0;
            for (List<String> row : table.getData()) {
                if (table.isHasHeader()) {
                    stringBuilder.append(THEAD_START);
                } else if (counterBodyRows == 0) {
                    stringBuilder.append(TBODY_START);
                    counterBodyRows++;
                }
                stringBuilder.append(TR_START);
                for (String cell : row) {
                    table.isHasHeader() ? stringBuilder.append(TH_START) : stringBuilder.append(TD_START);
                    stringBuilder.append(cell);
                    table.isHasHeader() ? stringBuilder.append(TH_END) : stringBuilder.append(TD_END);
                }
                stringBuilder.append(TR_END);
                table.isHasHeader() ? stringBuilder.append(THEAD_END) : "";
                table.setHasHeader(false);
            }
            stringBuilder.append(TBODY_END)
            counterBodyRows = 0
            stringBuilder.append(TABLE_END);
            result.replace(table.getStart() + shift, table.getEnd() + shift, stringBuilder.toString());
            shift += stringBuilder.length() - table.getEnd() + table.getStart();
        }
        return result.toString().replaceAll("[Џ˦]", "|").replaceAll("њ", "\n");
    }


    @Override
    String toMd(String s) {
        return null
    }
}
