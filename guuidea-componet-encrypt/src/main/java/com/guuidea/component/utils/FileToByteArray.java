package com.guuidea.component.utils;

import java.io.BufferedOutputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;

public class FileToByteArray {
	/**文件转换为字节数组
	 * @param file
	 * @return
	 */
	public static byte[] fileToByteArray(File file) {
        byte[] imagebs = null;
        try {
            //读取输入的文件====文件输入流
        	FileInputStream fis = new FileInputStream(file);
        	//字节数组输出流  在内存中创建一个字节数组缓冲区，所有输出流的数据都会保存在字符数组缓冲区中
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            int len;
            byte[] buffer = new byte[1024];
          //将文件读入到字节数组中
            while ((len = fis.read(buffer)) != -1) {
            	// 将指定字节数组中从偏移量 off 开始的 len 个字节写入此字节数组输出流。
                baos.write(buffer, 0, len);
            }
            imagebs = baos.toByteArray();//当前输出流的拷贝  拷贝到指定的字节数组中
 
            fis.close();
            baos.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
 
        return imagebs;

}
	 
    /**根据byte[] 数组生成文件  （在本地）
     * @param bfile  字节数组
     * @param filePath 文件路径
     * @param fileName 文件名
     */
    public static void getFile(byte[] bfile, String filePath,String fileName) {  
        BufferedOutputStream bos = null;  //带缓冲得文件输出流
        FileOutputStream fos = null;      //文件输出流
        File file = null;  
        try {  
            File dir = new File(filePath);  
            if(!dir.exists()&&dir.isDirectory()){//判断文件目录是否存在  
                dir.mkdirs();  
            }  
            file = new File(filePath+"/"+fileName);  //文件路径+文件名
            fos = new FileOutputStream(file);  
            bos = new BufferedOutputStream(fos);  
            bos.write(bfile);  
        } catch (Exception e) {  
            e.printStackTrace();  
        } finally {  
            if (bos != null) {  
                try {  
                    bos.close();  
                } catch (IOException e1) {  
                    e1.printStackTrace();  
                }  
            }  
            if (fos != null) {  
                try {  
                    fos.close();  
                } catch (IOException e1) {  
                    e1.printStackTrace();  
                }  
            }  
        }  
    } 

}

