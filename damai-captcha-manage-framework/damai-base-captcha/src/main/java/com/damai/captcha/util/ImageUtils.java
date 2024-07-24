/*
 *Copyright © 2018 anji-plus
 *安吉加加信息技术有限公司
 *http://www.anji-plus.com
 *All rights reserved.
 */
package com.damai.captcha.util;

import com.damai.captcha.model.common.CaptchaBaseMapEnum;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;
import java.util.Base64;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @program: 极度真实还原大麦网高并发实战项目。 添加 阿星不是程序员 微信，添加时备注 大麦 来获取项目的完整资料 
 * @description: ImageUtils
 * @author: 阿星不是程序员
 **/
public class ImageUtils {
    private static final Logger logger = LoggerFactory.getLogger(ImageUtils.class);
    /***
     * 旋转底图
     */
    private static final Map<String, String> ORIGINAL_ROTATE_CACHE_MAP = new ConcurrentHashMap<>();
    
    /**
     * 旋转块
     * */
    private static final Map<String, String> ROTATE_BLOCK_CACHE_MAP = new ConcurrentHashMap<>();
    
    /**
     * 滑块底图
     * */
    private static final Map<String, String> ORIGINAL_CACHE_MAP = new ConcurrentHashMap<>();
    
    /**
     * 滑块
     * */
    private static final Map<String, String> SLIDING_BLOCK_CACHE_MAP = new ConcurrentHashMap<>();
    
    /**
     * 点选文字
     * */
    private static final Map<String, String> PIC_CLICK_CACHE_MAP = new ConcurrentHashMap<>(); 
    
    private static final Map<String, String[]> FILE_NAME_MAP = new ConcurrentHashMap<>();
    
    private static final Integer SIX = 6;

    public static void cacheImage(String captchaOriginalPathJigsaw, String captchaOriginalPathClick, String captchaOriginalPathRotate) {
        // 旋转拼图
        if (StringUtils.isBlank(captchaOriginalPathRotate)) {
            ORIGINAL_ROTATE_CACHE_MAP.putAll(getResourcesImagesFile("defaultImages/rotate/original"));
            ROTATE_BLOCK_CACHE_MAP.putAll(getResourcesImagesFile("defaultImages/rotate/rotateBlock"));
        } else {
            ORIGINAL_ROTATE_CACHE_MAP.putAll(getImagesFile(captchaOriginalPathRotate + File.separator + "original"));
            ROTATE_BLOCK_CACHE_MAP.putAll(getImagesFile(captchaOriginalPathRotate + File.separator + "rotateBlock"));
        }
        //滑动拼图
        if (StringUtils.isBlank(captchaOriginalPathJigsaw)) {
            ORIGINAL_CACHE_MAP.putAll(getResourcesImagesFile("defaultImages/jigsaw/original"));
            SLIDING_BLOCK_CACHE_MAP.putAll(getResourcesImagesFile("defaultImages/jigsaw/slidingBlock"));
        } else {
            ORIGINAL_CACHE_MAP.putAll(getImagesFile(captchaOriginalPathJigsaw + File.separator + "original"));
            SLIDING_BLOCK_CACHE_MAP.putAll(getImagesFile(captchaOriginalPathJigsaw + File.separator + "slidingBlock"));
        }
        //点选文字
        if (StringUtils.isBlank(captchaOriginalPathClick)) {
            PIC_CLICK_CACHE_MAP.putAll(getResourcesImagesFile("defaultImages/pic-click"));
        } else {
            PIC_CLICK_CACHE_MAP.putAll(getImagesFile(captchaOriginalPathClick));
        }

        FILE_NAME_MAP.put(CaptchaBaseMapEnum.ORIGINAL.getCodeValue(), ORIGINAL_CACHE_MAP.keySet().toArray(new String[0]));
        FILE_NAME_MAP.put(CaptchaBaseMapEnum.SLIDING_BLOCK.getCodeValue(), SLIDING_BLOCK_CACHE_MAP.keySet().toArray(new String[0]));
        FILE_NAME_MAP.put(CaptchaBaseMapEnum.PIC_CLICK.getCodeValue(), PIC_CLICK_CACHE_MAP.keySet().toArray(new String[0]));
        FILE_NAME_MAP.put(CaptchaBaseMapEnum.ROTATE.getCodeValue(), ORIGINAL_ROTATE_CACHE_MAP.keySet().toArray(new String[0]));
        FILE_NAME_MAP.put(CaptchaBaseMapEnum.ROTATE_BLOCK.getCodeValue(), ROTATE_BLOCK_CACHE_MAP.keySet().toArray(new String[0]));
        logger.info("初始化底图:{}", JsonUtil.toJsonString(FILE_NAME_MAP));
    }

