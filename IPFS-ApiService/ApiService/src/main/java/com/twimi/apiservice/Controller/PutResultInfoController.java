package com.twimi.apiservice.Controller;

import com.google.gson.Gson;
import com.twimi.apiservice.Model.AddRequestData;
import com.twimi.apiservice.Model.GetRequestData;
import com.twimi.apiservice.Model.ReturnAddResult;
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


@WebServlet(urlPatterns = {"/api/putDmiResult"})
public class PutResultInfoController extends HttpServlet {
    UserDataService userDataService = new UserDataService();
    Gson gson = new Gson();

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        resp.setHeader("Content-type", "application/json;charset=UTF-8");
        String requestData = "";
        ReturnAddResult returnAddResult = null;
        //AddRequestData addReq = new AddRequestData();
        try {
            BufferedReader br = new BufferedReader(new InputStreamReader( (ServletInputStream) req.getInputStream(), "utf-8"));
            StringBuffer sb = new StringBuffer("");
            String temp;
            while ((temp = br.readLine()) != null) {
                sb.append(temp);
            }
            br.close();
            requestData = sb.toString();
            //addReq = gson.fromJson(requestData,AddRequestData.class);

            //存储
            returnAddResult = userDataService.addResultInformation(requestData);
        } catch (Exception e) {
            e.printStackTrace();
        }

        resp.getWriter().write(gson.toJson(returnAddResult));
    }
}
