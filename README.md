#QuickRv
#compile 'com.march.quickrvlibs:quickrvlibs:1.0.2'
#简化RecyclerView使用
##[ListView GridView适配简化](https://github.com/chendongMarch/QuickAdapter)

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
```