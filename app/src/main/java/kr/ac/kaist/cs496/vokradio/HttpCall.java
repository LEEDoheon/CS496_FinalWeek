package kr.ac.kaist.cs496.vokradio;

import android.app.Activity;
import android.os.Build;
import android.text.TextUtils;
import android.util.Log;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class HttpCall extends Activity {
    private static GetExample getexample = new GetExample();
    private static PostExample postexample = new PostExample();
    private static DeleteExample deleteexample = new DeleteExample();
    private static PutExample putexample = new PutExample();
    private static statusPutExample statusputexample = new statusPutExample();
    private static imgPutExample imgputexample = new imgPutExample();

    private static File thumbnail = null;
    private static String method = "";
    private static String urltext = "";
    private static String email = "";
    private static String name = "";
    private static String job = "";
    private static String yearnumber = "";
    private static String password = "";
    private static String id = "";
    private static String title = "";
    private static String category = "";
    private static String day = "";
    private static String time = "";
    private static ArrayList<String> producer = new ArrayList<>();
    private static ArrayList<String> engineer = new ArrayList<>();
    private static ArrayList<String> anouncer = new ArrayList<>();
    private static ArrayList<String> songs = new ArrayList<>();
    private static String response = "";
    private static String status = "";
    private static String body = "";

    public static void setBody(String s) { body = s; }
    public static void setMethodtext(String s) { method = s; }
    public static void setUrltext(String s) { urltext = s; }
    public static void setEmailtext(String s) { email = s; }
    public static void setNametext(String s) { name = s; }
    public static void setJobtext(String s) { job = s; }
    public static void setYeartext(String s) { yearnumber = s; }
    public static void setPasswordtext(String s) { password = s; }
    public static void setIdtext(String s) { id = s; }
    public static void setTitletext(String s) { title = s; }
    public static void setCategorytext(String s) { category = s; }
    public static void setDaytext(String s) { day = s; }
    public static void setTimetext(String s) { time = s; }
    public static void setStatustext(String s) { status = s; }

    public static void setProducer(ArrayList<String> a) { producer = a; }
    public static void setEngineer(ArrayList<String> a) { engineer = a; }
    public static void setAnouncer(ArrayList<String> a) { anouncer = a; }
    public static void setSongs(ArrayList<String> a) { songs = a; }

    public static void setThumbnail(File f) {
        thumbnail = f;
    }

    public static class GetExample {
        OkHttpClient client = new OkHttpClient();

        String run(String url) throws IOException {
            Request request = new Request.Builder()
                    .url(url)
                    .build();

            Response response = client.newCall(request).execute();
            return response.body().string();
        }
    }

    public static class PostExample {
        public final MediaType JSON = MediaType.parse("application/json; charset=utf-8");

        OkHttpClient client = new OkHttpClient();

        String post(String url, String json) throws IOException {
            RequestBody formBody = RequestBody.create(JSON, json);
            Request request = new Request.Builder()
                    .url(url)
                    .post(formBody)
                    .build();
            Response response = client.newCall(request).execute();
            return response.body().string();
        }
    }

    public static class DeleteExample {
        OkHttpClient client = new OkHttpClient();
        
        String delete(String url) throws IOException {
            Request request = new Request.Builder()
                    .url(url)
                    .delete()
                    .build();
            Response response = client.newCall(request).execute();
            return response.body().string();
        }
    }

    public static class statusPutExample {
        OkHttpClient client = new OkHttpClient();

        String put(String url, String status, ArrayList<String> songs) throws IOException {
            RequestBody formBody;
            formBody = new MultipartBody.Builder()
                    .setType(MultipartBody.FORM)
                    .addFormDataPart("status", status)
                    .addFormDataPart("songs[]", TextUtils.join(",", songs))
                    .build();

            Request request = new Request.Builder().url(url).put(formBody).build();
            Response response = client.newCall(request).execute();
            if (!response.isSuccessful()) throw new IOException("Unexpected code " + response);
            return response.body().string();
        }
    }

    public static class imgPutExample {
        OkHttpClient client = new OkHttpClient();

        String put(String url, File file, String id) throws IOException {
            RequestBody formBody;
            if (file != null) {
                String filenameArray[] = file.getName().split("\\.");
                String ext = filenameArray[filenameArray.length - 1];
                formBody = new MultipartBody.Builder()
                        .setType(MultipartBody.FORM)
                        .addFormDataPart("id", id)
                        .addFormDataPart("thumbnail", file.getName(), RequestBody.create(MediaType.parse("image/" + ext), file))
                        .build();
            } else {
                formBody = new MultipartBody.Builder()
                        .setType(MultipartBody.FORM)
                        .build();
            }

            Request request = new Request.Builder().url(url).put(formBody).build();
            Response response = client.newCall(request).execute();
            if (!response.isSuccessful()) throw new IOException("Unexpected code " + response);
            return response.body().string();
        }
    }

    public static class PutExample {
        OkHttpClient client = new OkHttpClient();

        String put(String url, File file, String title, String category, String day, ArrayList<String> producer,
                   ArrayList<String> engineer, ArrayList<String> anouncer) throws IOException {
            RequestBody formBody;
            if (file != null) {
                String filenameArray[] = file.getName().split("\\.");
                String ext = filenameArray[filenameArray.length - 1];
                formBody = new MultipartBody.Builder()
                        .setType(MultipartBody.FORM)
                        .addFormDataPart("title", title)
                        .addFormDataPart("category", category)
                        .addFormDataPart("day", day)
                        .addFormDataPart("time", time)
                        .addFormDataPart("producer[]", TextUtils.join(",", producer))
                        .addFormDataPart("engineer[]", TextUtils.join(",", engineer))
                        .addFormDataPart("anouncer[]", TextUtils.join(",", anouncer))
                        .addFormDataPart("thumbnail", file.getName(), RequestBody.create(MediaType.parse("image/" + ext), file))
                        .build();
            } else {
                formBody = new MultipartBody.Builder()
                        .setType(MultipartBody.FORM)
                        .addFormDataPart("title", title)
                        .addFormDataPart("category", category)
                        .addFormDataPart("day", day)
                        .addFormDataPart("time", time)
                        .addFormDataPart("producer[]", TextUtils.join(",", producer))
                        .addFormDataPart("engineer[]", TextUtils.join(",", engineer))
                        .addFormDataPart("anouncer[]", TextUtils.join(",", anouncer))
                        .build();
            }

            Request request = new Request.Builder().url(url).put(formBody).build();
            Response response = client.newCall(request).execute();
            if (!response.isSuccessful()) throw new IOException("Unexpected code " + response);
            return response.body().string();
        }
    }

    public static String getResponse() {
        Log.d("METHOD", method);
        Log.d("URL", urltext);
        if (method.equals("GET")) {
            getexample = new GetExample();
            response = null;

            getThread mThread = new getThread();
            mThread.start();
            try {
                mThread.join();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            return mThread.getResponse();

        } else if (method.equals("POST")) {
            postexample = new PostExample();
            response = null;

            postThread mThread = new postThread();
            mThread.start();
            try {
                mThread.join();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            return mThread.getResponse();

        } else if (method.equals("DELETE")) {
            deleteexample = new DeleteExample();
            response = null;

            deleteThread mThread = new deleteThread();
            mThread.start();
            try {
                mThread.join();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            return mThread.getResponse();
        } else if (method.equals("PUT")) {
            putexample = new PutExample();
            response = null;

            putThread mThread = new putThread();
            mThread.start();
            try {
                mThread.join();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            return mThread.getResponse();
        } else if (method.equals("imgPUT")) {
            imgputexample = new imgPutExample();
            response = null;

            imgputThread mThread = new imgputThread();
            mThread.start();
            try {
                mThread.join();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            return mThread.getResponse();
        }

        return null;
    }

    public static class putThread extends Thread {
        static String response;

        @Override
        public void run() {
            try {
                response = putexample.put("http://52.78.17.108:8080" + urltext, thumbnail,
                        title, category, day, producer, engineer, anouncer);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        public String getResponse() {
            return response;
        }
    }

    public static class deleteThread extends Thread {
        static String response;

        @Override
        public void run() {
            try {
                response = deleteexample.delete("http://52.78.17.108:8080" + urltext);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        public String getResponse() {
            return response;
        }
    }

    public static class imgputThread extends Thread {
        static String response;

        @Override
        public void run() {
            try {
                response = imgputexample.put("http://52.78.17.108:8080" + urltext, thumbnail, id);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        public String getResponse() {
            return response;
        }
    }

    public static class postThread extends Thread {
        static String response;

        @Override
        public void run() {
            try {
                /*
                response = postexample.post("http://52.78.17.108:8080" + urltext, thumbnail, email, name, job, yearnumber, password, id,
                        title, category, day, producer, engineer, anouncer);
                        */
                response = postexample.post("http://52.78.17.108:8080" + urltext,body);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        public String getResponse() {
            return response;
        }
    }

    public static class getThread extends Thread {
        static String response;

        @Override
        public void run() {
            try {
                response = getexample.run("http://52.78.17.108:8080" + urltext);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        public String getResponse() {
            return response;
        }
    }
}