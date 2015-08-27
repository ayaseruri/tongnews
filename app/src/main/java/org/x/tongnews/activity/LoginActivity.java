package org.x.tongnews.activity;

import android.support.design.widget.TextInputLayout;

import com.dd.processbutton.iml.ActionProcessButton;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.ViewById;
import org.x.tongnews.R;

@EActivity(R.layout.activity_login)
public class LoginActivity extends BaseActivity {

    @ViewById(R.id.email)
    TextInputLayout emailTIL;
    @ViewById(R.id.password)
    TextInputLayout passwordTIL;
    @ViewById(R.id.sign_in_button)
    ActionProcessButton signInButton;


    @AfterViews
    void init(){

    }

}

