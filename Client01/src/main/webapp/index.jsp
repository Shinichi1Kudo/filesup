<%--
  Created by IntelliJ IDEA.
  User: Administrator
  Date: 2020/4/22
  Time: 18:51
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>

<style>
    *{
        margin: 0px;
        padding: 0px;
    }
    video{
        position: fixed;
        right: 0px;
        bottom: 0px;
        min-width: 100%;
        min-height: 100%;
        height: auto;
        width: auto;
        /*加滤镜*/
        /*filter: blur(15px); //背景模糊设置 */
        /*-webkit-filter: grayscale(100%);*/
        /*filter:grayscale(100%); //背景灰度设置*/
        z-index:-11
    }
    source{
        min-width: 100%;
        min-height: 100%;
        height: auto;
        width: auto;
    }
    p{
        width: 100%;
        text-align: center;
        font-size: 40px;
        color: white;
    }
</style>


<script type="text/javascript" src="script/jquery-1.7.2.js"></script>
<script type="text/javascript">
    $(function(){
        // ajax--get请求
        $("#getBtn").click(function(){
            $.get("http://localhost:8080/client/user/getfileLoad",function (data) {
                var unixTimestamp = new Date(data.filetime) ;
                commonTime = unixTimestamp.toLocaleString();
                $("#div1").html("文件名字："+data.filename+"</br>"+"文件大小:"+data.filesize+"字节"+"</br>"+"文件类型："+data.filetype
                +"</br>"+"创建时间："+commonTime+"</br>"+"文件保存地址:"+data.filepath);
            },"json");

        });


    });
</script>
<head>
    <link rel="stylesheet" type="text/css" href="css/purple.css">
    <title>Title</title>
</head>
<body>
<br><br><br><br><br>
<div style="text-align: center;">
        <h2>跨服务器文件上传</h2><br>
    <form action="user/fileupload" method="post" enctype="multipart/form-data">
        选择文件：<input type="file" name="upload"><br>
        <input type="submit" id="tj">
    </form><br><br>

        <button id="getBtn">查看文件信息</button>
        <div id="div1">您还尚未上传文件噢~~~</div>


<form action="user/filedownload" method="get" enctype="multipart/form-data">
    <input type="submit" value="下载最近上传文件" id="xz">
</form>
</div>
<%--自动播放背景video--%>
<video autoplay loop muted>
    <source src="images/sea.mp4" type="video/mp4"  />
</video>


</body>
</html>
