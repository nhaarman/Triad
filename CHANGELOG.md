# Changelog

Version 0.7.1 *(2015-07-18)*
-----------------------------------

 * Updated `TriadAppCompatActivity`

Version 0.7.0 *(2015-07-11)*
-----------------------------------

 * Made `Presenter.getContainer()` return `Optional` instance.
 * Updated ButterKnife
 * Bugfixes

Version 0.6.0 *(2015-07-06)*
-----------------------------------

 * Use a custom version of Flow (now called Triad)
 * Code cleanup and optimizations
 * Don't require to specify an initial screen on startup

Version 0.5.1 *(2015-06-20)*
-----------------------------------

 * Made `FlowManager` public.
 * Code cleanup and optimizations

Version 0.5.0 *(2015-06-17)*
-----------------------------------

 * Removed dialogs to make place for a better implementation;
 * Made `TriadDelegate` public.


Version 0.4.0 *(2015-04-23)*
-----------------------------------

 * Use the new `AppCompatActivity` instead of `ActionBarActivity`;
 * Made `RelativeLayoutContainer` constructors public.

Version 0.3.0 *(2015-04-05)*
-----------------------------------

 * Provided `Flow` instance to `ScreenPresenters`
 * Added `OnScreenChangedListener`
 * Added `triad-appcompat-v7` which includes `TriadActionBarActivity`

Version 0.2.2 *(2015-03-30)*
-----------------------------------

 * Support API 14
 * Presenters will now receive a call to onControlLost when the Activity's onStop method is called


Version 0.2.1 *(2015-03-04)*
-----------------------------------

 * Fixed crash on devices running KitKat or lower
 * Moved screen initialization from onStart() to onPostCreate()
 * Fixed bug in sample application

Version 0.2.0 *(2015-02-25)*
-----------------------------------

 * Created sample application

Version 0.1.0 *(2015-02-22)*
-----------------------------------

 * First 0.x release.