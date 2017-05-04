package com.example.util;

import android.content.Context;
import android.util.Log;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Request.Method;
import com.android.volley.Response;
import com.android.volley.Response.ErrorListener;
import com.android.volley.Response.Listener;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.example.nyapp.MyVolley;

import java.util.Map;

/*
 * 封装网络的操作工具类，实现可以从网络获取或者上传数据
 * 
 */
public class HttpconnectionUtil {


    public interface ReturnResult2 {
        public void getResult(String result);
    }

    public static void uploadFile(final Context context, final ReturnResult2 callback, final int flag, final String url, final Map<String, String> params) {

//		  RequestQueue  queue=Volley.newRequestQueue(context);
        if (flag == Method.GET) {
//			  System.out.println("--GET方法被调用");
            StringRequest stringRequest = new StringRequest(Request.Method.GET, url, new Listener<String>() {

                @Override
                public void onResponse(String response) {
                    // TODO Auto-generated method stub
//						System.out.println("--结果被传递");
                    callback.getResult(response);
                }
            }, new ErrorListener() {

                @Override
                public void onErrorResponse(VolleyError error) {
                    // TODO Auto-generated method stub
                    try {
//							System.out.println("--error---------------->"+error);
                        Log.e("LOGIN-ERROR", error.getMessage(), error);
                        byte[] htmlBodyBytes = error.networkResponse.data;
                        Log.e("LOGIN-ERROR", new String(htmlBodyBytes), error);
//				        	System.out.println(error.networkResponse.statusCode+"----------------------->"+new String(htmlBodyBytes));
                    } catch (Exception e) {
                        // TODO: handle exception
                    } finally {
                        if (error.networkResponse == null) {
                            callback.getResult("error");
                        } else {
                            callback.getResult("");
                        }
                    }

                }
            });
            stringRequest.setRetryPolicy(new DefaultRetryPolicy(10000, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

            MyVolley.getRequestQueue().add(stringRequest);
        } else {
            StringRequest stringRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    callback.getResult(response);
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    try {
//			        		System.out.println("error----------------->"+error);
                        Log.e("LOGIN-ERROR", error.getMessage(), error);
                        byte[] htmlBodyBytes = error.networkResponse.data;
                        Log.e("LOGIN-ERROR", new String(htmlBodyBytes), error);
//				        	System.out.println(error.networkResponse.statusCode+"----------------------->"+new String(htmlBodyBytes));
                    } catch (Exception e) {
                        // TODO: handle exception
                    } finally {
                        if (error.networkResponse == null) {
                            callback.getResult("error");
                        } else {
                            callback.getResult("");
                        }
                    }
                }
            }) {
                @Override
                protected Map<String, String> getParams() {
//			            Map<String,String> params = new HashMap<String, String>();
//			            params.put("User_Name", "1");
//			            params.put("Mobile", "1");
//			            params.put("MessageContent", "1");
                    return params;
                }


            };
            stringRequest.setRetryPolicy(new DefaultRetryPolicy(30000, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
            MyVolley.getRequestQueue().add(stringRequest);
        }


    }


}
