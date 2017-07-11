# RxAndroidSimpleExample
## Activity 1
  - Creating an Observable
    - Created our Observable using the static Observable.create() method. There are a ton of different ways to create Observables, some of which we’ll get to later, and most of which you’ll discover based on your needs.
  - Creating an Observer
    - Creating our Observer is pretty straightforward. We simply override the Observer interface methods. Notice that we have empty implementations for both onCompleted and onError.
  - Linking Observable and Observer
    - Now that we’ve created both the Observable and Observer, we have to link them, so that the Observable knows to publish to the Observer. This is done with the Observable’s subscribe() method, which is called whenever the “Subscribe” button is clicked.
  - We have used reactive programming to fetch and display a string in an Android app. Now, this is a lot of work to display a string, and can be done with much more simplicity and elegance using `mRx1Binding.textView.setText(mRx1Binding.editText.getText().toString());`

## Activity 2
  - Asynchronous Example
  - In activity 1, the Observable fetched a string instantly from within the app. It is highly likely, however, that the Observable would need to perform some long running task such as fetching data from a remote server. For this next sample, we are going to pause for 3 seconds while in the Observable, and then modify the received string before publishing it to the Observer.
  - The Observer remains unchanged from the activity 1. It expects a string from the Observable, and publishes this string to the TextBlock.
  - Execute Observable on another thread
    - The subscribeOn() method above specifies the thread that the Observable runs on, which is set to the IO Scheduler (Schedulers.io()). The observeOn() method indicates what thread the Observer should execute on. By default, the Observer will be executed on the same thread as the Observable, but we have set the observeOn() thread to AndroidSchedulers.mainThread() (RxAndroid specific method that indicates the Android UI thread). With just those two lines, we have specified that the blocking/time consuming Observable execute in the background, and whenever it completes, the result should be displayed on the UI.
  - Introducing Operators
  ```java
  map(new Function<Integer, String>() {
      @Override 
        public String apply(@NonNull String s) throws Exception { 
          return String.valueOf(integer);
        } 
    });
```
  - In the snippet above, we are converting an Observable that publishes an Integer into an Observable that publishes a String. In the call method, we receive an Integer, and return the String equivalent. The map() method handles the creation of the relevant Observable.
  
## Activity 3
  - We will split a string into a list, then, for each item in the list, we would append it’s position in the list, and publish each item.
  
## Activity 4
  - Handling Errors
    - An error anywhere in between the source Observable and target Observer gets propagated directly to the Observer’s onError() method. The advantage of this is that there is one central place for error handling. No matter how complex your Observable tree gets, the error handling is done at the end, within the Observer’s onError().
  - Stopping Subscriptions
    - So far, we have gone by the assumption that our Observables will execute to completion, and publish all values to the Observer. This is not practicable in real life, since the user might close your app before your Observable has done executing. Also, your Observable can possibly never terminate, publishing values on a timer, or on receipt of data from some other source.
    - The subscribe() method calls the Observers OnSubscribe(). In Activity 4, we dispose of the subscription when the user clicks unsubscribe.
    
## Final Notes
Observers should be what reacts to mutations. You should avoid performing any cpu and/or network intensive tasks on an Observer. In fact, you should perform as little computation as possible in your Observers. All the hard work should be done in the Observable, while the Observer receives the results. 
    
