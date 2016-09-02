
## Demo视频演示
- [视频演示](http://7xtjec.com1.z0.glb.clouddn.com/ittleQ.mp4)

## GitHub
- [GitHub地址      https://github.com/chendongMarch/QuickRv](https://github.com/chendongMarch/QuickRv)

## Gradle
- `compile 'com.march.quickrvlibs:quickrvlibs:2.1.1'`


## 配置图片加载
```java
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

```

## RvViewHolder的使用

- 为了简化数据的载入,使用RvViewHolder作为统一的ViewHolder,并提供了简单的方法


```java
//RvViewHolder已经提供部分简化的方法,可以使用连式编程快速显示数据而且有足够的扩展性

//获取控件
public <T extends View> T getView(int resId)
public <T extends View> T getView(String resId)
//设置可见
public RvViewHolder setVisibility(int resId, int v)
//设置背景
public RvViewHolder setBg(int resId, int bgRes)
//文字
public RvViewHolder setText(int resId, String txt)
//图片
public RvViewHolder setImg(int resId, int imgResId)
public RvViewHolder setImg(int resId, Bitmap bit)
public RvViewHolder setImg(Context context, int resId, String url)
public RvViewHolder setImg(Context context, int resId, String url,int w,int h,int placeHolder)
//监听
public RvViewHolder setClickLis(int resId, View.OnClickListener listener)
```


## 快速构建适配器
### 单类型
```java
//一个简单的实现,实体类不需要再去实现RvQuickInterface接口
SimpleRvAdapter simpleAdapter =
new SimpleRvAdapter<Demo>(self, demos, R.layout.rvquick_item_a) {
            @Override
            public void onBindView(RvViewHolder holder, Demo data, int pos) {
                holder.setText(R.id.item_a_tv, data.title);
            }
        };
```
### 多类型
```java
//调用addType()方法配置每种类型的layout资源
//实体类需要实现RvQuickInterface接口
TypeRvAdapter<Demo> typeAdapter =
new TypeRvAdapter<Demo>(context, data) {
            @Override
            public void onBindView(RvViewHolder holder, Demo data, int pos, int type) {
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

        };
typeAdapter.addType(Demo.CODE_DETAIL, R.layout.item_quickadapter_type)
                .addType(Demo.JUST_TEST, R.layout.item_quickadapter);
```
### 带有ItemHeader
- 每个Header下面由多个item。类似微信九宫格照片展示
- [Details](../ItemHeaderAdapter.md)

```java
// ItemHeader表示header的数据类型，Content表示内部数据的数据类型
ItemHeaderAdapter<ItemHeader, Content> adapter = new ItemHeaderAdapter<ItemHeader, Content>(this) {
    @Override
    protected void onBindItemHeader(RvViewHolder holder, ItemHeader data, int pos, int type) {
        //绑定每一项的 header
        holder.setText(R.id.info1, data.getTitle());
    }
    @Override
    protected void onBindContent(RvViewHolder holder, Content data, int pos, int type) {
        // 绑定每一个数据
        ViewGroup.LayoutParams layoutParams = holder.getParentView().getLayoutParams();
        layoutParams.height = (int) (getResources().getDisplayMetrics().widthPixels / 3.0f);
    }
};
// 添加 header的布局文件
adapter.addHeaderLayout(R.layout.item_header_header);
// 添加多种类型数据
adapter.addType(Content.TYPE_CONTENT, R.layout.item_header_content);
List<Content> contents = new ArrayList<>();
for (int i = 0; i < 7; i++) {
    contents.add(new Content("this is " + i));
}
// 添加数据时按照如下格式，一个header对应一个list表示该header下的数据
adapter.addData(new ItemHeader("title_1"), contents);
adapter.addData(new ItemHeader("title_2"), contents);
adapter.addData(new ItemHeader("title_3"), contents);
adapter.addData(new ItemHeader("title_4"), contents);

mRv.setAdapter(adapter);
```



## RvConverter 转换器
1. 解决Java内置对象(String,Integer这些对象是没有办法实现固定接口的)的数据转换问题，当需要对Java内置对象实现分类适配时使用。

2. 将Integer,String类型的list转换为List<RvQuickModel>
`public static <T> List<RvQuickModel> convert(List<T> list)`

3. 将Integer,String类型的数组转换为List<RvQuickModel>
`public static <T> List<RvQuickModel> convert(T[] list)`

4. 使用转换器将把指定对象包装成RvQuickModel,可以使用`public <T> T get()`获取包装的数据,这样对象就不需要实现固定接口了


```java
String[] strs = new String[]{"a","a","a","a","a","a","a","a","a","a"};
TypeRvAdapter<RvQuickModel> adapter =
      new TypeRvAdapter<RvQuickModel>(this, RvConverter.convert(strs)) {
      @Override
      public void onBindView(RvViewHolder holder, RvQuickModel data, int pos, int type) {
                String s = data.<String>get();
                ...
      }
};
```


## 两种监听事件
- 单击事件 和 长按事件,带有范型

```java
public void setOnItemClickListener(OnItemClickListener<D> mClickLis)

public void setOnItemLongClickListener(OnItemLongClickListener<D> mLongClickLis)

eg:
quickAdapter.setOnItemClickListener(new OnItemClickListener<Demo>() {
            @Override
            public void onItemClick(int pos, RvViewHolder holder, Demo data) {
                Toast.makeText(self, "click " + pos + "  " + data.toString(), Toast.LENGTH_SHORT).show();
            }
        });
```

## Module化编程

- 为了更好的扩展adapter，和实现功能的分离,每个模块负责自己的工作更加清晰

- 使用HFModule实现添加Header和Footer

- 使用LoadMoreModule实现预加载更多功能


## 添加Header和Footer

- Header和Footer的添加使用模块的方式,相关操作依赖于HFModule


- 资源Id设置(推荐使用这种方式,header 和 footer的数据配置可以在抽象方法中操作)

```java
HFModule hfModule =
    new HFModule(context, R.layout.header,R.layout.footer, recyclerView);
quickAdapter.addHeaderFooterModule(hfModule);
```

- 使用加载好的View设置

```java
View headerView = getLayoutInflater().inflate(R.layout.header, recyclerView,false)
View footerView = getLayoutInflater().inflate(R.layout.footer, recyclerView,false)
HFModule hfModule = new HFModule(headerView,footerView);
quickAdapter.addHFModule(hfModule);
```
- 抽象方法实现Header,Footer显示

```java
quickAdapter = new TypeRvAdapter<Demo>(self, demos) {
            @Override
            public void onBindHeader(RvViewHolder header) {
               //给Header绑定数据和事件,不需要可以不实现
            }

            @Override
            public void onBindFooter(RvViewHolder footer) {
               //给footer绑定数据和事件,不需要可以不实现
            }
        };
```
- 相关API

```java
quickAdapter.getHFModule().isHasHeader();
quickAdapter.getHFModule().isHasFooter();
// 隐藏和显示header和footer
quickAdapter.getHFModule().setFooterEnable(true);
quickAdapter.getHFModule().setHeaderEnable(true);
```

## 预加载

- 预加载模块,添加LoadMoreModule实现加载更多，当接近数据底部时会出发加载更多

- preLoadNum,表示提前多少个Item触发预加载，未到达底部时,距离底部preLoadNum个Item开始加载

- 每当到达底部时会触发加载，为防止多次加载，一次加载未完成时会禁止第二次加载，当加载结束之后调用finishLoad()，保证第二次加载可以进行。

```java
//方法
public void addLoadMoreModule(LoadMoreModule loadMore)

//当数据加载完毕时调用,才能使下次加载有效,防止重复加载
mLoadMore.finishLoad();


eg:
LoadMoreModule loadMoreModule =
    new LoadMoreModule(2, new OnLoadMoreListener() {
            @Override
            public void onLoadMore(final LoadMoreModule mLoadMore) {
                Log.e("chendong", "4秒后加载新的数据");
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        for (int i = 0; i < 10; i++) {
                            demos.add(new Demo(i, "new " + i));
                        }
                        mLoadMore.finishLoad();
                        //隐藏footer
                        quickAdapter.getHFModule().setFooterEnable(false);
                    }
                }, 4000);
            }
        });
quickAdapter.addLoadMoreModule(loadMoreModule);
```


## 使用adapterId

- 由于可以使用匿名内部类的形式快速实现,就无法通过类的instantOf方法区分,此时可以使用adapterId区分

```java
public int getAdapterId();

public void setAdapterId(int adapterId);

public boolean isUseThisAdapter(RecyclerView rv) {
        return ((RvAdapter) rv.getAdapter()).getAdapterId() == adapterId;
}
```

## 数据更新

- 针对某些时候`notifyDataSetChanged()`无效的问题

```java
public void updateData(List<D> data) {
        this.datas = data;
        notifyDataSetChanged();
}
```

## 举个例子
```java


//内部类实现
quickAdapter = new TypeRvAdapter<Demo>(self, demos) {
            @Override
            public void onBindView(RvViewHolder holder, Demo data, int pos, int type) {
               // 给控件绑定数据,必须实现
            }

            @Override
            public void onBindHeader(RvViewHolder header) {
               //给Header绑定数据和事件,不需要可以不实现
            }

            @Override
            public void onBindFooter(RvViewHolder footer) {
               //给footer绑定数据和事件,不需要可以不实现
            }
        };





//继承实现
public class MyAdapter extends TypeRvAdapter<Demo> {

    public MyAdapter(Context context, List<Demo> data) {
        super(context, data);
    }

    @Override
    public void onBindView(RvViewHolder holder, Demo data, int pos, int type) {
       // 给控件绑定数据,必须实现
    }

    @Override
    public void onBindHeader(RvViewHolder header) {
       //给Header绑定数据和事件,不需要可以不实现
    }

    @Override
    public void onBindFooter(RvViewHolder footer) {
       //给footer绑定数据和事件,不需要可以不实现
    }
}
```