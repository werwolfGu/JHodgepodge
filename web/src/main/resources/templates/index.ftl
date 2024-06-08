<#ftl attributes={"content_type":"text/html; charset=UTF-8"}>
<?xml version="1.0" encoding="utf-8"?>
<html>
<head>
    <title>FreeMarker 实验</title>
</head>
<body>
<h1 style="color: aqua">数据库评估表</h1>

<table border="1" style="border-collapse: collapse;">
    <caption align="left" style="align-content: normal; " >
        <div style="color: aqua"><strong>表格的标题</strong></div>
    </caption>
    <tr bgcolor="#deb887">
        <th>姓名</th>
        <th>年龄</th>
        <th>Email</th>
    </tr>
    </br>
    <#list userList as user>
        <tr>
            <td style="width: 100px;text-align: center" >${user.name}</td>
            <td style="width: 100px;text-align: center" >${user.age}</td>
            <td style="width: 100px;text-align: center" >${user.email}</td>
        </tr>
        </br>
    </#list>
</table>
<table border="1" style="border-collapse: collapse;">
    <caption align="left" >表格的标题2</caption>
    <tr>
        <th>姓名</th>
        <th>年龄</th>
        <th>Email</th>
    </tr>
    </br>
    <#list dataList as user>
        <tr>
            <td align="center" width="100">${user.name}</td>
            <td align="center" width="100">${user.age}</td>
            <td align="center" width="100">${user.email}</td>
        </tr>
        </br>
    </#list>
</table>
</body>
</html>