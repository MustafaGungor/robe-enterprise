package com.mebitech.robe.convert.tsv;

import com.mebitech.robe.convert.csv.CSVExporter;
import org.supercsv.prefs.CsvPreference;

public class TSVExporter<T> extends CSVExporter<T> {

    public TSVExporter(Class dataClass) {
        super(dataClass, CsvPreference.TAB_PREFERENCE);
    }

}
