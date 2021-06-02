# SpringSecurity 安全框架
## 主要功能
* 认证(Authentication)
* 授权(Authorization)
## 特点（相对于Shiro）
* 与Spring 高度结合 尤其是SpringBoot 自动配置 可以少配置实现权限控制
* 全方面的权限控制
* 旧版本不能脱离web 新版本脱离web 提供core 模块做权限控制
* 重量级
## 快速开始
1. 依赖pom
2. 启动项目访问web 接口时候需要填写用户密码
3. 默认账号是 user 密码在项目启动过程中会生成
## 权限概念
* principal 主体
  使用系统的用户 或者远程登录过来的用户，谁使用 谁是主体
* Authentication 认证
  主体是否能够访问系统
* Authorization 授权
   给主体分配权限
## SpringSecurity 基本原理
_SpringSecurity 本质是过滤器链_ <br>
从启动是可以获取到过滤器链:
```
org.springframework.security.web.context.request.async.WebAsyncManagerIntegrationFilter
org.springframework.security.web.context.SecurityContextPersistenceFilter
org.springframework.security.web.header.HeaderWriterFilter
org.springframework.security.web.csrf.CsrfFilter
org.springframework.security.web.authentication.logout.LogoutFilter
org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter
org.springframework.security.web.authentication.ui.DefaultLoginPageGeneratingFilter
org.springframework.security.web.authentication.ui.DefaultLogoutPageGeneratingFilter
org.springframework.security.web.savedrequest.RequestCacheAwareFilter
org.springframework.security.web.servletapi.SecurityContextHolderAwareRequestFilter
org.springframework.security.web.authentication.AnonymousAuthenticationFilter
org.springframework.security.web.session.SessionManagementFilter
org.springframework.security.web.access.ExceptionTranslationFilter
org.springframework.security.web.access.intercept.FilterSecurityInterceptor
```
<br>

* FilterSecurityInterceptor 方法级别的权限过滤器，在过滤连最底层
  super.beforeInvocation(fi) 表示查看之前的 filter 是否通过。
  fi.getChain().doFilter(fi.getRequest(), fi.getResponse()); 表示真正的调用后台的服务。
* ExceptionTranslationFilter   是个异常过滤器，用来处理在认证授权过程中抛出的异常
* UsernamePasswordAuthenticationFilter 对 / login 的 POST 请求做拦截，校验表单中用户名，密码
* UserDetailsService 查询数据库用户名和密码的过程
  当什么也没有配置的时候，账号和密码是由 Spring Security 定义生成的。而在实际项目中账号和密码都是从数据库中查询出来的。 所以我们要通过自定义逻辑控制认证逻辑。
  如果需要自定义逻辑时，只需要实现 UserDetailsService 接口即可 
  返回信息：
   ```
  package org.springframework.security.core.userdetails;

    import java.io.Serializable;
    import java.util.Collection;
    import org.springframework.security.core.GrantedAuthority;
    
    public interface UserDetails extends Serializable {
    // 表示获取登录用户所有权限
    Collection<? extends GrantedAuthority> getAuthorities();
    // 表示获取密码
    String getPassword();
    // 表示获取用户名
    String getUsername();
    // 表示账号是否过期
    boolean isAccountNonExpired();
    // 表示账号是否锁定
    boolean isAccountNonLocked();
    // 表示密码是否过期
    boolean isCredentialsNonExpired();
    // 表示账号是否可用
    boolean isEnabled();
    }
   ```
* PasswordEncoder  密码加秘接口,

    // 表示把参数按照特定的解析规则进行解析
    String encode(CharSequence rawPassword);
    
    // 表示验证从存储中获取的编码密码与编码后提交的原始密码是否匹配。如果密码匹配，则返回true；如果不匹配，则返回false。第一个参数表示需要被解析的密码。第二个参数表示存储的密码。
    boolean matches(CharSequence rawPassword, String encodedPassword);
    
    // 表示如果解析的密码能够再次进行解析且达到更安全的结果则返回true，否则返回false。默认返false。
    default boolean upgradeEncoding(String encodedPassword) {
        return false;
    }
  BCryptPasswordEncoder  是Spring官方推荐的加秘类
