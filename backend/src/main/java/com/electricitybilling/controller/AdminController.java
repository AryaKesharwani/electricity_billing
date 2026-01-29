package com.electricitybilling.controller;

import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/admin")
@Tag(name = "Administrator", description = "Administrator management endpoints")
@RequiredArgsConstructor
public class AdminController {
    // Admin login uses hardcoded credentials (see LoginService). Admin registration is disabled.
}
