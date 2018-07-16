/**
 * Copyright (c) 2011-2016, hubin (jobob@qq.com).
 * <p>
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not
 * use this file except in compliance with the License. You may obtain a copy of
 * the License at
 * <p>
 * http://www.apache.org/licenses/LICENSE-2.0
 * <p>
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations under
 * the License.
 */
package com.baomidou.mybatisplus.generator;

import com.baomidou.mybatisplus.enums.FieldFill;
import com.baomidou.mybatisplus.generator.config.*;
import com.baomidou.mybatisplus.generator.config.po.TableFill;
import com.baomidou.mybatisplus.generator.config.po.TableInfo;
import com.baomidou.mybatisplus.generator.config.rules.DbType;
import com.baomidou.mybatisplus.generator.config.rules.NamingStrategy;
import com.baomidou.mybatisplus.generator.engine.FreemarkerTemplateEngine;
import com.bytter.scf.core.utils.StringUtils;

import java.io.File;
import java.util.*;

/**
 * <p>
 * 代码生成器演示
 * </p>
 *
 * @author hubin
 * @date 2016-12-01
 */
public class MysqlGenerator {


    private GlobalConfig getGlobalConfig(String outputDir) {
        // 全局配置
        GlobalConfig globalConfig = new GlobalConfig()
                .setOutputDir(outputDir)//输出目录
                .setFileOverride(true)// 是否覆盖文件
                .setActiveRecord(false)// 开启 activeRecord 模式
                .setEnableCache(false)// XML 二级缓存
                .setBaseResultMap(true)// XML ResultMap
                .setBaseColumnList(true)// XML columList
                //.setKotlin(true) 是否生成 kotlin 代码
                .setAuthor(GeneratorConfig.AUTHOR)
                // 自定义文件命名，注意 %s 会自动填充表实体属性！
                // .setMapperName("%sDao")
                // .setXmlName("%sDao")
                .setServiceName("%sService");
        // .setServiceImplName("%sServiceDiy")
        // .setControllerName("%sAction")
        return globalConfig;
    }

    private DataSourceConfig getDataSourceConfig() {
        // 数据源配置
        DataSourceConfig dataSourceConfig = new DataSourceConfig()
                .setDbType(DbType.MYSQL)// 数据库类型
//                        .setTypeConvert(new MySqlTypeConvert() {
//                            // 自定义数据库表字段类型转换【可选】
//                            @Override
//                            public DbColumnType processTypeConvert(String fieldType) {
//                                System.out.println("转换类型：" + fieldType);
//                                // if ( fieldType.toLowerCase().contains( "tinyint" ) ) {
//                                //    return DbColumnType.BOOLEAN;
//                                // }
//                                return super.processTypeConvert(fieldType);
//                            }
//                        })
                .setDriverName(GeneratorConfig.DB_DRIVER)
                .setUsername(GeneratorConfig.DB_USERNAME)
                .setPassword(GeneratorConfig.DB_PASSWORD)
                .setUrl(GeneratorConfig.DB_URL);
        return dataSourceConfig;
    }

    private StrategyConfig getStrategyConfig() {
        // 策略配置
        StrategyConfig strategyConfig = new StrategyConfig()
                // .setCapitalMode(true)// 全局大写命名
                // .setDbColumnUnderline(true)//全局下划线命名
                .setTablePrefix(GeneratorConfig.STRATEGY_TABLE_PRIFIX)// 此处可以修改为您的表前缀
                .setNaming(NamingStrategy.underline_to_camel)// 表名生成策略
                .setInclude(GeneratorConfig.STRATEGY_TABLE_INCLUDE) // 需要生成的表
                .setExclude(GeneratorConfig.STRATEGY_TABLE_EXCLUDE) // 排除生成的表
                // 自定义实体父类
                // .setSuperEntityClass("com.baomidou.demo.TestEntity")
                // 自定义实体，公共字段
//                .setSuperEntityColumns(new String[]{"test_id"})
//                .setTableFillList(tableFillList)
                // 自定义 controller 父类
                .setSuperControllerClass("com.bytter.scf.core.base.BaseController")
                //自定义 mapper 父类
                .setSuperMapperClass("com.bytter.scf.core.base.BaseMapper")
                // 自定义 service 父类
                .setSuperServiceClass("com.bytter.scf.core.base.IBaseService")
                // 自定义 service 实现类父类
                .setSuperServiceImplClass("com.bytter.scf.core.base.impl.BaseService")
                // 自定义 接口 父类
                .setSuperMapperClass("com.bytter.scf.core.base.BaseMapper");
        // 【实体】是否生成字段常量（默认 false）
        // public static final String ID = "test_id";
        // .setEntityColumnConstant(true)
        // 【实体】是否为构建者模型（默认 false）
        // public User setName(String name) {this.name = name; return this;}
        // .setEntityBuilderModel(true)
        // 【实体】是否为lombok模型（默认 false）<a href="https://projectlombok.org/">document</a>
        // .setEntityLombokModel(true)
        // Boolean类型字段是否移除is前缀处理
        // .setEntityBooleanColumnRemoveIsPrefix(true)
        // .setRestControllerStyle(true)
        // .setControllerMappingHyphenStyle(true)
        return strategyConfig;
    }

