package ru.ushell.app.SQLite;

public class TableColumns {
    protected static final String DATABASE_NAME = "TimeTable.db";
    protected static final int DATABASE_VERSION = 1;
    protected static final String TABLE_NAME_MAIN = "main_schedule";
    protected static final String TABLE_NAME_SECONDARY = "secondary_schedule";

    protected static final String COLUMN_ID = "_id";

    public static final String COLUMN_WEEK = "week";
    public static final String COLUMN_DATE_LESSON = "data_lesson";
    public static final String COLUMN_DAY_WEEK = "day_week";
    public static final String COLUMN_NUM_LESSON = "num_lesson";
    public static final String COLUMN_TIME_START = "timeStart";
    public static final String COLUMN_TIME_END = "timeEnd";
    public static final String COLUMN_TYPE_LESSON = "fullType";
    public static final String COLUMN_SUBJECT = "subjectName";
    public static final String COLUMN_SUBGROUP = "subgroup";
    public static final String COLUMN_WITH_WHOM = "withWhom";
    public static final String COLUMN_ID_GROUP = "id_group";
    public static final String COLUMN_CLASSROOM = "classroom";
}
