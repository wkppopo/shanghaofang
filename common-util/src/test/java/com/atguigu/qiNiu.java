package com.atguigu;

import com.atguigu.util.util.FileUtil;
import com.google.gson.Gson;
import com.qiniu.common.QiniuException;
import com.qiniu.common.Zone;
import com.qiniu.http.Response;
import com.qiniu.storage.BucketManager;
import com.qiniu.storage.Configuration;
import com.qiniu.storage.UploadManager;
import com.qiniu.storage.model.DefaultPutRet;
import com.qiniu.util.Auth;
import org.junit.Test;

import java.util.Random;

public class qiNiu {

    /**
     * AK: 3_kMvvL9wAU7y1IUfWxN8pUWjE6gT02HCe7ucMji
     * SK: yyj8sobZYid3NaTBNrNQgwabSr4YfM0ssteR1rxc
     */
    @Test
    public void test() {
        //构造一个带指定 Zone.zone2(){表示华南地区} 对象的配置类
        Configuration cfg = new Configuration(Zone.zone2());
//...其他参数参考类注释
        UploadManager uploadManager = new UploadManager(cfg);
//...生成上传凭证，然后准备上传
        String accessKey = "3_kMvvL9wAU7y1IUfWxN8pUWjE6gT02HCe7ucMji";
        String secretKey = "yyj8sobZYid3NaTBNrNQgwabSr4YfM0ssteR1rxc";
        String bucket = "wangkaiping";
//如果是Windows情况下，格式是 D:\\qiniu\\test.png
        String localFilePath = "C:\\Users\\22196\\Desktop\\服务器响应码.png";
//默认不指定key的情况下，以文件内容的hash值作为文件名
        String uuidName = FileUtil.getUUIDName("服务器响应码.png");
        String key = uuidName;

        Auth auth = Auth.create(accessKey, secretKey);
        String upToken = auth.uploadToken(bucket);
        try {
            Response response = uploadManager.put(localFilePath, key, upToken);
            //解析上传成功的结果
            DefaultPutRet putRet = new Gson().fromJson(response.bodyString(), DefaultPutRet.class);
            System.out.println(putRet.key);
            System.out.println(putRet.hash);
        } catch (QiniuException ex) {
            Response r = ex.response;
            System.err.println(r.toString());
            try {
                System.err.println(r.bodyString());
            } catch (QiniuException ex2) {
                //ignore
            }
        }

    }

    @Test
    public void testDelete(){
        //构造一个带指定 Zone 对象的配置类
        Configuration cfg = new Configuration(Zone.zone2());
//...其他参数参考类注释
        String accessKey = "3_kMvvL9wAU7y1IUfWxN8pUWjE6gT02HCe7ucMji";
        String secretKey = "yyj8sobZYid3NaTBNrNQgwabSr4YfM0ssteR1rxc";
        String bucket = "wangkaiping";

        String key = "1c3c0a8f-1630-405b-b87d-a3e0cbc4864e.png";
        Auth auth = Auth.create(accessKey, secretKey);
        BucketManager bucketManager = new BucketManager(auth, cfg);
        try {
            bucketManager.delete(bucket, key);
        } catch (QiniuException ex) {
            //如果遇到异常，说明删除失败
            System.err.println(ex.code());
            System.err.println(ex.response.toString());
        }
    }

    @Test
    public void test2(){
        int i = new Random(10).nextInt(10);
        System.out.println(i);


    }
}
