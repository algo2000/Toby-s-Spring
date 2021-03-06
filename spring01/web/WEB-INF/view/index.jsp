<%--
  Created by IntelliJ IDEA.
  User: leo
  Date: 20. 11. 3.
  Time: 오후 6:35
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html>
<head>
  <title>Skeleton Animation</title>
  <link rel="stylesheet" type="text/css" href="/css/Skeleton_Animation.css" />
  <style>
    .box_big
    {
      height: 500px;
      width: 100%;
      border-radius: 10px;
      margin: 10px 0px;
    }
    .box_thin_long
    {
      height: 6px;
      width: 100%;
      border-radius: 20px;
      margin-inline-end: 10px;
      margin: auto 0;
      display: block;
      margin-bottom: 5px;
    }
    .box_thin_long_far-obj
    {
      height: 6px;
      width: 100%;
      border-radius: 20px;
      margin-inline-end: 10px;
      margin: auto 0;
      display: block;
      margin-bottom: 20px;
    }
    .box_thin_short
    {
      height: 6px;
      width: 30%;
      border-radius: 20px;
      margin-inline-end: 10px;
      margin: auto 0;
      display: block;
      margin-bottom: 5px;
    }
    .box_cycle
    {
      height: 30px;
      width: 30px;
      border-radius: 15px;
      margin-inline-end: 10px;
      display: inline-block;
    }
    .group01
    {
      margin: 50px auto;
      max-width: 800px;
    }
    .group02
    {
      text-align: center;
    }
    .group03
    {
      display: inline-block;
      width: 150px;
    }
  </style>
</head>
<body>
<button  onclick="location.href='/notice/list'">리스트 보기</button>
<div class="group01">
  <div class="box_cycle skeleton-ui_animation02"></div>
  <div class="group03">
    <div class="box_thin_long skeleton-ui_animation02"></div>
    <div class="box_thin_short skeleton-ui_animation02"></div>
  </div>
  <div class="box_big skeleton-ui_animation02"></div>
  <div class="box_thin_long_far-obj skeleton-ui_animation02"></div>
  <div class="box_thin_long_far-obj skeleton-ui_animation02"></div>
  <div class="box_thin_long_far-obj skeleton-ui_animation02"></div>
  <div class="box_thin_long_far-obj skeleton-ui_animation02"></div>
</div>

<div class="group01">
  <div class="box_cycle skeleton-ui_animation01"></div>
  <div class="group03">
    <div class="box_thin_long skeleton-ui_animation01"></div>
    <div class="box_thin_short skeleton-ui_animation01"></div>
  </div>
  <div class="box_big skeleton-ui_animation01"></div>
  <div class="box_thin_long_far-obj skeleton-ui_animation01"></div>
  <div class="box_thin_long_far-obj skeleton-ui_animation01"></div>
  <div class="box_thin_long_far-obj skeleton-ui_animation01"></div>
  <div class="box_thin_long_far-obj skeleton-ui_animation01"></div>
</div>

<div class="group01">
  <div class="box_cycle skeleton-ui_animation02"></div>
  <div class="group03">
    <div class="box_thin_long skeleton-ui_animation02"></div>
    <div class="box_thin_short skeleton-ui_animation02"></div>
  </div>
  <div class="box_big skeleton-ui_animation02"></div>
  <div class="box_thin_long_far-obj skeleton-ui_animation02"></div>
  <div class="box_thin_long_far-obj skeleton-ui_animation02"></div>
  <div class="box_thin_long_far-obj skeleton-ui_animation02"></div>
  <div class="box_thin_long_far-obj skeleton-ui_animation02"></div>
</div>
</body>
</html>
