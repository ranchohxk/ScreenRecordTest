package com.supper.lupingdashi.fragment;

import android.content.Context;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.supper.lupingdashi.R;
import com.supper.lupingdashi.adapter.VideoAdapter;
import com.supper.lupingdashi.bean.VideoBean;
import com.supper.lupingdashi.utils.VideoUtils;
import com.supper.lupingdashi.view.DividerItemDecoration;
import com.supper.lupingdashi.view.OnItemClickListener;

import java.util.List;

public class VideoFragment extends Fragment {

    private static final String TAG = "VideoFragment";
    private Context mContext;
    public VideoAdapter mAdapter;
    public ProgressBar mProgressBar;

    private OnFragmentInteractionListener mListener;

    public VideoFragment() {
        // Required empty public constructor
    }


    public static VideoFragment newInstance(String param1, String param2) {
        VideoFragment fragment = new VideoFragment();

        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mContext = getActivity().getApplicationContext();
        mProgressBar = (ProgressBar) getActivity().findViewById(R.id.progressbar);
        //异步加载   手机视频比较多时，主线程加载容易阻塞
        new LoadVideoAsyncTask("loadvideoasyncstask").execute();


    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        return inflater.inflate(R.layout.fragment_video, container, false);
    }


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }


    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }

    private void initRecycleView(List<VideoBean> videoBeen) {
        /**
         * RecycleView配置
         */
        RecyclerView rececleView = (RecyclerView) getActivity().findViewById(R.id.recycleview);
        rececleView.setLayoutManager(new LinearLayoutManager(this.getActivity()));
        rececleView.setHasFixedSize(true);
        //分割线
        rececleView.addItemDecoration(new DividerItemDecoration(this.getActivity(),
                DividerItemDecoration.VERTICAL_LIST));
        mAdapter = new VideoAdapter(videoBeen);
        mAdapter.setmOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                VideoBean video = mAdapter.getItem(position);
                Log.d(TAG, "onItemClick!\n");
                //传递video 跳转 太大了  选择性传递
                if (video != null) {

                } else {
                    Toast.makeText(mContext, "video is null", Toast.LENGTH_SHORT).show();
                }
            }
        });
        rececleView.setAdapter(mAdapter);
    }

    public class LoadVideoAsyncTask extends AsyncTask<Void, Integer, List<VideoBean>> {
        private String name = "LoadVideoAsyncTask";

        public LoadVideoAsyncTask(String name) {
            this.name = name;
        }

        @Override
        protected void onPreExecute() {
            Log.d(TAG, "onPreExecute");
            super.onPreExecute();
            mProgressBar.setVisibility(View.VISIBLE);
        }

        @Override
        protected List<VideoBean> doInBackground(Void... params) {
            return VideoUtils.getList(mContext);
        }

        @Override
        protected void onPostExecute(List<VideoBean> videoBeen) {
            super.onPostExecute(videoBeen);
            mProgressBar.setVisibility(View.GONE);
            initRecycleView(videoBeen);

        }
    }
}
