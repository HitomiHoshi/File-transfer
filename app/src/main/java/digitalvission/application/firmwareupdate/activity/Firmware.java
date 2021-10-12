package digitalvission.application.firmwareupdate.activity;

import android.app.DownloadManager;
import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.io.IOException;

import digitalvission.application.firmwareupdate.data.Constant;
import digitalvission.application.firmwareupdate.databinding.FirmwarePageBinding;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class Firmware  extends Fragment {

    private FirmwarePageBinding binding;

    Button getBleFile, sendBleFile, getWifiFile, sendWifiFile;
    EditText bleUrlText, wifiUrlText;

    @Override
    public View onCreateView(
            LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState
    ) {

        binding = FirmwarePageBinding.inflate(inflater, container, false);
        return binding.getRoot();

    }

    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        getBleFile = binding.getBleFileButton;
        sendBleFile = binding.sendBleFileButton;
        bleUrlText = binding.bleUrlText;

        getWifiFile = binding.getWifiFileButton;
        sendWifiFile = binding.sendWifiFileButton;
        wifiUrlText = binding.wifiUrlText;

        getBleFile.setOnClickListener(new View.OnClickListener() {


            @Override
            public void onClick(View view) {

                fileExists("firmwareBle.bin");

                DownloadManager.Request request = new DownloadManager.Request(Uri.parse(Constant.url + "firmware/file/ble"));
                request.setAllowedNetworkTypes(DownloadManager.Request.NETWORK_WIFI | DownloadManager.Request.NETWORK_MOBILE);

                request.setTitle("Download");
                request.setDescription("Downloading file...");

                request.allowScanningByMediaScanner();
                request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED);
                request.setDestinationInExternalPublicDir(Environment.DIRECTORY_DOWNLOADS, "" + "firmwareBle.bin");

                DownloadManager manager = (DownloadManager) requireActivity().getSystemService(Context.DOWNLOAD_SERVICE);
                manager.enqueue(request);
            }
        });

        sendBleFile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                File firmwareFile = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS),"" + "firmwareBle.bin");

                RequestBody requestBody=new MultipartBody.Builder().setType(MultipartBody.FORM)
                        .addFormDataPart("update",firmwareFile.getName(),RequestBody.create(MediaType.parse("image/*"),firmwareFile))
                        .build();

                Request request=new Request.Builder()
                        .url(bleUrlText.getText().toString())
                        .post(requestBody)
                        .build();

                OkHttpClient client = new OkHttpClient();
                client.newCall(request).enqueue(new Callback() {
                    @Override
                    public void onFailure(@NotNull Call call, @NotNull IOException e) {
                        e.printStackTrace();

                    }

                    @Override
                    public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                        Log.e("RESPONSE", "onResponse: " + response);

                    }
                });

                client.connectionPool().evictAll();
            }
        });

        getWifiFile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                fileExists("firmwareWifi.bin");

                DownloadManager.Request request = new DownloadManager.Request(Uri.parse(Constant.url + "firmware/file/wifi"));
                request.setAllowedNetworkTypes(DownloadManager.Request.NETWORK_WIFI | DownloadManager.Request.NETWORK_MOBILE);

                request.setTitle("Download");
                request.setDescription("Downloading file...");

                request.allowScanningByMediaScanner();
                request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED);
                request.setDestinationInExternalPublicDir(Environment.DIRECTORY_DOWNLOADS, "" + "firmwareWifi.bin");

                DownloadManager manager = (DownloadManager) requireActivity().getSystemService(Context.DOWNLOAD_SERVICE);
                manager.enqueue(request);
            }
        });

        sendWifiFile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                File firmwareFile = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS),"" + "firmwareWifi.bin");

                RequestBody requestBody=new MultipartBody.Builder().setType(MultipartBody.FORM)
                        .addFormDataPart("update",firmwareFile.getName(),RequestBody.create(MediaType.parse("image/*"),firmwareFile))
                        .build();

                Request request=new Request.Builder()
                        .url(wifiUrlText.getText().toString())
                        .post(requestBody)
                        .build();

                OkHttpClient client = new OkHttpClient();
                client.newCall(request).enqueue(new Callback() {
                    @Override
                    public void onFailure(@NotNull Call call, @NotNull IOException e) {
                        e.printStackTrace();

                    }

                    @Override
                    public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                        Log.e("RESPONSE", "onResponse: " + response);

                    }
                });

                client.connectionPool().evictAll();
            }
        });
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

    public void fileExists(String fileName){

        File file = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS),"" + fileName);

        boolean fileExists = file.exists();
        boolean isDirectory = file.isDirectory();
        boolean isFile = file.isFile();
        boolean canWrite = file.canWrite();
        boolean deleted = file.delete();
        Log.i("TAG", String.format( "File %s does%s exist, is%s a file, is%s writable, and has%s been deleted.", file.getAbsolutePath(), fileExists ? "" : " not", isFile ? "" : " not", canWrite ? "" : " not", deleted ? "" : " not"));

        if(file.exists()) {
            file.delete();
            Log.e("delete", String.valueOf(file.delete()));
        }
        else Log.e("delete","not exist");
    }
}