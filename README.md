#QuickRv


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