## SpringSecurity web
* 配置账号密码
  ```
    spring.security.user.name= user
    spring.security.user.password= password
  ```
  或者 自定义实现
      1. 创建SecurityConfig类继承 WebSecurityConfigurerAdapter
      2. 重写 config (AuthenticationManagerBuilder  a) 方法,在里面填写用户、密码信息
      auth.inMemoryAuthentication().withUser("user").password("password").roles();
  或者 从数据库中读取
    重复   上面 1，2 注入UserDetailsService
* 配置访问过滤路径等
```
  @Override
    protected void configure(HttpSecurity http) throws Exception {
        super.configure(http);

        http.formLogin()//自定义自己编写的登录页面
                .loginPage("/login.html")//配置哪个 url 为登录页面,这里可以配置请求，也可以直接配置页面
                .loginProcessingUrl("/user/login")// 设置哪个是登录的 url。
                .defaultSuccessUrl("/test/index")//登录成功之后跳转到哪个 url
                .failureForwardUrl("/fail")// 登录失败之后跳转到哪个 url
                .permitAll()
                .and().authorizeRequests()
                .antMatchers("/","/test/hello","/user/login").permitAll()//设置哪些路径可以直接访问，不需要认证
                .anyRequest().authenticated()//表示所有请求都可以访问
                .and().csrf().disable();//关闭csrf防护
        // 更多配置
        http.headers().cacheControl().and().frameOptions().disable()
                .and()
                .cors()
                .and()
                .csrf().disable()
                .exceptionHandling()
                .and()
                .sessionManagement()
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                .and()
                .authorizeRequests()
                .antMatchers(WEBJARS_ENTRY_POINT).permitAll() // Webjars
                .antMatchers(DEVICE_API_ENTRY_POINT).permitAll() // Device HTTP Transport API
                .antMatchers(FORM_BASED_LOGIN_ENTRY_POINT).permitAll() // Login end-point
                .antMatchers(PUBLIC_LOGIN_ENTRY_POINT).permitAll() // Public login end-point
                .antMatchers(TOKEN_REFRESH_ENTRY_POINT).permitAll() // Token refresh end-point
                .antMatchers(NON_TOKEN_BASED_AUTH_ENTRY_POINTS).permitAll() // static resources, user activation and password reset end-points
                .and()
                .authorizeRequests()
                .antMatchers(WS_TOKEN_BASED_AUTH_ENTRY_POINT).authenticated() // Protected WebSocket API End-points
                .antMatchers(TOKEN_BASED_AUTH_ENTRY_POINT).authenticated() // Protected API End-points
                .antMatchers("").hasAnyAuthority("") //有权限才能访问
                .and()
                .exceptionHandling().accessDeniedHandler(restAccessDeniedHandler)
                .and()

                // filter 顺序
                .addFilterBefore(buildRestLoginProcessingFilter(), UsernamePasswordAuthenticationFilter.class)
                .addFilterBefore(buildRestPublicLoginProcessingFilter(), UsernamePasswordAuthenticationFilter.class)
                .addFilterBefore(buildJwtTokenAuthenticationProcessingFilter(), UsernamePasswordAuthenticationFilter.class)
                .addFilterBefore(buildRefreshTokenProcessingFilter(), UsernamePasswordAuthenticationFilter.class)
                .addFilterBefore(buildWsJwtTokenAuthenticationProcessingFilter(), UsernamePasswordAuthenticationFilter.class)
                .addFilterAfter(rateLimitProcessingFilter, UsernamePasswordAuthenticationFilter.class);

        


    }
    @Bean
    protected RestLoginProcessingFilter buildRestLoginProcessingFilter() throws Exception {
        RestLoginProcessingFilter filter = new RestLoginProcessingFilter(FORM_BASED_LOGIN_ENTRY_POINT, successHandler, failureHandler, objectMapper);
        filter.setAuthenticationManager(this.authenticationManager);
        return filter;
    }
```
## 配置多端登录 和授权
1. 实现认证授权的抽象类 AbstractAuthenticationProcessingFilter 取名 比如 xxxWxFiter,JwtTokenFilter,Auth2Filter,在方法attemptAuthentication 实现对数据包装
<br>eg：
```
  
       
   
        import org.springframework.beans.factory.annotation.Autowired;
        import org.springframework.security.core.Authentication;
        import org.springframework.security.core.AuthenticationException;
        import org.springframework.security.core.context.SecurityContext;
        import org.springframework.security.core.context.SecurityContextHolder;
        import org.springframework.security.web.authentication.AbstractAuthenticationProcessingFilter;
        import org.springframework.security.web.authentication.AuthenticationFailureHandler;
        import org.springframework.security.web.util.matcher.RequestMatcher;
        import com.aibaixun.dulink.server.service.security.auth.JwtAuthenticationToken;
        import com.aibaixun.dulink.server.service.security.auth.jwt.extractor.TokenExtractor;
        import com.aibaixun.dulink.server.service.security.model.token.RawAccessJwtToken;
        
        import javax.servlet.FilterChain;
        import javax.servlet.ServletException;
        import javax.servlet.http.HttpServletRequest;
        import javax.servlet.http.HttpServletResponse;
        import java.io.IOException;
        
        public class JwtTokenAuthenticationProcessingFilter extends AbstractAuthenticationProcessingFilter {
        private final AuthenticationFailureHandler failureHandler;
        private final TokenExtractor tokenExtractor;
    
        @Autowired
        public JwtTokenAuthenticationProcessingFilter(AuthenticationFailureHandler failureHandler,
                                                      TokenExtractor tokenExtractor, RequestMatcher matcher) {
            super(matcher);
            this.failureHandler = failureHandler;
            this.tokenExtractor = tokenExtractor;
        }
    
        @Override
        public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response)
                throws AuthenticationException, IOException, ServletException {
            RawAccessJwtToken token = new RawAccessJwtToken(tokenExtractor.extract(request));
            return getAuthenticationManager().authenticate(new JwtAuthenticationToken(token));
        }
    
        @Override
        protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain chain,
                                                Authentication authResult) throws IOException, ServletException {
            SecurityContext context = SecurityContextHolder.createEmptyContext();
            context.setAuthentication(authResult);
            SecurityContextHolder.setContext(context);
            chain.doFilter(request, response);
        }
    
        @Override
        protected void unsuccessfulAuthentication(HttpServletRequest request, HttpServletResponse response,
                                                  AuthenticationException failed) throws IOException, ServletException {
            SecurityContextHolder.clearContext();
            failureHandler.onAuthenticationFailure(request, response, failed);
        }
    }
```

