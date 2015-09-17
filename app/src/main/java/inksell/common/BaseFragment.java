package inksell.common;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import butterknife.ButterKnife;

/**
 * Created by Abhinav on 17/09/15.
 */
public abstract class BaseFragment extends Fragment {

    public abstract int getViewResId();

    public abstract void initFragment();

    public abstract void initView(View view);

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.initFragment();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState)
    {
        View view = inflater.inflate(getViewResId(), container, false);
        ButterKnife.inject(this, view);
        initView(view);
        return view;
    }
}
