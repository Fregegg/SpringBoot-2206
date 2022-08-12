package com.tedu.springboot2206.controller;

import com.tedu.springboot2206.entity.User;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.util.ArrayList;
import java.util.List;

@Controller
public class UserController {
    //表示保存所有用户信息的目录
    private static final File userDir;

    static {
        userDir = new File("./users");
        if (!userDir.exists()) {
            //noinspection ResultOfMethodCallIgnored
            userDir.mkdirs();
        }
    }

    /**
     * @param request  请求对象，封装着浏览器发送过来的所有内容
     * @param response 响应对象，封装着我们即将给浏览器回复的内容
     */
    /*
        注册账号
     */
    @RequestMapping("/regUser")
    public void reg(HttpServletRequest request, HttpServletResponse response) {
        System.out.println("开始处理注册！！！");
        /*
            处理注册的大致过程
            1：获取用户在注册页面上输入的注册信息(通过请求对象获取浏览器提交的表单数据)
            2：处理注册
            3：设置响应对象，将处理结果回馈给浏览器
         */
        /*
            HttpServletRequest的重要方法:
            String getParameter(String name)
            获取浏览器传递过来的某个参数的值
            这里的传入的是参数名，对应的是页面表单输入框的名字(name属性对应的值)
         */
        //  1:    获取注册页面的表单信息
        String username = request.getParameter("username");//获取用户名
        String pwd = request.getParameter("pwd");
        String nickname = request.getParameter("nickname");
        String ageStr = request.getParameter("age");
        System.out.println("username: " + username + "pwd:" + pwd + "nickname:" + nickname + "age:" + ageStr);

        if (username == null || username.trim().isEmpty()
                || nickname == null || nickname.trim().isEmpty()
                || ageStr == null || ageStr.trim().isEmpty() || !ageStr.matches("^[0-9]+$")
                || pwd == null || pwd.trim().isEmpty()) {
            try {
                //sendRedirect会给客户端发送一个html地址，请求客户端发送访问请求，而不是直接返回html
                response.sendRedirect("/reg_info_error.html");
            } catch (IOException e) {
                e.printStackTrace();
            }
            return;
        }

        /*  2:
            将该注册用户信息以User对象形式表示并序列化到文件中保存
            将保存用户信息的文件统一放在users目录下，文件的名字格式为”用户名.obj“
         */

        int age = Integer.parseInt(ageStr);
        User user = new User(username, pwd, nickname, age);

        /*
            File的重载构造器
            File(File parent,String child)
            该File对象表达的是再parent表示的目录中的子项child
         */

        File file = new File(userDir, username + ".obj");
        //判断用户是否已经存在   dfsdfs d
        if (file.exists()) {
            try {
                response.sendRedirect("/have_user.html");
            } catch (IOException e) {
                e.printStackTrace();
            }
            return;
        }

        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(file))) {
            oos.writeObject(user);
        } catch (IOException e) {
        }

        //  3: 让浏览器重定向到指定的路径查看处理结果页面
        try {
            /*
                "/"表示static目录下,"/reg_success.html"是发给浏览器的
             */
            response.sendRedirect("/reg_success.html");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /*
        用户登录
     */
    @RequestMapping("/loginUser")
    public void login(HttpServletRequest request,HttpServletResponse response){
        System.out.println("开始处理登录");
        String name = request.getParameter("username");
        String pwd = request.getParameter("pwd");

        File file = new File(userDir,name+".obj");

        if (file.exists()){
            try(FileInputStream fis = new FileInputStream(file);
                ObjectInputStream ois = new ObjectInputStream(fis)
            )  {
                User user = (User) ois.readObject();
                if (user.getPwd().equals(pwd)){
                    response.sendRedirect("/login_success.html");
                    return;
                }
            } catch (IOException | ClassNotFoundException e){
                e.printStackTrace();
            }

        }
        //登录失败
        try {
            response.sendRedirect("/login_fail.html");
        } catch (IOException e) {
            e.printStackTrace();
        }

        /*
        if (file.exists()){
            try {
                User user =(User) ois.readObject();
                if (name==null||name.equals("")||pwd==null||pwd.equals("")){
                    response.sendRedirect("/login_info_error.html");
                    return;
                }
                if (!user.getUsername().equals(name)||!user.getPwd().equals(pwd)){
                    response.sendRedirect("/login_fail.html");
                    return;
                }
                response.sendRedirect("/login_success.html");
            } catch (IOException | ClassNotFoundException e) {
                e.printStackTrace();
            }
        }
        try {
            response.sendRedirect("/login_info_error.html");
        } catch (IOException e) {
            e.printStackTrace();
        }

         */

    }

    /*
        用户列表
     */
    @RequestMapping("userList")
    public void userList(HttpServletRequest request,HttpServletResponse response){
        System.out.println("正在处理动态页面！！！！！！");
        /*

         */
        //用于存放user对象的数组列表
        List<User> userList = new ArrayList<>();
        //获取users目录中的所有obj文件
        File[] files = userDir.listFiles(f->f.getName().endsWith(".obj"));
        for (File file : files){
            try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(file));
            ){
                User user = (User) ois.readObject();
                userList.add(user);
            } catch (IOException | ClassNotFoundException e) {
                e.printStackTrace();
            }
        }
        System.out.println(userList);

        try {
            response.setContentType("text/html;charset=utf-8");
            PrintWriter pw = response.getWriter();
            pw.println("<!DOCTYPE html>\n" +
                    "<html lang=\"en\">\n" +
                    "<head>\n" +
                    "    <meta charset=\"UTF-8\">\n" +
                    "    <title>用户列表</title>\n" +
                    "</head>\n" +
                    "<body>\n" +
                    "<center>\n"+
                    "<h1>用户列表</h1>\n" +
                    "<table border=\"1\" align=\"center\">\n" +
                    "  <tr>\n" +
                    "    <td>用户名</td>\n" +
                    "    <td>密码</td>\n" +
                    "    <td>昵称</td>\n" +
                    "    <td>年龄</td>\n" +
                    "    <td>操作</td>\n" +
                    "  </tr>\n");
            for (User user : userList){
                pw.println("<tr>");
                pw.println("<td>"+user.getUsername()+"</td>");
                pw.println("<td>"+user.getPwd()+"</td>");
                pw.println("<td>"+user.getNickname()+"</td>");
                pw.println("<td>"+user.getAge()+"</td>");
                pw.println("<td><a href='/deleteUser?username="+user.getUsername()+"'>删除</a></td>");
                pw.println("</tr>");
            }
            pw.println("</center>\n" +
                    "</table>\n" +
                    "</body>\n" +
                    "</html>");
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
    @RequestMapping("deleteUser")
    public void delete(HttpServletRequest request,HttpServletResponse response){
        System.out.println("开始处理删除！！！！");
        String username = request.getParameter("username");
        System.out.println("要删除的用户是："+username);
        File file = new File("./users/"+username+".obj");
        file.delete();
        try {
            response.sendRedirect("/userList");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
