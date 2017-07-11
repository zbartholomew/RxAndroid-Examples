package com.zachbartholomew.rxandroidsimpleexample;

import android.databinding.DataBindingUtil;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import com.zachbartholomew.rxandroidsimpleexample.databinding.ActivitySampleRx1Binding;

import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.Observer;
import io.reactivex.annotations.NonNull;
import io.reactivex.disposables.Disposable;

/**
 * This sample activity is the quintessential Hello World.
 * Create an Observable that retrieves a string from a TextView,
 * and publishes this string to an Observer. This Observer then outputs the string to a TextBlock.
 */

public class SampleRxActivity1 extends AppCompatActivity {

    ActivitySampleRx1Binding mRx1Binding;
    Observable<String> myObservable;
    Observer<String> myObserver;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTitle("Activity 1");
        createObservableAndObserver();

        mRx1Binding = DataBindingUtil.setContentView(this, R.layout.activity_sample_rx1);

        // Linking Observable and Observer
        // Now that we’ve created both the Observable and Observer, we have to link them,
        // so that the Observable knows to publish to the Observer.
        // This is done with the Observable’s subscribe() method,
        // which is called whenever the “Subscribe” button is clicked.
        mRx1Binding.buttonSubscribe.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                myObservable.subscribe(myObserver);
            }
        });
    }

    private void createObservableAndObserver() {

        // Creating an Observable
        // We’ve elected to create our Observable using the static Observable.create() method.
        // There are a ton of different ways to create Observables.
        // The Observable’s onSubscribe() method calls the Subscriber’s onNext(),
        // followed immediately by onCompleted().
        // (A Subscriber is a class that implements the Observer interface).
        myObservable = Observable.create(
                new ObservableOnSubscribe<String>() {
                    @Override
                    public void subscribe(@NonNull ObservableEmitter<String> sub) throws Exception {
                        sub.onNext(mRx1Binding.editText.getText().toString());
                        sub.onComplete();
                    }
                }
        );

        // Creating an Observer
        // Creating our Observer is pretty straightforward, as seen in the code chunk below.
        // We simply override the Observer interface methods.
        // Notice that we have empty implementations for both onCompleted and onError.
        myObserver = new Observer<String>() {
            @Override
            public void onSubscribe(@NonNull Disposable d) {

            }

            @Override
            public void onNext(@NonNull String s) {
                mRx1Binding.textView.setText(s);
            }

            @Override
            public void onError(@NonNull Throwable e) {

            }

            @Override
            public void onComplete() {

            }
        };

    }
}
