# customWidget（自定义控件）
custom widget for Android ｜自定义控件

## TextView
_org.lybf.custom.views.TextView_

支持超大文本显示(>10MB)
### Methods
  
name  | parameter  |   return | description
---------  |  ----------------- |  ---------- | -------------------
setText  |  String |  void | set
setTextSize  | float   | void   |  use it to change Text size
### Usage
   .java
   1. 
>TextView textview = new TextView(context);
>textview.setText("TextView!");
>//use this method

//textview.loadText("/sdcard/example.txt");