```

    
    
    import com.fasterxml.jackson.databind.ObjectMapper;
    import lombok.extern.slf4j.Slf4j;
    import org.apache.commons.lang3.StringUtils;
    import org.springframework.http.HttpMethod;
    import org.springframework.security.authentication.AuthenticationDetailsSource;
    import org.springframework.security.authentication.AuthenticationServiceException;
    import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
    import org.springframework.security.core.Authentication;
    import org.springframework.security.core.AuthenticationException;
    import org.springframework.security.core.context.SecurityContextHolder;
    import org.springframework.security.web.authentication.AbstractAuthenticationProcessingFilter;
    import org.springframework.security.web.authentication.AuthenticationFailureHandler;
    import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
    import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
    import com.aibaixun.dulink.server.service.security.exception.AuthMethodNotSupportedException;
    import com.aibaixun.dulink.server.service.security.model.UserPrincipal;
    
    import javax.servlet.FilterChain;
    import javax.servlet.ServletException;
    import javax.servlet.http.HttpServletRequest;
    import javax.servlet.http.HttpServletResponse;
    import java.io.IOException;
    
    @Slf4j
    public class RestLoginProcessingFilter extends AbstractAuthenticationProcessingFilter {
    
        private final AuthenticationDetailsSource<HttpServletRequest, ?> authenticationDetailsSource = new RestAuthenticationDetailsSource();
    
        private final AuthenticationSuccessHandler successHandler;
        private final AuthenticationFailureHandler failureHandler;
    
        private final ObjectMapper objectMapper;
    
        public RestLoginProcessingFilter(String defaultProcessUrl, AuthenticationSuccessHandler successHandler,
                                         AuthenticationFailureHandler failureHandler, ObjectMapper mapper) {
            super(defaultProcessUrl);
            this.successHandler = successHandler;
            this.failureHandler = failureHandler;
            this.objectMapper = mapper;
        }
    
        @Override
        public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response)
                throws AuthenticationException, IOException, ServletException {
            if (!HttpMethod.POST.name().equals(request.getMethod())) {
                if(log.isDebugEnabled()) {
                    log.debug("Authentication method not supported. Request method: " + request.getMethod());
                }
                throw new AuthMethodNotSupportedException("Authentication method not supported");
            }
    
            LoginRequest loginRequest;
            try {
                loginRequest = objectMapper.readValue(request.getReader(), LoginRequest.class);
            } catch (Exception e) {
                throw new AuthenticationServiceException("Invalid login request payload");
            }
    
            if (StringUtils.isBlank(loginRequest.getUsername()) || StringUtils.isBlank(loginRequest.getPassword())) {
                throw new AuthenticationServiceException("Username or Password not provided");
            }
    
            UserPrincipal principal = new UserPrincipal(UserPrincipal.Type.USER_NAME, loginRequest.getUsername());
    
            UsernamePasswordAuthenticationToken token = new UsernamePasswordAuthenticationToken(principal, loginRequest.getPassword());
            token.setDetails(authenticationDetailsSource.buildDetails(request));
            return this.getAuthenticationManager().authenticate(token);
        }
    
        @Override
        protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain chain,
                                                Authentication authResult) throws IOException, ServletException {
            successHandler.onAuthenticationSuccess(request, response, authResult);
        }
    
        @Override
        protected void unsuccessfulAuthentication(HttpServletRequest request, HttpServletResponse response,
                                                  AuthenticationException failed) throws IOException, ServletException {
            SecurityContextHolder.clearContext();
            failureHandler.onAuthenticationFailure(request, response, failed);
        }
    }


```


