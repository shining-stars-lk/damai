package com.damai.captcha.service.impl;

import com.damai.captcha.model.common.CaptchaTypeEnum;
import com.damai.captcha.model.common.RepCodeEnum;
import com.damai.captcha.model.common.ResponseModel;
import com.damai.captcha.model.vo.CaptchaVO;
import com.damai.captcha.model.vo.PointVO;
import com.damai.captcha.util.AesUtil;
import com.damai.captcha.util.ImageUtils;
import com.damai.captcha.util.JsonUtil;
import com.damai.captcha.util.RandomUtils;
import com.damai.captcha.util.StringUtils;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.util.Base64;
import java.util.Objects;
import java.util.Properties;
import java.util.Random;

import static com.damai.captcha.service.impl.CaptchaConstant.FIVE;
import static com.damai.captcha.service.impl.CaptchaConstant.THREE;
import static com.damai.captcha.service.impl.CaptchaConstant.TWO;

/**
 * @program: 极度真实还原大麦网高并发实战项目。 添加 阿星不是程序员 微信，添加时备注 大麦 来获取项目的完整资料 
 * @description: 滑动验证码
 * @author: 阿星不是程序员
 **/
public class BlockPuzzleCaptchaServiceImpl extends AbstractCaptchaService {
    
    @Override
    public void init(Properties config) {
        super.init(config);
    }
    
    @Override
    public void destroy(Properties config) {
        logger.info("start-clear-history-data-{}",captchaType());
    }
    
    @Override
    public String captchaType() {
        return CaptchaTypeEnum.BLOCKPUZZLE.getCodeValue();
    }
    
    @Override
    public ResponseModel get(CaptchaVO captchaVO) {
        ResponseModel r = super.get(captchaVO);
        if(!validatedReq(r)){
            return r;
        }
        //原生图片
        BufferedImage originalImage = ImageUtils.getOriginal();
        if (null == originalImage) {
            logger.error("滑动底图未初始化成功，请检查路径");
            return ResponseModel.errorMsg(RepCodeEnum.API_CAPTCHA_BASEMAP_NULL);
        }
        //设置水印
        Graphics backgroundGraphics = originalImage.getGraphics();
        int width = originalImage.getWidth();
        int height = originalImage.getHeight();
        backgroundGraphics.setFont(waterMarkFont);
        backgroundGraphics.setColor(Color.white);
        backgroundGraphics.drawString(waterMark, width - getEnOrChLength(waterMark), height - (HAN_ZI_SIZE / 2) + 7);
        
        //抠图图片
        String jigsawImageBase64 = ImageUtils.getslidingBlock();
        BufferedImage jigsawImage = ImageUtils.getBase64StrToImage(jigsawImageBase64);
        if (null == jigsawImage) {
            logger.error("滑动底图未初始化成功，请检查路径");
            return ResponseModel.errorMsg(RepCodeEnum.API_CAPTCHA_BASEMAP_NULL);
        }
        CaptchaVO captcha = pictureTemplatesCut(originalImage, jigsawImage, jigsawImageBase64);
        if (captcha == null
                || StringUtils.isBlank(captcha.getJigsawImageBase64())
                || StringUtils.isBlank(captcha.getOriginalImageBase64())) {
            return ResponseModel.errorMsg(RepCodeEnum.API_CAPTCHA_ERROR);
        }
        logger.info("=====captcha get secretKey:{}", captcha.getSecretKey());
        return ResponseModel.successData(captcha);
    }
    
