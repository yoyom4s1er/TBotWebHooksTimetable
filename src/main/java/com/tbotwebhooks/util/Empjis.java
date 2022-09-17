package com.tbotwebhooks.util;

import com.vdurmont.emoji.EmojiParser;
import lombok.AllArgsConstructor;

@AllArgsConstructor
public enum Empjis {
    MAN_TEACHER(EmojiParser.parseToUnicode(":man_teacher:")),
    CLOCK(EmojiParser.parseToUnicode(":clock4:")),
    WORLD_MAP(EmojiParser.parseToUnicode(":world_map:")),
    MAN_STUDENT(EmojiParser.parseToUnicode(":man_student:")),
    PEN(EmojiParser.parseToUnicode(":lower_left_ballpoint_pen:"));

    private String emojiName;

    @Override
    public String toString() {
        return emojiName;
    }
}
