package com.zonaut.sbreactive.types;

import java.util.EnumSet;
import java.util.Set;

public enum TodoPriority {

    NORMAL,
    LOW,
    HIGH;

    public static final String OBJECT_TYPE = "todo_priority_type";

    public static final Set<TodoPriority> ALL_VALUES = EnumSet.allOf(TodoPriority.class);

}
