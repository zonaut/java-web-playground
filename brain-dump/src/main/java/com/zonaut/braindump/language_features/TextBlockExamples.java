package com.zonaut.braindump.language_features;

public class TextBlockExamples {

    public static void main(String[] args) {
        new TextBlockExamples();
    }

    public TextBlockExamples() {
        System.out.print(html);
        printLine();
        System.out.print(sql);
    }

    String html = """
            <html>
                <body>
                    <p>Hello World</p>
                </body>
            </html>
            """;

    String sql = """
            SELECT * from table t 
                WHERE t.field = 'something' 
            """;


    private void printLine() {
        System.out.println("-".repeat(50));
    }
}
