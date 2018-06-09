/*
 * Manifest intent filter
 * (And Application class in name property)
*/
<intent-filter>
    <action android:name="ishere.amador.com.itishere.ErrorRestart" />
    <category android:name="android.intent.category.DEFAULT" />
    <data android:mimeType="text/plain" />
</intent-filter>

/*
 * Method for every Activity
*/
private void restartSecure(){
    Intent restartIntent = getApplicationContext().getPackageManager().getLaunchIntentForPackage(getApplicationContext().getPackageName() );
    final PendingIntent restart = PendingIntent.getActivity(getApplicationContext(), 0, restartIntent, PendingIntent.FLAG_ONE_SHOT);
    restartIntent.putExtra("error_restart","ERROR");
    restartIntent.setAction("com.example.ErrorRestart");

    Thread.setDefaultUncaughtExceptionHandler(new Thread.UncaughtExceptionHandler() {
        @Override
        public void uncaughtException(Thread thread, Throwable ex) {
            Log.e("", "restarting app");

            try {
                OutputStreamWriter outputStreamWriter = new OutputStreamWriter(getApplicationContext()
                    .openFileOutput("errorlog.txt", Context.MODE_PRIVATE));
                outputStreamWriter.write("Caida en: "+actualTag+" ");
                outputStreamWriter.write(ex.getMessage());
                outputStreamWriter.close();

            }catch (IOException e) {
                Log.e("Exception", "File write failed: " + e.toString());
            }

            AlarmManager manager = (AlarmManager) getApplicationContext().getSystemService(Context.ALARM_SERVICE);
            manager.set(AlarmManager.RTC, System.currentTimeMillis() + 1000, restart);
            finish();
            System.exit(2);
        }
    });
}

/*
 * For the first Activity of the project
*/
@Override
protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_splash_screen);

    if(getIntent().getExtras() == null) {
        loadApp();
    }else {
        DialogUtils.showRestarDialog(this, new DialogUtils.IDialogCommunication() {
            @Override
            public void onPositiveButtonPress() {
                sendResstarMail();
                loadApp();
            }
        });
    }
}

private void sendResstarMail() {
    try {
        InputStream inputStream = openFileInput("errorlog.txt");
        if (inputStream != null) {
            InputStreamReader inputStreamReader = new InputStreamReader(inputStream);
            BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
            String receiveString = "";
            StringBuilder stringBuilder = new StringBuilder();
            
            while ((receiveString = bufferedReader.readLine()) != null) {
                stringBuilder.append(receiveString);
            }
            inputStream.close();
            
            Email email = new Email();
            email.setFrom(Preferences.getEmail());
            email.setMsg("Envio de errores\r\n"+stringBuilder.toString());
            Bundle bundle = new Bundle();

            bundle.putParcelable(SendMailForResstarApp_Service.KEY_RECOVERY_EMAIL, email);
            Intent intent = new Intent(this, SendMailForRestatrApp_Service.class);
            intent.putExtras(bundle);
            startService(intent);
        }
    } catch(IOException ex){
    }
}