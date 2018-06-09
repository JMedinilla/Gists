/* CON LA GALERÍA FUNCIONA, CON LA CÁMARA NO */
String s = getRealPathFromUri(this, uri);
File file = new File(s);
FileInputStream stream = new FileInputStream(file);
ExifInterface exifInterface = new ExifInterface(stream);

public static String getRealPathFromUri(Context context, Uri contentUri) {
    Cursor cursor = null;
    try {
        String[] proj = {MediaStore.Images.Media.DATA};
        cursor = context.getContentResolver().query(contentUri, proj, null, null, null);
        int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
        cursor.moveToFirst();
        return cursor.getString(column_index);
    } finally {
        if (cursor != null) {
            cursor.close();
        }
    }
}