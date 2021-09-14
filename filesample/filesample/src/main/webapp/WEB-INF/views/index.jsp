<%@page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
    <!DOCTYPE html>
    <html>
    <head>
        <meta charset="UTF-8">
        <title>Document</title>
        <script src="http://code.jquery.com/jquery-3.4.1.min.js"></script>
        <script>
            $(function(){
                $("send").click(function(){
                    let fom = $("#fileForm");
                    let formData = new FormData(form[0]);
                    $.ajax({
                        type:"post",
                        url:"/upload",
                        data:formData,
                        contenType:false,
                        processData:false,
                        success:function(r) {
                            alert(r.message);
                        }
                    })
                })
            })
        </script>
    </head>
    <body>
        <h1>메인페이지</h1>
        <fom id="fileFrom">
            <input type="file" name="file">
            <button type="button" id="send">전송</button>
        </fom>
    </body>
    </html>