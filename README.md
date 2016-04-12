#QuickRv
#compile 'com.march.quickrvlibs:quickrvlibs:1.0.6'
#简化RecyclerView使用
##[ListView GridView适配简化](https://github.com/chendongMarch/QuickAdapter)


##更新日志
<font color="orange">###1.0.1       快速适配器,简化ViewHoler使用，一键绑定文字，监听...     (2016.2.10   )</font>

<font color="orange">###1.0.2       添加分割线,自定义网络加载框架加载图片(2016.2.15 )</font>

<font color="orange">###1.0.3       可以添加header和footer ( 2016.3.24 )</font>

<font color="orange">###1.0.4       添加分辨adapter类型的方法 ( 2016.4.6 )</font>

<font color="orange">###1.0.5       兼容使用数组创建Adapter,兼容Java内置对象类型(String,Integer)创建Adapter( 2016.4.10 )</font>

<font color="orange">###1.0.6       瀑布流添加Header和Footer的方法优化( 2016.4.11 )</font>

<font color="orange">###2.0.1       代码重构,基于装饰者模式,实现快速适配,添加Header和Footer,上拉刷新等功能( 2016.4.12 )</font>


----------


#RecyclerView的快速适配

##单类型
```java
//单类型适配使用带有layout资源的构造方法,不要再重复调用addType()方法,实体类需要实现RvQuickInterface接口
RvQuickAdapter<Demo> adapter = new RvQuickAdapter<Demo>(TestApplication.getInst(), data, R.layout.item_quickadapter) {

            @Override
            public void bindData4View(RvViewHolder holder, Demo data, int pos, int type) {
               //绑定数据
                holder.setText(R.id.item_quickadapter_title, data.getmDemoTitle());
            }

            @Override
            public void bindListener4View(RvViewHolder holder, int type) {
                //不使用可不实现
            }
};
```
#多类型
```java
//多类型适配使用不带有layout资源的构造方法,调用addType()方法配置每种类型的layout资源,实体类需要实现RvQuickInterface接口
RvQuickAdapter<Demo> typeAdapter = new RvQuickAdapter<Demo>(TestApplication.getInst(), data) {

            @Override
            public void bindData4View(RvViewHolder holder, Demo data, int pos, int type) {
                //根据类型绑定数据
                switch (type) {
                    case Demo.CODE_DETAIL:
                        holder.setText(R.id.item_quickadapter_type_title, data.getmDemoTitle()).setText(R.id.item_quickadapter_desc, data.getmDescStr());
                        break;
                    case Demo.JUST_TEST:
                        holder.setText(R.id.item_quickadapter_title, data.getmDemoTitle());
                        break;
                }
            }
            @Override
            public void bindListener4View(RvViewHolder holder, int type) {
                 //不使用可不实现
            }
        };
typeAdapter.addType(Demo.CODE_DETAIL, R.layout.item_quickadapter_type)
                .addType(Demo.JUST_TEST, R.layout.item_quickadapter);
```



#兼容使用数组创建Adapter,兼容Java内置对象(String,Integer)类型创建Adapter
```java
//RvQuickModel是我内置的封装类,用来封装基本数据类型和java基本对象
String[] strs = new String[]{"a","a","a","a","a","a","a","a","a","a"};
RvQuickAdapter<RvQuickModel> adapter =
                new RvQuickAdapter<RvQuickModel>(this, RvConvertor.convert(strs)) {
      @Override
      public void bindData4View(RvViewHolder holder, RvQuickModel data, int pos, int type) {
                String s = data.<String>get();
      }
};

Demo[] demoarr = new Demo[]{};
RvQuickAdapter<Demo> adapter1 = new RvQuickAdapter<Demo>(this,demoarr) {
      @Override
      public void bindData4View(RvViewHolder holder, Demo data, int pos, int type) {

      }
};
```

#如何实现上拉加载
```java
//使用RvLoadMoreAdapter包装,可以包装RvHFQuickAdapter(添加Header,Footer)也可以包装RvQuickAdapter,因为他们都是BaseRvAdapter的实现类
//数据加载完毕之后调用rvLoadMoreAdapter.finishLoad();通知数据结束刷新,内部已经添加了防止数据重复获取的机制
//简单使用
RvLoadMoreAdapter rvLoadMoreAdapter = new RvLoadMoreAdapter(rvHFQuickAdapter);
RvLoadMoreAdapter rvLoadMoreAdapter = new RvLoadMoreAdapter(rvHFQuickAdapter, new RvLoadMoreAdapter.OnLoadMoreListener() {
            @Override
            public void onLoadMore() {
                for (int i = 0; i < 50; i++) {
                    demos.add(new Demo(i, i + " <- new"));
                }
                rvLoadMoreAdapter.notifyDataSetChanged();
                rvLoadMoreAdapter.finishLoad();
            }
});
```



