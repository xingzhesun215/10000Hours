package com.vigorchip.sqlhelper;


import de.greenrobot.daogenerator.DaoGenerator;
import de.greenrobot.daogenerator.Entity;
import de.greenrobot.daogenerator.Schema;


public class FruitDaoGenerator {


    public static final int version = 1;
    public static final String entityPackageName = "com.michoi.db.entity";
    public static final String daoPackageName = "com.michoi.db.dao";

    public static final String autoGenerateJavaPath = GreenDaoConstant.DAO_PATH;

    public static void main(String[] args) throws Exception {

        Schema schema = new Schema(version, entityPackageName);
        schema.setDefaultJavaPackageDao(daoPackageName);
        newRecordTable(schema, "RecordBean");
        new DaoGenerator().generateAll(schema, autoGenerateJavaPath);
    }

    public static void newRecordTable(Schema schema, String beanName) {
        Entity entity = schema.addEntity(beanName);
        entity.addIdProperty();
        entity.addStringProperty("rid");
        entity.addIntProperty("year");
        entity.addIntProperty("month");
        entity.addIntProperty("day");
        entity.addIntProperty("hour");
        entity.addIntProperty("minute");

        entity.addLongProperty("startTime");
        entity.addLongProperty("endTime");
        entity.setJavaDoc("auto greenDao generate javaBean by MonkeyKing");
        entity.setTableName("tb_record");
    }
}
