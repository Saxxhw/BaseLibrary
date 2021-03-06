package com.saxxhw.state;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.saxxhw.R;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class StateRelativeLayout extends RelativeLayout implements StateLayout {

    private static final String TAG_LOADING = "ProgressActivity.TAG_LOADING";
    private static final String TAG_EMPTY = "ProgressActivity.TAG_EMPTY";
    private static final String TAG_ERROR = "ProgressActivity.TAG_ERROR";

    final String CONTENT = "type_content";
    final String LOADING = "type_loading";
    final String EMPTY = "type_empty";
    final String ERROR = "type_error";

    LayoutInflater inflater;
    View view;
    LayoutParams layoutParams;
    Drawable currentBackground;

    List<View> contentViews = new ArrayList<>();

    RelativeLayout loadingStateRelativeLayout;

    RelativeLayout emptyStateRelativeLayout;
    TextView emptyStateTitleTextView;
    TextView emptyStateContentTextView;

    RelativeLayout errorStateRelativeLayout;
    TextView errorStateTitleTextView;
    TextView errorStateContentTextView;

    private String state = CONTENT;

    public StateRelativeLayout(Context context) {
        super(context);
    }

    public StateRelativeLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public StateRelativeLayout(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init();
    }

    private void init() {
        inflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        currentBackground = this.getBackground();
    }

    @Override
    public void addView(View child, int index, ViewGroup.LayoutParams params) {
        super.addView(child, index, params);
        if (child.getTag() == null || (!child.getTag().equals(TAG_LOADING) && !child.getTag().equals(TAG_EMPTY) && !child.getTag().equals(TAG_ERROR))) {
            contentViews.add(child);
        }
    }

    /**
     * Hide all other states and show content
     */
    @Override
    public void showContent() {
        switchState(CONTENT, null, null, null, Collections.<Integer>emptyList());
    }

    /**
     * Hide content and show the progress bar
     */
    @Override
    public void showLoading() {
        switchState(LOADING, null, null, null, Collections.<Integer>emptyList());
    }

    /**
     * Show empty view when there are not data to show
     *
     * @param emptyTextTitle   Title of the empty view to show
     * @param emptyTextContent Content of the empty view to show
     * @param onClickListener  Listener of the error view button
     */
    @Override
    public void showEmpty(@Nullable String emptyTextTitle, @Nullable String emptyTextContent, OnClickListener onClickListener) {
        switchState(EMPTY, emptyTextTitle, emptyTextContent, onClickListener, Collections.<Integer>emptyList());
    }

    /**
     * Show error view with a button when something goes wrong and prompting the user to try again
     *
     * @param errorTextTitle   Title of the error view to show
     * @param errorTextContent Content of the error view to show
     */
    @Override
    public void showError(String errorTextTitle, String errorTextContent, OnClickListener onClickListener) {
        switchState(ERROR, errorTextTitle, errorTextContent, onClickListener, Collections.<Integer>emptyList());
    }

    /**
     * Get which state is set
     *
     * @return State
     */
    public String getState() {
        return state;
    }

    /**
     * Check if content is shown
     *
     * @return boolean
     */
    public boolean isContent() {
        return state.equals(CONTENT);
    }

    /**
     * Check if loading state is shown
     *
     * @return boolean
     */
    public boolean isLoading() {
        return state.equals(LOADING);
    }

    /**
     * Check if empty state is shown
     *
     * @return boolean
     */
    public boolean isEmpty() {
        return state.equals(EMPTY);
    }

    /**
     * Check if error state is shown
     *
     * @return boolean
     */
    public boolean isError() {
        return state.equals(ERROR);
    }

    private void switchState(String state, String errorText, String errorTextContent, OnClickListener onClickListener, List<Integer> skipIds) {
        this.state = state;

        switch (state) {
            case CONTENT:
                //Hide all state views to display content
                setContentState(skipIds);
                break;
            case LOADING:
                setLoadingState(skipIds);
                break;
            case EMPTY:
                setEmptyState(skipIds);

                emptyStateTitleTextView.setText(!TextUtils.isEmpty(errorText) ? errorText : getContext().getText(R.string.state_empty_message));
                emptyStateContentTextView.setText(!TextUtils.isEmpty(errorTextContent) ? errorTextContent : getContext().getText(R.string.state_empty_desc));
                emptyStateRelativeLayout.setOnClickListener(onClickListener);
                break;
            case ERROR:
                setErrorState(skipIds);

                errorStateTitleTextView.setText(R.string.state_error_message);
                errorStateContentTextView.setText(R.string.state_error_desc);
                errorStateRelativeLayout.setOnClickListener(onClickListener);
                break;
        }
    }

    private void setLoadingView() {
        if (loadingStateRelativeLayout == null) {
            view = inflater.inflate(R.layout.state_relative_layout_loading_view, null);
            loadingStateRelativeLayout = view.findViewById(R.id.relative_layout_progress);
            loadingStateRelativeLayout.setTag(TAG_LOADING);

            layoutParams = new LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
            layoutParams.addRule(CENTER_IN_PARENT);

            addView(loadingStateRelativeLayout, layoutParams);
        } else {
            loadingStateRelativeLayout.setVisibility(VISIBLE);
        }
    }

    private void setEmptyView() {
        if (emptyStateRelativeLayout == null) {
            view = inflater.inflate(R.layout.state_relative_layout_empty_view, null);
            emptyStateRelativeLayout = view.findViewById(R.id.relative_layout_empty);
            emptyStateRelativeLayout.setTag(TAG_EMPTY);

            emptyStateTitleTextView = view.findViewById(R.id.text_title);
            emptyStateContentTextView = view.findViewById(R.id.text_content);

            layoutParams = new LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
            layoutParams.addRule(CENTER_IN_PARENT);

            addView(emptyStateRelativeLayout, layoutParams);
        } else {
            emptyStateRelativeLayout.setVisibility(VISIBLE);
        }
    }

    private void setErrorView() {
        if (errorStateRelativeLayout == null) {
            view = inflater.inflate(R.layout.state_relative_layout_error_view, null);
            errorStateRelativeLayout = view.findViewById(R.id.relative_layout_error);
            errorStateRelativeLayout.setTag(TAG_ERROR);

            errorStateTitleTextView = view.findViewById(R.id.text_title);
            errorStateContentTextView = view.findViewById(R.id.text_content);

            layoutParams = new LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
            layoutParams.addRule(CENTER_IN_PARENT);

            addView(errorStateRelativeLayout, layoutParams);
        } else {
            errorStateRelativeLayout.setVisibility(VISIBLE);
        }
    }

    /**
     * Helpers
     */

    private void setContentState(List<Integer> skipIds) {
        hideLoadingView();
        hideEmptyView();
        hideErrorView();

        setContentVisibility(true, skipIds);
    }

    private void setLoadingState(List<Integer> skipIds) {
        hideEmptyView();
        hideErrorView();

        setLoadingView();
        setContentVisibility(false, skipIds);
    }

    private void setEmptyState(List<Integer> skipIds) {
        hideLoadingView();
        hideErrorView();

        setEmptyView();
        setContentVisibility(false, skipIds);
    }

    private void setErrorState(List<Integer> skipIds) {
        hideLoadingView();
        hideEmptyView();

        setErrorView();
        setContentVisibility(false, skipIds);
    }

    private void setContentVisibility(boolean visible, List<Integer> skipIds) {
        for (View v : contentViews) {
            if (!skipIds.contains(v.getId())) {
                v.setVisibility(visible ? View.VISIBLE : View.GONE);
            }
        }
    }

    private void hideLoadingView() {
        if (loadingStateRelativeLayout != null) {
            loadingStateRelativeLayout.setVisibility(GONE);
        }
    }

    private void hideEmptyView() {
        if (emptyStateRelativeLayout != null) {
            emptyStateRelativeLayout.setVisibility(GONE);
        }
    }

    private void hideErrorView() {
        if (errorStateRelativeLayout != null) {
            errorStateRelativeLayout.setVisibility(GONE);
        }
    }
}
