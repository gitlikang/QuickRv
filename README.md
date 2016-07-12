## QuickRv
## compile 'com.march.quickrvlibs:quickrvlibs:2.0.6-beta2'
## 该库的出现旨在简化RecyclerView适配器的实现



[TOC]

##目录
###1.RvViewHolder的使用
###2.RecyclerView的快速数据适配(兼容数组和list)
###3.RvConvertor 转换器
###4.两种监听事件
###5.添加Header和Footer
###6.如何实现上拉加载
###7.使用adapterId区分adapter类型
###8.举个例子

##1.RvViewHolder的使用
###为了简化数据的载入,使用RvViewHolder作为统一的ViewHolder,并提供了简单的方法
```
//RvViewHolder已经提供了大量简化的方法,可以使用连式编程快速显示数据而且有足够的扩展性


//首先由于每个人使用的库不一样,加载网络图片时,需要配置
//你可以在Activity或者Application调用这段代码进行全局配置,第二次调用会将以前的设置覆盖,所以只需要执行一次
RvQuick.init(new  QuickLoad() {
            @Override
            public void load(Context context, String url, ImageView view) {
                Glide.with(context).load(url).centerCrop().crossFade().into(view);
            }

            @Override
            public void loadSizeImg(Context context, String url, ImageView view, int w, int h, int placeHolder) {
                Glide.with(context).load(url).centerCrop().crossFade().into(view);
            }
        });
//在adapter中使用下面的方法加载网络图片
public RvViewHolder setImg(Context context, int resId, String url)


//设置可见
public RvViewHolder setVisibility(int resId, int visiable)
//针对checkBox
public RvViewHolder setChecked(int resId, boolean isChecked)
//背景
public RvViewHolder setBg(int resId, int bgRes) 
//文字
public RvViewHolder setText(int resId, String txt) 
public RvViewHolder setText(int resId, SpannableString txt)
//图片
public RvViewHolder setImg(int resId, int imgResId)
public RvViewHolder setImg(int resId, Bitmap bit) 
public RvViewHolder setImg(Context context, int resId, String url) 
//监听
public RvViewHolder setClickLis(int resId, View.OnClickListener listener) 
//tag,tagId必须是XML资源,例如R.String.key
public RvViewHolder setTag(int resId, Object tag) 
public RvViewHolder setTag(int resId,int tagId, Object tag)
//如果你使用的控件RvViewHolder没有为你集成,如何避免强转?使用泛型解决
holder.<Button>getView(R.id.abc).setText("");
```


##2.RecyclerView的快速数据适配(兼容数组和list)
###单类型
```java
//1.单类型适配使用带有layout资源的构造方法
//2.不要再重复调用addType()方法
//3.实体类需要实现RvQuickInterface接口
RvQuickAdapter<Demo> adapter = new RvQuickAdapter<Demo>(context, data, R.layout.item_test) {

            @Override
            public void bindData4View(RvViewHolder holder, Demo data, int pos, int type) {
               //绑定数据
                holder.setText(R.id.item_title, data.geTitle());
            }

            @Override
            public void bindListener4View(RvViewHolder holder, int type) {
                //不使用可不实现
            }
};
//一个简单的实现,实体类不需要再去实现RvQuickInterface接口
SimpleRvAdapter simpleAdapter = new SimpleRvAdapter<Demo>(self, demos, R.layout.rvquick_item_a) {
            @Override
            public void bindData4View(RvViewHolder holder, Demo data, int pos) {
                holder.setText(R.id.item_a_tv, data.title);
            }
        };

```
###多类型
```java
//1.多类型适配使用不带有layout资源的构造方法
//2.调用addType()方法配置每种类型的layout资源
//3.实体类需要实现RvQuickInterface接口
RvQuickAdapter<Demo> typeAdapter = new RvQuickAdapter<Demo>(context, data) {

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



##3.RvConvertor 转换器
###一方面解决Java内置对象(String,Integer这些对象是没有办法实现固定接口的)的数据转换问题,另一方面也给出单类型适配的新的解决方案
```java
public static <T> List<RvQuickModel> convert(List<T> list)
public static <T> List<RvQuickModel> convert(T[] list)
// RvQuickModel是内置的封装类,用来封装基本数据类型和java基本对象,
// 使用转换器将把指定对象包装成RvQuickModel,可以使用public <T> T get()获取包装的数据,这样对象就不需要实现固定接口了
String[] strs = new String[]{"a","a","a","a","a","a","a","a","a","a"};
RvQuickAdapter<RvQuickModel> adapter =
                new RvQuickAdapter<RvQuickModel>(this, RvConvertor.convert(strs)) {
      @Override
      public void bindData4View(RvViewHolder holder, RvQuickModel data, int pos, int type) {
                String s = data.<String>get();
      }
};
// 单类型适配
Demo[] demos = new Demo[10];
for...// 数据装填
RvQuickAdapter<RvQuickModel> adapter =
                new RvQuickAdapter<RvQuickModel>(this, RvConvertor.convert(demos)) {
      @Override
      public void bindData4View(RvViewHolder holder, RvQuickModel data, int pos, int type) {
                Demo d = data.<Demo>get();
      }
};

