package com.guce.scope;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

/**
 * @Author chengen.gce
 * @DATE 2021/12/8 10:47 下午
 */
@Service
@Scope("customScope")
public class CustomScopeServiceTest {
}
