## ItemHeaderAdapter
- 针对某个Header下有多个数据，类似微信朋友圈九宫格图片展示的好友动态列表

- 建议采用第二种方式。

- 目前支持Grid和Linear.瀑布流开发中

- 定义Header和Content实体

```java
    class ItemHeader extends RvQuickItemHeader {
        String title;

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public ItemHeader(String title) {
            this.title = title;
        }
    }

    static class Content implements RvQuickInterface {
        public static final int TYPE_CONTENT = 1;
        String title;

        public Content(String title) {
            this.title = title;
        }

        @Override
        public int getRvType() {
            return TYPE_CONTENT;
        }
    }
```


## 一、使用map的方式添加

```java
// 构建adapter
ItemHeaderAdapter<ItemHeader, Content> adapter =
        new ItemHeaderAdapter<ItemHeader, Content>(this) {
    @Override
    protected void onBindItemHeader(RvViewHolder holder, ItemHeader data, int pos, int type) {
        // 绑定Header显示
        holder.setText(R.id.info1, data.getTitle());
    }
    @Override
    protected void onBindContent(RvViewHolder holder, Content data, int pos, int type) {
        // 绑定内容数据显示
        ViewGroup.LayoutParams layoutParams = holder.getParentView().getLayoutParams();
        layoutParams.height = (int) (getResources().getDisplayMetrics().widthPixels / 3.0f);
    }
};
// 添加header布局
adapter.addHeaderLayout(R.layout.item_header_header);
// 添加类型map,参考TypeRvAdapter
adapter.addType(Content.TYPE_CONTENT, R.layout.item_header_content);
// 构建数据
List<Content> contents = new ArrayList<>();
for (int i = 0; i < 7; i++) {
    contents.add(new Content("this is " + i));
}
adapter.addData(new ItemHeader("title_1"), contents)
        .addData(new ItemHeader("title_2"), contents)
        .addData(new ItemHeader("title_3"), contents)
        .addData(new ItemHeader("title_4"), contents)
        .addData(new ItemHeader("title_5"), contents);
```


##  二、使用ItemHeaderRule自动生成Header

- ItemHeaderAdapter继承自TypeRvAdapter，可以适应多种数据展示。

- 针对有序数据。

- 我们之所以添加添加Header很多情况下是数据之间存在一些关系需要分类展示，采用配置规则（ItemHeaderRule）的方式可以自动生成Header.

```java
// rule接口，他决定了是否需要生成Header和生成什么样的Header
public interface ItemHeaderRule<IH, ID> {

    IH buildItemHeader(int currentPos,ID preData, ID currentData, ID nextData);

    boolean isNeedItemHeader(int currentPos,ID preData, ID currentData, ID nextData);
}
```

- 针对多种情况提供了多种构造方法,下面只有针对List的实现，针对数组也有相似构造方法。

- 当内容只有一种类型时，使用`public ItemHeaderAdapter(Context context, List<ID> originDatas, int headerLayoutId, int contentLayoutId)`，此时实体类（Content）不需要实现QuickRvInterface

- 当内容需要分类型展示时，使用`public ItemHeaderAdapter(Context context, List<ID> originDatas, int headerLayoutId)`，然后调用`addType()`配置多类型展示,此时实体类（Content）需要实现QuickRvInterface

- 如何配置规则?

```java
adapter.addItemHeaderRule(new ItemHeaderRule<ItemHeader, Content>() {
            @Override
            public ItemHeader buildItemHeader(int currentPos, Content preData, Content currentData, Content nextData) {
                // 使用数据，创建一个ItemHeader,他应该实现RvQuickItemHeader接口
                return new ItemHeader("pre is " + getIndex(preData) + " current is " + getIndex(currentData) + " next is " + getIndex(nextData));
            }

            @Override
            public boolean isNeedItemHeader(int currentPos, Content preData, Content currentData, Content nextData) {
                // 针对当前数据，前一个数据，后一个数据进行比较决定此时需不需要创建Header
                // currentPos = 0时，preData为null
                // currentPos = list.size()-1时，nextData为null
                Log.e("chendong", getString(preData) + "  " + getString(currentData) + "  " + getString(nextData));
                return currentData.index % 5 == 0;
            }
        });
```


- 数据更新

```java
public void updateDataAndItemHeader(List<ID> data)
```