```
    //
    // Source code recreated from a .class file by IntelliJ IDEA
    // (powered by FernFlower decompiler)
    //

 
  
    import javax.servlet.http.HttpServletRequest;
    import javax.servlet.http.HttpServletResponse;
    import org.springframework.security.core.Authentication;
    import org.springframework.security.core.AuthenticationException;
    import org.springframework.security.oauth2.client.OAuth2AuthorizedClient;
    import org.springframework.security.oauth2.client.OAuth2AuthorizedClientService;
    import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
    import org.springframework.security.oauth2.client.authentication.OAuth2LoginAuthenticationToken;
    import org.springframework.security.oauth2.client.registration.ClientRegistration;
    import org.springframework.security.oauth2.client.registration.ClientRegistrationRepository;
    import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
    import org.springframework.security.oauth2.core.OAuth2Error;
    import org.springframework.security.oauth2.core.endpoint.OAuth2AuthorizationExchange;
    import org.springframework.security.oauth2.core.endpoint.OAuth2AuthorizationRequest;
    import org.springframework.security.oauth2.core.endpoint.OAuth2AuthorizationResponse;
    import org.springframework.security.web.authentication.AbstractAuthenticationProcessingFilter;
    import org.springframework.security.web.util.UrlUtils;
    import org.springframework.util.Assert;
    import org.springframework.util.MultiValueMap;
    import org.springframework.web.util.UriComponentsBuilder;
    
    public class OAuth2LoginAuthenticationFilter extends AbstractAuthenticationProcessingFilter {
    public static final String DEFAULT_FILTER_PROCESSES_URI = "/login/oauth2/code/*";
    private static final String AUTHORIZATION_REQUEST_NOT_FOUND_ERROR_CODE = "authorization_request_not_found";
    private static final String CLIENT_REGISTRATION_NOT_FOUND_ERROR_CODE = "client_registration_not_found";
    private ClientRegistrationRepository clientRegistrationRepository;
    private OAuth2AuthorizedClientRepository authorizedClientRepository;
    private AuthorizationRequestRepository<OAuth2AuthorizationRequest> authorizationRequestRepository;
    
        public OAuth2LoginAuthenticationFilter(ClientRegistrationRepository clientRegistrationRepository, OAuth2AuthorizedClientService authorizedClientService) {
            this(clientRegistrationRepository, authorizedClientService, "/login/oauth2/code/*");
        }
    
        public OAuth2LoginAuthenticationFilter(ClientRegistrationRepository clientRegistrationRepository, OAuth2AuthorizedClientService authorizedClientService, String filterProcessesUrl) {
            this(clientRegistrationRepository, (OAuth2AuthorizedClientRepository)(new AuthenticatedPrincipalOAuth2AuthorizedClientRepository(authorizedClientService)), filterProcessesUrl);
        }
    
        public OAuth2LoginAuthenticationFilter(ClientRegistrationRepository clientRegistrationRepository, OAuth2AuthorizedClientRepository authorizedClientRepository, String filterProcessesUrl) {
            super(filterProcessesUrl);
            this.authorizationRequestRepository = new HttpSessionOAuth2AuthorizationRequestRepository();
            Assert.notNull(clientRegistrationRepository, "clientRegistrationRepository cannot be null");
            Assert.notNull(authorizedClientRepository, "authorizedClientRepository cannot be null");
            this.clientRegistrationRepository = clientRegistrationRepository;
            this.authorizedClientRepository = authorizedClientRepository;
        }
    
        public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException {
            MultiValueMap<String, String> params = OAuth2AuthorizationResponseUtils.toMultiMap(request.getParameterMap());
            if (!OAuth2AuthorizationResponseUtils.isAuthorizationResponse(params)) {
                OAuth2Error oauth2Error = new OAuth2Error("invalid_request");
                throw new OAuth2AuthenticationException(oauth2Error, oauth2Error.toString());
            } else {
                OAuth2AuthorizationRequest authorizationRequest = this.authorizationRequestRepository.removeAuthorizationRequest(request, response);
                if (authorizationRequest == null) {
                    OAuth2Error oauth2Error = new OAuth2Error("authorization_request_not_found");
                    throw new OAuth2AuthenticationException(oauth2Error, oauth2Error.toString());
                } else {
                    String registrationId = (String)authorizationRequest.getAttribute("registration_id");
                    ClientRegistration clientRegistration = this.clientRegistrationRepository.findByRegistrationId(registrationId);
                    if (clientRegistration == null) {
                        OAuth2Error oauth2Error = new OAuth2Error("client_registration_not_found", "Client Registration not found with Id: " + registrationId, (String)null);
                        throw new OAuth2AuthenticationException(oauth2Error, oauth2Error.toString());
                    } else {
                        String redirectUri = UriComponentsBuilder.fromHttpUrl(UrlUtils.buildFullRequestUrl(request)).replaceQuery((String)null).build().toUriString();
                        OAuth2AuthorizationResponse authorizationResponse = OAuth2AuthorizationResponseUtils.convert(params, redirectUri);
                        Object authenticationDetails = this.authenticationDetailsSource.buildDetails(request);
                        OAuth2LoginAuthenticationToken authenticationRequest = new OAuth2LoginAuthenticationToken(clientRegistration, new OAuth2AuthorizationExchange(authorizationRequest, authorizationResponse));
                        authenticationRequest.setDetails(authenticationDetails);
                        OAuth2LoginAuthenticationToken authenticationResult = (OAuth2LoginAuthenticationToken)this.getAuthenticationManager().authenticate(authenticationRequest);
                        OAuth2AuthenticationToken oauth2Authentication = new OAuth2AuthenticationToken(authenticationResult.getPrincipal(), authenticationResult.getAuthorities(), authenticationResult.getClientRegistration().getRegistrationId());
                        oauth2Authentication.setDetails(authenticationDetails);
                        OAuth2AuthorizedClient authorizedClient = new OAuth2AuthorizedClient(authenticationResult.getClientRegistration(), oauth2Authentication.getName(), authenticationResult.getAccessToken(), authenticationResult.getRefreshToken());
                        this.authorizedClientRepository.saveAuthorizedClient(authorizedClient, oauth2Authentication, request, response);
                        return oauth2Authentication;
                    }
                }
            }
        }
    
        public final void setAuthorizationRequestRepository(AuthorizationRequestRepository<OAuth2AuthorizationRequest> authorizationRequestRepository) {
            Assert.notNull(authorizationRequestRepository, "authorizationRequestRepository cannot be null");
            this.authorizationRequestRepository = authorizationRequestRepository;
        }
    }

```
2. 实现AuthenticationProvider 做业务逻辑
<br> eg:
```java

public class JwtAuthenticationProvider implements AuthenticationProvider {

    private final JwtTokenFactory tokenFactory;

    @Autowired
    public JwtAuthenticationProvider(JwtTokenFactory tokenFactory) {
        this.tokenFactory = tokenFactory;
    }

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        RawAccessJwtToken rawAccessToken = (RawAccessJwtToken) authentication.getCredentials();
        SecurityUser securityUser = tokenFactory.parseAccessJwtToken(rawAccessToken);
        return new JwtAuthenticationToken(securityUser);
    }

    @Override
    public boolean supports(Class<?> authentication) {
        return (JwtAuthenticationToken.class.isAssignableFrom(authentication));
    }
}
```   

