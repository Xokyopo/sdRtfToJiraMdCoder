package ru.itsm365.vita.coder.special

import ru.itsm365.vita.coder.Coder

class LineBreakCoder implements Coder {
    private static final String MD_LINE_BREAKER = "\n";
    private static final String RTF_LINE_BREAKER = "<br/>";

    @Override
    String toRtf(String s) {
        return s.replaceAll(MD_LINE_BREAKER, RTF_LINE_BREAKER);
    }

    @Override
    String toMd(String s) {
        StringBuilder result = new StringBuilder(s);
        String lowerCaseIn = s.toLowerCase();

        List<Integer> matches = [];
        Closure<Integer> getNextPosition = { int it -> lowerCaseIn.indexOf(RTF_LINE_BREAKER, it + RTF_LINE_BREAKER.length()) }
        for (int position = lowerCaseIn.indexOf(RTF_LINE_BREAKER); position > 0; position = getNextPosition(position)) {
            matches << position;
        }

        matches.reverse().forEach { result.replace(it, it + RTF_LINE_BREAKER.length(), MD_LINE_BREAKER) }

        return result.toString();
    }
}
