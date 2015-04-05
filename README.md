Triad
=====
[![Build Status](https://travis-ci.org/nhaarman/Triad.svg?branch=master)](https://travis-ci.org/nhaarman/Triad)
[ ![Download](https://api.bintray.com/packages/nhaarman/maven/Triad/images/download.svg) ](https://bintray.com/nhaarman/maven/Triad/_latestVersion) 

[![In Progress](https://badge.waffle.io/nhaarman/triad.svg?label=in-progress&title=In Progress)](http://waffle.io/nhaarman/triad)
[![Ready for Release](https://badge.waffle.io/nhaarman/triad.svg?label=next-release&title=Ready for Release)](http://waffle.io/nhaarman/triad)

Triad is an Android library which enables use of the Model-View-Presenter pattern in an easy way.

An application that uses Triad consists of a single `Activity` that extends `TriadActivity` or `TriadActionBarActivity` and hosts all screens in the app. Using Square's [Flow](https://github.com/square/flow/), Jake Wharton's [Butter Knife](https://github.com/JakeWharton/butterknife), and optionally [Dagger 2.0](https://github.com/google/dagger), creating an Android application has never been easier.

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

A screen in an Android application that uses Triad consists of four classes: a `Screen` which defines the `View` and `Presenter`, a `Presenter` which handles logic formats data for the view, a `Container` which acts as an interface between the `Presenter` and the `View`, and the `View` class which displays data to the user.

### Screen

```java
public class MyScreen extends Screen<MyPresenter, MyContainer, MainComponent> {

  @Override
  protected int getLayoutResId() {
    return R.layout.view_my;
  }

  @Override
  protected MyPresenter createPresenter(MainComponent mainComponent) {
    return new MyPresenter();
  }
}
```
### View

```java
public class MyView extends RelativeLayoutContainer<MyPresenter, MyContainer> implements MyContainer {

  @InjectView(R.id.view_my_textview)
  protected TextView mTextView;

  public MyView(Context context, AttributeSet attrs) {
    super(context, attrs);
  }

  public MyView(Context context, AttributeSet attrs, int defStyleAttr) {
    super(context, attrs, defStyleAttr);
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
interface MyContainer extends ScreenContainer<MyPresenter, MyContainer> {

  void setText(String text);
}
```

### Presenter

```java
class MyPresenter extends ScreenPresenter<MyPresenter, MyContainer> {

  @Override
  public void onControlGained(MyContainer container) {
    container.setText("Hello world!");
  }
}
```

For more information, see the [Wiki](https://github.com/nhaarman/Triad/wiki).


License
=======

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
