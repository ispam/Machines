package tech.destinum.machines.data;

import android.content.Context;
import android.os.Environment;
import android.text.TextUtils;
import android.util.Log;

import com.opencsv.CSVWriter;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import tech.destinum.machines.data.local.ViewModel.IncomeViewModel;
import tech.destinum.machines.data.local.ViewModel.MachineViewModel;

public class ExportCSV {

    private Context context;

    public ExportCSV(Context context) {
        this.context = context;
    }


    public void createCSV(String[] headers, List<?> rows, String tableName) throws IOException {

        String baseDir = context.getDataDir().getAbsolutePath();
        String fileName = tableName;
        String filePath = baseDir + File.separator + fileName;
        File baseFile = new File(filePath);
        CSVWriter writer;

        if (!baseFile.exists() && !baseFile.isDirectory()){
            baseFile.mkdirs();
        }

        try {

            writer = new CSVWriter(new FileWriter(tableName), ',');

            String[] newRows = rows.toArray(new String[0]);
            writer.writeNext(headers);
            writer.writeNext(newRows);
            writer.close();

        } catch (Exception e) {
            e.printStackTrace();
        }


    }

}
