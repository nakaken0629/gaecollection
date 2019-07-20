package com.appspot.collabosketch;

public class Command {
    /* コマンド名 */
    private String name;
    /* 送信情報 */
    private String content;

    public Command(String name, String content) {
        this.name = name;
        this.content = content;
    }

    public String getName() { return this.name; }
    public String getContent() { return this.content; }
}
