package com.ruoyi.web.test;

import com.alibaba.fastjson.JSON;
import com.dingtalk.api.DefaultDingTalkClient;
import com.dingtalk.api.DingTalkClient;
import com.dingtalk.api.request.*;
import com.dingtalk.api.response.*;
import com.taobao.api.ApiException;

/**
 * 获取部门信息
 */
public class getPersonnels {

    /**
     *
     * @param appKey 应用的唯一标识key。
     * @param appSecret  应用的密钥。AppKey和AppSecret可在钉钉开发者后台的应用详情页面获取。
     * @return
     */
    public static String queryAccessToken(String appKey,String appSecret){
        DingTalkClient client = new DefaultDingTalkClient("https://oapi.dingtalk.com/gettoken");
        OapiGettokenRequest request = new OapiGettokenRequest();
        request.setAppkey(appKey);
        request.setAppsecret(appSecret);
        request.setHttpMethod("GET");
        OapiGettokenResponse response = null;
        try {
            response = client.execute(request);
        } catch (ApiException e) {
            e.printStackTrace();
        }
        System.out.println(response.getBody());
        String body = response.getBody();
        String accessToken = JSON.parseObject(body).get("access_token").toString();
        //部门信息

        queryPersonnel(accessToken,1L);
        return accessToken;
    }

    /**
     *
     * @param accessToken 调用企业接口凭证
     * @param deptId 父部门id，只支持查询下一级子部门
     */
    public static void queryPersonnel(String accessToken,long deptId){
        try {
            DingTalkClient client = new DefaultDingTalkClient("https://oapi.dingtalk.com/topapi/v2/department/listsub");
            OapiV2DepartmentListsubRequest req = new OapiV2DepartmentListsubRequest();
            req.setDeptId(deptId);
            OapiV2DepartmentListsubResponse rsp = client.execute(req,accessToken);
            System.out.println("获取部门列表：----"+rsp.getBody());
            queryUserInfo(accessToken,1L,0L,10L);
        } catch (ApiException e) {
            e.printStackTrace();
        }

    }

    /**
     *
     * @param accessToken 调用企业接口凭证
     * @param deptId 父部门id，只支持查询下一级子部门
     * @param cursor 游标
     * @param size  分页长度
     */
    private static void queryUserInfo(String accessToken,long deptId,long cursor,long size) {
        try {
            DingTalkClient client = new DefaultDingTalkClient("https://oapi.dingtalk.com/topapi/user/listsimple");
            OapiUserListsimpleRequest req = new OapiUserListsimpleRequest();
            req.setDeptId(deptId);
            req.setCursor(cursor);
            req.setSize(size);
            OapiUserListsimpleResponse rsp = client.execute(req, accessToken);
            System.out.println("查询部门用户简易信息：-----"+rsp.getBody());
        } catch (ApiException e) {
            e.printStackTrace();
        }
    }


    public static void main(String[] args) {
        queryAccessToken("dingvyh3a8ka9ga4pyct","ug36MsdvskxctEhq-zoB-l-Ot9oaTtVm9sbhpSf0YJ4IxuFo5wz_Weh5jzkfT3gt");
    }

}
