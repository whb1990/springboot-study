package com.springboot.whb.study.modules.seckill.service.impl;

import com.springboot.whb.study.common.Result;
import com.springboot.whb.study.modules.seckill.po.Seckill;
import com.springboot.whb.study.modules.seckill.repository.SeckillRepository;
import com.springboot.whb.study.modules.seckill.service.ICreateHtmlService;
import freemarker.template.Configuration;
import freemarker.template.Template;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.*;

/**
 * @author: whb
 * @date: 2019/7/12 15:06
 * @description: 生成页面接口实现
 */
@Service
public class CreateHtmlServiceImpl implements ICreateHtmlService {

    private static final Logger logger = LoggerFactory.getLogger(CreateHtmlServiceImpl.class);
    /**
     * 核心线程数
     */
    private static int corePoolSize = Runtime.getRuntime().availableProcessors();
    /**
     * 线程池
     */
    private static ThreadPoolExecutor poolExecutor = new ThreadPoolExecutor(corePoolSize, corePoolSize + 1, 10L, TimeUnit.SECONDS,
            new LinkedBlockingQueue<>(1000));

    @Autowired
    public Configuration configuration;
    @Autowired
    private SeckillRepository seckillRepository;
    @Value("${spring.freemarker.html.path}")
    private String path;

    @Override
    public Result createAllHtml() {
        List<Seckill> list = seckillRepository.findAll();
        final List<Future<String>> result = new ArrayList<>();
        for (Seckill seckill : list) {
            result.add(poolExecutor.submit(new createHtml(seckill)));
        }
        for (Future<String> future : result) {
            try {
                //打印各个线任务执行的结果，调用future.get() 阻塞主线程，获取异步任务的返回结果
                logger.info(future.get());
            } catch (InterruptedException e) {
                logger.error("生成页面失败，当前线程：{},错误原因：{}", Thread.currentThread().getName(), e);
            } catch (ExecutionException e) {
                logger.error("生成页面失败，当前线程：{},错误原因：{}", Thread.currentThread().getName(), e);
            }
        }
        return Result.ok();
    }

    class createHtml implements Callable<String> {
        private Seckill seckill;

        public createHtml(Seckill seckill) {
            this.seckill = seckill;
        }

        @Override
        public String call() throws Exception {
            Template template = configuration.getTemplate("goods.ftl");
            File file = new File(path + seckill.getSeckillId() + ".html");
            Writer writer = new OutputStreamWriter(new FileOutputStream(file), "UTF-8");
            template.process(seckill, writer);
            return "create " + seckill.getSeckillId() + ".html success";
        }
    }
}

