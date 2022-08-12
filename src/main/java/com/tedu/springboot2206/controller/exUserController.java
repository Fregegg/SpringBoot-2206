//package com.tedu.springboot2206.controller;
//
//import com.tedu.springboot2206.entity.User;
//import org.springframework.stereotype.Controller;
//import org.springframework.web.bind.annotation.RequestMapping;
//
//import javax.servlet.http.HttpServletRequest;
//import javax.servlet.http.HttpServletResponse;
//import java.io.File;
//import java.io.FileOutputStream;
//import java.io.IOException;
//import java.io.ObjectOutputStream;
//
//@Controller
//public class exUserController {
//    private static final File userDir;
//
//    static {
//        userDir = new File("./users1");
//        if (!userDir.exists()){
//            //noinspection ResultOfMethodCallIgnored
//            userDir.mkdirs();
//        }
//    }
//
//
//    @RequestMapping("/regUser")
//    public void reg(HttpServletRequest request, HttpServletResponse response){
//        String username = request.getParameter("username");
//        String pwd = request.getParameter("pwd");
//        String nickname = request.getParameter("nickname");
//        String ageStr = request.getParameter("ageStr");
//
//        if (username==null||username.trim().isEmpty()||
//                pwd==null||pwd.trim().isEmpty()||
//                nickname==null||nickname.trim().isEmpty()||
//                ageStr==null||ageStr.trim().isEmpty()||ageStr.matches("^[0-9]+$")){
//            try {
//                response.sendRedirect("/reg_info_error/html");
//            } catch (IOException e) {
//                e.printStackTrace();
//            }
//            return;
//        }
//
//        int age = Integer.parseInt(ageStr);
//        User user = new User(username,pwd,nickname,age);
//        File file = new File(userDir,username+".obj");
//        if (file.exists()){
//            try {
//                response.sendRedirect("/have_user.html");
//            } catch (IOException e) {
//                e.printStackTrace();
//            }
//            return;
//        }
//
//        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(file))){
//            oos.writeObject(user);
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//
//        try {
//            response.sendRedirect("reg_success.html");
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//
//    }
//}
