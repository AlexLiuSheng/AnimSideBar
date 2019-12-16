# AnimSideBar
A SiderBar base on Android that has a wave anim
# update in 19/12/16
implementation this widget in a new way.
## effect
<img style="width:300px;" src="https://github.com/AlexLiuSheng/AnimSideBar/blob/master/ui/ui2.gif"/><img style="width:300px;" src="https://github.com/AlexLiuSheng/AnimSideBar/blob/master/ui/ui1.gif"/>

## use
### include:
```
	dependencies {
	       implementation 'com.github.AlexLiuSheng:AnimSideBar:1.0.0'
	}
```
alternatively,you can include it just copy the code into your project.

### use
to use,you should write like this in your layout  
```
  <com.allenliu.sidebar.SideBar
    android:layout_alignParentRight="true"
    android:textColor="@color/colorAccent"
    android:textSize="12sp"
    android:paddingRight="10dp"
    android:layout_width="wrap_content"
    android:id="@+id/bar"
    android:layout_height="match_parent" />
 ```

after this ,you can add a setOnStrSelectCallBack,like this

    bar.setOnStrSelectCallBack(new ISideBarSelectCallBack() {
            @Override
            public void onSelectStr(int index, String selectStr) {
                Toast.makeText(SideBarDemoActivity.this,selectStr,Toast.LENGTH_SHORT).show();
            }
        });
        
        
there are some other methods to apply:
     
```
        <attr name="fontScale" format="float" />   //text Font scale  when scrolling ,defalut :1
        <attr name="bigTextSize" format="float" /> // the big text size ,default is 3 times than text size
        <attr name="openCount" format="integer" />  //the parabola opening size ,defalut 13,
        <attr name="A" format="integer" />   // the size of amplitude,defalut is 100dp
        <attr name="gapBetweenText" format="integer" /> // the distance between BigText and peak,defalut is 50dp
```
