package com.springboot.whb.study.common.mybatisPlus;

import com.baomidou.mybatisplus.generator.AutoGenerator;
import com.baomidou.mybatisplus.generator.config.DataSourceConfig;
import com.baomidou.mybatisplus.generator.config.GlobalConfig;
import com.baomidou.mybatisplus.generator.config.PackageConfig;
import com.baomidou.mybatisplus.generator.config.StrategyConfig;
import com.baomidou.mybatisplus.generator.config.converts.MySqlTypeConvert;
import com.baomidou.mybatisplus.generator.config.rules.DbColumnType;
import com.baomidou.mybatisplus.generator.config.rules.NamingStrategy;

/**
 * @author: whb
 * @date: 2019/7/15 10:14
 * @description: MybatisPlus自动生成代码
 */
public class MybatisPlusGenerator {

    private static MybatisPlusGenerator single = null;

    private MybatisPlusGenerator() {
        super();
    }

    private static MybatisPlusGenerator getSingle() {
        if (single == null) {
            single = new MybatisPlusGenerator();
        }
        return single;
    }

    public void autoGeneration() {
        //全局配置
        GlobalConfig config = new GlobalConfig();
        String projectPath = System.getProperty("user.dir");
        //不需要ActiveRecord特性请改为false
        config.setActiveRecord(false)
                //XML二级缓存
                .setEnableCache(false)
                //XML ResultMap
                .setBaseResultMap(true)
                //XML columnList
                .setBaseColumnList(true)
                //是否覆盖
                .setFileOverride(true)
                //作者
                .setAuthor("whb")
                //文件输出位置
                .setOutputDir(projectPath + "\\src\\main\\java")
                .setControllerName("%sController")
                .setServiceName("%sService")
                .setServiceImplName("%sServiceImpl")
                .setMapperName("%sMapper")
                .setXmlName("%sMapper");

        //数据源配置
        String dbUrl = "jdbc:mysql://127.0.0.1:3306/seckill?useUnicode=true&characterEncoding=UTF-8&serverTimezone=UTC&useSSL=false";
        DataSourceConfig dataSourceConfig = new DataSourceConfig();
        dataSourceConfig.setTypeConvert(new MySqlTypeConvert() {
            // 自定义数据库表字段类型转换【可选】
            @Override
            public DbColumnType processTypeConvert(String fieldType) {
                System.out.println("转换类型：" + fieldType);
                // 注意！！processTypeConvert 存在默认类型转换，如果不是你要的效果请自定义返回、非如下直接返回。
                return super.processTypeConvert(fieldType);
            }
        })
                .setUrl(dbUrl)
                .setUsername("root")
                .setPassword("root")
                .setDriverName("com.mysql.jdbc.Driver");

        //策略配置
        StrategyConfig strategyConfig = new StrategyConfig();
        strategyConfig.setCapitalMode(true)
                // .setTablePrefix(new String[] { "sys_" })// 此处可以修改为您的表前缀
                .setEntityLombokModel(false)
                .setDbColumnUnderline(true)
                .setNaming(NamingStrategy.underline_to_camel);

        //包配置
        PackageConfig packageConfig = new PackageConfig();
        packageConfig.setParent("com.springboot.whb.study.modules.seckill")
                .setMapper("dao")
                .setController("controller")
                .setEntity("entity")
                .setService("service")
                .setServiceImpl("service.impl")
                .setXml("xml");

        new AutoGenerator().setGlobalConfig(config).setDataSource(dataSourceConfig)
                .setStrategy(strategyConfig).setPackageInfo(packageConfig).execute();
    }

    public static void main(String[] args) {
        MybatisPlusGenerator generator = MybatisPlusGenerator.getSingle();
        generator.autoGeneration();
    }
}
