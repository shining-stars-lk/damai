package com.damai.captcha.util;


import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.Reader;
import java.io.StringWriter;
import java.io.Writer;

/**
 * @program: 极度真实还原大麦网高并发实战项目。 添加 阿星不是程序员 微信，添加时备注 大麦 来获取项目的完整资料 
 * @description: FileCopyUtils
 * @author: 阿星不是程序员
 **/
public class FileCopyUtils {

    public FileCopyUtils() {
    }

    public static int copy(InputStream in, OutputStream out) throws IOException {
        int var2;
        try {
            var2 = StreamUtils.copy(in, out);
        } finally {
            try {
                in.close();
            } catch (IOException var12) {
            }

            try {
                out.close();
            } catch (IOException var11) {
            }

        }

        return var2;
    }

    public static void copy(byte[] in, OutputStream out) throws IOException {
        try {
            out.write(in);
        } finally {
            try {
                out.close();
            } catch (IOException var8) {
            }

        }

    }

    public static byte[] copyToByteArray(InputStream in) throws IOException {
        if (in == null) {
            return new byte[0];
        } else {
            ByteArrayOutputStream out = new ByteArrayOutputStream(4096);
            copy((InputStream)in, (OutputStream)out);
            return out.toByteArray();
        }
    }

    public static int copy(Reader in, Writer out) throws IOException {
        try {
            int byteCount = 0;
            char[] buffer = new char[4096];

            int bytesRead;
            for(boolean var4 = true; (bytesRead = in.read(buffer)) != -1; byteCount += bytesRead) {
                out.write(buffer, 0, bytesRead);
            }

            out.flush();
            int var5 = byteCount;
            return var5;
        } finally {
            try {
                in.close();
            } catch (IOException var15) {
            }

            try {
                out.close();
            } catch (IOException var14) {
            }

        }
    }

    public static void copy(String in, Writer out) throws IOException {
        try {
            out.write(in);
        } finally {
            try {
                out.close();
            } catch (IOException var8) {
            }

        }

    }

    public static String copyToString(Reader in) throws IOException {
        if (in == null) {
            return "";
        } else {
            StringWriter out = new StringWriter();
            copy((Reader)in, (Writer)out);
            return out.toString();
        }
    }

    /**
     * 删除文件
     *
     * @param fileUrl 文件路径
     * @return
     */
    public static boolean deleteFile(String fileUrl) {
        try {
            File file = new File(fileUrl);
            // 路径为文件且不为空则进行删除
            if (file.isFile() && file.exists()) {
                file.delete();
                return true;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

}

