package com.guuidea.component.docker;

import com.alibaba.fastjson.JSONObject;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.POST;
import retrofit2.http.Path;
import retrofit2.http.Query;

import static com.guuidea.component.docker.constants.DockerApiConstants.*;

/**
 * Docker 远程API.
 *
 * @author hzchendou
 * @date 2020/2/17
 * @since 1.0
 */
public interface DockerRemoteApi {

    /**
     * 获取Docker daemon服务 状态
     *
     * @return
     */
    @GET(PING_API_PATH)
    Call<ResponseBody> ping();

    /**
     * 查询容器信息
     *
     * @return
     */
    @GET(CONTAINERS_JSON_PATH)
    Call<ResponseBody> containers(@Query("filters") String filters);

    /**
     * 创建容器镜像信息
     *
     * @param name
     * @param body
     * @return
     */
    @POST(CONTAINERS_CREATE_PATH)
    Call<ResponseBody> createContainer(@Query("name") String name, @Body JSONObject body);

    /**
     * 启动容器
     *
     * @param id
     * @return
     */
    @POST(CONTAINERS_START_PATH)
    Call<ResponseBody> startContainer(@Path("id") String id);

    /**
     * 停止容器
     *
     * @param id
     * @return
     */
    @POST(CONTAINERS_STOP_PATH)
    Call<ResponseBody> stopContainer(@Path("id") String id);

    /**
     * 重启容器
     *
     * @param id
     * @return
     */
    @POST(CONTAINERS_RESTART_PATH)
    Call<ResponseBody> restartContainer(@Path("id") String id);

    /**
     * 终止容器
     *
     * @param id
     * @return
     */
    @POST(CONTAINERS_KILL_PATH)
    Call<ResponseBody> killContainer(@Path("id") String id);

    /**
     * 删除容器
     *
     * @param id
     * @return
     */
    @DELETE(CONTAINERS_DELETE_PATH)
    Call<ResponseBody> deleteContainer(@Path("id") String id);

    /**
     * 查询镜像信息
     *
     * @param filters
     * @return
     */
    @GET(IMAGES_JSON_PATH)
    Call<ResponseBody> images(@Query("filters") String filters);

    /**
     * 构建镜像
     *
     * @param t          镜像tag信息
     * @param remote     远程文件
     * @param dockerfile dockerfile 路径
     * @param argsJson   构建擦数
     * @return
     */
    @POST(IMAGES_BUILD_PATH)
    Call<ResponseBody> buildImage(@Query("t") String t, @Query("remote") String remote,
            @Query("dockerfile") String dockerfile, @Query("buildargs") String argsJson);

    /**
     * 推送镜像到仓库
     *
     * @param name
     * @param auth
     * @return
     */
    @POST(IMAGES_PUSH_PATH)
    Call<ResponseBody> pushImage(@Path("name") String name, @Header("X-Registry-Auth") String auth);


    @POST(IMAGES_CREATE_PATH)
    Call<ResponseBody> createImage(@Query("fromImage") String fromImage, @Header("X-Registry-Auth") String auth);

}