    public static void cacheBootImage(Map<String, String> originalMap, Map<String, String> slidingBlockMap, Map<String, String> picClickMap) {
        ORIGINAL_CACHE_MAP.putAll(originalMap);
        SLIDING_BLOCK_CACHE_MAP.putAll(slidingBlockMap);
        PIC_CLICK_CACHE_MAP.putAll(picClickMap);
        FILE_NAME_MAP.put(CaptchaBaseMapEnum.ORIGINAL.getCodeValue(), ORIGINAL_CACHE_MAP.keySet().toArray(new String[0]));
        FILE_NAME_MAP.put(CaptchaBaseMapEnum.SLIDING_BLOCK.getCodeValue(), SLIDING_BLOCK_CACHE_MAP.keySet().toArray(new String[0]));
        FILE_NAME_MAP.put(CaptchaBaseMapEnum.PIC_CLICK.getCodeValue(), PIC_CLICK_CACHE_MAP.keySet().toArray(new String[0]));
        logger.info("自定义resource底图:{}", JsonUtil.toJsonString(FILE_NAME_MAP));
    }

    public static BufferedImage getRotate() {
        String[] strings = FILE_NAME_MAP.get(CaptchaBaseMapEnum.ROTATE.getCodeValue());
        if (null == strings || strings.length == 0) {
            return null;
        }
        Integer randomInt = RandomUtils.getRandomInt(0, strings.length);
        String s = ORIGINAL_ROTATE_CACHE_MAP.get(strings[randomInt]);
        return getBase64StrToImage(s);
    }

    public static String getRotateBlock() {
        String[] strings = FILE_NAME_MAP.get(CaptchaBaseMapEnum.ROTATE_BLOCK.getCodeValue());
        if (null == strings || strings.length == 0) {
            return null;
        }
        Integer randomInt = RandomUtils.getRandomInt(0, strings.length);
        String s = ROTATE_BLOCK_CACHE_MAP.get(strings[randomInt]);
        return s;
    }

    public static BufferedImage getOriginal() {
        String[] strings = FILE_NAME_MAP.get(CaptchaBaseMapEnum.ORIGINAL.getCodeValue());
        if (null == strings || strings.length == 0) {
            return null;
        }
        Integer randomInt = RandomUtils.getRandomInt(0, strings.length);
        String s = ORIGINAL_CACHE_MAP.get(strings[randomInt]);
        return getBase64StrToImage(s);
    }

    public static String getslidingBlock() {
        String[] strings = FILE_NAME_MAP.get(CaptchaBaseMapEnum.SLIDING_BLOCK.getCodeValue());
        if (null == strings || strings.length == 0) {
            return null;
        }
        Integer randomInt = RandomUtils.getRandomInt(0, strings.length);
        String s = SLIDING_BLOCK_CACHE_MAP.get(strings[randomInt]);
        return s;
    }

    public static BufferedImage getPicClick() {
        String[] strings = FILE_NAME_MAP.get(CaptchaBaseMapEnum.PIC_CLICK.getCodeValue());
        if (null == strings || strings.length == 0) {
            return null;
        }
        Integer randomInt = RandomUtils.getRandomInt(0, strings.length);
        String s = PIC_CLICK_CACHE_MAP.get(strings[randomInt]);
        return getBase64StrToImage(s);
    }

    /**
     * 图片转base64 字符串
     *
     * @param templateImage
     * @return
     */
    public static String getImageToBase64Str(BufferedImage templateImage) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        try {
            ImageIO.write(templateImage, "png", baos);
        } catch (IOException e) {
            e.printStackTrace();
        }
        byte[] bytes = baos.toByteArray();

        Base64.Encoder encoder = Base64.getEncoder();

        return encoder.encodeToString(bytes).trim();
    }

    /**
     * base64 字符串转图片
     *
     * @param base64String
     * @return
     */
    public static BufferedImage getBase64StrToImage(String base64String) {
        try {
            Base64.Decoder decoder = Base64.getDecoder();
            byte[] bytes = decoder.decode(base64String);
            ByteArrayInputStream inputStream = new ByteArrayInputStream(bytes);
            return ImageIO.read(inputStream);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }


    private static Map<String, String> getResourcesImagesFile(String path) {
        //默认提供六张底图
        Map<String, String> imgMap = new HashMap<>(64);
        ClassLoader classLoader = ImageUtils.class.getClassLoader();
        for (int i = 1; i <= SIX; i++) {
            InputStream resourceAsStream = classLoader.getResourceAsStream(path.concat("/").concat(String.valueOf(i).concat(".png")));
            byte[] bytes = new byte[0];
            try {
                bytes = FileCopyUtils.copyToByteArray(resourceAsStream);
            } catch (IOException e) {
                e.printStackTrace();
            }
            String string = Base64Utils.encodeToString(bytes);
            String filename = String.valueOf(i).concat(".png");
            imgMap.put(filename, string);
        }
        return imgMap;
    }

    private static Map<String, String> getImagesFile(String path) {
        Map<String, String> imgMap = new HashMap<>(64);
        File file = new File(path);
        if (!file.exists()) {
            return new HashMap<>(64);
        }
        File[] files = file.listFiles();
        assert files != null;
        Arrays.stream(files).forEach(item -> {
            try {
                FileInputStream fileInputStream = new FileInputStream(item);
                byte[] bytes = FileCopyUtils.copyToByteArray(fileInputStream);
                String string = Base64Utils.encodeToString(bytes);
                imgMap.put(item.getName(), string);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
        return imgMap;
    }

}
