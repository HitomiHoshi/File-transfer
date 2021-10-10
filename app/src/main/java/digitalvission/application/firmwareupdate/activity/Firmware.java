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

    Button getFile, sendFile;
    EditText apiText;

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

        getFile = binding.getFileButton;
        sendFile = binding.sendFileButton;
        apiText = binding.apiText;

        getFile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DownloadManager.Request request = new DownloadManager.Request(Uri.parse(Constant.url + "firmware/file"));
                request.setAllowedNetworkTypes(DownloadManager.Request.NETWORK_WIFI | DownloadManager.Request.NETWORK_MOBILE);

                request.setTitle("Download");
                request.setDescription("Downloading file...");

                request.allowScanningByMediaScanner();
                request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED);
                request.setDestinationInExternalPublicDir(Environment.DIRECTORY_DOWNLOADS, "" + "firmware.bin");

                DownloadManager manager = (DownloadManager) requireActivity().getSystemService(Context.DOWNLOAD_SERVICE);
                manager.enqueue(request);
            }
        });
        sendFile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                File firmwareFile = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS),"" + "firmware.bin");

                RequestBody requestBody=new MultipartBody.Builder().setType(MultipartBody.FORM)
                        .addFormDataPart("firmware",firmwareFile.getName(),RequestBody.create(MediaType.parse("image/*"),firmwareFile))
                        .build();

                Request request=new Request.Builder()
                        .url(apiText.getText().toString())
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

}