    @Override
    public ResponseModel check(CaptchaVO captchaVO) {
        ResponseModel r = super.check(captchaVO);
        if(!validatedReq(r)){
            return r;
        }
        //取坐标信息
        String codeKey = String.format(REDIS_CAPTCHA_KEY, captchaVO.getToken());
        if (!CaptchaServiceFactory.getCache(cacheType).exists(codeKey)) {
            return ResponseModel.errorMsg(RepCodeEnum.API_CAPTCHA_INVALID);
        }
        String s = CaptchaServiceFactory.getCache(cacheType).get(codeKey);
        //验证码只用一次，即刻失效
        CaptchaServiceFactory.getCache(cacheType).delete(codeKey);
        PointVO point;
        PointVO point1;
        String pointJson;
        try {
            point = JsonUtil.parseObject(s, PointVO.class);
            //aes解密
            pointJson = decrypt(captchaVO.getPointJson(), point.getSecretKey());
            point1 = JsonUtil.parseObject(pointJson, PointVO.class);
        } catch (Exception e) {
            logger.error("验证码坐标解析失败", e);
            afterValidateFail(captchaVO);
            return ResponseModel.errorMsg(e.getMessage());
        }
        if (point.x - Integer.parseInt(slipOffset) > point1.x
                || point1.x > point.x + Integer.parseInt(slipOffset)
                || point.y != point1.y) {
            afterValidateFail(captchaVO);
            return ResponseModel.errorMsg(RepCodeEnum.API_CAPTCHA_COORDINATE_ERROR);
        }
        //校验成功，将信息存入缓存
        String secretKey = point.getSecretKey();
        String value;
        try {
            value = AesUtil.aesEncrypt(captchaVO.getToken().concat("---").concat(pointJson), secretKey);
            logger.info("=====captcha secretKey:{}",secretKey);
            logger.info("=====captcha value:{}",value);
        } catch (Exception e) {
            logger.error("AES加密失败", e);
            afterValidateFail(captchaVO);
            return ResponseModel.errorMsg(e.getMessage());
        }
        String secondKey = String.format(REDIS_SECOND_CAPTCHA_KEY, value);
        CaptchaServiceFactory.getCache(cacheType).set(secondKey, captchaVO.getToken(), EXPIRE_SIN_THREE);
        captchaVO.setResult(true);
        captchaVO.resetClientFlag();
        captchaVO.setCaptchaVerification(value);
        logger.info("=====captcha pointJson:{}",captchaVO.getPointJson());
        logger.info("=====captcha token:{}",captchaVO.getToken());
        return ResponseModel.successData(captchaVO);
    }
    
    @Override
    public ResponseModel verification(CaptchaVO captchaVO) {
        ResponseModel r = super.verification(captchaVO);
        if(!validatedReq(r)){
            return r;
        }
        try {
            String codeKey = String.format(REDIS_SECOND_CAPTCHA_KEY, captchaVO.getCaptchaVerification());
            if (!CaptchaServiceFactory.getCache(cacheType).exists(codeKey)) {
                return ResponseModel.errorMsg(RepCodeEnum.API_CAPTCHA_INVALID);
            }
            //二次校验取值后，即刻失效
            CaptchaServiceFactory.getCache(cacheType).delete(codeKey);
        } catch (Exception e) {
            logger.error("验证码坐标解析失败", e);
            return ResponseModel.errorMsg(e.getMessage());
        }
        return ResponseModel.success();
    }
    
