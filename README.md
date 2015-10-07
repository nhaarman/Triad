Triad
=====
[ ![Download](https://api.bintray.com/packages/nhaarman/maven/Triad/images/download.svg) ](https://bintray.com/nhaarman/maven/Triad/_latestVersion)

[ ![In Progress](https://badge.waffle.io/nhaarman/triad.svg?label=in-progress&title=In Progress) ](http://waffle.io/nhaarman/triad)
[ ![Ready for Release](https://badge.waffle.io/nhaarman/triad.svg?label=next-release&title=Ready for Release) ](http://waffle.io/nhaarman/triad)

Triad is a tiny Android library which enables use of the Model-View-Presenter pattern in an easy way.
It uses custom Views to replace the [dreaded Fragments](https://corner.squareup.com/2014/10/advocating-against-android-fragments.html), and introduces Presenter classes to separate business logic from view logic.
Since the Presenters are plain Java objects, tests for these classes can run blazingly fast on a local JVM.

## Setup

Add the following to your dependencies in your `build.gradle` file:

```groovy
repositories {
  jcenter()
}

dependencies {
  compile 'com.nhaarman:triad:x.x.x'
  compile 'com.nhaarman:triad-appcompat-v7:x.x.x' // Optional; For use with appcompat-v7 libraries.
}
```

## Classes

A simple screen in an Android application that uses Triad consists of four classes: a `Screen` which defines the `View` and `Presenter`, a `Presenter` which handles logic formats data for the view, a `Container` which acts as an interface between the `Presenter` and the `View`, and the `View` class which displays data to the user.

### Screen

```java
public class MyScreen extends Screen<ApplicationComponent> {

  @Override
  protected int getLayoutResId() {
    return R.layout.view_my;
  }

  @Override
  public <P extends Presenter<?, ?>> Presenter<?, ?> createPresenter(@NonNull final Class<P> presenterClass) {
    if (presenterClass.equals(MyPresenter.class) {
      return new MyPresenter();
    }
    
    throw new AssertionError("Unknown Presenter class: " + presenterClass);
  }
}
```
### View

```java
public class MyView extends RelativeLayoutContainer<ActivityComponent, MyPresenter, MyContainer> implements MyContainer {

  @InjectView(R.id.view_my_textview)
  protected TextView mTextView;

  public MyView(Context context, AttributeSet attrs) {
    super(context, attrs, MyPresenter.class);
  }

  public MyView(Context context, AttributeSet attrs, int defStyleAttr) {
    super(context, attrs, defStyleAttr, MyPresenter.class);
  }

  @Override
  public void setText(String text) {
    mTextView.setText(text);
  }
}
```

```xml
<?xml version="1.0" encoding="utf-8"?>
<com.example.MyView xmlns:android="http://schemas.android.com/apk/res/android"
  android:layout_width="match_parent"
  android:layout_height="match_parent">

  <TextView
    android:id="@+id/view_my_textview"
    android:layout_width="wrap_content"
    android:layout_height="wrap_content"
    android:layout_centerInParent="true" />

</com.example.MyView>
```

### Container

```java
interface MyContainer extends Container<ActivityComponent> {

  void setText(String text);
}
```

### Presenter

```java
class MyPresenter extends Presenter<ActivityComponent, MyContainer> {

  @Override
  public void onControlGained(MyContainer container, ActivityComponent activityComponent) {
    container.setText("Hello world!");
  }
}
```

For more information, see the [Wiki](https://github.com/nhaarman/Triad/wiki).

## Flow
Navigating through screens is based on earlier versions of Square's [Flow](https://github.com/square/flow).

## License

    Copyright 2015 Niek Haarman

    Licensed under the Apache License, Version 2.0 (the "License");
    you may not use this file except in compliance with the License.
    You may obtain a copy of the License at

       http://www.apache.org/licenses/LICENSE-2.0

    Unless required by applicable law or agreed to in writing, software
    distributed under the License is distributed on an "AS IS" BASIS,
    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
    See the License for the specific language governing permissions and
    limitations under the License.
