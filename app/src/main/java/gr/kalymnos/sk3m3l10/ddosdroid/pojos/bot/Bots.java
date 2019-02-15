package gr.kalymnos.sk3m3l10.ddosdroid.pojos.bot;

import gr.kalymnos.sk3m3l10.ddosdroid.mvc_model.bot.FirebaseInstanceId;

public final class Bots {
    public static Bot local() {
        return new Bot(new FirebaseInstanceId().getInstanceId());
    }

    public static String localId() {
        return local().getId();
    }
}
