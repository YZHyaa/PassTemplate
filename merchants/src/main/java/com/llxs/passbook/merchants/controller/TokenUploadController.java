package com.llxs.passbook.merchants.controller;

import com.llxs.passbook.merchants.constant.Constants;
import com.llxs.passbook.merchants.service.PassTemplateService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisCallback;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Slf4j
@Controller
public class TokenUploadController {

    @Autowired
    private StringRedisTemplate redisTemplate;

    @Autowired
    private PassTemplateService passTemplateService;

    /**
     * 获取上传 token 页面
     */
    @GetMapping("/upload")
    public String upload() {
        return "upload";
    }

    /**
     * 保存 token 文件，返回上传状态页面
     * 注：在上传后点击保存后，前端会将 PassTemplate 的 hasToken 置为 true，然后一起传到
     */
    @PostMapping("/token/upload")
    public String tokenFileUpload(@RequestParam("merchantsId") String merchantsId,
                                  @RequestParam("passTemplateId") String passTemplateId,
                                  @RequestParam("file") MultipartFile file,
                                  RedirectAttributes redirectAttributes) throws IOException {

        if (null == passTemplateId || file.isEmpty()) {
            redirectAttributes.addFlashAttribute("message", "passTemplateId is null or file is empty");
            // 重定向到 uploadStatus 请求
            return "redirect:/uploadStatus";
        }

        // /tmp/token/merchantsId/
        File tokenDir = new File(Constants.TOKEN_DIR + merchantsId);
        if (!tokenDir.exists()) {
            log.info("Create File: {}", tokenDir.mkdir());
        }
        // /tmp/token/merchantsId/passTemplateId
        Path path = Paths.get(Constants.TOKEN_DIR, merchantsId, passTemplateId);
        Files.write(path, file.getBytes());

        // 所有 token 存到 redis
        if(!writeTokenToRedis(path, Constants.TOKEN_PREFIX +passTemplateId)) {
            redirectAttributes.addFlashAttribute("message", "write token error");
        } else {
            redirectAttributes.addFlashAttribute("message", "You successfully uploaded '" + file.getOriginalFilename() + "'");
        }

        return "redirect:/uploadStatus";
    }

    /**
     * 将 token 写入 redis
     */
    private boolean writeTokenToRedis(Path path, String key) {

        Set<String> tokens;
        try {
            Stream<String> lines = Files.lines(path);
            tokens = lines.collect(Collectors.toSet());
        } catch (IOException e) {
            log.error(e.getLocalizedMessage());
            return false;
        }

        if (!CollectionUtils.isEmpty(tokens)) {
            redisTemplate.executePipelined((RedisCallback<Object>) connection -> {
                for (String token : tokens) {
                    connection.sAdd(key.getBytes(), token.getBytes());
                }
                return null;
            });
        }

        return true;
    }

    /**
     * 获取 token 上传状态页面
     */
    @GetMapping("/uploadStatus")
    public String uploadStatus() {
        return "uploadStatus";
    }

}