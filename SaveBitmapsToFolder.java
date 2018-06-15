public void getImagesToSave(List<Adjunto> list) throws IOException {
    List<String> namesList = new ArrayList<>();
    List<Bitmap> bitsList = new ArrayList<>();
    for (Adjunto ad : list) {
        namesList.add(System.currentTimeMillis() + "_" + ad.getNombre());
        Bitmap b = Utils.baseToBitmap(ad.getBase64());
        bitsList.add(b);
    }
    File folder = new File(App.getAppContext().getFilesDir(), App.IMAGE_FOLDER);
    if (folder.exists()) {
        File[] children = folder.listFiles();
        for (File f : children) {
            if (f.delete()) {
                Log.i("tmp_images_folder", "file deleted");
            }
        }
        savePhotos(folder, namesList, bitsList);
    } else {
        if (folder.mkdirs()) {
            savePhotos(folder, namesList, bitsList);
        }
    }
}

private void savePhotos(File folder, List<String> nms, List<Bitmap> bps) throws IOException {
    for (int i = 0; i < nms.size(); i++) {
        File photoPath = new File(folder, nms.get(i));
        FileOutputStream stream = new FileOutputStream(photoPath);
        bps.get(i).compress(Bitmap.CompressFormat.JPEG, 100, stream);
        stream.close();
    }
}