package ru.itsm365.vita.coder.model

class TestValue {
    public final String md;
    public final String rtf;

    TestValue(String md, String rtf) {
        this.md = loadResource(md)
        this.rtf = loadResource(rtf)
    }

    private static String loadResource(String text) {
        return TestValue.class.getResourceAsStream(text).withCloseable {
            if (it == null) {
                throw new IllegalArgumentException("Ресурс с таким именем не найден или доступ к ресурсу запрещен менеджером безопасности");
            }

            return it.text
        }
    }
}
