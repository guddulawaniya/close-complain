package com.example.complaintclose.javafiles;

import static android.content.ContentValues.TAG;

import android.os.AsyncTask;
import android.util.Log;

import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;


public class ImageUploadTask extends AsyncTask<String, Integer, Void> {

    @Override
    protected Void doInBackground(String... params) {
        String imagePath = params[0];
        String serverUrl = config_file.Base_url + "imageupload.php?upload="; // Replace with the actual server URL

        try {
            File file = new File(imagePath);
            if (!file.exists()) {
                Log.e(TAG, "File not found: " + imagePath);
                return null;
            }

            // Open a connection
            URL url = new URL(serverUrl);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setDoOutput(true);
            connection.setRequestMethod("POST");
            connection.setRequestProperty("Content-Type", "multipart/form-data;boundary=" + "*****");

            // Create the multipart form data
            DataOutputStream outputStream = new DataOutputStream(connection.getOutputStream());
            outputStream.writeBytes("--*****\r\n");
            outputStream.writeBytes("Content-Disposition: form-data; name=\"file\";filename=\"" + file.getName() + "\"\r\n");
            outputStream.writeBytes("\r\n");

            // Read file data and write to the output stream
            FileInputStream fileInputStream = new FileInputStream(file);
            byte[] buffer = new byte[1024];
            int bytesRead, totalBytesRead = 0;
            int fileLength = (int) file.length();

            while ((bytesRead = fileInputStream.read(buffer)) != -1) {
                outputStream.write(buffer, 0, bytesRead);
                totalBytesRead += bytesRead;

                // Calculate and publish progress
                int progress = (int) ((totalBytesRead * 100) / fileLength);
                publishProgress(progress);
            }

            fileInputStream.close();

            outputStream.writeBytes("\r\n");
            outputStream.writeBytes("--*****--\r\n");
            outputStream.flush();
            outputStream.close();

            // Get the server response code
            int responseCode = connection.getResponseCode();
            Log.d(TAG, "Server Response Code: " + responseCode);
            if (responseCode == HttpURLConnection.HTTP_OK) {
                // The upload was successful, handle it accordingly
                Log.d(TAG, "Upload successful");
            } else {
                // The upload failed, handle it accordingly
                Log.e(TAG, "Upload failed. Response Code: " + responseCode);
            }
            // Handle the server response as needed

            connection.disconnect();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return null;
    }

    @Override
    protected void onProgressUpdate(Integer... values) {
        super.onProgressUpdate(values);
        int progress = values[0];
    }

    @Override
    protected void onPostExecute(Void aVoid) {
        super.onPostExecute(aVoid);
        // You can add a notification here to indicate that the upload is complete if needed
    }
}
