package com.microcell.imapp;

import android.content.Intent;
import android.os.AsyncTask;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Dictionary;
import java.util.Hashtable;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadPoolExecutor;

public class Const {
    public static final String host="http://81.70.242.139:7070/";

    public static String session="";
    public static String name="";
    public static String password="";
    public static ArrayList<Message> list=new ArrayList<Message>();
    public static Dictionary<String,Message> users=new Hashtable();

    public static void SendMsg(AppCompatActivity activity,String to,String text){
        class ConnectTask extends AsyncTask {
            String str;
            String text;
            String to;
            AppCompatActivity context;
            public ConnectTask(AppCompatActivity context,String to,String text){
                this.text=text;
                this.to=to;
                this.context=context;
            }
            @Override
            protected Object doInBackground(Object[] objects) {
                str=doGet(host+"send?session="+session+"&to="+to+"&text="+text);
                return null;
            }
            @Override
            protected void onPostExecute(Object object){
                if(isEmp(str)||str.equals("Fail")){
                    Toast.makeText(context,"发送失败",Toast.LENGTH_SHORT).show();
                }else {
                    Toast.makeText(context,"发送成功",Toast.LENGTH_SHORT).show();
                }
            }
        }
        ConnectTask task=new ConnectTask(activity,to,text);
        task.executeOnExecutor((ThreadPoolExecutor) Executors.newCachedThreadPool());
    }

