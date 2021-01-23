package org.jpcl.dbop.controller;

import org.jpcl.dbop.cache.DefaultCacheService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author Administrator
 */
@RestController
public class DemoController {

    @Autowired
    private Demo demo;

    @GetMapping("/test.do")
    public String cacheTest1(String par1, String par2) {
        return demo.get(par1, par2);
    }

    @Component
    public static class Demo {

        private String a = "a123";
        /**
         * value 必须要有 指定缓存名称 比如你存储到map中 那么就是给这个map起一个名字 没用运行时保存
         * key-value key可以自己指定 这里是默认生成的类名+方法名+参数 大概这样 value就是return值了
         */
        @Cacheable(value = "cache1", key="target.getA()")
        public String get(String content, String sign) {
            System.out.println(content);
            return content + " Hello !";
        }

        public String getA() {
            return a;
        }

        public void setA(String a) {
            this.a = a;
        }
    }
}
