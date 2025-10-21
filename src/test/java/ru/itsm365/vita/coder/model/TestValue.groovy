package ru.itsm365.vita.coder.model

class TestValue {
    public final String md;
    public final String rtf;

    TestValue(String md, String rtf) {
        this.md = loadResource(md)
        this.rtf = loadResource(rtf)
    }

    private static String loadResource(String text) {
        InputStream inputStream = TestValue.class.getResourceAsStream(text)
        try {
            if (inputStream == null) {
                throw new IllegalArgumentException("Ресурс с таким именем не найден или доступ к ресурсу запрещен менеджером безопасности");
            }

            return inputStream.text
        } finally {
            if (inputStream) {
                inputStream.close();
            }
        }
    }
}
