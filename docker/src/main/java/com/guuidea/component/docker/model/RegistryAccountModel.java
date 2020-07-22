package com.guuidea.component.docker.model;

import lombok.Data;

/**
 * 账户数据模型.
 *
 * @author hzchendou
 * @date 2020/2/17
 * @since 1.0
 */
@Data
public class RegistryAccountModel {
    String username;
    String password;
    String email;
    String serveraddress;
}
