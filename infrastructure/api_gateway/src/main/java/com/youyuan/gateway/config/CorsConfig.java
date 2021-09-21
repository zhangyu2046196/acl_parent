package com.youyuan.gateway.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.reactive.CorsWebFilter;
import org.springframework.web.cors.reactive.UrlBasedCorsConfigurationSource;
import org.springframework.web.util.pattern.PathPatternParser;

/**
 * 类名称：CorsConfig <br>
 * 类描述： 自定义配置类解决跨域问题 <br>
 *
 * @author zhangyu
 * @version 1.0.0
 * @date 创建时间：2021/9/20 22:03<br>
 */
@Configuration
public class CorsConfig {

    /**
     * 方法名: corsWebFilter <br>
     * 方法描述: 配置对象解决跨域问题 <br>
     *
     * @return {@link CorsWebFilter 返回生成对象 }
     * @date 创建时间: 2021/9/20 22:04 <br>
     * @author zhangyu
     */
    @Bean
    public CorsWebFilter corsWebFilter() {
        CorsConfiguration corsConfiguration = new CorsConfiguration();
        corsConfiguration.addAllowedHeader("*");
        corsConfiguration.addAllowedMethod("*");
        corsConfiguration.addAllowedOrigin("*");

        UrlBasedCorsConfigurationSource corsConfigurationSource = new UrlBasedCorsConfigurationSource(new
                PathPatternParser());
        corsConfigurationSource.registerCorsConfiguration("/**", corsConfiguration);

        return new CorsWebFilter(corsConfigurationSource);
    }
}
