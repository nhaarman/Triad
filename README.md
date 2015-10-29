Triad
=====
[ ![Download](https://api.bintray.com/packages/nhaarman/maven/Triad/images/download.svg) ](https://bintray.com/nhaarman/maven/Triad/_latestVersion)

[ ![In Progress](https://badge.waffle.io/nhaarman/triad.svg?label=in-progress&title=In Progress) ](http://waffle.io/nhaarman/triad)
[ ![Ready for Release](https://badge.waffle.io/nhaarman/triad.svg?label=next-release&title=Ready for Release) ](http://waffle.io/nhaarman/triad)

Triad is a tiny Android library which enables use of the Model-View-Presenter pattern in an easy way.
It uses custom Views to replace the [dreaded Fragments](https://corner.squareup.com/2014/10/advocating-against-android-fragments.html), and introduces Presenter classes to separate business logic from view logic.
Since the Presenters are plain Java objects, tests for these classes can run blazingly fast on a local JVM.

_Please note that Triad is still under development, and API's **may change**. Feel free to try it out and leave your feedback!_

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

## Getting Started

We're gonna create a little counter app to get ourselves started. Whenever we touch a button a counter is incremented on screen:

![Counter app](https://raw.githubusercontent.com/nhaarman/Triad/master/art/counter.png)

Every screen in an application that uses Triad is represented by a `Screen`. A screen can consist of multiple components, each backed by a `Presenter`. In this case, we only have one presenter. The presenter communicates with a custom view (`CounterView`) through an interface (`CounterContainer`).

### Screen

The `CounterScreen` class defines which layout to use, and instantiates the `CounterPresenter` class. The `CounterView` is automatically inflated and bound to the presenter.

```java
public class CounterScreen extends Screen<ApplicationComponent> {

  @Override
  protected int getLayoutResId() {
    return R.layout.view_counter;
  }

  @Override
  public Presenter<?, ?> createPresenter(@NonNull final Class<? extends Presenter<?,?>> presenterClass) {
    if (presenterClass.equals(CounterPresenter.class) {
      return new CounterPresenter();
    }
    
    throw new AssertionError("Unknown Presenter class: " + presenterClass);
  }
}
```
### View

The `CounterView` extends a `ViewGroup`, and reacts on user input of its children. The view notifies the presenter that something happened.

```java
public class CounterView extends RelativeLayoutContainer<CounterPresenter, ActivityComponent> implements CounterContainer {

  @Bind(R.id.countertv)
  protected TextView mCounterTV;

  public MyView(Context context, AttributeSet attrs) {
    super(context, attrs, CounterPresenter.class);
  }

  public MyView(Context context, AttributeSet attrs, int defStyleAttr) {
    super(context, attrs, defStyleAttr, CounterPresenter.class);
  }

  @Override
  public void setCounterText(final String counterText) {
    mCounterTV.setText(counterText);
  }
  
  @OnClick(R.id.incrementbutton)
  public void onIncrementButtonClicked() {
    getPresenter().onIncrementButtonClicked();
  }
}
```

The xml layout of the view is defined below. The root of the layout is a `CounterView`, and the `TextView` and `Button` are nested inside the `CounterView`.

```xml
<?xml version="1.0" encoding="utf-8"?>
<com.example.CounterView xmlns:android="http://schemas.android.com/apk/res/android"
  android:layout_width="match_parent"
  android:layout_height="match_parent">

  <TextView
    android:id="@+id/countertv"
    android:layout_width="wrap_content"
    android:layout_height="wrap_content"
    android:layout_centerInParent="true" />

  <Button
    android:id="@+id/incrementbutton"
    android:layout_width="wrap_content"
    android:layout_height="wrap_content"
    android:layout_below="@id/countertv"
    android:layout_centerHorizontal="true"
    android:text="@string/increment" />

</com.example.CounterView>
```

### Container

The `CounterContainer` acts as a separating layer between the presenter and view. This makes it possible to create different implementations of `CounterView` for different devices, such as phones or tablets.

```java
interface CounterContainer extends Container {

  void setCounterText(String counterText) {
}
```

### Presenter

Finally, the `CounterPresenter` handles any logic, and formats the data to display in the view. Presenters survive orientation changes, so our counter variable will not get lost on a configuration change.

```java
class MyPresenter extends Presenter<MyContainer, ActivityComponent> {

  private int mCounter;

  CounterPresenter() {
    mCounter = 0;
  }

  @Override
  protected void onControlGained(@NonNull final CounterContainer container, @NonNull final ActivityComponent activityComponent) {
    container.setCounterText(formatCounterText());
  }

  void onIncrementButtonClicked() {
    if (!container().isPresent()) {
      return;
    }

    mCounter++;
    container().get().setCounterText(formatCounterText());
  }

  @NonNull
  private String formatCounterText() {
    return String.valueOf(mCounter);
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
