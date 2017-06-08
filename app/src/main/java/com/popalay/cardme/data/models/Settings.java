package com.popalay.cardme.data.models;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

public class Settings extends RealmObject {

    @PrimaryKey
    private String id = "0";
    private String language;
    private String theme;

    public Settings() {}

    private Settings(Builder builder) {
        setLanguage(builder.language);
        theme = builder.theme;
    }

    public String getId() {
        return id;
    }

    public String getLanguage() {
        return language;
    }

    public void setLanguage(String language) {
        this.language = language;
    }

    public String getTheme() {
        return theme;
    }

    public void setTheme(String theme) {
        this.theme = theme;
    }

    public static final class Builder {

        private String language;
        private String theme;

        public Builder() {}

        public Builder language(String val) {
            language = val;
            return this;
        }

        public Builder theme(String val) {
            theme = val;
            return this;
        }

        public Settings build() {
            return new Settings(this);
        }
    }
}