    public static void GetMsg(){
        class ConnectTask extends AsyncTask {
            String str;
            public ConnectTask(){
                str="";
            }
            @Override
            protected Object doInBackground(Object[] objects) {
                str=doGet(host+"receive?session="+session);
                if(isEmp(str)) return null;
                if(str.equals("Fail")) return null;
                ArrayList<Message> res =new ArrayList<Message>();
                JSONArray arr = null;
                try {
                    arr = new JSONArray(str);
                    for(int i=0;i<arr.length();i++){
                        JSONObject jsonObj = arr.getJSONObject(i);
                        Message m=new Message(jsonObj.getString("from"),jsonObj.getString("to"),jsonObj.getString("message"),jsonObj.getLong("time"));
                        res.add(m);
                    }
                    list=res;
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                return null;
            }
        }
        ConnectTask task = new ConnectTask();
        task.executeOnExecutor((ThreadPoolExecutor) Executors.newCachedThreadPool());
    }
    public static void Login(String n, String pass, AppCompatActivity activity){
        class ConnectTask extends AsyncTask{
            AppCompatActivity context;
            String name;
            String password;

            public ConnectTask(String name, String password,AppCompatActivity context){
                this.name=name;
                this.password=password;
                this.context=context;
            }

            @Override
            protected Object doInBackground(Object[] objects) {
                session=doGet(host+"user/login?name="+name+"&password="+password);
                return null;
            }
            @Override
            protected void onPostExecute(Object object){
                if(isEmp(session)){
                    Toast.makeText(context,"登录失败",Toast.LENGTH_SHORT).show();
                }else {
                    Toast.makeText(context,"登录成功",Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent();
                    /* 指定intent要启动的类 */
                    intent.setClass(context, ListActivity.class);
                    /* 启动一个新的Activity */
                    context.startActivity(intent);
                    context.overridePendingTransition(0, 0);
                    /* 关闭当前的Activity */
                    context.finish();
                }
            }
        }
        name=n;
        password=Sha256(pass);
        ConnectTask task = new ConnectTask(name,password,activity);
        task.executeOnExecutor((ThreadPoolExecutor) Executors.newCachedThreadPool());
    }
    public static void Sign(String n, String pass,String nick, AppCompatActivity activity){
        class ConnectTask extends AsyncTask{
            AppCompatActivity context;
            String name;
            String password;
            String nick;

            public ConnectTask(String name, String password,String nick,AppCompatActivity context){
                this.name=name;
                this.password=password;
                this.context=context;
                this.nick=nick;
            }

            @Override
            protected Object doInBackground(Object[] objects) {
                String res=doGet(host+"user/signin?name="+name+"&password="+password+"&nick="+nick);
                if(isEmp(res)||res.equals("Fail")){
                    return null;
                }else {
                    session=doGet(host+"user/login?name="+name+"&password="+password);
                }

                return null;
            }
            @Override
            protected void onPostExecute(Object object){
                if(isEmp(session)){
                    Toast.makeText(context,"注册失败",Toast.LENGTH_SHORT).show();
                }else {
                    Toast.makeText(context,"注册成功",Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent();
                    /* 指定intent要启动的类 */
                    intent.setClass(context, ListActivity.class);
                    /* 启动一个新的Activity */
                    context.startActivity(intent);
                    context.overridePendingTransition(0, 0);
                    /* 关闭当前的Activity */
                    context.finish();
                }
            }
        }
        name=n;
        password=Sha256(pass);
        ConnectTask task = new ConnectTask(name,password,nick,activity);
        task.executeOnExecutor((ThreadPoolExecutor) Executors.newCachedThreadPool());
    }
    //判断字符串为空
    public static boolean isEmp(String str){
        if(str==null) return true;
        return str.isEmpty();
    }
    //哈希算法
    public static String Sha256(String str) {
        MessageDigest sha256 = null;
        try {
            sha256 = MessageDigest.getInstance("SHA-256");
            sha256.update(str.getBytes());
            byte []sha256Bin = sha256.digest();
            return bytesToHexString(sha256Bin);
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return "";
    }

    private static byte toByte(char c) {
        byte b = (byte) "0123456789abcdef".indexOf(c);
        return b;
    }

    /**
     * 把字节数组转换成16进制字符串
     * @param bArray
     * @return
     */
    private static final String bytesToHexString(byte[] bArray) {
        StringBuffer sb = new StringBuffer(bArray.length);
        String sTemp;
        for (int i = 0; i < bArray.length; i++) {
            sTemp = Integer.toHexString(0xFF & bArray[i]);
            if (sTemp.length() < 2) {
                sb.append(0);
            }
            sb.append(sTemp.toLowerCase());
        }
        return sb.toString();
    }

    public static String doGet(String httpurl) {
        HttpURLConnection connection = null;
        InputStream is = null;
        BufferedReader br = null;
        String result = null;// 返回结果字符串
        try {
            // 创建远程url连接对象
            URL url = new URL(httpurl);
            // 通过远程url连接对象打开一个连接，强转成httpURLConnection类
            connection = (HttpURLConnection) url.openConnection();
            // 设置连接方式：get
            connection.setRequestMethod("GET");
            connection.setUseCaches(false);
            connection.setRequestProperty("Content-type", "application/x-java-serialized-object");
            // 设置连接主机服务器的超时时间：15000毫秒
            connection.setConnectTimeout(1500);
            // 设置读取远程返回的数据时间：60000毫秒
            connection.setReadTimeout(2000);
            // 发送请求
            connection.connect();
            // 通过connection连接，获取输入流
            if (connection.getResponseCode() == 200) {
                is = connection.getInputStream();
                // 封装输入流is，并指定字符集
                br = new BufferedReader(new InputStreamReader(is, "UTF-8"));
                // 存放数据
                StringBuffer sbf = new StringBuffer();
                String temp = null;
                while ((temp = br.readLine()) != null) {
                    sbf.append(temp);
                    sbf.append("\r\n");
                }
                result = sbf.toString();
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            // 关闭资源
            if (null != br) {
                try {
                    br.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

            if (null != is) {
                try {
                    is.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

            connection.disconnect();// 关闭远程连接
        }

        return result;

    }


    public static String doPost(String httpUrl, String param) {
        HttpURLConnection connection = null;
        InputStream is = null;
        OutputStream os = null;
        BufferedReader br = null;
        String result = null;
        try {
            URL url = new URL(httpUrl);
            // 通过远程url连接对象打开连接
            connection = (HttpURLConnection) url.openConnection();
            // 设置连接请求方式
            connection.setRequestMethod("POST");
            // 设置连接主机服务器超时时间：15000毫秒
            connection.setConnectTimeout(15000);
            // 设置读取主机服务器返回数据超时时间：60000毫秒
            connection.setReadTimeout(60000);

            // 默认值为：false，当向远程服务器传送数据/写数据时，需要设置为true
            connection.setDoOutput(true);
            // 默认值为：true，当前向远程服务读取数据时，设置为true，该参数可有可无
            connection.setDoInput(true);
            // 设置传入参数的格式:请求参数应该是 name1=value1&name2=value2 的形式。
            connection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
            // 设置鉴权信息：Authorization: Bearer da3efcbf-0845-4fe3-8aba-ee040be542c0
            connection.setRequestProperty("Authorization", "Bearer da3efcbf-0845-4fe3-8aba-ee040be542c0");
            // 通过连接对象获取一个输出流
            os = connection.getOutputStream();
            // 通过输出流对象将参数写出去/传输出去,它是通过字节数组写出的
            os.write(param.getBytes());
            // 通过连接对象获取一个输入流，向远程读取
            if (connection.getResponseCode() == 200) {

                is = connection.getInputStream();
                // 对输入流对象进行包装:charset根据工作项目组的要求来设置
                br = new BufferedReader(new InputStreamReader(is, "UTF-8"));

                StringBuffer sbf = new StringBuffer();
                String temp = null;
                // 循环遍历一行一行读取数据
                while ((temp = br.readLine()) != null) {
                    sbf.append(temp);
                    sbf.append("\r\n");
                }
                result = sbf.toString();
            }
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            // 关闭资源
            if (null != br) {
                try {
                    br.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if (null != os) {
                try {
                    os.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if (null != is) {
                try {
                    is.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            // 断开与远程地址url的连接
            connection.disconnect();
        }
        return result;
    }
}
