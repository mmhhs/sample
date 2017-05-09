package com.little.sample.util;

import android.content.Context;
import android.graphics.Bitmap;
import android.widget.ImageView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by mmh on 2017/5/8.
 */
public class VolleyUtil {
    private static VolleyUtil volleyUtil;
    private RequestQueue mRequestQueue;
    private Context context;//最好使用Application的上下文

    public VolleyUtil(Context context) {
        this.context = context;
        mRequestQueue = Volley.newRequestQueue(context);
    }

    public static synchronized VolleyUtil getInstance(Context context){
        if (volleyUtil==null){
            volleyUtil = new VolleyUtil(context);
        }
        return volleyUtil;
    }

    public RequestQueue getRequestQueue() {
        return mRequestQueue;
    }

    public <T> void add(Request<T> req, String tag) {
        req.setTag(tag);
        getRequestQueue().add(req);
    }

    public void cancelByTag(String tag) {
        mRequestQueue.cancelAll(tag);
    }



    public class CustomStringRequest extends StringRequest {

        public CustomStringRequest(int method, String url, Response.Listener<String> listener, Response.ErrorListener errorListener) {
            super(method, url, listener, errorListener);
        }

        private Map<String, String> headers = new HashMap<>();

        public void setCookies(List<String> cookies) {
            StringBuilder sb = new StringBuilder();
            for (String cookie : cookies) {
                sb.append(cookie).append("; ");
            }
            headers.put("Cookie", sb.toString());
        }

        @Override
        public Map<String, String> getHeaders() throws AuthFailureError {
            return headers;
        }

        private Priority mPriority;

        public void setPriority(Priority priority) {
            mPriority = priority;
        }

        @Override
        public Priority getPriority() {
            return mPriority == null ? Priority.NORMAL : mPriority;
        }
    }

    public class CustomJsonObjectRequest extends JsonObjectRequest {

        public CustomJsonObjectRequest(int method, String url, JSONObject jsonRequest,
                                       Response.Listener<JSONObject> listener, Response.ErrorListener errorListener) {
            super(method, url, jsonRequest, listener, errorListener);
        }

        private Map<String, String> headers = new HashMap<>();

        public void setCookies(List<String> cookies) {
            StringBuilder sb = new StringBuilder();
            for (String cookie : cookies) {
                sb.append(cookie).append("; ");
            }
            headers.put("Cookie", sb.toString());
        }

        @Override
        public Map<String, String> getHeaders() throws AuthFailureError {
            return headers;
        }

        private Priority mPriority;

        public void setPriority(Priority priority) {
            mPriority = priority;
        }

        @Override
        public Priority getPriority() {
            return mPriority == null ? Priority.NORMAL : mPriority;
        }
    }

    public class CustomImageRequest extends ImageRequest {

        public CustomImageRequest(String url, Response.Listener<Bitmap> listener, int maxWidth, int maxHeight,
                                  ImageView.ScaleType scaleType, Bitmap.Config decodeConfig, Response.ErrorListener errorListener) {
            super(url, listener, maxWidth, maxHeight, scaleType, decodeConfig, errorListener);
        }

        private Map<String, String> headers = new HashMap<>();

        public void setCookies(List<String> cookies) {
            StringBuilder sb = new StringBuilder();
            for (String cookie : cookies) {
                sb.append(cookie).append("; ");
            }
            headers.put("Cookie", sb.toString());
        }

        @Override
        public Map<String, String> getHeaders() throws AuthFailureError {
            return headers;
        }

        private Priority mPriority;

        public void setPriority(Priority priority) {
            mPriority = priority;
        }

        @Override
        public Priority getPriority() {
            return mPriority == null ? Priority.NORMAL : mPriority;
        }
    }


    public CustomStringRequest visit(int method, String url,String tag,final Map<String, String> params){
        CustomStringRequest customStringRequest = new CustomStringRequest(method, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        error.printStackTrace();
                    }
                }) {
                    @Override
                    protected Map<String, String> getParams()
                    {
                        return params;
                    }
                };
        add(customStringRequest,tag);
        return customStringRequest;
    }

    private void loadImage(String imageUrl,String tag) {
        // Retrieves an image specified by the URL, and displays it in the UI
        CustomImageRequest request = new CustomImageRequest(imageUrl,
                new Response.Listener<Bitmap>() {
                    @Override
                    public void onResponse(Bitmap bitmap) {

                    }
                }, 0, 0, ImageView.ScaleType.CENTER_CROP, Bitmap.Config.ARGB_8888,
                new Response.ErrorListener() {
                    public void onErrorResponse(VolleyError error) {

                    }
                });

        request.setPriority(Request.Priority.LOW);
        add(request, tag);
    }

}