```


##4.两种监听事件
```
public void setOnItemClickListener(OnItemClickListener<RvViewHolder> mClickLis)

public void setOnItemLongClickListener(OnItemLongClickListener<RvViewHolder> mLongClickLis) 
```


##5.添加Header和Footer
```
// RvQuickAdapter可以使用一下方法添加header和footer
如:quickAdapter.addHeaderOrFooter(R.layout.header,R.layout.footer,recyclerView);
//使用View设置
quickAdapter.addHeaderOrFooter(
                getLayoutInflater().inflate(R.layout.rvquick_header, recyclerView,false)
                , getLayoutInflater().inflate(R.layout.rvquick_footer, recyclerView,false)
);
public void addHeaderOrFooter(View mHeader, View mFooter)
//使用资源设置,建议使用,然后在实现抽象方法设置header,footer的数据监听绑定
public void addHeaderOrFooter(int mHeaderRes, int mFooterRes,RecyclerView rv)

// 隐藏和显示header和footer
public void setFooterEnable(boolean footerEnable)
public void setHeaderEnable(boolean headerEnable)

// 获取header数目,添加header之后数据和listview一样,数据会错位
public int getHeaderCount()
```

##6.如何实现上拉加载
```java
//使用下面的方法启动加载更多
//preLoadNum 提前加载,未到达底部时,距离底部preLoadNum个项开始加载
//loadMoreListener 回调
public void addLoadMoreModule(int preLoadNum,
                                  LoadMoreModule.OnLoadMoreListener loadMoreListener){
    this.mLoadMoreMoudle = new LoadMoreModule();
    mLoadMoreMoudle.setLoadMore(preLoadNum,loadMoreListener);
}

//当数据加载完毕时调用,才能使下次加载有效,防止重复加载
public void finishLoad(){
        mLoadMoreMoudle.finishLoad();
}

eg:
quickAdapter.addLoadMoreModule(2, new LoadMoreModule.OnLoadMoreListener() {
            @Override
            public void onLoadMore() {
                Log.e("test","4秒后加载新的数据");
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        for (int i=0;i<10;i++){
                            demos.add(new Demo(i,"new "+i));
                        }
                        quickAdapter.notifyDataSetChanged();
                        quickAdapter.finishLoad();
                    }
                },4000);
            }
        });
```


##7.使用adapterId区分adapter类型
###由于可以使用匿名内部类的形式快速实现,就无法通过类的instant_of方法区分,此时可以使用adapterId区分
```java
public int getAdapterId();

public void setAdapterId(int adapterId);

public boolean isThisAdapter(RvQuickAdapter adapter);
```


##8.举个例子
```
//匿名内部类实现
quickAdapter = new RvQuickAdapter<Demo>(self, demos) {
            @Override
            public void bindData4View(RvViewHolder holder, Demo data, int pos, int type) {
               // 给控件绑定数据,必须实现
            }

            @Override
            public void bindListener4View(RvViewHolder holder, in super.bindListener4View(holder, type);
                //给控件绑定监听事件,不需要可以不实现
            }

            @Override
            public void bindLisAndData4Header(RvViewHolder header) {
               //给Header绑定数据和事件,不需要可以不实现
            }

            @Override
            public void bindLisAndData4Footer(RvViewHolder footer) {
               //给footer绑定数据和事件,不需要可以不实现
            }
        };
        
//继承实现
public class MyAdapter extends RvQuickAdapter<Demo> {
    
    public MyAdapter(Context context, List<Demo> data) {
        super(context, data);
    }

    @Override
    public void bindData4View(RvViewHolder holder, Demo data, int pos, int type) {
       // 给控件绑定数据,必须实现
    }
    
    @Override
    public void bindListener4View(RvViewHolder holder, in super.bindListener4View(holder, type);
        //给控件绑定监听事件,不需要可以不实现
    }
    
    @Override
    public void bindLisAndData4Header(RvViewHolder header) {
       //给Header绑定数据和事件,不需要可以不实现
    }
    
    @Override
    public void bindLisAndData4Footer(RvViewHolder footer) {
       //给footer绑定数据和事件,不需要可以不实现
    }
}
```