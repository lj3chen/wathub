package io.github.wztlei.wathub.ui;

import android.graphics.Typeface;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.Toolbar;
import android.util.Patterns;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.view.View.*;
import android.widget.EditText;
import android.widget.Toast;

import butterknife.BindString;
import butterknife.BindView;
import io.github.wztlei.wathub.R;
import io.github.wztlei.wathub.ui.view.ElevationOffsetListener;
import io.github.wztlei.wathub.utils.FontUtils;
import io.github.wztlei.wathub.utils.Px;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

import io.github.wztlei.wathub.ui.modules.Feedback.feedbacksendout;

public class FeedbackActivity extends BaseActivity {
    private EditText feedbackname;
    private EditText feedbackinput;
    private EditText feedbackemail;

    @BindView(R.id.appbar)
    AppBarLayout mAppBarLayout;
    @BindView(R.id.collapsing_toolbar)
    CollapsingToolbarLayout mCollapsingLayout;
    @BindView(R.id.toolbar)
    Toolbar mToolbar;

    @BindString(R.string.menu_about)
    String mFeedbackString;

    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_feedback);

        mAppBarLayout.addOnOffsetChangedListener(new ElevationOffsetListener(Px.fromDpF(8)));

        final Typeface typeface = FontUtils.getFont(FontUtils.BOOK);
        mCollapsingLayout.setCollapsedTitleTypeface(typeface);
        mCollapsingLayout.setExpandedTitleTypeface(typeface);
        mCollapsingLayout.setTitle(mFeedbackString);

        setSupportActionBar(mToolbar);

        final ActionBar actionBar = getSupportActionBar();

        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setTitle(mFeedbackString);
            actionBar.setElevation(Px.fromDpF(8));
        }

        feedbackname = (EditText) findViewById(R.id.feedback_name);
        feedbackinput = (EditText) findViewById(R.id.feedback_input);
        feedbackemail = (EditText) findViewById(R.id.feedback_email);

        findViewById(R.id.fb_submit).setOnClickListener(
                new OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        checkInput();
                    }
                }
        );
    }

    @Override
    public void onBackPressed() {
        finish();
        overridePendingTransition(R.anim.stay, R.anim.bottom_out);
    }

    @Override
    public boolean onOptionsItemSelected(final MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            onBackPressed();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void checkInput() {
        if (feedbackinput.getText().toString().trim().length() == 0) {
            feedbackinput.setError("Please enter your feedback!");
            Toast.makeText(FeedbackActivity.this, "Please fill feedback!", Toast.LENGTH_LONG).show();
        }
        else {
            checkRest();
        }
    }

    private void checkRest() {
        if (feedbackname.getText().toString().trim().length() == 0) {
            String NN = "Null Name";
            feedbackname.setText(NN);
        }
        if (!Patterns.EMAIL_ADDRESS.matcher(feedbackemail.getText()).matches()) {
            String EM = "Null Email";
            feedbackemail.setText(EM);
        }
        sendout();
    }

    /*
    private void checkName() {
        if (feedbackname.getText().toString().trim().length() == 0) {
            feedbackname.setText("Null Name");
            checkEmail();
        }
        else {
            checkEmail();
        }
    }

    private void checkEmail() {
        if (Patterns.EMAIL_ADDRESS.matcher(feedbackemail.getText()).matches()) {
            sendout();
        }
        else {
            feedbackemail.setText("Null Email");
            sendout();
        }
    }
    */

    private void sendout() {

        Retrofit rf = new Retrofit.Builder()
                .baseUrl("https://docs.google.com/spreadsheets/d/1Pl5vnBZU7D1HWlvgM_Ocfa1eWxJ6uGW9zXLaLN6HaUQ/edit#gid=157590125")
                .build();

        String input = feedbackinput.getText().toString();
        String name = feedbackname.getText().toString();
        String email = feedbackemail.getText().toString();

        final feedbacksendout fbout = rf.create(feedbacksendout.class);

        Call<Void> fbcall = fbout.fbSend(input, name, email);
        fbcall.enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                //Log.d("XXX", "Submitted. " + response);
                Toast.makeText(FeedbackActivity.this,"Feedback Submitted!",Toast.LENGTH_LONG).show();
                feedbackinput.setText("");
                feedbackname.setText("");
                feedbackemail.setText("");
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                //Log.e("XXX", "Failed", t);
                Toast.makeText(FeedbackActivity.this,"Failed.",Toast.LENGTH_LONG).show();
            }}
        );
    }
}