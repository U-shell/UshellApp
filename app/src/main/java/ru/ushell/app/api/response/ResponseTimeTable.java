package ru.ushell.app.api.response;

import java.util.Map;

public class ResponseTimeTable {

    private Map<String, Object> main_schedule;

    private Map<String, Object> secondary_schedule;

    public Map<String, Object> getMain_schedule() {
        return main_schedule;
    }

    public void setMain_schedule(Map<String, Object> main_schedule) {
        this.main_schedule = main_schedule;
    }

    public Map<String, Object> getSecondary_schedule() {
        return secondary_schedule;
    }

    public void setSecondary_schedule(Map<String, Object> secondary_schedule) {
        this.secondary_schedule = secondary_schedule;
    }
}