#如何添加header和footer
```java
//使用RvHFAdapter包装RvQuickAdapter,使之具有添加Header和Footer的功能
RvHFAdapter rvHFQuickAdapter = new RvHFAdapter(rvQuickAdapter);

//使用View添加
rvHFQuickAdapter.addHeader(LayoutInflater.from(this).inflate(R.layout.rvquick_header, null));
rvHFQuickAdapter.addFooter(LayoutInflater.from(this).inflate(R.layout.rvquick_footer, null));

//使用资源添加
rvHFQuickAdapter.addHeader(R.layout.rvquick_header);
rvHFQuickAdapter.addFooter(R.layout.rvquick_footer);

//你可以继承重写方法使用RvViewHolder更简单的操作View,而不需要在获取很多控件更改他的显示,当然也可以不使用,一行代码实现就可以包装
RvHFAdapter rvHFQuickAdapter = new RvHFAdapter(rvQuickAdapter);
RvHFAdapter rvHFQuickAdapter = new RvHFAdapter(rvQuickAdapter){
            @Override
            public void bindLisAndData4Footer(RvViewHolder footer) {
                super.bindLisAndData4Footer(footer);
            }

            @Override
            public void bindLisAndData4Header(RvViewHolder header) {
                super.bindLisAndData4Header(header);
            }
        };

//设置监听事件时返回的pos是在整个布局中的位置,如果你设置了header,你点击获得的下标实际上并不是真正的下标
//使用rvHFQuickAdapter.getDataPos(pos)可以获得点击位置在datas中真正的数据,当然你也可以使用pos-1这样更直接粗暴的方法
rvQuickAdapter.setClickListener(new OnRecyclerItemClickListener<RvViewHolder>() {
      @Override
      public void onItemClick(int pos, RvViewHolder holder) {
          Toast.makeText(MainActivity.this, "点击" + rvQuickAdapter.getDataPos(pos), Toast.LENGTH_SHORT).show();
      }
});
```



#设置监听
```java
// 设置item监听事件
rvQuickAdapter.setClickListener(new OnRecyclerItemClickListener<RvViewHolder>() {
            @Override
            public void onItemClick(int pos, RvViewHolder holder) {

            }
        });
//设置item长按事件
rvQuickAdapter.setLongClickListenter(new OnRecyclerItemLongClickListener<RvViewHolder>() {
            @Override
            public void onItemLongClick(int pos, RvViewHolder holder) {

            }
        });
```



#adapterId区分adapter类型
```java
public int getAdapterId();

public void setAdapterId(int adapterId);

public boolean isThisAdapter(RvQuickAdapter adapter);
```


#给Item加分割线
```java
//你可以使用shape自定义分割线样式
Drawable line = ContextCompat.getDrawable(self,R.drawable.shape_line);
//两种布局格式使用不同的分割线
linear = new LinearDividerDecoration(self, LinearDividerDecoration.VERTICAL_LIST,line);
grid = new GridDividerDecoration(self,line);
//设置
mRv.addItemDecoration(linear);
```

#加载网络图片可以提前创建图片加载工具,然后调用ViewHodler.setImg()方法可以直接加载
```java
//你可以在Activity或者Application调用这段代码进行全局配置,第二次调用会将以前的设置覆盖,所以只需要执行一次
RvQuick.init(new RvQuick.QuickLoad() {
            @Override
            public void load(Context context, String url, ImageView view) {
                Log.e("chendong","加载图片");
                Glide.with(context).load("http://www.fresco-cn.org/static/fresco-logo.png").into(view);
            }
        });

//在adapter中使用
public RvViewHolder setImg(Context context, int resId, String url)
```

#更新使用方法
```java
//Demo类是我的实体类
//如果你使用的控件RvViewHolder没有为你集成,如何避免强转?使用泛型解决
holder.<Button>getView(R.id.abc).setText("");

//设置监听事件
public RvViewHolder setLis(int resId,View.OnClickListener listener,Object tag)//带有tag监听
public RvViewHolder setLis(int resId,View.OnClickListener listener)//不带tag监听
public RvViewHolder setTag(int resId, Object tag)//给控件设置tag
public <T> T getTag(int resId)//从RvViewHolder获取tag,包含泛型,你可以这样holder.<Demo>getTag(R.id.xxx)

//如果你在控件中设置了tag,当你在适配器外部使用tag时务必使用改方法获取,用来替代view.getTag()方法,
包含泛型,你可以这样用 Quick.<Demo>getTagOutOfAdapter(listView);
RvQuick.getTagOutOfAdapter(View view)


//设置图片
public RvViewHolder setImg(int resId, Bitmap bit)
public RvViewHolder setImg(Context context, int resId, String url)
```