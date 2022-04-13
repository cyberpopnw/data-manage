package com.bastion.cyber.config;

import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

/**
 * Created on 2022/2/16.
 *
 * @author haogege
 */
@Configuration
@Data
public class IRefreshScopeConfig {
    @Value("${cyber.jwtExpire}")
    private Long jwtExpire;
}
