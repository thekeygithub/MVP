package com.twimi.apiservice.Controller;

import com.google.gson.Gson;
import com.twimi.apiservice.Model.*;
import com.twimi.apiservice.Service.UserDataService;

import javax.servlet.ServletException;
import javax.servlet.ServletInputStream;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

@WebServlet(urlPatterns = {"/api/getUserInformation"})
public class GetUserInformationController extends HttpServlet {
    UserDataService userDataService = new UserDataService();
    Gson gson = new Gson();
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String requestData = "";
        GetRequestData getRequestData = null;
        resp.setHeader("Content-type", "application/json;charset=UTF-8");
        try {
            BufferedReader br = new BufferedReader(new InputStreamReader( (ServletInputStream) req.getInputStream(), "utf-8"));
            StringBuffer sb = new StringBuffer("");
            String temp;
            while ((temp = br.readLine()) != null) {
                sb.append(temp);
            }
            br.close();
            requestData = sb.toString();
            getRequestData = gson.fromJson(requestData, GetRequestData.class);
        } catch (Exception e) {
            e.printStackTrace();
        }
        String hash = getRequestData.getHashId();

        //获取用户的全部信息，包括validInfo，resultInfo，payInfo
        if(getRequestData.getInfoType() == null || getRequestData.getInfoType().equals(""))
        {
            ReturnData alldata = userDataService.getAllDataByHash(hash);
            resp.getWriter().write(gson.toJson(alldata));
            resp.getWriter().flush();
            return;
        }


        //获取全部用户validationInfo
        if (getRequestData.getInfoType().equals("0")){
            ReturnData alldata = userDataService.getAllValidationData(hash);
            resp.getWriter().write(gson.toJson(alldata));
        }else if (getRequestData.getInfoType().equals("1")) {
            //获取用户基本信息userInfo
            ReturnData returnUserInfo = userDataService.getUserInfoByHash(hash);
            resp.getWriter().write(gson.toJson(returnUserInfo));
        }else if (getRequestData.getInfoType().equals("2")){
            //获取用户处方信息
            ReturnData returnPrescriptionEntity = userDataService.getPrescriptionByHash(hash);
            resp.getWriter().write(gson.toJson(returnPrescriptionEntity));
        }else if (getRequestData.getInfoType().equals("3")){
            //获取用户照片信息
            ReturnData returnImage = userDataService.getImageByHash(hash);
            resp.getWriter().write(gson.toJson(returnImage));
        }else if (getRequestData.getInfoType().equals("4")){
            //获取用户支付账单信息 payInfo
            ReturnData returnPayData = userDataService.getPayInfomation(hash);
            resp.getWriter().write(gson.toJson(returnPayData));
        }else if (getRequestData.getInfoType().equals("5")){
            //获取用户验证结果信息以及预结算分账单 payInfo
            ReturnData returnDmiData = userDataService.getResultInfomation(hash);
            resp.getWriter().write(gson.toJson(returnDmiData));
        }

        resp.getWriter().flush();
    }
}
