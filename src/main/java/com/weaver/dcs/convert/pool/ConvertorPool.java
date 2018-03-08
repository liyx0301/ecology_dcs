package com.weaver.dcs.convert.pool;

import lombok.Getter;
import lombok.Setter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.util.ArrayList;

/**
 * 附件转换池
 */
public class ConvertorPool {
    private static final Logger logger = LoggerFactory.getLogger(ConvertorPool.class);
    // 线程池大小，通过配置文件application.properties获取
    @Value("${convert.threadCount}")
    private int threadCount;

    private ConvertorPool() {}

    private static ConvertorPool instance = null;

    private ArrayList<ConvertorObject> pool = new ArrayList<ConvertorObject>();

    private int availSize = 0;

    private int current = 0;

    public static ConvertorPool getInstance() {
        try
        {
            if (instance == null) {
                instance = new ConvertorPool();
            }
            return instance;
        }
        catch (Exception ex)
        {
            logger.error(ex.getMessage());
        }
        return null;
    }

    public synchronized ConvertorObject getConvertor() {
        try
        {
            if (availSize > 0) {
                return getIdleConvertor();
            } else if (pool.size() < threadCount) {
                return createNewConvertor();
            } else {
                try {
                    wait();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                return getConvertor();
            }
        }
        catch (Exception ex)
        {
            logger.error(ex.getMessage());
        }
        return null;
    }

    public synchronized void returnConvertor(ConvertorObject convertor) {
        try
        {
            for (ConvertorObject co : pool) {
                if (co == convertor) {
                    co.available = true;
                    availSize++;
                    notify();
                    break;
                }
            }
        }
        catch (Exception ex)
        {
            logger.error(ex.getMessage());
        }
    }

    private synchronized ConvertorObject getIdleConvertor() {
        try
        {
            for (ConvertorObject co : pool) {
                if (co.available) {
                    co.available = false;
                    availSize--;
                    return co;
                }
            }
        }
        catch (Exception ex)
        {
            logger.error(ex.getMessage());
        }
        return null;
    }

    private synchronized ConvertorObject createNewConvertor() {
        try
        {
            ConvertorObject co = new ConvertorObject(++current);
            co.convertor = new ConvertToHtml();
            co.available = false;
            pool.add(co);
            return co;
        }
        catch (Exception ex)
        {
            logger.error(ex.getMessage());
        }
        return null;
    }


    public class ConvertorObject {
        public ConvertorObject(int id) {
            this.id = id;
        }
        public boolean convert(String src, String dest) {
            return false;
        }
        public int id;
        public ConvertToHtml convertor;
        public boolean available;
    }

}