    /**
     * 根据模板切图
     *
     */
    public CaptchaVO pictureTemplatesCut(BufferedImage originalImage, BufferedImage jigsawImage, String jigsawImageBase64) {
        try {
            CaptchaVO dataVO = new CaptchaVO();
            
            int originalWidth = originalImage.getWidth();
            int originalHeight = originalImage.getHeight();
            int jigsawWidth = jigsawImage.getWidth();
            int jigsawHeight = jigsawImage.getHeight();
            
            //随机生成拼图坐标
            PointVO point = generateJigsawPoint(originalWidth, originalHeight, jigsawWidth, jigsawHeight);
            int x = point.getX();
            
            //生成新的拼图图像
            BufferedImage newJigsawImage = new BufferedImage(jigsawWidth, jigsawHeight, jigsawImage.getType());
            Graphics2D graphics = newJigsawImage.createGraphics();
            
            int bold = 5;
            //如果需要生成RGB格式，需要做如下配置,Transparency 设置透明
            newJigsawImage = graphics.getDeviceConfiguration().createCompatibleImage(jigsawWidth, jigsawHeight, Transparency.TRANSLUCENT);
            // 新建的图像根据模板颜色赋值,源图生成遮罩
            cutByTemplate(originalImage, jigsawImage, newJigsawImage, x);
            
            extracted(originalImage, jigsawImageBase64, originalWidth, jigsawWidth, x);
            
            // 设置“抗锯齿”的属性
            graphics.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            graphics.setStroke(new BasicStroke(bold, BasicStroke.CAP_BUTT, BasicStroke.JOIN_BEVEL));
            graphics.drawImage(newJigsawImage, 0, 0, null);
            graphics.dispose();
            //新建流。
            ByteArrayOutputStream os = new ByteArrayOutputStream();
            //利用ImageIO类提供的write方法，将bi以png图片的数据模式写入流。
            ImageIO.write(newJigsawImage, IMAGE_TYPE_PNG, os);
            byte[] jigsawImages = os.toByteArray();
            //新建流。
            ByteArrayOutputStream oriImagesOs = new ByteArrayOutputStream();
            //利用ImageIO类提供的write方法，将bi以jpg图片的数据模式写入流。
            ImageIO.write(originalImage, IMAGE_TYPE_PNG, oriImagesOs);
            byte[] oriCopyImages = oriImagesOs.toByteArray();
            Base64.Encoder encoder = Base64.getEncoder();
            dataVO.setOriginalImageBase64(encoder.encodeToString(oriCopyImages).replaceAll("\r|\n", ""));
            //point信息不传到前端，只做后端check校验
            dataVO.setJigsawImageBase64(encoder.encodeToString(jigsawImages).replaceAll("\r|\n", ""));
            dataVO.setToken(RandomUtils.getUuid());
            dataVO.setSecretKey(point.getSecretKey());
            
            //将坐标信息存入redis中
            String codeKey = String.format(REDIS_CAPTCHA_KEY, dataVO.getToken());
            CaptchaServiceFactory.getCache(cacheType).set(codeKey, JsonUtil.toJsonString(point), EXPIRE_SIN_SECONDS);
            logger.debug("token：{},point:{}", dataVO.getToken(), JsonUtil.toJsonString(point));
            return dataVO;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
    
    private static void extracted(final BufferedImage originalImage, final String jigsawImageBase64, final int originalWidth, final int jigsawWidth, final int x) {
        if (captchaInterferenceOptions > 0) {
            int position = 0;
            if (originalWidth - x - FIVE > jigsawWidth * TWO) {
                //在原扣图右边插入干扰图
                position = RandomUtils.getRandomInt(x + jigsawWidth + 5, originalWidth - jigsawWidth);
            } else {
                //在原扣图左边插入干扰图
                position = RandomUtils.getRandomInt(100, x - jigsawWidth - 5);
            }
            while (true) {
                String s = ImageUtils.getslidingBlock();
                if (!jigsawImageBase64.equals(s)) {
                    interferenceByTemplate(originalImage, Objects.requireNonNull(ImageUtils.getBase64StrToImage(s)), position);
                    break;
                }
            }
        }
        if (captchaInterferenceOptions > 1) {
            while (true) {
                String s = ImageUtils.getslidingBlock();
                if (!jigsawImageBase64.equals(s)) {
                    Integer randomInt = RandomUtils.getRandomInt(jigsawWidth, 100 - jigsawWidth);
                    interferenceByTemplate(originalImage, Objects.requireNonNull(ImageUtils.getBase64StrToImage(s)),
                            randomInt);
                    break;
                }
            }
        }
    }
    
    
    /**
     * 随机生成拼图坐标
     *
     * @param originalWidth originalWidth
     * @param originalHeight originalHeight
     * @param jigsawWidth jigsawWidth
     * @param jigsawHeight jigsawHeight
     * @return PointVO
     */
    private static PointVO generateJigsawPoint(int originalWidth, int originalHeight, int jigsawWidth, int jigsawHeight) {
        Random random = new Random();
        int widthDifference = originalWidth - jigsawWidth;
        int heightDifference = originalHeight - jigsawHeight;
        int x, y;
        if (widthDifference <= 0) {
            x = 5;
        } else {
            x = random.nextInt(originalWidth - jigsawWidth - 100) + 100;
        }
        if (heightDifference <= 0) {
            y = 5;
        } else {
            y = random.nextInt(originalHeight - jigsawHeight) + 5;
        }
        String key = null;
        if (captchaAesStatus) {
            key = AesUtil.getKey();
        }
        return new PointVO(x, y, key);
    }
    
    /**
     * @param oriImage 原图
     * @param templateImage 模板图
     * @param newImage 新抠出的小图
     * @param x 随机扣取坐标X
     */
    private static void cutByTemplate(BufferedImage oriImage, BufferedImage templateImage, BufferedImage newImage, int x) {
        //临时数组遍历用于高斯模糊存周边像素值
        int[][] martrix = new int[3][3];
        int[] values = new int[9];
        
        int xLength = templateImage.getWidth();
        int yLength = templateImage.getHeight();
        // 模板图像宽度
        for (int i = 0; i < xLength; i++) {
            // 模板图片高度
            for (int j = 0; j < yLength; j++) {
                // 如果模板图像当前像素点不是透明色 copy源文件信息到目标图片中
                int rgb = templateImage.getRGB(i, j);
                if (rgb < 0) {
                    newImage.setRGB(i, j, oriImage.getRGB(x + i, j));
                    
                    //抠图区域高斯模糊
                    readPixel(oriImage, x + i, j, values);
                    fillMatrix(martrix, values);
                    oriImage.setRGB(x + i, j, avgMatrix(martrix));
                }
                
                //防止数组越界判断
                if (i == (xLength - 1) || j == (yLength - 1)) {
                    continue;
                }
                int rightRgb = templateImage.getRGB(i + 1, j);
                int downRgb = templateImage.getRGB(i, j + 1);
                //描边处理，,取带像素和无像素的界点，判断该点是不是临界轮廓点,如果是设置该坐标像素是白色
                if (case1(rgb,rightRgb)|| case2(rgb,rightRgb) || case3(rgb,downRgb) || case4(rgb,downRgb)) {
                    newImage.setRGB(i, j, Color.white.getRGB());
                    oriImage.setRGB(x + i, j, Color.white.getRGB());
                }
            }
        }
        
    }
    
    public static boolean case1(int rgb,int rightRgb){
        return rgb >= 0 && rightRgb < 0;
    }
    
    public static boolean case2(int rgb,int rightRgb){
        return rgb < 0 && rightRgb >= 0;
    }
    
    public static boolean case3(int rgb,int downRgb){
        return rgb >= 0 && downRgb < 0;
    }
    
    public static boolean case4(int rgb,int downRgb){
        return rgb < 0 && downRgb >= 0;
    }
    
    /**
     * 干扰抠图处理
     *
     * @param oriImage 原图
     * @param templateImage 模板图
     * @param x 随机扣取坐标X
     */
    private static void interferenceByTemplate(BufferedImage oriImage, BufferedImage templateImage, int x) {
        //临时数组遍历用于高斯模糊存周边像素值
        int[][] martrix = new int[3][3];
        int[] values = new int[9];
        
        int xLength = templateImage.getWidth();
        int yLength = templateImage.getHeight();
        // 模板图像宽度
        for (int i = 0; i < xLength; i++) {
            // 模板图片高度
            for (int j = 0; j < yLength; j++) {
                // 如果模板图像当前像素点不是透明色 copy源文件信息到目标图片中
                int rgb = templateImage.getRGB(i, j);
                if (rgb < 0) {
                    //抠图区域高斯模糊
                    readPixel(oriImage, x + i, j, values);
                    fillMatrix(martrix, values);
                    oriImage.setRGB(x + i, j, avgMatrix(martrix));
                }
                //防止数组越界判断
                if (i == (xLength - 1) || j == (yLength - 1)) {
                    continue;
                }
                int rightRgb = templateImage.getRGB(i + 1, j);
                int downRgb = templateImage.getRGB(i, j + 1);
                //描边处理，,取带像素和无像素的界点，判断该点是不是临界轮廓点,如果是设置该坐标像素是白色
                if (case1(rgb,rightRgb)|| case2(rgb,rightRgb) || case3(rgb,downRgb) || case4(rgb,downRgb)) {
                    oriImage.setRGB(x + i, j, Color.white.getRGB());
                }
            }
        }
        
    }
    
    private static void readPixel(BufferedImage img, int x, int y, int[] pixels) {
        int xStart = x - 1;
        int yStart = y - 1;
        int current = 0;
        for (int i = xStart; i < THREE + xStart; i++) {
            for (int j = yStart; j < THREE + yStart; j++) {
                int tx = i;
                if (tx < 0) {
                    tx = -tx;
                    
                } else if (tx >= img.getWidth()) {
                    tx = x;
                }
                int ty = j;
                if (ty < 0) {
                    ty = -ty;
                } else if (ty >= img.getHeight()) {
                    ty = y;
                }
                pixels[current++] = img.getRGB(tx, ty);
                
            }
        }
    }
    
    private static void fillMatrix(int[][] matrix, int[] values) {
        int filled = 0;
        for (int[] x : matrix) {
            for (int j = 0; j < x.length; j++) {
                x[j] = values[filled++];
            }
        }
    }
    
    private static int avgMatrix(int[][] matrix) {
        int r = 0;
        int g = 0;
        int b = 0;
        for (int[] x : matrix) {
            for (int j = 0; j < x.length; j++) {
                if (j == 1) {
                    continue;
                }
                Color c = new Color(x[j]);
                r += c.getRed();
                g += c.getGreen();
                b += c.getBlue();
            }
        }
        return new Color(r / 8, g / 8, b / 8).getRGB();
    }
    
    
}
