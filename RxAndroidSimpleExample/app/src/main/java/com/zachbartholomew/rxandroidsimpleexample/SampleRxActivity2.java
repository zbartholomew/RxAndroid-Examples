package com.zachbartholomew.rxandroidsimpleexample;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.TextView;

import com.zachbartholomew.rxandroidsimpleexample.databinding.ActivitySampleRx2Binding;

import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.annotations.NonNull;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

/**
 * In Activity 1, the Observable fetched a string instantly from within the app.
 * It is highly likely, however, that the Observable would need to perform some long running task
 * such as fetching data from a remote server. For this next sample, we are going to pause for
 * 3 seconds while in the Observable, and then modify the received string before publishing it
 * to the Observer.
 */

public class SampleRxActivity2 extends AppCompatActivity {

    ActivitySampleRx2Binding mRx2Binding;
    Observable<String> myObservable;
    Observer<String> myObserver;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTitle("Activity 2");
        createObservableAndObserver();

        mRx2Binding = DataBindingUtil.setContentView(this, R.layout.activity_sample_rx2);

        // The subscribeOn() method specifies the thread that the Observable runs on,
        // which is set to the IO Scheduler (Schedulers.io()) . The observeOn() method indicates
        // what thread the Observer should execute on. By default, the Observer will be executed on
        // the same thread as the Observable, but we have set the observeOn() thread to
        // AndroidSchedulers.mainThread()
        // (RxAndroid specific method that indicates the Android UI thread).
        // With just those two lines, we have specified that the blocking/time consuming Observable
        // execute in the background, and whenever it completes, the result should be
        // displayed on the UI.
        mRx2Binding.buttonSubscribe.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mRx2Binding.buttonSubscribe.setEnabled(false);
                myObservable.subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(myObserver);
            }
        });
    }

    private void createObservableAndObserver() {

        myObservable = Observable.create(
                new ObservableOnSubscribe<String>() {
                    @Override
                    public void subscribe(@NonNull ObservableEmitter<String> sub) throws Exception {

                        // execution is paused for 3000 milliseconds
                        try {
                            Thread.sleep(3000);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }

                        // The content of the EditText is split based on newlines.
                        // Each line is numbered, and then all the lines are published to the Observer.
                        String[] strings = mRx2Binding.editText.getText().toString().split("\n");
                        StringBuilder builder = new StringBuilder();

                        for (int i = 0; i < strings.length; i++) {
                            builder.append(i + 1).append(". ").append(strings[i]).append("\n");
                        }

                        sub.onNext(builder.toString());
                        sub.onComplete();
                    }
                }
        );

        // The Observer remains unchanged from Activity 1 sample.
        // It expects a string from the Observable, and publishes this string to the TextBlock.
        myObserver = new Observer<String>() {
            @Override
            public void onSubscribe(@NonNull Disposable d) {

            }

            @Override
            public void onNext(@NonNull String s) {
                ((TextView) findViewById(R.id.textView)).setText(s);
            }

            @Override
            public void onError(@NonNull Throwable e) {

            }

            @Override
            public void onComplete() {
                // allow the user to click the subscribe button again once task is complete
                mRx2Binding.buttonSubscribe.setEnabled(true);
            }
        };
    }
}
