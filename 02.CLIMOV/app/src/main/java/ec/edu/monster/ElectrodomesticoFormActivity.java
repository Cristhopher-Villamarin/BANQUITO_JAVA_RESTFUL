package ec.edu.monster;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Base64;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.widget.Toolbar;

import androidx.appcompat.app.AppCompatActivity;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;

import ec.edu.monster.db.HttpClientUtil;
import ec.edu.monster.model.Electrodomestico;
import ec.edu.monster.model.FotoUploadRequest;
import ec.edu.monster.model.FotoUploadResponse;
import ec.edu.monster.service.ElectrodomesticoService;
import ec.edu.monster.util.SessionManager;

public class ElectrodomesticoFormActivity extends AppCompatActivity {

    private EditText editNombre;
    private EditText editPrecio;
    private Button btnGuardar;
    private ProgressBar progressBar;
    private ImageView imgPreview;
    private Button btnSeleccionarImagen;
    private ElectrodomesticoService electrodomesticoService;
    private SessionManager sessionManager;
    private String action;
    private Integer id;
    private String currentFotoUrl;
    private String selectedImageBase64;

    private static final int REQUEST_IMAGE_PICK = 1001;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_electrodomestico_form);
        // En el método onCreate, después de setContentView
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
        }

        // Manejar el botón de retroceso
        toolbar.setNavigationOnClickListener(v -> onBackPressed());

        sessionManager = new SessionManager(this);

        // Verificar autenticación
        if (!sessionManager.isLoggedIn() || !sessionManager.isAdmin()) {
            startActivity(new Intent(this, LoginActivity.class));
            finish();
            return;
        }

        // Restaurar token de sesión
        String token = sessionManager.getToken();
        if (token != null) {
            ec.edu.monster.db.HttpClientUtil.setAuthToken(token);
        }

        electrodomesticoService = new ElectrodomesticoService();
        editNombre = findViewById(R.id.editNombre);
        editPrecio = findViewById(R.id.editPrecio);
        btnGuardar = findViewById(R.id.btnGuardar);
        progressBar = findViewById(R.id.progressBar);
        imgPreview = findViewById(R.id.imgPreview);
        btnSeleccionarImagen = findViewById(R.id.btnSeleccionarImagen);

        action = getIntent().getStringExtra("action");
        if ("editar".equals(action)) {
            id = getIntent().getIntExtra("id", 0);
            editNombre.setText(getIntent().getStringExtra("nombre"));
            editPrecio.setText(String.valueOf(getIntent().getDoubleExtra("precio", 0)));
            currentFotoUrl = getIntent().getStringExtra("fotoUrl");
            setTitle("Editar Electrodoméstico");
            TextView titleText = findViewById(R.id.titleText);
            if (titleText != null) {
                titleText.setText("Editar Electrodoméstico");
            }
            // Cargar imagen existente si hay URL
            if (currentFotoUrl != null && !currentFotoUrl.isEmpty()) {
                new LoadImageTask(imgPreview).execute(currentFotoUrl);
            }
        } else {
            setTitle("Nuevo Electrodoméstico");
        }

        btnSeleccionarImagen.setOnClickListener(v -> {
            Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
            intent.setType("image/*");
            startActivityForResult(Intent.createChooser(intent, "Seleccionar imagen"), REQUEST_IMAGE_PICK);
        });

        btnGuardar.setOnClickListener(v -> {
            String nombre = editNombre.getText().toString().trim();
            String precioStr = editPrecio.getText().toString().trim();

            if (nombre.isEmpty() || precioStr.isEmpty()) {
                Toast.makeText(this, "Complete todos los campos", Toast.LENGTH_SHORT).show();
                return;
            }

            try {
                double precio = Double.parseDouble(precioStr);
                if (selectedImageBase64 != null) {
                    // Subir imagen primero y luego crear/actualizar
                    new UploadAndSaveTask(nombre, precio).execute();
                } else {
                    String fotoUrlToUse = currentFotoUrl;
                    if ("editar".equals(action)) {
                        new UpdateElectrodomesticoTask().execute(id, nombre, precio, fotoUrlToUse);
                    } else {
                        new CreateElectrodomesticoTask().execute(nombre, precio, fotoUrlToUse);
                    }
                }
            } catch (NumberFormatException e) {
                Toast.makeText(this, "Precio inválido", Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_IMAGE_PICK && resultCode == RESULT_OK && data != null) {
            Uri imageUri = data.getData();
            if (imageUri != null) {
                try {
                    InputStream inputStream = getContentResolver().openInputStream(imageUri);
                    Bitmap bitmap = BitmapFactory.decodeStream(inputStream);
                    imgPreview.setImageBitmap(bitmap);

                    ByteArrayOutputStream baos = new ByteArrayOutputStream();
                    bitmap.compress(Bitmap.CompressFormat.JPEG, 80, baos);
                    byte[] imageBytes = baos.toByteArray();
                    selectedImageBase64 = Base64.encodeToString(imageBytes, Base64.NO_WRAP);
                } catch (Exception e) {
                    Toast.makeText(this, "Error al procesar la imagen", Toast.LENGTH_SHORT).show();
                }
            }
        }
    }

    private class CreateElectrodomesticoTask extends AsyncTask<Object, Void, Electrodomestico> {
        private String errorMessage = null;

        @Override
        protected void onPreExecute() {
            progressBar.setVisibility(View.VISIBLE);
            btnGuardar.setEnabled(false);
        }

        @Override
        protected Electrodomestico doInBackground(Object... params) {
            try {
                return electrodomesticoService.crear((String) params[0], (Double) params[1], (String) params[2]);
            } catch (Exception e) {
                errorMessage = e.getMessage();
                return null;
            }
        }

        @Override
        protected void onPostExecute(Electrodomestico result) {
            progressBar.setVisibility(View.GONE);
            btnGuardar.setEnabled(true);
            if (result != null) {
                Toast.makeText(ElectrodomesticoFormActivity.this, "Electrodoméstico creado", Toast.LENGTH_SHORT).show();
                finish();
            } else {
                Toast.makeText(ElectrodomesticoFormActivity.this, "Error: " + errorMessage, Toast.LENGTH_LONG).show();
            }
        }
    }

    private class UpdateElectrodomesticoTask extends AsyncTask<Object, Void, Electrodomestico> {
        private String errorMessage = null;

        @Override
        protected void onPreExecute() {
            progressBar.setVisibility(View.VISIBLE);
            btnGuardar.setEnabled(false);
        }

        @Override
        protected Electrodomestico doInBackground(Object... params) {
            try {
                return electrodomesticoService.actualizar((Integer) params[0], (String) params[1], (Double) params[2], (String) params[3]);
            } catch (Exception e) {
                errorMessage = e.getMessage();
                return null;
            }
        }

        @Override
        protected void onPostExecute(Electrodomestico result) {
            progressBar.setVisibility(View.GONE);
            btnGuardar.setEnabled(true);
            if (result != null) {
                Toast.makeText(ElectrodomesticoFormActivity.this, "Electrodoméstico actualizado", Toast.LENGTH_SHORT).show();
                finish();
            } else {
                Toast.makeText(ElectrodomesticoFormActivity.this, "Error: " + errorMessage, Toast.LENGTH_LONG).show();
            }
        }
    }

    private class UploadAndSaveTask extends AsyncTask<Void, Void, String> {
        private final String nombre;
        private final double precio;
        private String errorMessage;

        UploadAndSaveTask(String nombre, double precio) {
            this.nombre = nombre;
            this.precio = precio;
        }

        @Override
        protected void onPreExecute() {
            progressBar.setVisibility(View.VISIBLE);
            btnGuardar.setEnabled(false);
        }

        @Override
        protected String doInBackground(Void... voids) {
            try {
                if (selectedImageBase64 == null) {
                    return currentFotoUrl;
                }
                FotoUploadRequest request = new FotoUploadRequest(selectedImageBase64, "electrodomestico.jpg");
                FotoUploadResponse response = HttpClientUtil.post("/electrodomesticos/foto", request, FotoUploadResponse.class);
                if (response != null) {
                    return response.getFotoUrl();
                }
                return null;
            } catch (IOException e) {
                errorMessage = e.getMessage();
                return null;
            }
        }

        @Override
        protected void onPostExecute(String fotoUrl) {
            if (fotoUrl == null && selectedImageBase64 != null) {
                progressBar.setVisibility(View.GONE);
                btnGuardar.setEnabled(true);
                Toast.makeText(ElectrodomesticoFormActivity.this, "Error subiendo imagen: " + errorMessage, Toast.LENGTH_LONG).show();
                return;
            }

            currentFotoUrl = fotoUrl;
            selectedImageBase64 = null;

            if ("editar".equals(action)) {
                new UpdateElectrodomesticoTask().execute(id, nombre, precio, currentFotoUrl);
            } else {
                new CreateElectrodomesticoTask().execute(nombre, precio, currentFotoUrl);
            }
        }
    }

    private static class LoadImageTask extends AsyncTask<String, Void, Bitmap> {
        private final ImageView imageView;

        LoadImageTask(ImageView imageView) {
            this.imageView = imageView;
        }

        @Override
        protected Bitmap doInBackground(String... params) {
            String url = params[0];
            try {
                java.io.InputStream in = new java.net.URL(url).openStream();
                return BitmapFactory.decodeStream(in);
            } catch (Exception e) {
                return null;
            }
        }

        @Override
        protected void onPostExecute(Bitmap bitmap) {
            if (bitmap != null) {
                imageView.setImageBitmap(bitmap);
            }
        }
    }
}

