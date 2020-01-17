package com.example.androidnotestesttask.ui;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.widget.Toast;

import com.example.androidnotestesttask.App;
import com.example.androidnotestesttask.R;
import com.example.androidnotestesttask.Screens;
import com.example.androidnotestesttask.db.model.NoteModel;
import com.example.androidnotestesttask.ui.base.activity.BaseActivity;
import com.example.androidnotestesttask.ui.common.BackButtonListener;

import javax.inject.Inject;

import ru.terrakok.cicerone.Navigator;
import ru.terrakok.cicerone.NavigatorHolder;
import ru.terrakok.cicerone.Router;
import ru.terrakok.cicerone.android.SupportFragmentNavigator;
import ru.terrakok.cicerone.commands.BackTo;
import ru.terrakok.cicerone.commands.Command;
import ru.terrakok.cicerone.commands.Replace;

public class MainActivity extends BaseActivity {

    @Inject
    NavigatorHolder navigatorHolder;

    @Inject
    Router router;

    private final Navigator navigator = new SupportFragmentNavigator(getSupportFragmentManager(), R.id.fragmentContainer) {
        @Override
        protected Fragment createFragment(String screenKey, Object data) {
            switch (screenKey) {
                case Screens.FRAGMENT_NOTES:
                    return FragmentNotes.getNewInstance();
                case Screens.FRAGMENT_EDIT:
                    return FragmentEdit.getNewInstance((NoteModel) data);
            }
            return null;
        }

        @Override
        protected void setupFragmentTransactionAnimation(Command command, Fragment currentFragment, Fragment nextFragment, FragmentTransaction fragmentTransaction) {
            super.setupFragmentTransactionAnimation(command, currentFragment, nextFragment, fragmentTransaction);
            fragmentTransaction.setCustomAnimations(
                    R.animator.fade_in,
                    R.animator.fade_out,
                    R.animator.fade_in,
                    R.animator.fade_out);
        }

        @Override
        protected void showSystemMessage(String message) {
            Toast.makeText(MainActivity.this, message, Toast.LENGTH_SHORT).show();
        }

        @Override
        protected void exit() {
            finish();
        }

        @Override
        public void applyCommands(Command[] commands) {
            try {
                super.applyCommands(commands);
                getSupportFragmentManager().executePendingTransactions();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        App.INSTANCE.getAppComponent().inject(this);
        setContentView(R.layout.main);
        initButterKnife();
        if (savedInstanceState == null)
            navigator.applyCommands(new Command[]{new BackTo(null), new Replace(Screens.FRAGMENT_NOTES, null)});
    }

    @Override
    public void onBackPressed() {
        Fragment fragment = getSupportFragmentManager().findFragmentById(R.id.fragmentContainer);
        if (fragment != null
                && fragment instanceof BackButtonListener
                && ((BackButtonListener) fragment).onBackPressed()) {
            return;
        } else {
            super.onBackPressed();
        }
    }

    @Override
    protected void onResumeFragments() {
        if (navigatorHolder != null) {
            super.onResumeFragments();
            navigatorHolder.setNavigator(navigator);
        }
    }

    @Override
    protected void onPause() {
        if (navigatorHolder != null) navigatorHolder.removeNavigator();
        super.onPause();
    }
}
