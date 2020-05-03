# recyclerviewHelper
Recyclerview上拉加载更多的工具封装类
# 说明  
recyclerviewHelper是一个为Recyclerview实现上拉加载显示效果的工具类，该类通过装饰者模式的编成思想，将上拉加载更多的效果的实现代码从适配Recyclerview数据的Recyclerview.Adapter中抽取出来，这样就使得，当我们想要为Recyclerview添加上拉加载更多的效果的时候，就不需要在原来的数据适配器中加上实现代码了。这样做的好处是避免了我们在实际开发的过程中重复的造车轮的现象的发生。如果，我们想要实现上拉加载更多的效果的时候，我们在数据适配器的外面给它加上就行了，而数据适配器只负责对数据进行适配和绑定，这样也使得数据适配器的代码变得更加的简洁。  
# 集成方式
* Gradle  
```java
dependencies {
	        implementation 'com.github.hanjie511:recyclerviewHelper:v1.0.0'
	}
```  
* maven  
```java
<repositories>
		<repository>
		    <id>jitpack.io</id>
		    <url>https://jitpack.io</url>
		</repository>
	</repositories>
  <dependency>
	    <groupId>com.github.hanjie511</groupId>
	    <artifactId>recyclerviewHelper</artifactId>
	    <version>v1.0.0</version>
	</dependency>
  ```
  # 如何使用
  * step 1  
  创建你的Recyclerview的RecyclerView.Adapter
  ```java
  MyRecyclerViewAdapter myRecyclerViewAdapter = new MyRecyclerViewAdapter(dataList);
  ```  
  * step 2  
  通过调用RecyclerViewHelper工具类对Recyclerview的RecyclerView.Adapter进行装饰  
  ```java
   RecyclerViewHelper recyclerViewHelper = new RecyclerViewHelper(myRecyclerViewAdapter);
   ```  
   * step3   
   将RecyclerView的适配器设置为RecyclerViewHelper并设置滚动监听  
   ```java
   recyclerView.setAdapter(recyclerViewHelper);
   recyclerView.addOnScrollListener(new RecyclerViewHelper.RecyclerViewScrollListener() {
            @Override
            public void loadMore() {//开始加载数据的回调方法
                recyclerViewHelper.setLoadingState(recyclerViewHelper.isLoading);//设置正在加载数据的状态
                recyclerViewHelper.setLoadingState(recyclerViewHelper.isLoadindComplete);//设置数据加载完成的状态
                recyclerViewHelper.setLoadingState(recyclerViewHelper.isLoadindEnd);//设置没有更多数据可以加载的状态
            }
        });
```  
#下拉刷新效果如何实现的
用了google官方提供的SwipeRefreshLayout来实现下拉刷新的效果，由于实现过程简单，直接上代码：  
* 布局文件 
```java
<androidx.swiperefreshlayout.widget.SwipeRefreshLayout
        app:layout_constraintTop_toBottomOf="@id/toolbar"
        app:layout_constraintLeft_toLeftOf="parent"
        app:layout_constraintRight_toRightOf="parent"
        android:layout_width="match_parent"
        android:layout_height="match_parent"
        android:id="@+id/swipeRefreshLayout"
        >
    <androidx.recyclerview.widget.RecyclerView
        app:layout_constraintTop_toBottomOf="@id/toolbar"
        android:id="@+id/recyclerview"
        android:layout_width="match_parent"
        android:layout_height="match_parent"
         />
    </androidx.swiperefreshlayout.widget.SwipeRefreshLayout> 
```  
* 代码  
```java
 swipeRefreshLayout = findViewById(R.id.swipeRefreshLayout);
 swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {//设置下拉刷新的监听
            @Override
            public void onRefresh() {//开始刷新的回调方法
                /**
                  *获取数据的相关代码
                  */
                swipeRefreshLayout.setRefreshing(false);//数据加载完毕关闭刷新效果
            }
        });
```  


# 节目效果