    private PackageConfig getPackageConfig() {
        // 包配置
        PackageConfig packageConfig = new PackageConfig()
                .setModuleName(GeneratorConfig.MODULE_NAME)
                .setParent(GeneratorConfig.PACKAGE_NAME)// 自定义包路径
                .setController("controller");// 这里是控制器包名，默认 web
        return packageConfig;
    }

    private void createPath(String path) {
        File file = new File(path);
        boolean exist = file.exists();
        if (!exist) {
            file.mkdir();
        }
    }


    private InjectionConfig getInjectionConfig(GlobalConfig globalConfig, PackageConfig packageConfig) {
        String outputDir = globalConfig.getOutputDir();
        String moduleName = packageConfig.getModuleName();
        // 注入自定义配置，可以在 VM 中使用 cfg.abc 设置的值
        InjectionConfig injectionConfig = new InjectionConfig() {
            @Override
            public void initMap() {
                Map<String, Object> map = new HashMap<>();
                this.setMap(map);
            }
        };

        List<FileOutConfig> fileOutConfigs = new ArrayList<>();
        //映射配置
        fileOutConfigs.add(new FileOutConfig("/templates/mapper.xml.ftl") {
            // 自定义输出文件目录
            @Override
            public String outputFile(TableInfo tableInfo) {
                String path = outputDir + "/mapper/";
                createPath(path);
                return path + tableInfo.getEntityName() + "Mapper.xml";
            }
        });
        //新增界面
        fileOutConfigs.add(new FileOutConfig("/templates/pages/add.ftl") {
            @Override
            public String outputFile(TableInfo tableInfo) {
                String path = outputDir + "/templates/" + moduleName + "/" + StringUtils.firstCharToLowerCase(tableInfo.getEntityName()) + "/";
                createPath(path);

                return path + "add.ftl";
            }
        });

        //更新界面
        fileOutConfigs.add(new FileOutConfig("/templates/pages/update.ftl") {
            @Override
            public String outputFile(TableInfo tableInfo) {
                String path = outputDir + "/templates/" + moduleName + "/" + StringUtils.firstCharToLowerCase(tableInfo.getEntityName()) + "/";
                createPath(path);

                return path + "update.ftl";
            }
        });

        //列表页面
        fileOutConfigs.add(new FileOutConfig("/templates/pages/list.ftl") {
            @Override
            public String outputFile(TableInfo tableInfo) {
                String path = outputDir + "/templates/" + moduleName + "/" + StringUtils.firstCharToLowerCase(tableInfo.getEntityName()) + "/";
                createPath(path);

                return path + "list.ftl";
            }
        });

        //表单页面
        fileOutConfigs.add(new FileOutConfig("/templates/pages/form.ftl") {
            @Override
            public String outputFile(TableInfo tableInfo) {
                String path = outputDir + "/templates/" + moduleName + "/" + StringUtils.firstCharToLowerCase(tableInfo.getEntityName()) + "/";
                createPath(path);

                return path + "form.ftl";
            }
        });

        injectionConfig.setFileOutConfigList(fileOutConfigs);
        return injectionConfig;
    }

    private TemplateConfig getTemplateConfig() {
        // 关闭默认 xml 生成，调整生成 至 根目录
        TemplateConfig templateConfig = new TemplateConfig().setXml(null);
        // 自定义模板配置，模板可以参考源码 /mybatis-plus/src/main/resources/template 使用 copy
        // 至您项目 src/main/resources/template 目录下，模板名称也可自定义如下配置：
        // .setController("...");
        // .setEntity("...");
        // .setMapper("...");
        // .setXml("...");
        // .setService("...");
        // .setServiceImpl("...");
        return templateConfig;
    }

    public void generator() {
        // 自定义需要填充的字段
        List<TableFill> tableFillList = new ArrayList<>();
        tableFillList.add(new TableFill("ASDD_SS", FieldFill.INSERT_UPDATE));
        String outputDir = GeneratorConfig.OUT_PUT_DIR;
        GlobalConfig globalConfig = getGlobalConfig(outputDir);
        PackageConfig packageConfig = getPackageConfig();
        // 代码生成器
        AutoGenerator mpg = new AutoGenerator()
                .setGlobalConfig(globalConfig)
                .setDataSource(getDataSourceConfig())
                .setStrategy(getStrategyConfig())
                .setPackageInfo(packageConfig)
                .setCfg(getInjectionConfig(globalConfig, packageConfig))
                .setTemplate(getTemplateConfig());
        // 执行生成
        mpg.setTemplateEngine(new FreemarkerTemplateEngine());
        mpg.execute();

    }
}
