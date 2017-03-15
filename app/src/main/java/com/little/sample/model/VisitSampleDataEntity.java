package com.little.sample.model;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

@DatabaseTable(tableName="VisitSampleDataEntity")
public class VisitSampleDataEntity {
    @DatabaseField(id=true)
    public String brandName;
}
