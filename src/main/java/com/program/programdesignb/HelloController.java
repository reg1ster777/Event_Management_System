package com.program.programdesignb;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

// 简单健康检查接口，GET /api/ping 返回 "ok"
@RestController
public class HelloController {
    @GetMapping("/api/ping")
    public String ping() { return "ok"; }
}
