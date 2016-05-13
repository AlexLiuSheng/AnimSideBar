# AnimSideBar
A SiderBar base on Android that has a beautiful anim

to use,you should write like this on your layout  

     <com.allenliu.sidebar.SideBar
    android:layout_alignParentRight="true"
    android:textColor="@color/colorAccent"
    android:textSize="15sp"
    android:paddingRight="10dp"
    android:layout_width="200dp"
    android:id="@+id/bar"
    android:layout_height="match_parent" />

after this ,you can add a setOnStrSelectCallBack,like this

    bar.setOnStrSelectCallBack(new ISideBarSelectCallBack() {
            @Override
            public void onSelectStr(int index, String selectStr) {
                Toast.makeText(SideBarDemoActivity.this,selectStr,Toast.LENGTH_SHORT).show();
            }
        });
        
        
there are some other methods to apply:
     
    setDataResource(String[] data)
    setScaleTime(int scale)
    setScaleItemCount(int scaleItemCount)
