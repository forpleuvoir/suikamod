package com.forpleuvoir.suika.util;

import java.io.*;
import java.nio.charset.StandardCharsets;

/**
 * 文件工具类
 * @BelongsProject suikamod
 * @BelongsPackage com.forpleuvoir.suika.util
 * @ClassName FileUtil
 * @author forpleuvoir
 * @CreateTime 2020/10/21 9:47
 * @Description 文件工具类
 */
public class FileUtil {
    /**
     * 加载文件中的内容
     *
     * @param path 文件路径
     * @param name 文件名
     * @return 内容
     * @throws Exception
     */
    public static String loadFile(String path, String name) throws Exception {
        File file = new File(path + "/" + name);
        if (!file.exists()) {
            throw new FileNotFoundException("文件未找到:"+path+name);
        }
        StringBuilder result = new StringBuilder();
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(new FileInputStream(file), StandardCharsets.UTF_8));
        String s;
        while ((s = bufferedReader.readLine()) != null) {
            result.append(System.lineSeparator() + s);
        }
        bufferedReader.close();
        return result.toString();
    }

    /**
     * 创建文件
     *
     * @param path 文件路径
     * @param name 文件名
     * @return 成功创建的文件对象
     * @throws Exception
     */
    public static File createFile(String path, String name) throws Exception {
        File fileDir = new File(path);
        if (!fileDir.exists()) {
            fileDir.mkdirs();
        }
        File file = new File(path + "/" + name);
        if (!file.exists()) {
            file.createNewFile();
        }
        return file;
    }

    /**
     * 将字符串写入文件
     * @param file 文件对象
     * @param content 写入的内容
     * @param append 是否从最后添加
     * @return 文件对象
     * @throws Exception
     */
    public static File writeFile(File file, String content) throws Exception {
        if (!file.exists()) {
            throw new FileNotFoundException();
        }
        OutputStreamWriter fileWriter = new OutputStreamWriter(new FileOutputStream(file), StandardCharsets.UTF_8);
        fileWriter.write(content);
        fileWriter.close();
        return file;
    }
}
