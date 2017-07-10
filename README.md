# RxAndroid-Examples - Reactive Extensions for Android
This module adds a number of classes to RxJava that make writing reactive components in 
Android applications easy and hassle free. More specifically, it

- provides a `Scheduler` that schedules an `Observable` on a given Android `Handler` thread, particularly the main UI thread
- provides base `Observer` implementations that make guarantees w.r.t. to reliable and thread-safe use throughout 
      `Fragment` and `Activity` life-cycle callbacks (coming soon)
- provides reusable, self-contained reactive components for common Android use cases and UI concerns

## Observing on the UI thread

One of the most common operations when dealing with asynchronous tasks on Android is to observe the task's
result or outcome on the main UI thread. Using vanilla Android, this would
typically be accomplished with an `AsyncTask`. With RxJava instead you would declare your `Observable`
to be observed on the main thread:

```java
public class ReactiveFragment extends Fragment {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Observable.from("one", "two", "three", "four", "five")
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(/* an Observer */);
    }
```

This will execute the Observable on a new thread, and emit results through `onNext` on the main UI thread.

## Observing on arbitrary threads
The previous sample is merely a specialization of a more general concept, namely binding asynchronous
communication to an Android message loop using the `Handler` class. In order to observe an `Observable`
on an arbitrary thread, create a `Handler` bound to that thread and use the `AndroidSchedulers.handlerThread`
scheduler:

```java
new Thread(new Runnable() {
    @Override
    public void run() {
        final Handler handler = new Handler(); // bound to this thread
        Observable.from("one", "two", "three", "four", "five")
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.handlerThread(handler))
                .subscribe(/* an Observer */)

        // perform work, ...
    }
}, "custom-thread-1").start();
```
## Reactive programming concepts
This will execute the Observable on a new thread and emit results through `onNext` on "custom-thread-1".
(This example is contrived since you could as well call `observeOn(Schedulers.currentThread())` but it
shall suffice to illustrate the idea.)

Reactive programming is an extension of the Observer software design pattern, where an object has a list of Observers that are dependent on it, and these Observers are notified by the object whenever it’s state changes.

There are two basic and very important items in reactive programming, Observables and Observers. Observables publish values, while Observers subscribe to Observables, watching them and reacting when an Observable publishes a value.

### In simpler terms:
- An Observable performs some action, and publishes the result.
- An Observer waits and watches the Observable, and reacts whenever the Observable publishes results.

There are three different changes that can occur on an Observable that the Observer reacts to. These are:
- Publishing a value
- Throwing an error
- Completed publishing all values

A class that implements the Observer interface must provide methods for each of the three changes above:
- An `onNext()` method that the Observable calls whenever it wishes to publish a new value
- An `onError()` method that’s called exactly once, when an error occurs on the Observable.
- An `onCompleted()` method that’s called exactly once, when the Observable completes execution.

So an Observable that has an Observer subscribed to it will call the Observer’s onNext() zero or more times, as long as it has values to publish, and terminates by either calling onError() or onCompleted().
