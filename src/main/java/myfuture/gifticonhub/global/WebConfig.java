package myfuture.gifticonhub.global;

import myfuture.gifticonhub.global.formatter.uploadFileFormatter;
import myfuture.gifticonhub.global.interceptor.AuthenticationInterceptor;
import myfuture.gifticonhub.global.interceptor.LogInterceptor;
import myfuture.gifticonhub.global.interceptor.TempSessionInterceptor;
import myfuture.gifticonhub.global.session.LoginMemberArgumentResolver;
import org.springframework.context.annotation.Configuration;
import org.springframework.format.FormatterRegistry;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.util.List;

@Configuration
public class WebConfig implements WebMvcConfigurer {
    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(new LogInterceptor())
                .order(1)
                .addPathPatterns("/**")
                .excludePathPatterns("/css/**", "/*.ico", "/error");

        registry.addInterceptor(new AuthenticationInterceptor())
                .order(2)
                .addPathPatterns("/**")
                .excludePathPatterns("/", "/images/**", "/join", "/login", "/logout", "/css/**", "/*.ico", "/error", "/api/**", "/api");
        registry.addInterceptor(new TempSessionInterceptor())
                .order(3)
                .addPathPatterns("/items")
                .excludePathPatterns("/items/{itemId}","/items/{itemId}/edit");
    }

    @Override
    public void addArgumentResolvers(List<HandlerMethodArgumentResolver> resolvers) {
        resolvers.add(new LoginMemberArgumentResolver());
    }

    @Override
    public void addFormatters(FormatterRegistry registry) {
        registry.addFormatter(new uploadFileFormatter());
    }
}
