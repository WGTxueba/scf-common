package com.baomidou.mybatisplus.generator;


/**
 * 代码生成说明：
 * 1、按照下面常量的注释，配置自己的相关信息
 * 2、生成完成后，该程序会自动打开代码生成的目录
 * 3、将com文件夹复制并粘贴到工程的java目录，即可将所有java文件转移到工程内。mapper和templates同理
 */
public class GeneratorConfig {

    /**
     * 输出目录
     * (需要配置)
     */
    public static final String OUT_PUT_DIR = "D:\\Generator";

    /**
     * 模块名称
     * (需要配置)
     */
    public static final String MODULE_NAME = "demo";

    /**
     * 模块负责人
     * (需要配置)
     */
    public static final String AUTHOR = "chenlj";

    /**
     * 表前缀
     */
    public static final String[] STRATEGY_TABLE_PRIFIX = {""};

    /**
     * 需要按哪个表进行代码生成
     */
    public static final String[] STRATEGY_TABLE_INCLUDE = {"demo2"};
    /**
     * 排除哪些表不需要生产
     */
    public static final String[] STRATEGY_TABLE_EXCLUDE = {};


    /**
     * 数据库相关配置
     * (按个人要求配置)
     */
    public static final String DB_DRIVER = "com.mysql.jdbc.Driver";
    public static final String DB_USERNAME = "root";
    public static final String DB_PASSWORD = "123456";
    public static final String DB_URL = "jdbc:mysql://119.29.25.112:3306/scf_front?characterEncoding=utf8";

    /**
     * 包名称
     */
    public static final String PACKAGE_NAME = "com.bytter.scf.front";

    public static void main(String[] args) {
        new MysqlGenerator().generator();
    }

}
