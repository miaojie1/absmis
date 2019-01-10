package com.absmis.controller;

import com.absmis.domain.authority.Resource;
import com.absmis.domain.authority.Role;
import com.absmis.domain.authority.RoleAssResource;
import com.absmis.domain.authority.User;
import com.absmis.service.authority.RoleService;
import com.absmis.service.authority.UserService;
import org.apache.catalina.servlet4preview.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpRequest;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.context.SecurityContextImpl;
import org.springframework.security.web.authentication.WebAuthenticationDetails;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;
@RestController
public class CreateMenuController {
    @Autowired
    private UserService userService;
    @Autowired
    private RoleService roleService;
    @RequestMapping(value = "/menu", method = RequestMethod.GET)
    public List<Resource> treelist(HttpServletRequest request) {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        System.out.println();
        System.out.println("***************");
        /* 经过spring security认证后,security会把一个SecurityContextImpl对象存储到session中，此对象中有当前用户的各种资料*/
        SecurityContextImpl securityContextImpl = (SecurityContextImpl) request
                .getSession().getAttribute("SPRING_SECURITY_CONTEXT");
        // 登录名
        System.out.println("Username:" + securityContextImpl.getAuthentication().getName());
        //登录密码，未加密的
        System.out.println("Credentials:" + securityContextImpl.getAuthentication().getCredentials());

        WebAuthenticationDetails details = (WebAuthenticationDetails) securityContextImpl
                .getAuthentication().getDetails();
        //获得访问地址
        System.out.println("RemoteAddress:" + details.getRemoteAddress());
        //获得sessionid
        System.out.println("SessionId:" + details.getSessionId());
        //获得当前用户所拥有的权限
        List<GrantedAuthority> authorities = (List<GrantedAuthority>) securityContextImpl
                .getAuthentication().getAuthorities();
        for (GrantedAuthority grantedAuthority : authorities) {
            System.out.println("Authority：" + grantedAuthority.getAuthority());
        }


        System.out.println("***************");
        User storedUser = userService.findByUsername(username);
        System.out.println("------"+username);
        Role role = roleService.findOne(storedUser.getRole().getId());
        System.out.println("------"+role.getDescription());
        List<RoleAssResource> roleAssResources = role.getRoleAssResource();
        System.out.println("------"+roleAssResources.size());
        List<Resource> resources = new ArrayList<>();
        for (RoleAssResource roleAssResource : roleAssResources) {
           resources.add(roleAssResource.getResource());
        }
        System.out.println("------"+resources.size());
        return resources;
    }
}
