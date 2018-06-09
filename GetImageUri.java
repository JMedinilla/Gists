public static Uri getImageUri(Context context) {
    Uri uri;
    File file;
    file = new File(IMAGE_ABSOLUTE_PATH);
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
        uri = FileProvider.getUriForFile(context,
            context.getApplicationContext().getPackageName() + ".provider", file);
    } else {
        uri = Uri.fromFile(file);
    }
    return uri;
}