```java

package com.aibaixun.dulink.server.service.security.auth.rest;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationDetailsSource;
import org.springframework.security.authentication.AuthenticationServiceException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.AbstractAuthenticationProcessingFilter;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import com.aibaixun.dulink.server.service.security.exception.AuthMethodNotSupportedException;
import com.aibaixun.dulink.server.service.security.model.UserPrincipal;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Slf4j
public class RestLoginProcessingFilter extends AbstractAuthenticationProcessingFilter {

    private final AuthenticationDetailsSource<HttpServletRequest, ?> authenticationDetailsSource = new RestAuthenticationDetailsSource();

    private final AuthenticationSuccessHandler successHandler;
    private final AuthenticationFailureHandler failureHandler;

    private final ObjectMapper objectMapper;

    public RestLoginProcessingFilter(String defaultProcessUrl, AuthenticationSuccessHandler successHandler,
                                     AuthenticationFailureHandler failureHandler, ObjectMapper mapper) {
        super(defaultProcessUrl);
        this.successHandler = successHandler;
        this.failureHandler = failureHandler;
        this.objectMapper = mapper;
    }

    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response)
            throws AuthenticationException, IOException, ServletException {
        if (!HttpMethod.POST.name().equals(request.getMethod())) {
            if(log.isDebugEnabled()) {
                log.debug("Authentication method not supported. Request method: " + request.getMethod());
            }
            throw new AuthMethodNotSupportedException("Authentication method not supported");
        }

        LoginRequest loginRequest;
        try {
            loginRequest = objectMapper.readValue(request.getReader(), LoginRequest.class);
        } catch (Exception e) {
            throw new AuthenticationServiceException("Invalid login request payload");
        }

        if (StringUtils.isBlank(loginRequest.getUsername()) || StringUtils.isBlank(loginRequest.getPassword())) {
            throw new AuthenticationServiceException("Username or Password not provided");
        }

        UserPrincipal principal = new UserPrincipal(UserPrincipal.Type.USER_NAME, loginRequest.getUsername());

        UsernamePasswordAuthenticationToken token = new UsernamePasswordAuthenticationToken(principal, loginRequest.getPassword());
        token.setDetails(authenticationDetailsSource.buildDetails(request));
        return this.getAuthenticationManager().authenticate(token);
    }

    @Override
    protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain chain,
                                            Authentication authResult) throws IOException, ServletException {
        successHandler.onAuthenticationSuccess(request, response, authResult);
    }

    @Override
    protected void unsuccessfulAuthentication(HttpServletRequest request, HttpServletResponse response,
                                              AuthenticationException failed) throws IOException, ServletException {
        SecurityContextHolder.clearContext();
        failureHandler.onAuthenticationFailure(request, response, failed);
    }
}

```

