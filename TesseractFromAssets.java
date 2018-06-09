private String datapath;
private TessBaseAPI tessBaseAPI;

protected void onCreate(Bundle savedInstanceState) {
    datapath = getFilesDir() + "/tesseract/";
    checkTrained(new File(datapath + "tessdata/"));

    tessBaseAPI = new TessBaseAPI();
    tessBaseAPI.init(datapath, "spa");
}

private void checkTrained(File dir) {
    if (!dir.exists() && dir.mkdirs()) {
        copyTrained();
    }
    if (dir.exists()) {
        String datafilepath = datapath + "/tessdata/spa.traineddata";
        File datafile = new File(datafilepath);
        if (!datafile.exists()) {
            copyTrained();
        }
    }
}

private void copyTrained() {
    try {
        String filepath = datapath + "/tessdata/spa.traineddata";
        AssetManager assetManager = getAssets();

        InputStream inputStream = assetManager.open("tessdata/spa.traineddata");
        OutputStream outputStream = new FileOutputStream(filepath);

        byte[] buffer = new byte[1024];
        int read;
        while ((read = inputStream.read(buffer)) != -1) {
            outputStream.write(buffer, 0, read);
        }
        outputStream.flush();
        outputStream.close();
        inputStream.close();
    } catch (IOException e) {
        e.printStackTrace();
    }
}