```java

package com.aibaixun.dulink.server.service.security.auth.rest;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.InsufficientAuthenticationException;
import org.springframework.security.authentication.LockedException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;
import com.aibaixun.dulink.server.common.data.Customer;
import com.aibaixun.dulink.server.common.data.User;
import com.aibaixun.dulink.server.common.data.audit.ActionType;
import com.aibaixun.dulink.server.common.data.id.CustomerId;
import com.aibaixun.dulink.server.common.data.id.EntityId;
import com.aibaixun.dulink.server.common.data.id.TenantId;
import com.aibaixun.dulink.server.common.data.id.UserId;
import com.aibaixun.dulink.server.common.data.security.Authority;
import com.aibaixun.dulink.server.common.data.security.UserCredentials;
import com.aibaixun.dulink.server.dao.audit.AuditLogService;
import com.aibaixun.dulink.server.dao.customer.CustomerService;
import com.aibaixun.dulink.server.dao.user.UserService;
import com.aibaixun.dulink.server.service.security.model.SecurityUser;
import com.aibaixun.dulink.server.service.security.model.UserPrincipal;
import com.aibaixun.dulink.server.service.security.system.SystemSecurityService;
import ua_parser.Client;

import java.util.UUID;


@Component
@Slf4j
public class RestAuthenticationProvider implements AuthenticationProvider {

    private final SystemSecurityService systemSecurityService;
    private final UserService userService;
    private final CustomerService customerService;
    private final AuditLogService auditLogService;

    @Autowired
    public RestAuthenticationProvider(final UserService userService,
                                      final CustomerService customerService,
                                      final SystemSecurityService systemSecurityService,
                                      final AuditLogService auditLogService) {
        this.userService = userService;
        this.customerService = customerService;
        this.systemSecurityService = systemSecurityService;
        this.auditLogService = auditLogService;
    }

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        Assert.notNull(authentication, "No authentication data provided");

        Object principal = authentication.getPrincipal();
        if (!(principal instanceof UserPrincipal)) {
            throw new BadCredentialsException("Authentication Failed. Bad user principal.");
        }

        UserPrincipal userPrincipal =  (UserPrincipal) principal;
        if (userPrincipal.getType() == UserPrincipal.Type.USER_NAME) {
            String username = userPrincipal.getValue();
            String password = (String) authentication.getCredentials();
            return authenticateByUsernameAndPassword(authentication, userPrincipal, username, password);
        } else {
            String publicId = userPrincipal.getValue();
            return authenticateByPublicId(userPrincipal, publicId);
        }
    }

    private Authentication authenticateByUsernameAndPassword(Authentication authentication, UserPrincipal userPrincipal, String username, String password) {
        User user = userService.findUserByEmail(TenantId.SYS_TENANT_ID, username);
        if (user == null) {
            throw new UsernameNotFoundException("User not found: " + username);
        }

        try {

            UserCredentials userCredentials = userService.findUserCredentialsByUserId(TenantId.SYS_TENANT_ID, user.getId());
            if (userCredentials == null) {
                throw new UsernameNotFoundException("User credentials not found");
            }

            try {
                systemSecurityService.validateUserCredentials(user.getTenantId(), userCredentials, username, password);
            } catch (LockedException e) {
                logLoginAction(user, authentication, ActionType.LOCKOUT, null);
                throw e;
            }

            if (user.getAuthority() == null)
                throw new InsufficientAuthenticationException("User has no authority assigned");

            SecurityUser securityUser = new SecurityUser(user, userCredentials.isEnabled(), userPrincipal);
            logLoginAction(user, authentication, ActionType.LOGIN, null);
            return new UsernamePasswordAuthenticationToken(securityUser, null, securityUser.getAuthorities());
        } catch (Exception e) {
            logLoginAction(user, authentication, ActionType.LOGIN, e);
            throw e;
        }
    }

    private Authentication authenticateByPublicId(UserPrincipal userPrincipal, String publicId) {
        CustomerId customerId;
        try {
            customerId = new CustomerId(UUID.fromString(publicId));
        } catch (Exception e) {
            throw new BadCredentialsException("Authentication Failed. Public Id is not valid.");
        }
        Customer publicCustomer = customerService.findCustomerById(TenantId.SYS_TENANT_ID, customerId);
        if (publicCustomer == null) {
            throw new UsernameNotFoundException("Public entity not found: " + publicId);
        }
        if (!publicCustomer.isPublic()) {
            throw new BadCredentialsException("Authentication Failed. Public Id is not valid.");
        }
        User user = new User(new UserId(EntityId.NULL_UUID));
        user.setTenantId(publicCustomer.getTenantId());
        user.setCustomerId(publicCustomer.getId());
        user.setEmail(publicId);
        user.setAuthority(Authority.CUSTOMER_USER);
        user.setFirstName("Public");
        user.setLastName("Public");

        SecurityUser securityUser = new SecurityUser(user, true, userPrincipal);

        return new UsernamePasswordAuthenticationToken(securityUser, null, securityUser.getAuthorities());
    }

    @Override
    public boolean supports(Class<?> authentication) {
        return (UsernamePasswordAuthenticationToken.class.isAssignableFrom(authentication));
    }

    private void logLoginAction(User user, Authentication authentication, ActionType actionType, Exception e) {
        String clientAddress = "Unknown";
        String browser = "Unknown";
        String os = "Unknown";
        String device = "Unknown";
        if (authentication != null && authentication.getDetails() != null) {
            if (authentication.getDetails() instanceof RestAuthenticationDetails) {
                RestAuthenticationDetails details = (RestAuthenticationDetails)authentication.getDetails();
                clientAddress = details.getClientAddress();
                if (details.getUserAgent() != null) {
                    Client userAgent = details.getUserAgent();
                    if (userAgent.userAgent != null) {
                        browser = userAgent.userAgent.family;
                        if (userAgent.userAgent.major != null) {
                            browser += " " + userAgent.userAgent.major;
                            if (userAgent.userAgent.minor != null) {
                                browser += "." + userAgent.userAgent.minor;
                                if (userAgent.userAgent.patch != null) {
                                    browser += "." + userAgent.userAgent.patch;
                                }
                            }
                        }
                    }
                    if (userAgent.os != null) {
                        os = userAgent.os.family;
                        if (userAgent.os.major != null) {
                            os += " " + userAgent.os.major;
                            if (userAgent.os.minor != null) {
                                os += "." + userAgent.os.minor;
                                if (userAgent.os.patch != null) {
                                    os += "." + userAgent.os.patch;
                                    if (userAgent.os.patchMinor != null) {
                                        os += "." + userAgent.os.patchMinor;
                                    }
                                }
                            }
                        }
                    }
                    if (userAgent.device != null) {
                        device = userAgent.device.family;
                    }
                }
            }
        }
        auditLogService.logEntityAction(
                user.getTenantId(), user.getCustomerId(), user.getId(),
                user.getName(), user.getId(), null, actionType, e, clientAddress, browser, os, device);
    }
}

```
3. config配置和设置顺序
```java
 @Override
    protected void configure(AuthenticationManagerBuilder auth) {
    
        auth.authenticationProvider(restAuthenticationProvider);
        auth.authenticationProvider(jwtAuthenticationProvider);
        auth.authenticationProvider(refreshTokenAuthenticationProvider);
    }
    protected void configure(HttpSecurity http) throws Exception {
        .addFilterBefore(buildRestLoginProcessingFilter(), UsernamePasswordAuthenticationFilter.class)
        .addFilterBefore(buildRestPublicLoginProcessingFilter(), UsernamePasswordAuthenticationFilter.class)
        .addFilterBefore(buildJwtTokenAuthenticationProcessingFilter(), UsernamePasswordAuthenticationFilter.class)
        .addFilterBefore(buildRefreshTokenProcessingFilter(), UsernamePasswordAuthenticationFilter.class)
        .addFilterBefore(buildWsJwtTokenAuthenticationProcessingFilter(), UsernamePasswordAuthenticationFilter.class)
        .addFilterAfter(rateLimitProcessingFilter, UsernamePasswordAuthenticationFilter.class);
   }
   // 注意 这种配置 不需要在filter 上加 @Compent ,authenticationManager 是自动注入的
@Bean
protected RestLoginProcessingFilter buildRestLoginProcessingFilter() throws Exception {
        RestLoginProcessingFilter filter = new RestLoginProcessingFilter(FORM_BASED_LOGIN_ENTRY_POINT, successHandler, failureHandler, objectMapper);
        filter.setAuthenticationManager(this.authenticationManager);
        return filter;
        }
      
    
```
## 注解的使用
1. @Secured 判断用户是否具有角色才能访问 需要加前缀ROLE_ ,对比config里面是不需要添加的
   使用该注解时候 必须开启@EnableGlobalMethodSecurity(securedEnabled=true)
   使用在控制器方法上
2. @PreAuthorize  开启 @EnableGlobalMethodSecurity(prePostEnabled = true)   
   注解适合进入方法前的权限验证， @PreAuthorize 可以将登录用 户的 roles/permissions 参数传到方法中
   举例子 @PreAuthorize("hasAuthority('ss')") 值可以为hasRole hasPerssion
3. @PostAuthorize 同上 方法后  需要开启
4. @PostFilter，方法返回数据进行过滤
   @PostFilter ：权限验证之后对数据进行过滤
    @PostFilter("filterObject.id=1") 
5. @PreFilter，方法前数据进行过滤   





